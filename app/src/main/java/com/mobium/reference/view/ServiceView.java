package com.mobium.reference.view;

import android.view.View;
import com.mobium.new_api.models.ShopPoint;
import com.mobium.reference.view.LoadableView;

import java.util.List;

/**
 *  on 24.07.15.
 * http://mobiumapps.com/
 */
public interface ServiceView extends LoadableView {
    void showTitle(String title);
    void showPicture(String url);
    void showRegionTitle(String regionTitle);

    void showServices(List<ShopPoint>list);
    void setUpButton(String title, View.OnClickListener listener);
}
