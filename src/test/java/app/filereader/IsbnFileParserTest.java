package app.filereader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class IsbnFileParserTest {

    private IsbnFileParser isbnFileParser;

    @BeforeEach
    void setUp() {
        isbnFileParser = new IsbnFileParser();
    }

    @Test
    void testParseWithValidFile() throws IOException, URISyntaxException {
        File testFile = getFileFromResources("app/filereader/test_isbn.txt");

        List<String> result = isbnFileParser.parse(testFile);

        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.contains("9788326841705"));
        assertTrue(result.contains("9788324631040"));
        assertTrue(result.contains("9788326841705"));
        assertTrue(result.contains("9781566199094"));
    }

    @Test
    void testParseWithEmptyFile() throws IOException, URISyntaxException {
        File testFile = getFileFromResources("app/filereader/empty_test.txt");

        List<String> result = isbnFileParser.parse(testFile);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseWithFileContainingOnlyWhitespaces() throws IOException, URISyntaxException {
        File testFile = getFileFromResources("app/filereader/whitespace_test.txt");

        List<String> result = isbnFileParser.parse(testFile);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseWithInvalidFile() {
        File nonExistentFile = new File("non_existent_file.txt");

        assertThrows(IOException.class, () -> isbnFileParser.parse(nonExistentFile));
    }

    private File getFileFromResources(String relativePath) throws URISyntaxException {
        Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(relativePath)).toURI());
        return path.toFile();
    }
}