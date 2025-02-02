package app.file.writer;

import model.BookInfo;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class XLSParserTest {

    @Test
    void shouldCreatesFileSuccessfully() {
        List<BookInfo> books = List.of(new BookInfo("123456", "Test Book", List.of("Author1", "Author2")));

        XLSParser.saveBooksToXls(books);

        File file = new File("src/output/Books.xls");
        assertTrue(file.exists());
    }

    @Test
    void shouldNotSaveFileWriteError() throws IOException {
        FileOutputStream fileOutputStreamMock = mock(FileOutputStream.class);
        doThrow(new IOException("Write error")).when(fileOutputStreamMock).write(any());

        List<BookInfo> books = List.of(new BookInfo("123456", "Test Book", List.of("Author1")));

        assertDoesNotThrow(() -> XLSParser.saveBooksToXls(books));
    }
}