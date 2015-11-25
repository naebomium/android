package com.mobium.reference.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.mobium.client.ShopDataStorage;
import com.mobium.new_api.Api;
import com.mobium.new_api.methodParameters.GetDeliveryMethodParam;
import com.mobium.new_api.methodParameters.GetShopPointParam;
import com.mobium.new_api.models.Region;
import com.mobium.new_api.models.ShopPoint;
import com.mobium.new_api.models.order.DeliveryMethod;
import com.mobium.new_api.models.order.DeliveryMethods;
import com.mobium.new_api.models.order.Field;
import com.mobium.reference.fragments.order.CheckoutFragment;
import com.mobium.reference.view.CheckoutView;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 *  on 14.10.15.
 */
public class CheckoutPresenterImpl implements CheckoutFragment.CheckoutPresenter {
    private final CheckoutFragment.GoodsModel model;
    private final CheckoutView view;
    private Subscription subscription;
    private Observable<Region> currentRegion;

    private Input input;

    public CheckoutPresenterImpl(CheckoutFragment.GoodsModel model, CheckoutView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void saveState(@NonNull Bundle bundle) {

    }

    @Override
    public void loadState(@NonNull Bundle bundle) {

    }

    @Override
    public void viewCreated(View root) {
        view.showMessageToUser("Заполните форму заказа, наш менеджер свяжется с Вами");
        currentRegion = view.showRegions(ShopDataStorage.getInstance().getRegions(), ShopDataStorage.getInstance().getCurrentRegion().orElse(null));
    }

    @Override
    public void loadStatus(LoadStatus status) {
        switch (status) {
            case mayLoading:
                if (subscription == null)
                    subscription =
                            currentRegion
                                    .flatMap(this::getDeliveryMethods)
                                    .flatMap(delivery -> {
                                        Observable<FilledMethod> method = view.showDeliveryMethods(delivery.methods);
                                        Observable<Map<Field, String>> staticFiled = view.showStaticFields(delivery.staticFields, null);
                                        return Observable.<FilledMethod,Map<Field, String>, Input>combineLatest(method, staticFiled, Input::new);
                                    })
                                    .doOnNext(input -> {
                                        if (input.method.method.isPickup())
                                            currentRegion
                                                    .flatMap(this::getPickUpPoints)
                                                    .flatMap(view::showPoints)
                                                    .doOnNext(shopPoint -> input.point = shopPoint);

                                    })
                                    .subscribe(input1 ->
                                        input = input1, throwable ->
                                            view.exitViewAlert("Ошибка во время обмена данных", throwable.getMessage())
                                    );

                break;
        }

    }

    @Override
    public void onBuyButtonClick(View button) {

    }


    private Observable<Boolean> dialogRetryOrExit(String message) {
        return Observable.create((Subscriber<? super Boolean> subscriber) ->
                view.showError(
                "Ошибка во премя обена данными",
                message,
                "Повторить",
                (dialog, which) -> subscriber.onNext(true),
                "Выйти",
                (dialog1, which1) -> {
                    subscriber.onCompleted();
                    view.exit();
                }
        ));
    }

    private Observable<DeliveryMethods> getDeliveryMethods(Region region) {
        return Api.getInstance().getDeliveryMethods(
                new GetDeliveryMethodParam(
                        model.cost,
                        region.getId(),
                        Stream.of(model.counts.entrySet())
                                .map(value -> new GetDeliveryMethodParam.OrderItem(value.getKey(), value.getValue()))
                                .collect(Collectors.toList())
                )

                )
                        .doOnRequest(l -> view.showProgress(true))
                        .doOnCompleted(() -> view.showContent(false))
                        .retryWhen(observable ->
                                        observable.flatMap(throwable ->
                                                        dialogRetryOrExit(throwable.getMessage())
                                        )
                        );

    }

    private Observable<ShopPoint[]> getPickUpPoints(Region region) {
        if (region.pickUps().isPresent())
            return Observable.just(region.pickUps().get());

        return Api.getInstance().getShopPoints(new GetShopPointParam(region, ShopPoint.ShopPointType.pickupShop))
                .doOnRequest(l -> view.showProgress(true))
                .doOnCompleted(() -> view.showContent(false))
                .retryWhen(observable ->
                                observable.flatMap(throwable ->
                                                dialogRetryOrExit(throwable.getMessage())
                                )
                ).doOnNext(region::setPickUp);

    }


    private static class Input {
        public final FilledMethod method;
        public final Map<Field, String> staticFields;

        private ShopPoint point;

        public Input(FilledMethod method, Map<Field, String> staticFields) {
            this.method = method;
            this.staticFields = staticFields;
        }

    }

    public static class FilledMethod {
        public DeliveryMethod method;
        public Map<Field, String> stringMap;

        public FilledMethod() {
        }
    }
}
