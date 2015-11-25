package com.mobium.client.api.networking;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mobium.client.api.Info;
import com.mobium.reference.ReferenceApplication;
import com.mobium.reference.utils.executing.ExecutingException;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

/**
 *  on 15.10.15.
 */
public class ExtraApiInterface implements IExtraApiInterface {

    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");
    private final Info info;
    private final String apiUrl;

    @Inject
    protected OkHttpClient okClient;

    private String appId;

    public ExtraApiInterface(Info info, String apiUrl, String appId) {
        this.info = info;
        this.apiUrl = apiUrl;
        this.appId = appId;
        ReferenceApplication.getInstance().inject(this);
    }


    @Override
    public JSONObject DoApiRequest(String method, JSONObject args, JSONObject extra) throws NetworkingException, ExecutingException {
        return doApiRequestInternal(method, args, extra);
    }

    private JSONObject doApiRequestInternal(String method, JSONObject args, JSONObject extra) throws NetworkingException {
        String requeststr = null;
        try {
            requeststr = createRequest(method, args, extra);
        } catch (JSONException e) {
            throw new NetworkingException(e);
        }

        Log.d("API", "Requsest: " + requeststr);

        long start = 0;
        if (info.isDebug) {
            start = System.currentTimeMillis();
        }

        try {
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, requeststr))
                    .addHeader("Accept-Encoding", "utf-8")
                    .build();

            Response response;
            try {
                response = okClient.newCall(request).execute();
                if (!response.isSuccessful())
                    throw new NetworkingException();
            } catch (Exception e) {
                throw new NetworkingException();
            }

            byte[] data = response.body().bytes();
            String res = new String(data, "utf-8");
            if (info.isDebug)
                Log.d("API", "Response : " + method + " time "+ (System.currentTimeMillis() - start) + "\n" + res);
            return new JSONObject(res);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NetworkingException(e);
        }
    }

    private String createRequest(String method, @Nullable JSONObject args, @NonNull JSONObject extra) throws JSONException {
        JSONObject res = new JSONObject();
        res.put("request", method);
        res.put("key", info.apiKey);
        res.put("version", info.appVersion);
        res.put("protocolVersion", info.protocolVersion);
        res.put("codeRevision", info.codeRevision);
        res.put("buildId", info.buildId);
        res.put("appId", appId);
        res.put("param", transFormArg(args));
        res.put("extra", extra);
        return res.toString();
    }

    private static Object transFormArg(JSONObject args) throws JSONException {
        if (args.length() == 1) {
            if (args.has("plain_id")) {
                return args.getString("plain_id");
            }
        }
        return args;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Override
    public JSONObject DoApiRequest(String method, JSONObject args) throws NetworkingException, ExecutingException {
        return DoApiRequest(method, args);
    }
}
