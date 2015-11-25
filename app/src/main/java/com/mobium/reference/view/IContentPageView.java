package com.mobium.reference.view;

import android.content.DialogInterface;
import android.view.View;

import com.mobium.client.models.ShopCategory;
import com.mobium.client.models.ShopItem;
import com.mobium.reference.utils.Functional;

import java.util.List;

/**
 *  on 07.08.15.
 * http://mobiumapps.com/
 */
public interface IContentPageView {
    void showTitle(String title);
    void showText(String html);
    void showImages(List<String> images);
    void showCategories(List<ShopCategory> categories, String title);
    void showProducts(List<ShopItem> products, String title);

    void showLoadBar();
    void showContent();

    void showError(String title, String message, DialogInterface.OnClickListener apply, DialogInterface.OnClickListener cancel);

    void doExit();

    void hideProducts();
    void hideCategories();
}
