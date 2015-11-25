package com.mobium.reference.fragments.shop_info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.new_api.models.ShopPoint;
import com.mobium.reference.R;
import com.mobium.reference.fragments.BasicContentFragment;
import com.mobium.reference.utils.Dialogs;
import com.mobium.reference.utils.FragmentActionHandler;
import com.mobium.reference.utils.Functional;
import com.mobium.reference.utils.MapUtils;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;

import java.util.Collections;

/**
 *  on 29.04.15.
 * http://mobiumapps.com/
 */
public class ShopPointFragment extends BasicContentFragment {
    private ShopPoint shopPoint;
    private MapView  mapView;

    private Bundle mBundle;



    public static void configureItem(View item, String title, String underTitle, int idRes) {
        TextView t = (TextView) item.findViewById(R.id.text);
        TextView ut = (TextView) item.findViewById(R.id.under_title);
        ((ImageView) item.findViewById(R.id.icon))
                .setImageResource(idRes);

        t.setText(title);
        if (underTitle != null)
            ut.setText(underTitle);
        else
            ut.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        Events.get(getActivity()).navigation().onPageOpen(OpenPageEvents.shop.name());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_shop_point, container, false);
        mapView = (MapView) result.findViewById(R.id.fragment_shop_point_map);
        ViewGroup makeCallView = (ViewGroup) result.findViewById(R.id.fragment_shop_point_tel);
        ViewGroup workTime = (ViewGroup) result.findViewById(R.id.fragment_shop_point_time);
        ViewGroup subway = (ViewGroup) result.findViewById(R.id.fragment_shop_point_subway);

        MapsInitializer.initialize(this.getActivity());
        mapView.onCreate(mBundle);

        MapUtils.updateMapState(shopPoint -> shopPoint.getPhone().ifPresent(this::doCallWithDialog),
                mapView.getMap(),
                Collections.singletonList(shopPoint)
        );

        Functional.optionalChoice(shopPoint.getPhone(),
                number -> {
                    configureItem(makeCallView, number, "Позвоните нам", R.drawable.phone);
                    makeCallView.setOnClickListener(l -> {
                        FragmentActionHandler.doAction(getActivity(), new Action(ActionType.DO_CALL, number));
                    });
                },
                () -> makeCallView.setVisibility(View.GONE)
        );

        Functional.optionalChoice(shopPoint.getSubway(),
                metro -> configureItem(subway, metro + "\n" + shopPoint.getAddress(), null, R.drawable.underground),
                () -> configureItem(subway, shopPoint.getAddress(), null, R.drawable.underground)
        );


        Functional.optionalChoice(shopPoint.getWorkday(),
                workDay -> configureItem(workTime, workDay, null, R.drawable.clock),
                () -> workTime.setVisibility(View.GONE)
        );

        return result;
    }

    public void doCallWithDialog(final String phoneNumber) {
        Dialogs.doCallWithDialog(getActivity(), phoneNumber);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
    }

    public void setShopPoint(ShopPoint shopPoint) {
        this.shopPoint = shopPoint;
    }

    public static ShopPointFragment getInstance(final ShopPoint point) {
        return new ShopPointFragment() {
            {
                setShopPoint(point);
            }
        };
    }
}
