package model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookInfo {
    private String isbn;
    private String title;
    private List<String> authors;
}
