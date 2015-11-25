package com.mobium.userProfile;

import android.support.annotation.Nullable;

import com.mobium.client.ShopDataStorage;
import com.mobium.reference.ReferenceApplication;
import com.mobium.userProfile.CallBack.ProfileCallBack;
import com.mobium.userProfile.ResponseParams.Activation;
import com.mobium.userProfile.ResponseParams.Authorization;
import com.mobium.userProfile.ResponseParams.OrderList;
import com.mobium.userProfile.ResponseParams.ProfileInfo;
import com.mobium.userProfile.ResponseParams.PushStatus;
import com.mobium.userProfile.ResponseParams.RegField;
import com.mobium.userProfile.ResponseParams.RegFields;
import com.mobium.userProfile.ResponseParams.Registration;
import com.mobium.userProfile.ResponseParams.SessionCheck;
import com.squareup.okhttp.OkHttpClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *  on 12.06.15.
 * http://mobiumapps.com/
 */

public class ProfileApi {

    private volatile static ProfileApi instance;

    private RawProfileApiInterface apiInterface;
    private String appId;
    private String platform;
    private String baseUrl;
    private String accessToken;

    private String regFieldsUrl = "regFields";
    private String ordersUrl = "orders/";
    private String checkAuthUrl = "checkAuth/";
    private String profileUrl = "profile/";
    private String pushUrl = "push/";
    private String authorizationUrl = "auth/";
    private String registrationUrl = "register/";
    private String activationUrl = "confirm/";
    private String exitUrl = "disauth/";
    private String resendCodeUrl = "resend";

    @Inject
    protected OkHttpClient client;

    public static void init(String appId, String platform, String baseUrl, String accessToken, boolean debug) {
        getInstance().setParams(appId, platform, baseUrl, accessToken, debug);
    }

