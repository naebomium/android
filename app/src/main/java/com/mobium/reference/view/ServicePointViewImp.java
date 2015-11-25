package com.mobium.reference.view;

import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.mobium.reference.R;
import com.mobium.reference.fragments.shop_info.ShopPointFragment;
import com.mobium.reference.presenter.ServicePresenter;
import com.mobium.reference.view.ServicePointView;


/**
 *  on 25.07.15.
 * http://mobiumapps.com/
 */
public class ServicePointViewImp implements ServicePointView {
    @Bind(R.id.shop_item_titles)
    protected LinearLayout linearLayout;

    private final ServicePresenter presenter;

    ServicePointViewImp(View v, ServicePresenter presenter) {
        this.presenter = presenter;
        ButterKnife.bind(this, v);
    }


    public void addInfo(String title,
                        String underTitle,
                        View.OnClickListener onClick,
                        @DrawableRes int img) {
        View v = LayoutInflater.from(linearLayout.getContext())
                .inflate(R.layout.view_icon_text_undertext, linearLayout, false);
        ShopPointFragment.configureItem(v, title, underTitle, img);
        v.setOnClickListener(onClick);
        linearLayout.addView(v);
    }


    @Override
    public void showTitle(String title) {
        addInfo(title, null, null, R.drawable.cart);
    }

    @Override
    public void showPhone(String phone) {
        addInfo(phone, "Позвоните нам", V -> presenter.onPhoneClick(phone), R.drawable.phone);
    }

    @Override
    public void showSubway(String subway) {
        addInfo(subway, "Метро", null, R.drawable.underground);

    }

    @Override
    public void showAddress(String address) {
        addInfo(address, null, null, R.drawable.shop2);
    }
}
