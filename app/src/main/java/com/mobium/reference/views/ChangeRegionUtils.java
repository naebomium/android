package com.mobium.reference.views;


import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.annimon.stream.Stream;
import com.mobium.google_places_api.PlaceHttpApi;
import com.mobium.google_places_api.models.AutoCompleteResult;
import com.mobium.new_api.models.Region;
import com.mobium.new_api.utills.RegionUtils;
import com.mobium.reference.R;
import com.mobium.reference.activity.AutoRegionActivity;
import com.mobium.reference.utils.Functional;
import com.mobium.reference.utils.ImageUtils;
import com.mobium.config.common.Config;

import java.util.List;

/**
 *  on 08.10.15.
 */
public class ChangeRegionUtils {
    private volatile static PlaceHttpApi api;

    public static PlaceHttpApi getApi(Context context, String apiKey, boolean debug) {
        if (api == null)
            api = PlaceHttpApi.getInstance(apiKey, Config.get().logDebugInfo());
        return api;
    }

    public interface DataProvider {
        List<Region> regions();
        String title();
        String message();
    }

    public static void showAutoCompleteDialog(Activity activity, DataProvider provider, Functional.Receiver<Region> receiver) {

        LayoutInflater inflater = activity.getLayoutInflater();

        View view = inflater.inflate(R.layout.view_loading_edit_text, null, false);
        DelayAutoCompleteTextView autoCompleteTextView = (DelayAutoCompleteTextView) view.findViewById(R.id.autoText);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
        autoCompleteTextView.setLoadingIndicator(progressBar);

        autoCompleteTextView.setTextColor(activity.getResources().getColor(R.color.white_text));
        autoCompleteTextView.setHint(null);

        int dp16 = ImageUtils.convertToPx(activity, 16);
        int dp4 = ImageUtils.convertToPx(activity, 4);

        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(provider.title())
                .setMessage(provider.message())
                .setView(view, dp16, dp4, dp16, dp4)
                .create();

        autoCompleteTextView.setAdapter(new AutoRegionActivity.AutoAdapter(getApi(activity, Config.get().getApplicationData().getApiKeyGooglePlaceApi(), false)));


        autoCompleteTextView.setOnItemClickListener((parent, view1, position, id) -> {
            AutoCompleteResult.Item currentItem = (AutoCompleteResult.Item) autoCompleteTextView.getAdapter().getItem(position);
            dialog.dismiss();
            sendResult(provider.regions(), currentItem, receiver);
        });

        dialog.show();
    }

    public static void sendResult(List<Region> regions, AutoCompleteResult.Item item, Functional.Receiver<Region> receiver) {
        Region result =
                Stream.of(regions)
                        .filter(region -> region.getGooglePlacesId().orElse("").equals(item.getPlace_id()))
                        .findFirst()
                        .orElseGet(() -> {
                            Region defaultRegion = RegionUtils.getOptionalDefaultRegion(regions).orElse(Region.getRussiaRegion());
                            defaultRegion.setTitle(item.getDescription());
                            return defaultRegion;
                        });

        receiver.onReceive(result);
    }
}
