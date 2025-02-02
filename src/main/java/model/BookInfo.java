package model;

import java.util.List;

public class BookInfo {
    private final String isbn;
    private final String title;
    private final List<String> authors;

    public BookInfo(String isbn, String title, List<String> authors) {
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public String getTitle() {
        return this.title;
    }

    public List<String> getAuthors() {
        return this.authors;
    }
}
