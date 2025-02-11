package api;

import model.BookInfo;
import model.api.exceptions.IsbnApiException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class IsbnApiCaller implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(IsbnApiCaller.class);

    private final CloseableHttpClient httpClient;

    public IsbnApiCaller() {
        this(HttpClients.createDefault());
    }

    public IsbnApiCaller(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

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
        } catch (IsbnApiException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private BookInfo callIsbnApi(String isbn) throws IsbnApiException {
        String apiUrl = "https://e-isbn.pl/IsbnWeb/api.xml?isbn=" + isbn;

        try {
            HttpGet request = new HttpGet(apiUrl);
            request.setHeader("Accept", "application/xml");

            HttpClientResponseHandler<String> responseHandler = (ClassicHttpResponse response) -> {
                int statusCode = response.getCode();
                if (statusCode != 200) {
                    throw new IsbnApiException("HTTP GET Request Failed with Error Code: " + statusCode);
                }

                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    throw new IsbnApiException("No response entity found");
                }

                try {
                    return EntityUtils.toString(entity);
                } catch (ParseException e) {
                    throw new IsbnApiException("Failed to parse response entity", e);
                }
            };

            String responseBody = httpClient.execute(request, responseHandler);
            return IsbnApiResponseMapper.parseResponse(responseBody);
        } catch (IOException e) {
            throw new IsbnApiException("Failed to execute HTTP request", e);
        }
    }
}
