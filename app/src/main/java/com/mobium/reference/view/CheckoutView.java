package com.mobium.reference.view;

import android.support.annotation.Nullable;

import com.mobium.new_api.models.Region;
import com.mobium.new_api.models.ShopPoint;
import com.mobium.new_api.models.order.DeliveryMethod;
import com.mobium.new_api.models.order.Field;
import com.mobium.new_api.models.order.PaymentType;
import com.mobium.reference.presenter.CheckoutPresenterImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 *  on 13.10.15.
 */
public interface CheckoutView extends LoadableView, CanShowErrorView, CanExit {

    Observable<CheckoutPresenterImpl.FilledMethod> showDeliveryMethods(DeliveryMethod... methods);
    Observable<Map<Field, String>> showStaticFields(Field[] field, @Nullable String[] saveValue);
    Observable<PaymentType> showPaymentTypes(PaymentType[] types);
    Observable<Region> showRegions(List<Region> regions, @Nullable Region current);
    Observable<ShopPoint> showPoints(ShopPoint[] points);

    void showMessageToUser(CharSequence message);


}
