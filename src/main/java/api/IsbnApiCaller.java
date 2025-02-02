package api;

import model.BookInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class IsbnApiCaller {

    public List<BookInfo> callIsbnApi(List<String> isbnList) {
        return Stream.ofNullable(isbnList)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .map(this::callApi)
                .filter(Objects::nonNull)
                .toList();
    }

    private BookInfo callApi(String isbn) {
        try {
            return callIsbnApi(isbn);
        } catch (Exception e) {
            return null;
        }
    }

    public BookInfo callIsbnApi(String isbn) throws Exception {
        String apiUrl = "https://e-isbn.pl/IsbnWeb/api.xml?isbn=" + isbn;
        URL url = URI.create(apiUrl).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/xml");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("HTTP GET Request Failed with Error Code: " + responseCode);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        connection.disconnect();

        return IsbnApiResponseMapper.parseResponse(response.toString());
    }
}
