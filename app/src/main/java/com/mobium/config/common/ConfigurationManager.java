package com.mobium.config.common;

import java.util.HashMap;

/**
 *  on 04.03.14.
 * http://mobiumapps.com/
 */
@Deprecated
public class ConfigurationManager {
    public enum StartPageButtons {
        SIMPLE,
        GRID
    }

    public enum AppType {
        SIMPLE,
        SHOP
    }

    public static final String MOBIUM_APP_TITLE = "mobium.appTitle";
    public static final String MOBIUM_BUILD_ID = "mobium.buildId";
    public static final String MOBIUM_URL = "mobium.url";
    public static String OTHER_URL = "";

    public static final String MOBIUM_IDENTITY_BUILD_ID = "identity.buildId";
    public static final String MOBIUM_IDENTITY_SHOP_ID = "identity.shopId";
    public static final String MOBIUM_IDENTITY_APP_ID = "identity.appId";

    public static final String MOBIUM_START_PAGE_BUTTONS = "mobium.startPage.buttons";
    public static final String MOBIUM_START_PAGE_HAS_SEARCH = "mobium.startPage.hasSearch";
    public static final String MOBIUM_START_PAGE_BACKGROUND_IMAGE = "mobium.startPage.bgImage";
    public static final String MOBIUM_START_PAGE_SHOW_BANNER = "mobium.startPage.showBanner";

    public static final String MOBIUM_CATALOG_PAGE_HAS_SEARCH = "mobium.catalogPage.hasSearch";

    public static final String MOBIUM_CATALOG_BUY_BUTTON = "mobium.catalog.buy.button";

    public static final String MOBIUM_APP_TYPE = "mobium.appType";
    public static final String MOBIUM_CITIES = "mobium.cities";
    public static final String MOBIUM_CURRENCY_PLACEHOLDER = "mobium.currencyPlaceholder";

    public static final String MOBIUM_ACTION_PREFIX = "mobium.actions.";
    public static final String MOBIUM_ACTION_TITLE_POSTFIX = ".actionTitle";
    public static final String MOBIUM_ACTION_TYPE_POSTFIX = ".actionType";
    public static final String MOBIUM_ACTION_DATA_POSTFIX = ".actionData";

    public static final String MOBIUM_ACTION_START_PAGE_BUTTON_ICON_POSTFIX = ".mainIcon";
    public static final String MOBIUM_ACTION_START_PAGE_BUTTON_BACKGROUND_POSTFIX = ".mainBackground";
    public static final String MOBIUM_ACTION_START_PAGE_BUTTON_TEXT_COLOR_POSTFIX = ".mainTextColor";
    public static final String MOBIUM_ACTION_START_PAGE_TITLE_POSTFIX = ".mainTitle";

    public static final String MOBIUM_ACTION_MENU_BUTTON_ICON_POSTFIX = ".menuIcon";
    public static final String MOBIUM_ACTION_MENU_BUTTON_BACKGROUND_POSTFIX = ".menuBackground";
    public static final String MOBIUM_ACTION_MENU_BUTTON_TEXT_COLOR_POSTFIX = ".menuTextColor";
    public static final String MOBIUM_ACTION_MENU_BUTTON_TITLE_POSTFIX = ".menuTitle";

    public static final String MOBIUM_WITHOUT_CART = "mobium.withoutCart";
    public static final String MOBIUM_WITHOUT_FAVOURITES = "mobium.withoutFavourites";

    public static final String MOBIUM_CATALOG_SHOW_PRICES = "mobium.catalog.showPrices";
    public static final String MOBIUM_SHOW_BUY_BUTTON = "mobium.showBuyButton";
    public static final String MOBIUM_BUY_BUTTON_TITLE = "mobium.buyButtonTitle";

    public static final String MOBIUM_FLURRY = "app.flurryId";
    public static final String MOBIUM_GOOGLE_ID = "app.googleId";
    public static final String MOBIUM_BUGSENSE_ID = "app.bugsenseId";
    public static final String MOBIUM_GCM = "app.gcm";
    public static final String MOBIUM_ACRA = "app.acraUrl";

    public static final String MOBIUM_BANNERS_INTERVAL = "mobium.banners.interval";

    public static final HashMap<String, String> MOBIUM_DEFAULT_CONFIGURATION = new HashMap<>();
    static {
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_START_PAGE_BUTTONS, "simple");
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_APP_TYPE, "shop");
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_URL, "http://extrashop.extradea.com/processor.ashx?shopId=7");
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_BUILD_ID, "a2778b30-51ba-4911-b927-900e1260cbe5");
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_IDENTITY_BUILD_ID, "093c4474-4aa1-4dff-a623-e2b118bf5ee3 ");
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_IDENTITY_SHOP_ID, "1");
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_IDENTITY_APP_ID, "0");
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_CURRENCY_PLACEHOLDER, "%s руб.");
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_CITIES, "false");
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_START_PAGE_HAS_SEARCH, "true");
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_FLURRY, "NTDJYSF23HRNJVDHRN4N");
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_GOOGLE_ID, "UA-37590608-1");
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_BUGSENSE_ID, "456ac964");
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_GCM, "193941593625");
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_WITHOUT_CART, "false");
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_WITHOUT_FAVOURITES, "false");
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_BANNERS_INTERVAL, "10");
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_CATALOG_PAGE_HAS_SEARCH, "true");
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_START_PAGE_SHOW_BANNER, "true");
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_SHOW_BUY_BUTTON, "true");
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_CATALOG_SHOW_PRICES, "true");
        MOBIUM_DEFAULT_CONFIGURATION.put(MOBIUM_CATALOG_BUY_BUTTON, "true");
    }

}
