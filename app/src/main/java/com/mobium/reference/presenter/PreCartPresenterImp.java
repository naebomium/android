package com.mobium.reference.presenter;

import android.app.Activity;
import android.view.View;

import com.annimon.stream.Optional;
import com.mobium.client.models.CartItem;
import com.mobium.client.models.ShopItem;
import com.mobium.client.models.ShoppingCart;
import com.mobium.reference.ReferenceApplication;
import com.mobium.reference.utils.Functional;
import com.mobium.config.common.Config;
import com.mobium.reference.utils.executing.ExecutingException;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.view.PreCartView;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *  on 05.10.15.
 */
public class PreCartPresenterImp implements PreCartPresenter {
    private final PreCartView view;
    private CartItem cartItem;
    private final Functional.ThrowableSupplier<List<ShopItem>, ExecutingException> supplier;
    private final String label = Config.get().strings().relatedItemsTitle();

    private Runnable retry;
    private ShoppingCart cart = ReferenceApplication.getInstance().getCart();


    private Subscription loadingSubscription;

    public PreCartPresenterImp(PreCartView view,
                               CartItem cartItem,
                               Functional.ThrowableSupplier<List<ShopItem>, ExecutingException> supplier) {
        this.view = view;
        this.cartItem = cartItem;
        this.supplier = supplier;
    }

    private Observable<List<ShopItem>> getRelatedItems() {
        return Observable.create(f -> {
            if (f.isUnsubscribed())
                return;

            Runnable requestData = () -> {
                try {
                    f.onNext(supplier.get());
                } catch (ExecutingException e) {
                    e.printStackTrace();
                    f.onError(e);
                }
            };

            retry = requestData::run;

            //firstLaunch
            requestData.run();
        });
    }

    @Override
    public void mayLoad() {
        if (loadingSubscription != null && !loadingSubscription.isUnsubscribed())
            return;
        loadingSubscription = getRelatedItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        listOfProducts ->
                                view.showRelatedItems(listOfProducts, label),

                        error ->
                                view.showError(
                                        "Ошибка во время загрузки",
                                        "Повторить?",
                                        (dialog, which) -> Optional.of(retry).ifPresent(Runnable::run),
                                        null
                                )
                );

    }

    @Override
    public void abortLoad() {
        if (loadingSubscription != null) {
            loadingSubscription.unsubscribe();
            loadingSubscription = null;
        }
    }

    @Override
    public void viewCreated() {
        view.showCartItem(cartItem);
        view.updateCounts(cartItem);
    }

    @Override
    public void onAddButtonClick(View v) {
        ReferenceApplication.getInstance().getCart().addItem(cartItem.shopItem);
        cartItem = cart.getItem(cartItem.shopItem.id).orElse(cartItem);
        Events.get((Activity) v.getContext()).cart().onAddToCart(cartItem);
        view.updateCounts(cartItem);
    }

    @Override
    public void onRemoveButtonClick(View v) {
        ReferenceApplication.getInstance().getCart().addItem(cartItem.shopItem, -1);
        cartItem = cart.getItem(cartItem.shopItem.id).orElse(cartItem);
        Events.get((Activity) v.getContext()).cart().onRemoveFromCart(cartItem);
        view.updateCounts(cartItem);
    }

    @Override
    public String title() {
        return cartItem.shopItem.title;
    }
}
