package com.mobium.client.api;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.mobium.client.api.networking.ApiInterface;
import com.mobium.client.api.networking.NetworkingException;
import com.mobium.reference.ReferenceApplication;
import com.mobium.reference.utils.executing.ExecutingException;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import javax.inject.Inject;

/**
 *
 *
 * Date: 27.11.12
 * Time: 21:15
 */

public class ShopApiInterface implements ApiInterface {
    private boolean isDebug;
    private String apiUrl;
    private String deviceId;
    private String installationId;
    private String appVersion;
    private String buildId;
    private String cityKey;
    private String codeRevision;
    private String authToken;
    private String categoryId;

    private String appId;

    @Inject
    protected OkHttpClient okClient;

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("application/json; charset=utf-8");


    private final Object registerAppLocker = new Object();

    public ShopApiInterface(Context context,
                            boolean isDebug,
                            String apiUrl,
                            String deviceId,
                            String installationId,
                            String appVersion,
                            String buildId,
                            String codeRevision,
                            String appId
                            ) {
        ReferenceApplication.getInstance().inject(this);
        this.isDebug = isDebug;
        this.apiUrl = apiUrl;
        this.deviceId = deviceId;
        this.installationId = installationId;
        this.appVersion = appVersion;
        this.buildId = buildId;
        this.codeRevision = codeRevision;
        this.appId = appId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCityKey() {
        return cityKey;
    }

    public void setCityKey(String cityKey) {
        this.cityKey = cityKey;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    private String createRequest(String method, JSONObject args) throws JSONException {
        JSONObject res = new JSONObject();
        res.put("request", method);
        res.put("key", "d7ea9ac1-8eb0-44f8-809d-bff6944db6c7");
        res.put("version", appVersion);
        res.put("protocolVersion", "1.7");
        res.put("codeRevision", codeRevision);
        res.put("buildId", buildId);

        if (authToken != null && !authToken.isEmpty()) {
            res.put("authToken", authToken);
        }

        if (args.has("#!guid")) {
            res.put("guid", args.getString("#!guid"));
        }

        if (args.has("#!extra_categoryId")) {
            categoryId = args.getString("#!extra_categoryId");
            args.remove("#!extra_categoryId");
        }

        String itemId = null;
        if (args.has("#!extra_accessories_productId")) {
            itemId = args.getString("#!extra_accessories_productId");
            args.remove("#!extra_accessories_productId");
        }

        Integer offset = null;
        if (args.has("#!offset")) {
            offset = args.getInt("#!offset");
            args.remove("#!offset");
        }

        Integer limit = null;
        if (args.has("#!limit")) {
            limit = args.getInt("#!limit");
            args.remove("#!limit");
        }
        String regionId = null;
        if (args.has(EXTRA_REGION_ID)) {
            regionId = args.getString(EXTRA_REGION_ID);
            args.remove(EXTRA_REGION_ID);
        }

        if (cityKey != null || categoryId != null || offset != null || limit != null || regionId != null) {
            JSONObject extraObj = new JSONObject();
            if (cityKey != null && !"".equals(cityKey)) {
                extraObj.put("cityKey", cityKey);
            }
            if (categoryId != null)
                extraObj.put("categoryId", categoryId);

            if (itemId != null)
                extraObj.put("accessories_productId", itemId);

            if (offset != null)
                extraObj.put("offset", offset);

            if (limit != null)
                extraObj.put("limit", limit);

            if (regionId != null)
                extraObj.put("regionId", regionId);

            res.put("extra", extraObj);


        }

        res.put("param", createArgs(args));
        if (appId != null) {
            res.put("appId", appId);
        }
        return res.toString();
    }

    private static Object createArgs(JSONObject args) throws JSONException {
        if (args.has("#!guid")) {
            JSONObject res = new JSONObject(args.toString());
            res.remove("#!guid");
            args = res;
        }

        if (args.length() == 1) {
            if (args.has("plain_id")) {
                return args.getString("plain_id");
            }
        }
        if (args.length() == 0) {
            return new JSONObject();
        }

        return args;
    }

    @Override
    public JSONObject DoApiRequest(String method, JSONObject args) throws NetworkingException, ExecutingException {
        return doApiRequestInternal(method, args);
    }

    private JSONObject doApiRequestInternal(String method, JSONObject args) throws NetworkingException {
        String requeststr = null;
        try {
            requeststr = createRequest(method, args);
        } catch (JSONException e) {
            throw new NetworkingException();
        }

        Log.d("Multishop", "Requsest: " + requeststr);


        long start = 0;
        if (isDebug) {
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

            if(isDebug)
                Log.d("Multishop", "ResponseSize: " + data.length);

            String res = new String(data, "utf-8");

            if(isDebug)
                Log.d("Multishop", res);
            return new JSONObject(res);
        } catch (Exception e) {

            e.printStackTrace();
            throw new NetworkingException(e);
        } finally {
            if (isDebug) {
                Log.d("BaseApplication", requeststr);
                Log.d("BaseApplication", "request time: " + (System.currentTimeMillis() - start) + " ms");
            }
        }
    }

//    private JSONObject doApiRequestInternalOld(String method, JSONObject args) throws NetworkingException {
//        String request = null;
//        try {
//            request = createRequest(method, args);
//        } catch (JSONException e) {
//            throw networkingException;
//        }
//        Log.d("Multishop", "Requsest: " + request);
//
//
//        long start = 0;
//        if (isDebug) {
//            start = System.currentTimeMillis();
//        }
//        InputStream instream = null;
//        try {
//
//            HttpPost post = new HttpPost(apiUrl);
//            post.addHeader("Accept-Encoding", "gzip");
//            post.setEntity(new StringEntity(request, "utf-8"));
//            HttpResponse response = client.execute(post);
//            HttpParams httpParameters = new BasicHttpParams();
//
//
//            instream = response.getEntity().getContent();
//            Header contentEncoding = response.getFirstHeader("Content-Encoding");
//            if (contentEncoding != null && "gzip".equalsIgnoreCase(contentEncoding.getValue())) {
//                instream = new GZIPInputStream(instream);
//            }
//
//            byte[] data = getBytes(instream);
//            instream.close();
//
//            response.getEntity().consumeContent();
//
//            Log.d("Multishop", "ResponseSize: " + data.length);
//            String res = new String(data, "utf-8");
//            Log.d("Multishop", res);
//            return new JSONObject(res);
//        } catch (Exception e) {
//
//            e.printStackTrace();
//            throw new NetworkingException(e);
//        } finally {
//            if (instream != null) {
//                try {
//                    instream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (isDebug) {
//                Log.d("BaseApplication", request);
//                Log.d("BaseApplication", "request time: " + (System.currentTimeMillis() - start) + " ms");
//            }
//        }
//    }


    public ShopApiInterface setAppId(String appId) {
        this.appId = appId;
        return this;
    }
}
