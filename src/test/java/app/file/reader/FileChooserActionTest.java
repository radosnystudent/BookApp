package app.file.reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileChooserActionTest {

    @Mock
    private FileChooserListener listener;

    @Mock
    private JFileChooser fileChooser;

    @Mock
    private IsbnFileParser parser;

    private FileChooserAction fileChooserAction;

    @BeforeEach
    void setUp() {
        fileChooserAction = new FileChooserAction();
        fileChooserAction.setFileChooser(fileChooser);
        fileChooserAction.addListener(listener);
        fileChooserAction.setParser(parser);
    }

    @Test
    void testActionPerformedWithFileSelected() throws IOException {
        ActionEvent mockEvent = mock(ActionEvent.class);
        File mockFile = mock(File.class);
        when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(mockFile);
        List<String> expectedIsbnList = Arrays.asList("1234567890", "0987654321");
        when(parser.parse(mockFile)).thenReturn(expectedIsbnList);

        fileChooserAction.actionPerformed(mockEvent);

        verify(listener).onFileParsed(expectedIsbnList);
    }

    @Test
    void testActionPerformedWithFileSelectionCancelled() {
        ActionEvent mockEvent = mock(ActionEvent.class);
        when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.CANCEL_OPTION);

        fileChooserAction.actionPerformed(mockEvent);

        verify(listener, never()).onFileParsed(anyList());
    }

    @Test
    void testActionPerformedWithIOException() throws IOException {
        ActionEvent mockEvent = mock(ActionEvent.class);
        File mockFile = mock(File.class);
        when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(mockFile);
        when(parser.parse(mockFile)).thenThrow(new IOException("File parsing failed"));

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> fileChooserAction.actionPerformed(mockEvent));

        verify(listener, never()).onFileParsed(anyList());
    }
}