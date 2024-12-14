package app.filereader;

import lombok.Getter;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class FileChooserAction implements ActionListener {

    private final List<FileChooserListener> listeners = new ArrayList<>();

    public void addListener(FileChooserListener listener) {
        listeners.add(listener);
    }

    public void removeListener(FileChooserListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final JFileChooser fileChooser = new JFileChooser();
        final IsbnFileParser parser = new IsbnFileParser();
        final int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                notifyListeners(parser.parse(selectedFile));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void notifyListeners(List<String> isbnList) {
        for (FileChooserListener listener : listeners) {
            listener.onFileParsed(isbnList);
        }
    }
}
