package com.mobium.reference.fragments.shop_info;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobium.new_api.models.ShopPoint;
import com.mobium.reference.R;
import com.mobium.reference.activity.MainDashboardActivity;
import com.mobium.reference.utils.Dialogs;


/**
 *
 *   Simonov
 * Date: 17.07.13
 * Time: 14:11
 * To change this template use File | Settings | File Templates.
 */
public class MapFragment extends SupportMapFragment {
    private GoogleMap map;
    private ShopPoint [] points;

    public MapFragment() {
    }

    protected String getTitle() {
        return getString(R.string.map_title);
    }

    public void setShopPoints(ShopPoint... cityShops) {
        this.points = cityShops;
    }

    private void setMarkers() {
        if (map == null) {
            map = getMap();
        }

        if (points == null) {

            map.addMarker(new MarkerOptions()
                    .position(new LatLng(60 / 1e6f, 30 / 1e6f))
                    .title("Адрес"))
                    .setSnippet("время");
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(60 / 1e6f, 30 / 1e6f), 15));

            return;
        }
        long latMin = Long.MAX_VALUE;
        long pointsCount = 0;
        long lngSum = 0;
        long latSum = 0;
        long lngMax = Long.MIN_VALUE;
        long lngMin = Long.MAX_VALUE;
        long latMax = Long.MIN_VALUE;
        for (ShopPoint point : points ) {
//            if (!point.hasCoordinates())
//                continue;
//
//            long lat = point.ge();
//            long lng = point.getLon();
//
//            if(lat == 0 || lng == 0)
//                continue;
//            pointsCount++;
//
//
//
//            latMin = latMin > lat ? lat : latMin;
//            latMax = latMax < lat ? lat : latMax;
//
//            lngMin = lngMin > lng ? lng : lngMin;
//            lngMax = lngMax < lng ? lng : lngMax;
//
//            latSum += lat;
//            lngSum += lng;

//            LatLng latLng = new LatLng(lat / 1e6f, lng / 1e6f);
//
//            String hours = point.getWorkday() == null ? "Часы работы: " + point.getWorkday() : "";
//
//            map.addMarker(new MarkerOptions()
//                    .position(latLng)
//                    .title(point.getAddress()))
//                    .setSnippet(hours + '\n' + "Телефон: " + point.getPhone());
        }
        map.setOnInfoWindowClickListener(marker -> {
            String phone = marker.getSnippet().substring(marker.getSnippet().lastIndexOf("Телефон:"));
            if (!phone.isEmpty())
                Dialogs.doCallWithDialog(getActivity(), phone, marker.getTitle());
        });

        if (pointsCount > 0) {
            latSum /= pointsCount;
            lngSum /= pointsCount;
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latSum / 1e6f, lngSum / 1e6f), 15));
        }

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }

    public void onStart() {
        super.onStart();
        ((MainDashboardActivity) getActivity()).updateTitle(getString(R.string.map_title));
        setMarkers();
    }
}
