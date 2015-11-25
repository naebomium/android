package com.mobium.reference.productPage;

/**
 *  on 14.05.15.
 * http://mobiumapps.com/
 */
public enum DetailsType {
    OTHER_ITEMS("Аналоги"),
    RELATED_ITEMS("С этим покупают"),
    PRODUCT_FEATURES("Харак-ки"),
    OPINIONS("Отзывы"),
    DESCRIPTION("Описание");

    private final String description;

    DetailsType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
