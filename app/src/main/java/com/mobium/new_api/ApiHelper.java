package com.mobium.new_api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobium.new_api.converters.Filter_Converter;
import com.mobium.new_api.converters.PaymentServiceConverter;
import com.mobium.new_api.converters.ShopItem_Converter;
import com.mobium.new_api.converters.ShopItems_Converter;
import com.mobium.new_api.models.Request;
import com.mobium.new_api.models.RequestExtra;
import com.mobium.new_api.models.catalog_.Filter_;
import com.mobium.new_api.models.catalog_.ShopItem_;
import com.mobium.new_api.models.catalog_.ShopItems_;
import com.mobium.new_api.models.order.PaymentTypeService;
import com.mobium.reference.ReferenceApplication;
import com.mobium.userProfile.ProfileApi;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Inject;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 *  on 19.06.15.
 * http://mobiumapps.com/
 *
 *
 * Gives ability to create and send request, handle response
 */
public class ApiHelper {
    private String codeRevision;
    private String version;
    private String protocolVersion;
    private String buildId;
    private String key;

    protected String methodPath;
    protected ApiInterface rawApi;
    
    private String appId;

    protected ApiHelper() {
        ReferenceApplication.getInstance().inject(this);
    }

    @Inject
    protected OkHttpClient okClient;

    public void init(Context context,
                     String codeRevision,
                     String version,
                     String protocolVersion,
                     String buildId,
                     String key,
                     String shopUrl,
                     String appId,
                     boolean debug) {
        this.codeRevision = codeRevision;
        this.version = version;
        this.protocolVersion = protocolVersion;
        this.buildId = buildId;
        this.key = key;
        this.appId = appId;

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Filter_.class, new Filter_Converter())
                .registerTypeAdapter(ShopItem_.class, new ShopItem_Converter())
                .registerTypeAdapter(ShopItems_.class, new ShopItems_Converter())
                .registerTypeAdapter(PaymentTypeService.class, new PaymentServiceConverter())
                .create();



        //split url to base url and method path
        //for retrofit
        int lastSlash = shopUrl.lastIndexOf("/");
        String baseUrl = shopUrl.substring(0, lastSlash);

        methodPath = shopUrl.substring(lastSlash + 1, shopUrl.length());


        rawApi = new RestAdapter.Builder()
                .setClient(new OkClient())
                .setEndpoint(baseUrl)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(debug ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .setRequestInterceptor(request ->
                            request.addHeader("content-type", "application/json")
                )
                .build()
                .create(ApiInterface.class);

    }

    protected <T> Request<T> fillRequest(Request<T> request) {
        request.appId = appId;
        request.buildId = buildId;
        request.codeRevision = codeRevision;
        request.key = key;
        request.protocolVersion = protocolVersion;
        request.version = version;
        return request;
    }


    protected Request createRequest(String method) {
        return new Request(
                method,
                codeRevision,
                version,
                protocolVersion,
                buildId,
                key,
                appId
                );
    }

    protected  <T> Request<T> createRequest(String method, T param) {
        return new Request<T>(
                method,
                codeRevision,
                version,
                protocolVersion,
                buildId,
                key,
                appId
        ).setParam(param);
    }

    protected  <T, E>
    RequestExtra<T, E> createRequest(String method, T param, E extra) {
        return new RequestExtra<T, E>(
                method,
                codeRevision,
                version,
                protocolVersion,
                buildId,
                key,
                appId
        ).setParamExtra(param, extra);
    }

    public void setAppId(String appId) {
        this.appId = appId;
        ProfileApi.getInstance().setAppId(appId);
    }

    public String getAppId() {
        return appId;
    }
}
