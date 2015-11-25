package com.mobium.config.common;

import android.content.Context;

import com.mobium.config.StringConstants;
import com.mobium.reference.R;

/**
 *  on 06.10.15.
 */
class StaticStringConstants implements StringConstants {
    private final Context context;

    StaticStringConstants(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public String getMainPageTitle() {
        return context.getString(R.string.app_name);
    }

    @Override
    public String otherItemsTitle() {
        return "Похожие товары";
    }

    @Override
    public String relatedItemsTitle() {
        return "С этим покупают";
    }

    @Override
    public String preCartTitle() {
        return "Промежуточная карзина";
    }

    @Override
    public String getCurrencyPlaceholder() {
        return "%s руб";
    }

    @Override
    public String noInternetErrorMessage() {
        return "Нет доступа к сети интернет";
    }

    @Override
    public String regionNotSelectedErrorMessage() {
        return "Регион не выбран, дождитесь окончания загрузки регионов";
    }

    @Override
    public String autoCompleteRegionTitle() {
        return "Выбор региона";
    }

    @Override
    public String autoCompleteRegionMessage() {
        return "Начните вводить название региона";
    }

    @Override
    public String getCheckoutTitle() {
        return "Новый заказ";
    }

    @Override
    public String getForgetPasswordUrl() {
        return "";
    }

    @Override
    public String getYoutubeUrl() {
        return "";
    }

    @Override
    public String getFbUrl() {
        return "";
    }

    @Override
    public String getVkUrl() {
        return "";
    }

    @Override
    public String getInstagammUrl() {
        return "";
    }

}
