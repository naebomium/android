package com.mobium.userProfile;

import com.mobium.userProfile.CallBack.ProfileCallBack;
import com.mobium.userProfile.ResponseParams.*;
import retrofit.http.*;

import java.util.Map;

/**
 *  on 11.06.15.
 * http://mobiumapps.com/
 */
public interface RawProfileApiInterface {


    @FormUrlEncoded
    @POST("/{resendUrl}/")
    void resendCode(@Path(value = "resendUrl",  encode = false) String resendUrl,
                    @Field("appId") String appId,
                    @Field("platform") String platform,
                    @Field("confirmToken") String confirmToken,
                    ProfileCallBack<Object> callBack);


    @FormUrlEncoded
    @POST("/{authorizationUrl}")
    void makeAuthorization(@Path(value = "authorizationUrl", encode = false) String authorizationUrl,
                           @Field("appId") String appId,
                           @Field("platform") String platform,
                           @Field(value = "user_email", encodeValue = false) String userLogin,
                           @Field("secret") String userPassword,
                           ProfileCallBack<Authorization> callBack);



    @FormUrlEncoded
    @POST("/{disauthUrl}")
    void makeExit(@Path(value = "disauthUrl", encode = false) String exitUrl,
                  @Field("accessToken") String accessToken,
                  @Field("appId") String appId,
                  ProfileCallBack<Object> callBack
    );

    @GET("/{ordersUrl}")
    void getOrdersAsync(@Path(value = "ordersUrl", encode = false) String ordersUrl,
                        @Query("accessToken") String accessToken,
                        @Query("appId") String appId,
                        ProfileCallBack<OrderList> callBack
    );

    @GET("/{ordersUrl}")
    Response<OrderList> getOrders(@Path(value = "ordersUrl", encode = false) String ordersUrl,
                        @Query("accessToken") String accessToken,
                        @Query("appId") String appId);


    @GET("/{regFieldsUrl}/")
    rx.Observable<Response<RegFields>>
    getRegFieldsAsync(@Path(value = "regFieldsUrl", encode = false) String regFieldsUrl);


    @POST("/{registrationUrl}")
    void makeRegistration(@Path(value = "registrationUrl", encode = false) String registrationUrl,
                          @FieldMap(encodeNames = false, encodeValues = false) Map<String, String> registrationFields,
                          ProfileCallBack<Registration> callBack

    );

    @FormUrlEncoded
    @POST("/{registrationUrl}")
    rx.Observable<Response<Registration>>
    makeRegistration(@Path(value = "registrationUrl", encode = false) String registrationUrl,
                     @FieldMap(encodeNames = false, encodeValues = false) Map<String, String> registrationFields);



    @FormUrlEncoded
    @POST("/{activationUrl}")
    rx.Observable<Response<Activation>>
    makeActivation(@Path(value = "activationUrl", encode = false) String activationUrl,
                   @Field("confirmToken") String confirmToken,
                   @Field("code") String code,
                   @Field("appId") String appId,
                   @Field("platform") String platform
    );

    @GET("/{checkAuthUrl}")
    void makeCheckAuth(@Path(value ="checkAuthUrl", encode = false) String checkAuthUrl,
                       @Query("accessToken") String accessToken,
                       @Query("appId") String appId,
                       ProfileCallBack<SessionCheck> callBack
    );

    //Не проверенно_____________________






    @GET("/{profileUrl}")
    void getProfileInfo(@Path(value = "profileUrl", encode = false) String profileUrl,
                        @Query("accessToken") String accessToken,
                        @Query("appId") String appId,
                        ProfileCallBack<ProfileInfo> callBack
    );

    @GET("/{pushUrl}")
    void getPushStatus(@Path("pushUrl") String pushUrl,
                       @Query("accessToken") String accessToken,
                       @Query("appId") String appId,
                       ProfileCallBack<PushStatus> callBack
    );





    @POST("/{activationUrl}")
    void makeActivation(@Path("activationUrl") String activationUrl,
                        @Field("confirmToken") String confirmToken,
                        @Field("code") String code,
                        ProfileCallBack<Activation> callBack
    );








    @FormUrlEncoded
    @POST("/{pushUrl}")
    void editPush(@Path("pushUrl") String pushUrl,
                  @Query("accessToken") String accessToken,
                  @Query("appId") String appId,
                  @Query("send") Boolean send,
                  ProfileCallBack<Object> callBack
    );







}
