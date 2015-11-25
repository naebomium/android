package com.mobium.reference.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.mobium.client.ShopDataStorage;
import com.mobium.google_places_api.PlaceHttpApi;
import com.mobium.google_places_api.models.AutoCompleteResult;
import com.mobium.google_places_api.models.AutoCompleteType;
import com.mobium.new_api.models.Region;
import com.mobium.reference.activity.ChoiceRegionActivity;
import com.mobium.reference.utils.Functional;
import com.mobium.config.common.Config;
import com.mobium.reference.views.ChangeRegionUtils;


/**
 *  on 15.10.15.
 *
 * Пользователь вводит регион, ему покаываются варианты ввода из ShopStorage;
 * Если он ввел что-то, чего нет в вариантах ввода, данные для ввода получать из автодополнения
 *
 * для автодополнения использовать getAutoCompleteAsync
 *
 * отправка результата: пользователь кликает на элемент списка, показывается диалог "вы уверены, что это ваш город"?
 * да -> один из sendresult в зависимости от типа данных
 *
 */
public class ChouceRegionFragment extends InjectAbstractFragment{
    private PlaceHttpApi api;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        api = ChangeRegionUtils.getApi(this.getActivity(), Config.get().getApplicationData().getApiKeyGooglePlaceApi(), Config.get().logDebugInfo());
    }



    private void getAutoCompleteAsync(String query, Functional.Receiver<AutoCompleteResult.Item[]> receiver) {
        showProgress();
        new AsyncTask<String, Void, AutoCompleteResult.Item[]>() {

            @Override
            protected void onPostExecute(AutoCompleteResult.Item[] items) {
                super.onPostExecute(items);
                receiver.onReceive(items);
            }

            @Override
            protected AutoCompleteResult.Item[] doInBackground(String... params) {
                try {
                    return api.getAutoComplete(
                            params[0],
                            new AutoCompleteType[]{AutoCompleteType.cities},
                            null,
                            null,
                            null,
                            null,
                            "country:ru",
                            null
                    ).getPredictions().get();
                } catch (Throwable e) {
                    getActivity().runOnUiThread(() -> showError(e));
                    return new AutoCompleteResult.Item[0];
                }
            }

        }.execute(query);
    }

    private void showError(Throwable e) {

    }

    private void showProgress() {

    }



    private void sendResult(AutoCompleteResult.Item item) {
        ChangeRegionUtils.sendResult(ShopDataStorage.getInstance().getRegions(), item, this::sendResult);
    }

    private void sendResult(Region region) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(ChoiceRegionActivity.REQUEST_EXTRA_CURRENT_REGION, region);
        getTargetFragment().onActivityResult(getTargetRequestCode(), ChoiceRegionActivity.RESULT_OK, resultIntent);
    }
    @Override
    public int getLayoutRes() {
        return 0;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        api = null;
    }
}
