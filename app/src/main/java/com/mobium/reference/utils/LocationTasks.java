package com.mobium.reference.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.mobium.new_api.Api;
import com.mobium.new_api.Handler;
import com.mobium.new_api.methodParameters.GetRegionsParam;
import com.mobium.new_api.models.MobiumError;
import com.mobium.new_api.models.RegionList;




/**
 *  on 24.09.15.
 */
public class LocationTasks {

    public static void getRegionsAsync(Activity activity, Functional.Receiver<RegionList> receiver) {
        Api.getInstance().GetRegions(0, null, null).invoke(new Handler<GetRegionsParam, RegionList>() {
            @Override
            public void onResult(RegionList regionList) {
                receiver.onReceive(regionList);
            }

            @Override
            public void onBadArgument(MobiumError mobiumError, GetRegionsParam getRegionsParam) {
                showRepeatError(mobiumError == null || mobiumError.description == null
                        ? "Ошибка на стороне сервера" : mobiumError.description);
            }

            @Override
            public void onException(Exception ex) {
                showRepeatError(ex.getMessage());
            }

            private void showRepeatError(@NonNull String message) {
                new AlertDialog.Builder(activity)
                        .setTitle("Ошибка при получении регионов")
                        .setMessage(message)
                        .setPositiveButton("Повторить", (dialog, which) -> {
                            getRegionsAsync(activity, receiver);
                        })
                        .setCancelable(false).show();
            }
        });
    }

}
