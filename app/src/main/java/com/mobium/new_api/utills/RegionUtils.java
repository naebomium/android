package com.mobium.new_api.utills;

import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.mobium.google_places_api.PlaceHttpApi;
import com.mobium.google_places_api.models.AutoCompleteResult;
import com.mobium.google_places_api.models.AutoCompleteType;
import com.mobium.new_api.Api;
import com.mobium.new_api.Handler;
import com.mobium.new_api.methodParameters.GetRegionsParam;
import com.mobium.new_api.models.MobiumError;
import com.mobium.new_api.models.Region;
import com.mobium.new_api.models.RegionList;
import com.mobium.reference.utils.Dialogs;
import com.mobium.reference.utils.Functional;
import com.mobium.reference.utils.LocationUtil;
import com.mobium.config.common.Config;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 *  on 26.08.15.
 */
public class RegionUtils {
    private static PlaceHttpApi api;

    public static
    @Nullable
    Region getDefaultRegion(@NonNull List<Region> regions) {
        for (Region region : regions)
            if (region.getType().equals(Region.Type.DEFAULT))
                return region;
        return null;
    }

    public static
    @NonNull
    Optional<Region> getOptionalDefaultRegion(@NonNull List<Region> regions) {
        for (Region region : regions)
            if (region.getType().equals(Region.Type.DEFAULT))
                return Optional.of(region);
        return Optional.empty();
    }

    public static
    @NonNull
    Region getRegionFormGooglePlaceItems(@NonNull AutoCompleteResult.Item arg, @NonNull Region defaultValue, @Nullable List<Region> regions) {
        if (regions == null) {
            defaultValue.setTitle(arg.getDescription());
            return defaultValue;
        } else {
            return Stream.of(regions)
                    .filter(r -> r.getGooglePlacesId().orElse("Not supported").equals(arg.getPlace_id()))
                    .findFirst()
                    .orElseGet(() -> {
                        defaultValue.setTitle(arg.getDescription());
                        return defaultValue;
                    });
        }
    }

    public static void loadRegionsByAutoCompleteRegion(@NonNull AutoCompleteResult.Item arg, Functional.Receiver<RegionAndList> onResult, Functional.Receiver<Exception> onError) {
        Api.getInstance().GetRegions(0, arg.getPlace_id(), arg.toString().split(",")[0]).invoke(new Handler<GetRegionsParam, RegionList>() {
            @Override
            public void onResult(RegionList regionList) {
                RegionAndList regionAndList = null;
                try {
                    List<Region> regions = regionList.getRegions();
                    Optional<Region> defaultRegion = getOptionalDefaultRegion(regions);
                    Region russiaRegion = Region.getRussiaRegion();

                    Region result = getRegionFormGooglePlaceItems(arg, defaultRegion.orElse(russiaRegion), regions);

                    regionAndList = new RegionAndList(result, regions);
                    onResult.onReceive(regionAndList);
                } catch (Exception e) {
                    e.printStackTrace();
                    onException(e);
                }
            }

            @Override
            public void onBadArgument(MobiumError mobiumError, GetRegionsParam getRegionsParam) {
                onException(new IllegalArgumentException(mobiumError.description));
            }

            @Override
            public void onException(Exception ex) {
                onError.onReceive(ex);
            }
        });
    }

