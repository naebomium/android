package com.mobium.client.models;

/**
 *  on 27.01.14.
 * http://mobiumapps.com/
 */

public enum ActionType {
    OPEN_START("openMainScreen"),
    OPEN_CATEGORY("openCategory"),
    OPEN_PRODUCT("openProduct"),

    OPEN_CATALOG("openCatalog"),


    OPEN_CATEGORY_BY_ALIAS("openCategoryByAlias"),

    OPEN_PRODUCT_BY_REAL_ID("openProductByRealId"),

    OPEN_PAGE("openPage"),
    OPEN_URL("openUrl"),
    OPEN_URL_EXTERNAL("openUrlExternal"),
    DO_CALL("doCall"),
    OPEN_SEARCH("openSearch"),
    OPEN_CONTENT_PAGE("openContentPage"),

    OPEN_SHOPS("openShops"),
    OPEN_CART("openCart"),
    OPEN_HISTORY("openHistory"),
    OPEN_FAVOURITES("openFavourites"),

    OPEN_CONTACTS("openContacts"),
    OPEN_DELIVERY_INFO("openDeliveryInfo"),

    OPEN_DEV_SETTING("openDevSetting"),
    OPEN_ARTICLE("openArticle"),
    OPEN_ARTICLES("openArticles"),
    GET_NEWS_ITEM("getNewsItem"),



    OPEN_CATALOG_INSIDE_MENU("openCatalogInsideMenu"),
    POP_CATALOG_INSIDE_MENU("backCatalogInsideMenu"),

    OPEN_LOGIN_SCREEN("openLogin"),
    OPEN_PROFILE_SCREEN("openProfile"),

    OPEN_SERVICES("openServices"),

    SEND_TEST_PUSH("SEND_TEST_PUSH"),

    SEND_TEST_PUSH_CONTENT("SEND_PUSH_CONTENT_TEST"),

    OPEN_LOADED_CONTENT_PAGE("OPEN_LOADED_CONTENT_PAGE"),

    OPEN_USEFUL("openUseful"),

    DO_OPEN_SCANNER("OPEN_SCANNER");



    private String name;

    ActionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ActionType getActionType(String actionName) {
        for (ActionType type : ActionType.values()) {
            if (type.getName().equals(actionName)) {
                return type;
            }
        }
        return OPEN_START;
    }
}
