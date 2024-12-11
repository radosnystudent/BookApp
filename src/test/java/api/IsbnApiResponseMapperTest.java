package api;

import model.BookInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IsbnApiResponseMapperTest {
    private static final String ISBN = "9788326841705";
    private static final String AUTHOR_1 = "Douglas Preston";
    private static final String AUTHOR_2 = "Lincoln Child";
    private static final String TITLE = "Ogon skorpiona";

    @Test
    void shouldParseResponse() {
        BookInfo result = IsbnApiResponseMapper.parseResponse(prepareXmlInput());

        assertNotNull(result);
        assertEquals(TITLE, result.getTitle());
        assertEquals(ISBN, result.getIsbn());
        assertEquals(2, result.getAuthors().size());
        assertEquals(AUTHOR_1, result.getAuthors().getFirst());
        assertEquals(AUTHOR_2, result.getAuthors().getLast());
    }

    @Test
    void shouldReturnNullIfExceptionThrown() {
        assertNull(IsbnApiResponseMapper.parseResponse(""));
    }

    private String prepareXmlInput() {
        return String.format(prepareXmlString(), TITLE, ISBN, AUTHOR_1, AUTHOR_2);
    }

    private String prepareXmlString() {
        return """
                <?xml version="1.0" encoding="UTF-8"?>
                <ONIXMessage release="3.0" xmlns="http://ns.editeur.org/onix/3.0/reference" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                    <Product>
                        <DescriptiveDetail>
                            <TitleDetail>
                                <TitleElement>
                                    <TitleText>%s</TitleText>
                                </TitleElement>
                            </TitleDetail>
                            <ProductIdentifier>
                                <ProductIDType>15</ProductIDType>
                                <IDValue>%s</IDValue>
                            </ProductIdentifier>
                            <Contributor>
                                <ContributorRole>A01</ContributorRole>
                                <PersonNameInverted>%s</PersonNameInverted>
                            </Contributor>
                            <Contributor>
                                <ContributorRole>A01</ContributorRole>
                                <PersonNameInverted>%s</PersonNameInverted>
                            </Contributor>
                            <Contributor>
                                <ContributorRole>B06</ContributorRole>
                                <PersonNameInverted>Jan Kraśko</PersonNameInverted>
                            </Contributor>
                        </DescriptiveDetail>
                    </Product>
                </ONIXMessage>
                """;
    }
}