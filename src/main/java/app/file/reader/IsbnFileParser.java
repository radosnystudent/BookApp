package app.file.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class IsbnFileParser {

    List<String> parse(File file) throws IOException {
        List<String> isbnList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                for (String part : parts) {
                    String cleanedIsbn = part.replace("-", "").trim();
                    if (!cleanedIsbn.isEmpty()) {
                        isbnList.add(cleanedIsbn);
                    }
                }
            }
        }
        return isbnList;
    }
}
