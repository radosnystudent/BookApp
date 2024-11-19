package api;

import model.BookInfo;
import model.api.XmlResponseTags;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class IsbnApiResponseMapper {

    private IsbnApiResponseMapper() {}

    public static BookInfo parseResponse(String response) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(new InputSource(new StringReader(response)));

            NodeList titleNodes = document.getElementsByTagName(XmlResponseTags.TITLE_TEXT.getCode());
            String title = titleNodes.item(0).getTextContent();

            NodeList productNodes = document.getElementsByTagName(XmlResponseTags.PRODUCT_IDENTIFIER.getCode());
            String isbn = productNodes.item(0).getTextContent();

            return BookInfo.builder()
                    .title(title)
                    .authors(createAuthors(document))
                    .isbn(isbn)
                    .build();
        } catch (Exception e) {
            System.out.printf("Error parsing XML: %s", e.getMessage());
            return null;
        }
    }

    private static List<String> createAuthors(Document document) {
        List<String> authors = new ArrayList<>();
        NodeList contributorNodes = document.getElementsByTagName(XmlResponseTags.CONTRIBUTOR.getCode());

        for (int i = 0; i < contributorNodes.getLength(); i++) {
            Element contributor = (Element) contributorNodes.item(i);
            NodeList roles = contributor.getElementsByTagName(XmlResponseTags.CONTRIBUTOR_ROLE.getCode());

            if (roles.getLength() > 0 && Objects.equals(XmlResponseTags.A01_ROLE.getCode(), roles.item(0).getTextContent())) {
                NodeList names = contributor.getElementsByTagName(XmlResponseTags.PERSON_NAME_INVERTED.getCode());
                if (names.getLength() > 0) {
                    authors.add(names.item(0).getTextContent());
                }
            }
        }

        return authors;
    }
}
