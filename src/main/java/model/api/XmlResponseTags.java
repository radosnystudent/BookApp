package model.api;

@SuppressWarnings("unused")
public enum XmlResponseTags {
    TITLE_TEXT("TitleText"),
    PRODUCT_IDENTIFIER("ProductIdentifier"),
    CONTRIBUTOR("Contributor"),
    CONTRIBUTOR_ROLE("ContributorRole"),
    A01_ROLE("A01"),
    PERSON_NAME_INVERTED("PersonNameInverted");

    private final String code;

    XmlResponseTags(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
