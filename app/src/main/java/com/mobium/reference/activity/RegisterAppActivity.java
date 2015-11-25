package com.mobium.reference.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.mobium.config.common.Config;
import com.mobium.new_api.Api;
import com.mobium.new_api.Handler;
import com.mobium.new_api.Method;
import com.mobium.new_api.methodParameters.GetRegionsParam;
import com.mobium.new_api.methodParameters.RegisterAppParam;
import com.mobium.new_api.models.MobiumError;
import com.mobium.new_api.models.Region;
import com.mobium.new_api.models.RegionList;
import com.mobium.new_api.utills.RegionUtils;
import com.mobium.reference.R;
import com.mobium.reference.utils.NetworkStateReceiver;
import com.mobium.reference.utils.NetworkUtils;
import com.mobium.reference.utils.storage.AppIdProvider;

import java.io.StringWriter;
import java.util.Collections;

/**
 *  on 23.06.15.
 * http://mobiumapps.com/
 */
public class RegisterAppActivity extends BaseStyledActivity implements NetworkStateReceiver.NetworkStateReceiverListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Method<RegisterAppParam, Api.StringWrapper> regAppMethod;
    private View loadView;
    private TextView hint;
    private final android.os.Handler handler = new android.os.Handler();

    private Runnable runWithNetwork;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMenuButtonState(ButtonState.gone);
        regAppMethod = application.RegAppAPI();
        final LayoutInflater inflater = getLayoutInflater();

        loadView = inflater.inflate(R.layout.fragment_progress, container, true);
        hint = ((TextView) loadView.findViewById(R.id.fragment_progress_hint));

        networkStateReceiver.addListener(this);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        if (api.getAppId() == null)
            registerAppTask();
        else if (
                storage.getRegions() == null ||
                        storage.getRegions().size() == 0) {
            regionTask();
        } else {
            sendResultAndFinish();
        }
    }


    // TODO:
    private void choiceRegion() {
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);

        result.setResultCallback((PlaceLikelihoodBuffer likelyPlaces) -> {
            final Region defaultRegion = RegionUtils.getDefaultRegion(storage.getRegions());

            PlaceLikelihood mostPossiblePlaceHood = null;

            for (PlaceLikelihood likelihood : likelyPlaces)
                if (mostPossiblePlaceHood == null ||
                        likelihood.getLikelihood() > mostPossiblePlaceHood.getLikelihood())
                    mostPossiblePlaceHood = likelihood;

            Region perhapsRegion = defaultRegion; // result of finding, by default = defaultRegion

            if (mostPossiblePlaceHood != null)
                for (Region region : storage.getRegions())
                    if (region.getGooglePlacesId().orElse("not support")
                            .equals(mostPossiblePlaceHood.getPlace().getId())) {
                        perhapsRegion = region;
                        break;
                    }

            if (perhapsRegion != null) {
                final Region finalPerhapsRegion = perhapsRegion;
                new AlertDialog.Builder(this)
                        .setMessage(String.format("Ваш регион '%s'?", finalPerhapsRegion.getTitle()))
                        .setPositiveButton("Да", (dialogInterface, i) -> {
                            storage.onChange(finalPerhapsRegion);
                            sendResultAndFinish();
                        })
                        .setNegativeButton("Нет", (dialogInterface1, i1) -> {
                            sendResultAndFinish();
                        })
                        .setCancelable(false)
                        .show();
            } else {
                sendResultAndFinish();
            }

            likelyPlaces.release();

        });
    }

    private void sendResultAndFinish() {
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }


    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.connect();
        super.onStop();
    }

    private void regionTask() {
        runWithNetwork = null;

        hint.setText("Получение списка регионов...");
        api.GetRegions(0, null, null).invoke(new Handler<GetRegionsParam, RegionList>() {
            @Override
            public void onResult(RegionList regionList) {
                if (regionList.getRegions() == null || regionList.getRegions().size() == 0)  // если RegionList пуст
                    storage.onReceive(Collections.singletonList(new Region("", "Россия", Region.Type.NONE))); // новый Регион -  Россия
                    // onReceive(List<Region> data)
                else {
                    storage.onReceive(regionList.getRegions());
                    final boolean googlePlacesEnabled = Region.PlacesService.google_places.equals(regionList.getService());

                    storage.setGooglePlacesEnabled(googlePlacesEnabled);
                    if (googlePlacesEnabled) {
                        choiceRegion();
                        sendResultAndFinish();

                    } else
                        sendResultAndFinish();
                }
                sendResultAndFinish();
            }


            @Override
            public void onBadArgument(MobiumError mobiumError, GetRegionsParam getRegionsParam) {
                hint.setText("Внутренняя ошибка сервера");
                handler.postDelayed(RegisterAppActivity.this::regionTask, 3000);
            }

            @Override
            public void onException(Exception ex) {
                hint.setText("Ошибка соедиения, ожидание подключения к интернету...");
                if (!NetworkUtils.isOnline(application)) {
                    runWithNetwork = RegisterAppActivity.this::regionTask;
                } else {
                    regionTask();
                }

            }
        });
    }


    private void registerAppTask() {
        runWithNetwork = null;

        hint.setText("Подготовка приложения...");
        regAppMethod.invoke(new Handler<RegisterAppParam, Api.StringWrapper>() {
            @Override
            public void onResult(Api.StringWrapper s) {
                AppIdProvider.putAppId(RegisterAppActivity.this, s.getValue());
                regionTask();
            }

            @Override
            public void onBadArgument(MobiumError mobiumError, RegisterAppParam registerAppParam) {
                hint.setText("Внутренняя ошибка сервера, переподключение...");
                handler.postDelayed(RegisterAppActivity.this::registerAppTask, 1500);
            }

            @Override
            public void onException(Exception ex) {
                hint.setText("Ошибка соединения, ожидание подключения к интернету...");
                if (!NetworkUtils.isOnline(application)) {
                    runWithNetwork = RegisterAppActivity.this::registerAppTask;
                } else {
                    registerAppTask();
                }
            }
        });
    }


    @Override
    public void onHaveInternetAccess() {
        if (runWithNetwork != null)
            runWithNetwork.run();
    }

    @Override
    public void networkUnavailable() {

    }


    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}


