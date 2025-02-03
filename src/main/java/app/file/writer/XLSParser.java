package app.file.writer;

import model.BookInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class XLSParser {
    private static final String OUTPUT_PATH = "src/output/Books.xls";
    private static final Logger logger = Logger.getLogger(XLSParser.class.getName());

    private XLSParser() {
    }

    public static void saveBooksToXls(final List<BookInfo> books) {
        final List<BookInfo> booksToSave = filterOutNullElements(books);
        if (CollectionUtils.isEmpty(booksToSave)) {
            logger.warning("Nothing to save");
            return;
        }

        File file = new File("src/output");
        if (!file.exists() && !file.mkdirs()) {
            logger.warning("Failed to create directory: " + file.getAbsolutePath());
            return;
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Books");
            OptionalInt optionalNumberOfAuthors = getMaxNumberOfAuthors(booksToSave);
            int numberOfAuthors = optionalNumberOfAuthors.orElse(0);

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ISBN");
            headerRow.createCell(1).setCellValue("Title");

            for (int i = 0; i < numberOfAuthors; i++) {
                headerRow.createCell(2 + i).setCellValue("Author " + (i + 1));
            }

            int rowNum = 1;
            for (BookInfo book : booksToSave) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(book.getIsbn());
                row.createCell(1).setCellValue(book.getTitle());

                List<String> authors = book.getAuthors();
                for (int i = 0; i < authors.size(); i++) {
                    row.createCell(2 + i).setCellValue(authors.get(i));
                }
            }

            try (FileOutputStream fileOut = new FileOutputStream(OUTPUT_PATH)) {
                workbook.write(fileOut);
            }

        } catch (IOException e) {
            logger.warning(e.toString());
        }
    }

    private static OptionalInt getMaxNumberOfAuthors(final List<BookInfo> books) {
        return Stream.ofNullable(books)
                .flatMap(Collection::stream)
                .map(BookInfo::getAuthors)
                .mapToInt(List::size)
                .max();
    }

    private static List<BookInfo> filterOutNullElements(final List<BookInfo> books) {
        return Stream.ofNullable(books)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .toList();
    }
}
