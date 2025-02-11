package api;

import model.BookInfo;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IsbnApiCallerTest {

    @Mock
    private CloseableHttpClient httpClient;

    @Mock
    private ClassicHttpResponse httpResponse;

    @Mock
    private HttpEntity httpEntity;

    private IsbnApiCaller isbnApiCaller;

    @BeforeEach
    void setUp() {
        isbnApiCaller = new IsbnApiCaller(httpClient);
    }

    @Test
    void shouldReturnBookInfoListWhenApiCallIsSuccessful() throws Exception {
        String isbn = "1234567890";
        String responseBody = "<book><title>Test Book</title></book>";
        BookInfo mockBookInfo = new BookInfo(isbn, "Test Book", List.of("Tom", "Jerry"));

        when(httpClient.execute(any(HttpGet.class), any(HttpClientResponseHandler.class)))
                .thenAnswer(invocation -> {
                    HttpClientResponseHandler<String> handler = invocation.getArgument(1);
                    return handler.handleResponse(httpResponse);
                });
        when(httpResponse.getCode()).thenReturn(200);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        InputStream inputStream = new ByteArrayInputStream(responseBody.getBytes(StandardCharsets.UTF_8));
        when(httpEntity.getContent()).thenReturn(inputStream);

        try (MockedStatic<IsbnApiResponseMapper> mockedStatic = mockStatic(IsbnApiResponseMapper.class)) {
            mockedStatic.when(() -> IsbnApiResponseMapper.parseResponse(responseBody))
                    .thenReturn(mockBookInfo);

            List<BookInfo> result = isbnApiCaller.callIsbnApi(List.of(isbn));

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Test Book", result.getFirst().getTitle());
        }
    }

    @Test
    void shouldReturnEmptyListWhenApiCallFailsWithHttpError() throws Exception {
        String isbn = "1234567890";
        when(httpClient.execute(any(HttpGet.class), any(HttpClientResponseHandler.class))).thenAnswer(invocation -> {
            HttpClientResponseHandler<String> handler = invocation.getArgument(1);
            return handler.handleResponse(httpResponse);
        });
        when(httpResponse.getCode()).thenReturn(404);

        List<BookInfo> result = isbnApiCaller.callIsbnApi(List.of(isbn));

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenApiCallFailsWithIOException() throws Exception {
        String isbn = "1234567890";

        when(httpClient.execute(any(HttpGet.class), any(HttpClientResponseHandler.class))).thenThrow(new IOException("Network error"));

        List<BookInfo> result = isbnApiCaller.callIsbnApi(List.of(isbn));

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenIsbnListIsNull() {
        List<BookInfo> result = isbnApiCaller.callIsbnApi(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenIsbnListIsEmpty() {
        List<BookInfo> result = isbnApiCaller.callIsbnApi(List.of());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}