package com.mobium.reference.utils;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.mobium.client.ShopDataStorage;
import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.client.models.CartItem;
import com.mobium.client.models.ShopItem;
import com.mobium.reference.R;
import com.mobium.reference.ReferenceApplication;
import com.mobium.reference.fragments.ProfileFragment;
import com.mobium.userProfile.CallBack.ProfileCallBack;
import com.mobium.userProfile.ProfileApi;
import com.mobium.userProfile.Response;
import com.mobium.userProfile.ResponseParams.OrderItem;
import com.mobium.userProfile.ResponseParams.OrderList;
import com.mobium.userProfile.ResponseParams.OrderProfile;
import com.mobium.userProfile.ResponseParams.SessionCheck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit.RetrofitError;
import rx.internal.operators.OperatorTimeInterval;

/**
 *  on 16.07.15.
 * http://mobiumapps.com/
 */
public class ProfileUtils {

    public static final String PROFILE_INFO_TAG = "PROFILE_INFO_TAG";

    //загружает по рилайди товары и наполняет ими классы заказов
    public static Response<OrderList> updateOrderTask() throws Exception {
        if (ProfileApi.getInstance().getAccessToken() == null) {
            ShopDataStorage.getInstance().setProfileOrderProfiles(Collections.emptyList());
            return new Response<>(Response.ResponseStatus.NEED_AUTH, null);
        }
        return ProfileApi.getInstance().getOrders();
    }



    public static void onLogin(final FragmentActivity activity) {
            try {
                new AsyncTask<Void, Void, Optional<OrderList>>() {
                    @Override
                    protected Optional<OrderList> doInBackground(Void... params) {
                        try {
                            Response<OrderList> orderListResponse =
                                updateOrderTask();
                            if (orderListResponse.getType().equals(Response.ResponseStatus.OK))
                                return Optional.ofNullable(orderListResponse.data);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return Optional.empty();
                    }

                    @Override
                    protected void onPostExecute(Optional<OrderList> orderListOptional) {
                        super.onPostExecute(orderListOptional);
                        orderListOptional.ifPresent(value -> {
                            ShopDataStorage.getInstance().setProfileOrderProfiles(value.getOrderProfiles());
                            Snackbar.make(activity.findViewById(R.id.activity_wrapper),
                                    "Ваши заказы загружены", Snackbar.LENGTH_LONG)
                                    .show();
                        });
                    }
                }.execute();

                activity.runOnUiThread(() -> ProfileApi.getInstance().makeCheckAuth(new ProfileCallBack<SessionCheck>() {
                    @Override
                    public void onSuccess(SessionCheck data) {
                        if (data.openPage != null) {
                            Snackbar.make(activity.findViewById(R.id.activity_wrapper),
                                    "Сессия с сайта восстановленна", Snackbar.LENGTH_LONG)
                                    .show();

                            switch (data.openPage.type) {
                                case offer:
                                    FragmentActionHandler.doAction(activity, new Action(ActionType.OPEN_PRODUCT, data.openPage.id));
                                    break;
                                case category:
                                    FragmentActionHandler.doAction(activity, new Action(ActionType.OPEN_CATEGORY, data.openPage.id));
                                    break;
                            }
                        } else {
                            openProfile();
                        }
                    }
                    private void openProfile() {
                        FragmentUtils.replace(activity, new ProfileFragment(), false, true);
                    }

                    @Override
                    public void onAbort(Response.ResponseStatus type, @Nullable SessionCheck data) {
                        openProfile();

                    }

                    @Override
                    public void onError(RetrofitError error) {
                        openProfile();
                    }
                })
                );
            } catch (Exception e) {
                activity.runOnUiThread(() ->
                    Snackbar.make(activity.findViewById(R.id.activity_wrapper),
                            "Ошибка во время загрузки заказов", Snackbar.LENGTH_LONG)
                            .setAction("Повторить попытку", view -> onLogin(activity))
                            .show()
                );
            }

    }
}
