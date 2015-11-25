package com.mobium.reference.productPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobium.client.models.ShopItem;
import com.mobium.reference.R;
import com.mobium.reference.ReferenceApplication;
import com.mobium.reference.activity.MainDashboardActivity;
import com.mobium.reference.utils.ImageUtils;
import com.mobium.reference.views.TabPanelController;
import com.mobium.reference.views.TabsFocuser;
import com.mobium.reference.views.Ui_configurator;


/**
 *   01.04.15.
 * http://mobiumapps.com/
 */
public abstract class ProductDetailsBase implements TabPanelController.TabPanelItem {
    private ViewGroup contentWrapper;
    protected ShopItem shopItem;
    protected MainDashboardActivity activity;
    protected ReferenceApplication application;
    private DetailsType type;
    protected TabsFocuser focuser;

    public ProductDetailsBase(MainDashboardActivity context, DetailsType type, ShopItem shopItem) {
        this.type = type;
        this.shopItem = shopItem;
        activity = context;
    }


    private void onLoadContentView() {
        fillContentWrapper(LayoutInflater.from(activity), contentWrapper);
        if (needAddButtons()) {
            View view = View.inflate(activity, R.layout.share_buttons, null);
            contentWrapper.addView(view);

            View shareButton = view.findViewById(R.id.share_btn);
            View favouriteBtn = view.findViewById(R.id.favourite_btn);
            TextView favouriteBtnText = (TextView) view.findViewById(R.id.addToFavoriteText);
            ((LinearLayout.LayoutParams) shareButton.getLayoutParams()).setMargins(ImageUtils.convertToPx(activity, 16), 0, 0, 0);
            Ui_configurator.getInstance(activity).configureShopItemButtons(
                    shopItem, shareButton, favouriteBtn, favouriteBtnText
            );

        }
    }



    protected abstract View fillContentWrapper(LayoutInflater inflater, ViewGroup contentWrapper);


    public DetailsType getType() {
        return type;
    }

    protected void setType(DetailsType type) {
        this.type = type;
    }


    public String getTitle() {
        return type.getDescription();
    }

    protected abstract boolean needAddButtons();

    @Override
    public View getView(ViewGroup viewGroup) {

        View resultView = LayoutInflater.from(activity).inflate(R.layout.fragment_product_details_page_wrapper, viewGroup, false);

        contentWrapper = (ViewGroup) resultView.findViewById(R.id.product_details_view_wrapper_content);

        application = (ReferenceApplication) activity.getApplication();

        onLoadContentView();

        return resultView;
    }

    public void setFocuser(TabsFocuser focuser) {
        this.focuser = focuser;
    }
}
