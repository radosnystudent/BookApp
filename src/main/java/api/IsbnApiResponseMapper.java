package api;

import model.BookInfo;
import model.api.XmlResponseTags;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

class IsbnApiResponseMapper {
    private static final Logger logger = Logger.getLogger(IsbnApiResponseMapper.class.getName());

    private IsbnApiResponseMapper() {
    }

    public static BookInfo parseResponse(String response) {
        try {
            Document document = parseXmlToDocument(response);
            String isbn = getIsbn(document);

            if (isbn == null || isbn.isEmpty()) {
                return null;
            }
            String title = getTitle(document);
            List<String> authors = createAuthors(document);

            return prepareBookInfo(isbn, title, authors);
        } catch (Exception e) {
            logger.warning(String.format("Error parsing XML: %s", e));

            return null;
        }
    }

    private static Document parseXmlToDocument(String response) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse(new InputSource(new StringReader(response)));
    }

    private static String getTitle(Document document) {
        return document.getElementsByTagName(XmlResponseTags.TITLE_TEXT.getCode()).item(0).getTextContent();
    }

    private static String getIsbn(Document document) throws XPathExpressionException {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();

        String expression = "//*[local-name()='ProductIdentifier']/*[local-name()='IDValue']";
        XPathExpression xPathExpression = xPath.compile(expression);

        return (String) xPathExpression.evaluate(document, XPathConstants.STRING);
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

    private static BookInfo prepareBookInfo(String isbn, String title, List<String> authors) {
        return new BookInfo(isbn, title, authors);
    }
}
