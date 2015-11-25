package com.mobium.new_api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.annimon.stream.function.Supplier;
import com.mobium.client.models.ShoppingCart;
import com.mobium.new_api.cache.ICacheManager;
import com.mobium.new_api.cache.RamCache;
import com.mobium.new_api.methodParameters.AppStartParam;
import com.mobium.new_api.methodParameters.GetDeliveryMethodParam;
import com.mobium.new_api.methodParameters.GetRegionsParam;
import com.mobium.new_api.methodParameters.GetShopPointParam;
import com.mobium.new_api.methodParameters.NewOrderParam;
import com.mobium.new_api.methodParameters.OnEventParam;
import com.mobium.new_api.methodParameters.PostPushGoalParam;
import com.mobium.new_api.methodParameters.RegisterAppParam;
import com.mobium.new_api.methodParameters.RegisterPushTokenParam;
import com.mobium.new_api.methodParameters.SendMessageParam;
import com.mobium.new_api.models.BannerList;
import com.mobium.new_api.models.BooleanResponse;
import com.mobium.new_api.models.LocationFilters;
import com.mobium.new_api.models.Region;
import com.mobium.new_api.models.RegionList;
import com.mobium.new_api.models.Response;
import com.mobium.new_api.models.ResponseBase;
import com.mobium.new_api.models.ShopPoint;
import com.mobium.new_api.models.SimpleResult;
import com.mobium.new_api.models.StringResponse;
import com.mobium.new_api.models.catalog_.ShopItem_;
import com.mobium.new_api.models.catalog_.ShopItems_;
import com.mobium.new_api.models.order.DeliveryMethods;
import com.mobium.new_api.models.order.NewOrderResult;
import com.mobium.new_api.models.order.PaymentTypesInfo;
import com.mobium.reference.ReferenceApplication;

import okio.ByteString;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *  on 19.06.15.
 * http://mobiumapps.com/
 * <p>
 * list of api methods
 */

public class Api extends ApiHelper {
    public static final ICacheManager cacheManager = new RamCache();
    private static final String errorMassage = "Ошибка во время обмена данных";
    private static volatile Api instance;

    protected Api() {
        super();
    }


