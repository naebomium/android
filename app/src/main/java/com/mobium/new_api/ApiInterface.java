package com.mobium.new_api;

import com.mobium.new_api.methodParameters.AppStartParam;
import com.mobium.new_api.methodParameters.GetDeliveryMethodParam;
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
import com.mobium.new_api.models.RegionList;
import com.mobium.new_api.models.Request;
import com.mobium.new_api.models.Response;
import com.mobium.new_api.models.ShopPoint;
import com.mobium.new_api.models.SimpleResult;
import com.mobium.new_api.models.StringResponse;
import com.mobium.new_api.models.catalog_.ShopItem_;
import com.mobium.new_api.models.catalog_.ShopItems_;
import com.mobium.new_api.models.order.DeliveryMethods;
import com.mobium.new_api.models.order.NewOrderResult;
import com.mobium.new_api.models.order.PaymentTypesInfo;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 *  on 19.06.15.
 * http://mobiumapps.com/
 *
 * api declaration
 */
public interface ApiInterface {

    @POST("/{url}")
    void onEvent(@Path(value = "url", encode = false) String url,
                 @Body Request<OnEventParam> body,
                 Callback<Response<SimpleResult>> callback);


    @POST("/{url}")
    void appStart(@Path(value = "url", encode = false) String url,
                  @Body Request<AppStartParam> body,
                  Callback<BooleanResponse> callBacks);


    @POST("/{url}")
    void postPushGoal(@Path(value = "url", encode = false) String url,
                      @Body Request<PostPushGoalParam> body,
                      Callback<BooleanResponse> callback);
    @POST("/{url}")
    void registerApp(@Path(value = "url", encode = false) String url,
                     @Body Request<RegisterAppParam> body,
                     Callback<StringResponse> callback);


    @POST("/{url}")
    void pushReceived(@Path(value = "url", encode = false) String url,
                      @Body Request<String> body,
                      Callback<Response<SimpleResult>> callback);

    @POST("/{url}")
    void registerPushToken(@Path(value = "url", encode = false) String url,
                           @Body Request<RegisterPushTokenParam> body,
                           Callback<Response<SimpleResult>> callback);

    @POST("/{url}")
    void getBanners(@Path(value = "url", encode = false) String url,
                    @Body Request body,
                    Callback<Response<BannerList>> callback);

    @POST("/{url}")
    Response<BannerList> getBanners(@Path(value = "url", encode = false) String url,
                                    @Body Request body);

    @POST("/{url}")
    void getRegions(@Path(value = "url", encode = false) String url,
                    @Body Request body,
                    Callback<Response<RegionList>> callback);

    @POST("/{url}")
    void getShopPoints(@Path(value = "url", encode = false) String url,
                       @Body Request body,
                       Callback<Response<ShopPoint.ShopPointList>> callback);


    @POST("/{url}")
    void getShopItem(@Path(value = "url", encode = false) String url,
                     @Body Request body,
                     Callback<Response<ShopItem_>> shopItem);


    @POST("/{url}")
    void getLocationFilters(@Path(value = "url", encode = false) String url,
                            @Body Request body,
                            Callback<Response<LocationFilters>> filters);

    @POST("/{url}")
    rx.Observable<Response<ShopItems_>> getShopItems(
            @Path(value = "url", encode = false) String url,
            @Body Request body);


    @POST("/{url}")
    rx.Observable<Response<ShopItem_>>
    getShopItem(@Path(value = "url", encode = false) String url,
                @Body Request body);


    @POST("/{url}")
    void sendMessage(@Path(value = "url", encode = false) String url,
                     @Body Request<SendMessageParam> body,
                     Callback<Response> filters);


    //Order___________________________________________________________
    @POST("/{url}")
    Response<DeliveryMethods> getDeliveryMethods(@Path(value = "url", encode = false) String url,
                                                 @Body Request<GetDeliveryMethodParam> body);


    @POST("/{url}")
    Response<PaymentTypesInfo> getPaymentTypes(@Path(value = "url", encode = false) String url,
                                               @Body Request body);


    @POST("/{url}")
    Response<NewOrderResult> newOrder(@Path(value = "url", encode = false) String url,
                                      @Body Request<NewOrderParam> body);
    //Order___________________________________________________________

    @POST("/{url}")
    Response<ShopPoint.ShopPointList> getShopPoints(@Path(value = "url", encode = false) String url,
                       @Body Request<GetShopPointParam> body);

}
