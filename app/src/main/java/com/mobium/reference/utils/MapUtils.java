package com.mobium.reference.utils;

import android.support.annotation.NonNull;
import com.annimon.stream.Stream;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobium.new_api.models.ShopPoint;
import com.mobium.reference.R;

import java.util.List;

/**
 *  on 31.07.15.
 * http://mobiumapps.com/
 */
public class MapUtils {

    public interface OnShopPointClickListener {
        void onClick(ShopPoint shopPoint);
    }


    public static void updateMapState(final @NonNull OnShopPointClickListener listener, @NonNull GoogleMap map, List<ShopPoint> points) {
        updateMapState(listener, map, points, null);
    }

    public static void updateMapState(final @NonNull OnShopPointClickListener listener, @NonNull GoogleMap map, List<ShopPoint> points, ShopPoint activePont) {
        if (points == null) {
            return;
        }

        long pointsCount = 0;
        long lngSum = 0;
        long latSum = 0;
        long lngMax = Long.MIN_VALUE;
        long lngMin = Long.MAX_VALUE;
        long latMax = Long.MIN_VALUE;
        long latMin = Long.MAX_VALUE;
        for (final ShopPoint point : points) {
            if (!point.getCoordinates().isPresent())
                continue;
            ShopPoint.Coordinates coordinates = point.getCoordinates().get();

            long lat = coordinates.getLat();
            long lng = coordinates.getLon();

            if (lat == 0 || lng == 0)
                continue;
            pointsCount++;


            latMin = latMin > lat ? lat : latMin;
            latMax = latMax < lat ? lat : latMax;

            lngMin = lngMin > lng ? lng : lngMin;
            lngMax = lngMax < lng ? lng : lngMax;

            latSum += lat;
            lngSum += lng;

            final LatLng latLng = new LatLng(lat / 1e6f, lng / 1e6f);

            final String hours = point.getWorkday().isPresent() ? "Часы работы: " + point.getWorkday().get() : "";


            final Marker marker =
                    map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(point.getAddress()));

            marker.setSnippet(hours + '\n' + "Телефон: " + point.getPhone().get());
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.app_icon));

            if(activePont != null && activePont.getCoordinates().isPresent()) {
                if(activePont.getCoordinates().get().equals(coordinates)) {
                    marker.showInfoWindow();
                }
            }

        }
        map.setOnInfoWindowClickListener(marker -> {
            final String title = marker.getTitle();
            Stream.of(points)
                    .filter(shopPoint -> shopPoint.getAddress().equals(title))
                    .findFirst()
                    .ifPresent(listener::onClick);

        });

        if(activePont != null) {
            if(activePont.getCoordinates().isPresent())
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(getLatLng(activePont.getCoordinates().get()), 5));
        } else if (pointsCount > 0) {
            latSum /= pointsCount;
            lngSum /= pointsCount;
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latSum / 1e6f, lngSum / 1e6f), 15));
        }

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }

    public static LatLng getLatLng(ShopPoint.Coordinates coordinates) {
        return new LatLng(coordinates.getLat() / 1e6f, coordinates.getLon() / 1e6f);
    }
}
