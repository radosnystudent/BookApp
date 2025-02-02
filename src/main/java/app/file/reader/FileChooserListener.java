package app.file.reader;

import java.util.List;

public interface FileChooserListener {
    void onFileParsed(List<String> isbnList);
}