    public static ProfileApi getInstance() {
        ProfileApi localInstance = instance;
        if (localInstance == null) {
            synchronized (ProfileApi.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ProfileApi();
                }
            }
        }
        return localInstance;
    }

    private ProfileApi() {
        ReferenceApplication.getInstance().inject(this);
    }

    private void setParams(String appId, String platform, String baseUrl, String accessToken, boolean debug) {
        this.appId = appId;
        this.platform = platform;
        this.baseUrl = baseUrl;
        this.accessToken = accessToken;
        this.apiInterface = buildApi(debug, client);
    }

    private RawProfileApiInterface buildApi(boolean debug, OkHttpClient client) {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .setClient(new OkClient(client))
                .setLogLevel(debug ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .build();

        return adapter.create(RawProfileApiInterface.class);
    }


    public rx.Observable<Response<RegFields>> getRegFields() {
        return apiInterface.getRegFieldsAsync(regFieldsUrl);
    }


    public void getOrders(ProfileCallBack<OrderList> callBack) {
        apiInterface.getOrdersAsync(ordersUrl, accessToken, appId, callBack);
    }


    public void makeCheckAuth(ProfileCallBack<SessionCheck> callBack) {
        apiInterface.makeCheckAuth(checkAuthUrl, accessToken, appId, callBack);
    }


    public void getProfileInfo(ProfileCallBack<ProfileInfo> callBack) {
        apiInterface.getProfileInfo(profileUrl, accessToken, appId, callBack);
    }


    public void getPushStatus(ProfileCallBack<PushStatus> callBack) {
        apiInterface.getPushStatus(pushUrl, accessToken, appId, callBack);
    }


    public void makeAuthorization(String userLogin, String userPassword, ProfileCallBack<Authorization> callBack) {
        /**
         handling accessToken from callback
         */


        ProfileCallBack<Authorization> wrappingCallBack = new ProfileCallBack<Authorization>() {
            @Override
            public void onSuccess(Authorization data) {
                accessToken = data.accessToken;
                callBack.onSuccess(data);
                ShopDataStorage.getInstance().setProfileAccessToken(accessToken);
            }

            @Override
            public void onAbort(Response.ResponseStatus type, @Nullable Authorization data) {
                callBack.onAbort(type, data);
            }

            @Override
            public void onError(RetrofitError error) {
                callBack.onError(error);
            }
        };

        apiInterface.makeAuthorization(authorizationUrl, appId, platform, userLogin, userPassword, wrappingCallBack);
    }


    public void makeRegistration(List<RegField> regFields, ProfileCallBack<Registration> callBack) {
        apiInterface.makeRegistration(registrationUrl, createRegistrationBody(appId, platform, regFields), callBack);
    }

    public rx.Observable<Response<Registration>> makeRegistration(List<RegField> regFields) {
        return apiInterface.makeRegistration(registrationUrl, createRegistrationBody(appId, platform, regFields));
    }

    public rx.Observable<Response<Activation>> makeActivation(String confirmToken, String code) {
        return apiInterface.makeActivation(activationUrl, confirmToken, code, appId, platform)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(activation -> {
                    if (activation.data != null && activation.data.accessToken != null) {
                        accessToken = activation.data.accessToken;
                        ShopDataStorage.getInstance().setProfileAccessToken(accessToken);
                    }
                });
    }


    public void makeActivation(String confirmToken, String code, ProfileCallBack<Activation> callBack) {
        ProfileCallBack<Activation> wrappingCallBack = new ProfileCallBack<Activation>() {
            @Override
            public void onSuccess(Activation data) {
                accessToken = data.accessToken;
                callBack.onSuccess(data);
            }

            @Override
            public void onAbort(Response.ResponseStatus type, @Nullable Activation data) {
                callBack.onAbort(type, data);
            }

            @Override
            public void onError(RetrofitError error) {
                callBack.onError(error);
            }
        };

        apiInterface.makeActivation(activationUrl, confirmToken, code, wrappingCallBack);
    }


    public void makeExit(ProfileCallBack<Object> callBack) {
        apiInterface.makeExit(exitUrl, accessToken, appId, callBack);
    }


    public void editPush(Boolean send, ProfileCallBack<Object> callBack) {
        apiInterface.editPush(pushUrl, accessToken, appId, send, callBack);
    }

    public void resendCode(String confirmToken, ProfileCallBack<Object> d) {
        apiInterface.resendCode(resendCodeUrl, appId, platform, confirmToken, d);
    }

    public Response<OrderList> getOrders() throws Exception {
        return apiInterface.getOrders(ordersUrl, accessToken, appId);
    }


    /**
     * create body for registration post request
     *
     * @param applicationId application id
     * @param platformId    android
     * @param regFields     list of filled regFields. Filled means RegFiled::value != null
     * @return Map for Registration request body
     */
    private static Map<String, String>
    createRegistrationBody(String applicationId,
                           String platformId,
                           List<RegField> regFields) {
        return new HashMap<String, String>(regFields.size() + 2) {
            {
                put("appId", applicationId);
                put("platform", platformId);
                for (RegField field : regFields)
                    put(field.id, field.getValue());
            }
        };
    }

    public void setAppId(String appId) {
        if (appId != null)
            this.appId = appId;
    }

    public ProfileApi setExitUrl(String exitUrl) {
        this.exitUrl = exitUrl;
        return this;
    }

    public ProfileApi setRegFieldsUrl(String regFieldsUrl) {
        this.regFieldsUrl = regFieldsUrl;
        return this;
    }

    public ProfileApi setListOrdersUrl(String ordersUrl) {
        this.ordersUrl = ordersUrl;
        return this;
    }

    public ProfileApi setCheckAuthUrl(String checkAuthUrl) {
        this.checkAuthUrl = checkAuthUrl;
        return this;
    }

    public ProfileApi setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
        return this;
    }

    public ProfileApi setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
        return this;
    }

    public ProfileApi setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
        return this;
    }

    public ProfileApi setRegistrationUrl(String registrationUrl) {
        this.registrationUrl = registrationUrl;
        return this;
    }

    public ProfileApi setActivationUrl(String activationUrl) {
        this.activationUrl = activationUrl;
        return this;
    }

    public ProfileApi setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

}
