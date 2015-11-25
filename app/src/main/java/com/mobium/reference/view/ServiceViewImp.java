package com.mobium.reference.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.mobium.new_api.models.ShopPoint;
import com.mobium.reference.R;
import com.mobium.reference.presenter.ServicePresenter;
import com.mobium.reference.views.VisibilityViewUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *  on 25.07.15.
 * http://mobiumapps.com/
 */
public class ServiceViewImp implements ServiceView {
    private final ServicePresenter presenter;

    @Bind(R.id.fragment_services_title)
    protected TextView m_title;

    @Bind(R.id.fragment_services_image)
    protected ImageView m_image;

    @Bind(R.id.fragment_services_button)
    protected Button button;

    @Bind(R.id.fragment_services_points)
    protected LinearLayout layout;

    @Bind(R.id.progress_view)
    protected View loadView;

    @Bind(R.id.fragment_services_region_title)
    protected TextView regionTitle;


    public ServiceViewImp(View v, ServicePresenter presenter) {
        this.presenter = presenter;
        ButterKnife.bind(this, v);
    }


    @Override
    public void showTitle(String title) {
        m_title.setText(title);
    }

    @Override
    public void showPicture(String url) {
        if (url != null && url.length() > 5)
            Picasso.with(m_image.getContext())
                    .load(url)
                    .into(m_image);
        else
            m_image.setVisibility(View.GONE);
    }

    @Override
    public void showRegionTitle(String title) {
        regionTitle.setText(title);
    }

    @Override
    public void showServices(List<ShopPoint> list) {
        layout.removeAllViews();
        for (ShopPoint point : list) {
            View v = LayoutInflater.from(button.getContext())
                    .inflate(R.layout.fragment_service_shop_item, layout, false);
            ServicePointView view = new ServicePointViewImp(v, presenter);

            view.showTitle(point.getTitle());
            view.showAddress(point.getAddress());
            point.getSubway().ifPresent(view::showSubway);
            point.getPhone().ifPresent(phoneList ->
                    Stream.of(phoneList.replace(";", ",").split(","))
                            .forEach(view::showPhone));
            layout.addView(v);
        }

    }

    @Override
    public void setUpButton(String title, View.OnClickListener listener) {
        button.setText(title);
        button.setOnClickListener(listener);
    }

    @Override
    public void showProgress(boolean animated) {
        VisibilityViewUtils.show(loadView, animated);
    }

    @Override
    public void showContent(boolean animated) {
        VisibilityViewUtils.hide(loadView, animated);
    }


    public static boolean isValidPhoneNumber(CharSequence target) {
        return target != null &&
                !(target.length() < 6 || target.length() > 13) &&
                android.util.Patterns.PHONE.matcher(target).matches();
    }
}