    public static Api getInstance() {
        Api localInstance = instance;
        if (localInstance == null) {
            synchronized (Api.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Api();
                }
            }
        }
        return localInstance;

    }


    public Method<RegisterAppParam, StringWrapper>
    RegisterApp(String platform,
                String os_version,
                String installation_id,
                String device_model,
                String device_id,
                String device_brand) {
        return new Method<>(
                (regAppParam, callBack)
                        -> rawApi.registerApp(
                        methodPath,
                        createRequest("RegisterApp", regAppParam),
                        wrapString(callBack)),
                new RegisterAppParam(
                        platform,
                        os_version,
                        installation_id,
                        device_model,
                        device_id,
                        device_brand),
                stringWrapper -> setAppId(stringWrapper.getValue())
        );
    }

    public Method<OnEventParam, SimpleResult>
    OnEvent(String eventName) {
        return new Method<>(
                (eventParam, callBack)
                        -> rawApi.onEvent(
                        methodPath,
                        createRequest("OnEvent", eventParam),
                        callBack),
                new OnEventParam(eventName)
        );
    }

    public Method<PostPushGoalParam, BooleanWrapper>
    PostPushGoal(String goal) {
        return new Method<>(
                (PostPushGoalParam postParam, Callback<Response<BooleanWrapper>> callback)
                        -> rawApi.postPushGoal(
                        methodPath,
                        createRequest("PostPushGoal", postParam),
                        wrapBool(callback)
                ),
                new PostPushGoalParam(goal)
        );
    }

    public Method<String, SimpleResult>
    PushReceived(String pushId) {
        return new Method<>(
                (id, callback)
                        -> rawApi.pushReceived(
                        methodPath,
                        createRequest("PushReceived", id),
                        callback),
                pushId
        );
    }


    public Method<RegisterPushTokenParam, SimpleResult>
    RegisterPushToken(String service, String token) {
        return new Method<>(
                (param, callback)
                        -> rawApi.registerPushToken(
                        methodPath,
                        createRequest("RegisterPushToken", param),
                        callback),
                new RegisterPushTokenParam(service, token)
        );
    }

    public Method<AppStartParam, BooleanWrapper>
    AppStart(String deviceId) {
        return new Method<>(
                (AppStartParam param, Callback<Response<BooleanWrapper>> callBack)
                        -> rawApi.appStart(
                        methodPath,
                        createRequest("AppStart", param),
                        wrapBool(callBack)),
                new AppStartParam(deviceId)
        );
    }

    public Response<BannerList> getBaners() {
        try {
            return new Response<>(cacheManager.getBannerList());
        } catch (Exception e) {
            e.printStackTrace();
            Response<BannerList> bannerListResponse =
                    rawApi.getBanners(methodPath, createRequest("GetBanners"));
            try {
                cacheManager.saveBannerList(bannerListResponse.result);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return bannerListResponse;
        }
    }

    public final SimpleMethod<BannerList> GetBanners() {
        return new SimpleMethod<>(callback
                -> {
            try {
                callback.success(new Response<>(cacheManager.getBannerList()), null);
            } catch (Exception e) {
                e.printStackTrace();
                rawApi.getBanners(
                        methodPath,
                        createRequest("GetBanners"),
                        callback);
            }
        }, bannerList -> {
            try {
                cacheManager.saveBannerList(bannerList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Method<GetRegionsParam, RegionList>
    GetRegions(int needPointsLimit, String google_id, String region_name) {
        return new Method<>(
                (param, callback) ->
                        rawApi.getRegions(
                                methodPath,
                                createRequest("GetRegions", param),
                                callback
                        ),
                new GetRegionsParam(needPointsLimit, google_id, region_name)
        );
    }

    public Method<GetShopPointParam, ShopPoint.ShopPointList>
    GetShopPoints(@NonNull Region region, @Nullable ShopPoint.ShopPointType type) {
        return GetShopPoints(region, type, null, null);
    }

    public Method<GetShopPointParam, ShopPoint.ShopPointList>
    GetShopPoints(@NonNull Region region, @Nullable ShopPoint.ShopPointType type, @Nullable String deliveryType, @Nullable ShoppingCart cart) {
        return new Method<>(
                (param, callback) ->
                        rawApi.getShopPoints(
                                methodPath,
                                createRequest("GetPoints", param),
                                callback
                        ),
                GetShopPointParam.getShopPointParam(region, type, deliveryType, cart)
        );
    }

    public Method<String, ShopItem_>
    GetShopItem(@NonNull String id) {
        return new Method<>(
                (param, callback) ->
                        rawApi.getShopItem(
                                methodPath,
                                createRequest("GetItem", param),
                                callback
                        ),
                id
        );
    }

    public Method<SendMessageParam, ResponseBase>
    SendMessage(@NonNull String message, @NonNull String email) {
        return new Method<>(
                (param, callback) ->
                        rawApi.sendMessage(methodPath,
                                createRequest("SendMessage", param),
                                new Callback<Response>() {
                                    @Override
                                    public void success(Response response, retrofit.client.Response response2) {
                                        callback.success(null, response2);
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        callback.failure(error);
                                    }
                                }),
                new SendMessageParam(email, message)
        );
    }

    public SimpleMethod<LocationFilters>
    GetPointFilters() {
        return new SimpleMethod<>(
                callBack -> rawApi.getLocationFilters(methodPath, createRequest("GetPointFilters"), callBack)
        );
    }

    public rx.Observable<ShopItems_> getShopItems(@NonNull String id) {
        return rx.Observable.<ShopItems_>create(subscriber -> {
            try {
                ShopItems_ items = cacheManager.getCategoryItems(id);
                subscriber.onNext(items);
                subscriber.onCompleted();
                Log.v("API", "getShopItems " + id + " : loaded cached" + items.items.size());
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }).onExceptionResumeNext(
                rawApi.getShopItems(methodPath, createRequest("GetItems", id))
                        .map(shopItem_response -> shopItem_response.result)
                        .doOnNext(items_ -> {
                            Log.v("API", "getShopItems " + id + " : loaded from web");
                            try {
                                cacheManager.saveCategoryItems(id, items_);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        })
        );
    }


    private <Result extends ResponseBase> Observable<Result> wrapByObservable(Supplier<Response<Result>> supplier) {
        return Observable.<Result>create(subscriber -> {
            try {
                Response<Result> response = supplier.get();
                if (response.success()) {
                    subscriber.onNext(response.result);
                    subscriber.onCompleted();
                } else throw new Throwable(response.error().description);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                subscriber.onError(new Throwable(errorMassage, throwable));
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<NewOrderResult> newOrder(NewOrderParam param) {
        return wrapByObservable(() -> rawApi.newOrder(methodPath, createRequest("NewOrder", param)));
    }

    public Observable<PaymentTypesInfo> getPayemntTypes() {
        return wrapByObservable(() -> rawApi.getPaymentTypes(methodPath, createRequest("GetPaymentTypes")));
    }

    public Observable<DeliveryMethods> getDeliveryMethods(GetDeliveryMethodParam param) {
        return wrapByObservable(() -> rawApi.getDeliveryMethods(methodPath, createRequest("GetDeliveryMethods", param)));
    }

    public Observable<ShopPoint[]> getShopPoints(GetShopPointParam param) {
        return wrapByObservable(() -> rawApi.getShopPoints(methodPath, createRequest("GetPoints", param))).map(list -> list.points);
    }


    public static class BooleanWrapper extends ResponseBase {
        private Boolean value;

        public Boolean getValue() {
            return value;
        }

        public void setValue(Boolean value) {
            this.value = value;
        }

        public BooleanWrapper() {
        }

        public BooleanWrapper(Boolean value) {
            this.value = value;
        }
    }


    public static class StringWrapper extends ResponseBase {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public StringWrapper(String value) {
            this.value = value;
        }

        public StringWrapper() {
        }
    }


    private static Callback<BooleanResponse> wrapBool(Callback<Response<BooleanWrapper>> wrapping) {
        return
                new Callback<BooleanResponse>() {
                    @Override
                    public void success(BooleanResponse booleanResponse, retrofit.client.Response response) {
                        if (booleanResponse.getResponse() != null)
                            wrapping.success(new Response<>(new BooleanWrapper(booleanResponse.getResponse())), response);
                        else
                            wrapping.failure(RetrofitError.unexpectedError(response.getUrl(),
                                    new NullPointerException("Boolean response is null")));

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        wrapping.failure(error);
                    }
                };
    }

    private static Callback<StringResponse> wrapString(Callback<Response<StringWrapper>> wrapping) {
        return new Callback<StringResponse>() {
            @Override
            public void success(StringResponse stringResponse, retrofit.client.Response response) {
                if (stringResponse.getResponse() == null) {
                    String description;
                    try {
                        String errorStr = ByteString.read(response.getBody().in(), (int) response.getBody().length()).utf8();
                        Error error =  ReferenceApplication.getInstance().gson.fromJson(errorStr, Error.class);
                        description = error.response.description;
                    } catch (Exception e) {
                        e.printStackTrace();
                        description = "Empty result id is null";
                    }
                    wrapping.failure(RetrofitError.unexpectedError(response.getUrl(), new NullPointerException(description)));
                }
                else
                    wrapping.success(new Response<StringWrapper>(new StringWrapper(stringResponse.getResponse())), response);
            }

            @Override
            public void failure(RetrofitError error) {
                wrapping.failure(error);
            }
        };
    }


    public static class Error {
        public ErrorBody response;

        public Error() {
        }

        public static class ErrorBody {
            public String error;
            public String description;

            public ErrorBody() {
            }
        }
    }

}