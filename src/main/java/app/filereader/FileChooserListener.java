package app.filereader;

import java.util.List;

public interface FileChooserListener {
    void onFileParsed(List<String> isbnList);
}