    public static void loadRegionByLocation(Location mLocation,
                                            Functional.Receiver<Optional<AutoCompleteResult.Item>> itemReceiver,
                                            Functional.Receiver<Exception> exceptionReceiver) {
        String getCityUrl = LocationUtil.getFullURL(mLocation, 1);

        String placeKey = Config.get().getApplicationData().getApiKeyGooglePlaceApi();

        if (api == null)
            api = PlaceHttpApi.getInstance(placeKey, Config.get().logDebugInfo());
        try {
            LocationUtil.get(getCityUrl).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    exceptionReceiver.onReceive(e);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String jsonData = response.body().string();
                    try {
                        String cityName = getCurrentDetails(jsonData);
                        AutoCompleteResult result = api.getAutoComplete(
                                cityName,
                                new AutoCompleteType[]{AutoCompleteType.cities},
                                null,
                                null,
                                null,
                                null,
                                "country:ru",
                                null
                        );
                        Optional<AutoCompleteResult.Item> perhapsRegion =
                                Stream.of(result.getPredictions().orElse(new AutoCompleteResult.Item[]{}))
                                        .findFirst();

                        itemReceiver.onReceive(perhapsRegion);

                    } catch (Exception e) {
                        e.printStackTrace();
                        exceptionReceiver.onReceive(e);
                    }
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
            exceptionReceiver.onReceive(e);
        }
    }

    public static void loadRegionByLocationWithDialog(Activity activity, Location mLocation, Functional. Receiver<Optional<AutoCompleteResult.Item>> itemReceiver) {
        activity.runOnUiThread(() -> {
            ProgressDialog dialog = new ProgressDialog(activity);
            dialog.setCancelable(false);
            dialog.setTitle("Загрузка регионов");
            dialog.show();

            loadRegionByLocation(mLocation, item -> {
                dialog.dismiss();
                itemReceiver.onReceive(item);
            }, e -> Dialogs.showOnlyRepeatDialog(activity, e.getMessage(), () -> loadRegionByLocationWithDialog(activity, mLocation, itemReceiver)));
        });
    }

//    private void onUiResult(Optional<AutoCompleteResult.Item> perhapsRegion) {
//        if (perhapsRegion.isPresent()) {
//            new AlertDialog.Builder(MainDashboardActivity.this)
//                    .setMessage(String.format("Ваш регион '%s'?", perhapsRegion.get().getDescription()))
//                    .setPositiveButton("Да", (dialogInterface, i) -> {
//                        onUserSelectedRegion(perhapsRegion.get());
//                    })
//                    .setNegativeButton("Нет", (dialogInterface1, i1) -> {
//                        onRegionChanged(defaultRegion);
//                        doOpenRegionsScreen();
//                    })
//                    .setCancelable(false)
//                    .show();
//        } else {
//            doOpenRegionsScreen();
//        }
//    }

    public static void loadRegionsByAutoCompleteRegionWithDialog(Activity context, @NonNull AutoCompleteResult.Item arg, Functional.Receiver<RegionAndList> onResult) {
        context.runOnUiThread(() -> {
            //показать диалог
            ProgressDialog dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setTitle("Загрузка регионов");
            dialog.show();
            //загрузить регионы асинхронно
            loadRegionsByAutoCompleteRegion(arg, regionAndListResult -> {
                //в случае успеха свернуть диалог
                dialog.dismiss();
                onResult.onReceive(regionAndListResult);
            }, exception -> {
                //в случае ошибки показать диалог с кнопкой повторить
                exception.printStackTrace();
                dialog.dismiss();
                Dialogs.showOnlyRepeatDialog(
                        context,
                        "Невозможно определить регион \n" + exception.getMessage(),
                        () -> loadRegionsByAutoCompleteRegionWithDialog(context, arg, onResult)
                );

            });
        });
    }





    public static class RegionAndList {
        public final Region region;
        public final List<Region> regions;

        public RegionAndList(Region region, List<Region> regions) {
            this.region = region;
            this.regions = regions;
        }
    }


    private static String getCurrentDetails(String jsonData) throws JSONException {

        String responseFinal = null;

        JSONObject jObject = new JSONObject(jsonData);
        JSONObject responseField = jObject.getJSONObject("response")
                .getJSONObject("GeoObjectCollection");
        JSONArray responseArray = responseField.getJSONArray("featureMember");
        JSONObject responseCity = responseArray.getJSONObject(0);
        JSONObject responseGeoObject = responseCity.getJSONObject("GeoObject");
        responseFinal = responseGeoObject.getString("name");
        return responseFinal;
    }
}
