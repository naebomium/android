package com.mobium.reference.fragments.goods;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.mobium.client.ShopDataStorage;
import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.client.models.CartItem;
import com.mobium.client.models.Marketing;
import com.mobium.client.models.SuccessOrderData;
import com.mobium.client.models.ShopItem;
import com.mobium.reference.R;
import com.mobium.reference.activity.MainDashboardActivity;
import com.mobium.reference.activity.RequestCodes;
import com.mobium.reference.fragments.BasicContentFragment;
import com.mobium.reference.fragments.LoginFragment;
import com.mobium.reference.fragments.RegistrationFragment;
import com.mobium.reference.fragments.order.OrderSuccessFragment;
import com.mobium.reference.fragments.order.ProcedureCheckoutFragment;
import com.mobium.config.common.ConfigUtils;
import com.mobium.reference.utils.FragmentActionHandler;
import com.mobium.reference.utils.FragmentUtils;
import com.mobium.reference.utils.PhoneUtils;
import com.mobium.config.common.Config;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;
import com.mobium.reference.views.CartAuthDialog;
import com.mobium.reference.views.WebImageView;

import static com.mobium.reference.utils.text.RussianPlurals.formatGoodsCount;

/**
 *
 *
 * Date: 28.11.12
 * Time: 16:39
 */
public class CartFragment extends BasicContentFragment implements CartAuthDialog.ICartAuthDialog {
    private LinearLayout layout;
    private TextView totalCost;
    private TextView count;
    private View freeDelivery;
    private Button buyButton;
    private View items_wrapper;
    private View hintView;
    private View bottomInfo;


    @Override
    protected String getTitle() {
        return getResources().getString(R.string.cart_title);
    }

    private void updateCart() {
        CartItem[] items = getApplication().getCart().getItems();
        boolean cartEmpty = items.length == 0;

        buyButton.setEnabled(!cartEmpty);

        bottomInfo.setVisibility(cartEmpty ? View.GONE : View.VISIBLE);
        items_wrapper.setVisibility(cartEmpty ? View.GONE : View.VISIBLE);
        hintView.setVisibility(cartEmpty ? View.VISIBLE : View.GONE);

        totalCost.setText(ConfigUtils.formatCurrency((getApplication().getCart().getItemsCost())));

        layout.removeAllViews();

        int totalCount = 0;

        for (final CartItem cartItem : items) {
            totalCount += cartItem.count;
            updateCartView(cartItem);
        }

        // количество товаров = количеству товаров с маркетинг free_delivery
        boolean allItemsHasFreeDelivery = items.length ==
                Stream.of(items)
                        .map(cartItem -> cartItem.shopItem)
                        .filter(shopItem -> Marketing.hasType(shopItem.getMarketing(), Marketing.Type.FREE_DELIVERY))
                        .count();

        if (freeDelivery != null)
            freeDelivery.setVisibility(allItemsHasFreeDelivery ? View.VISIBLE : View.GONE);

        count.setText(formatGoodsCount(totalCount));
    }


    private void updateCartView(CartItem cartItem) {
        View view = LayoutInflater.from(getActivity()).
                inflate(R.layout.fragment_cart_item, layout, false);

        WebImageView image = (WebImageView) view.findViewById(R.id.fragment_cart_item_icon);
        if (cartItem.shopItem.getIcon().isPresent()) {
            image.setUrl(cartItem.shopItem.getIcon().get().getUrl());
        }

        ((TextView) view.findViewById(R.id.fragment_cart_item_title)).setText(cartItem.shopItem.title);
        ((TextView) view.findViewById(R.id.fragment_cart_item_count)).setText(String.valueOf(cartItem.count));
        //itemCount

        if (Config.get().getApplicationData().getApplicationShopEnabled()) {
            ((TextView) view.findViewById(R.id.fragment_cart_item_cost)).setText(ConfigUtils.formatCurrency(cartItem.shopItem.cost.getCurrentConst()));
            ((TextView) view.findViewById(R.id.fragment_cart_item_total_cost)).setText(ConfigUtils.formatCurrency(cartItem.shopItem.cost.getCurrentConst() * cartItem.count));
        } else {
            view.findViewById(R.id.fragment_cart_item_cost).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.fragment_cart_item_total_cost).setVisibility(View.INVISIBLE);
        }

        final ShopItem finalCopy = cartItem.shopItem;

        view.setOnClickListener(view1 -> {
//                CartFragment.this.getDashboardActivity().onEvent(StatisticsEvent.VIEW_CART_OPEN, "item_id", cartItem.shopItem.id);
            Events.get(getActivity()).cart().onOpenFromCart(cartItem);
            FragmentActionHandler.doAction(CartFragment.this.getActivity(), new Action(ActionType.OPEN_PRODUCT, cartItem.shopItem.id));
        });

        view.findViewById(R.id.fragment_cart_add_button).setOnClickListener(view1 -> {
//                getDashboardActivity().onEvent(StatisticsEvent.VIEW_CART_ADD, "item_id", cartItem.shopItem.id, "old_count", String.valueOf(cartItem.count));
            getApplication().getCart().addItem(cartItem.shopItem);
            PhoneUtils.vibrate(getActivity(), 200);
            Events.get(getActivity()).cart().onAddToCart(cartItem);
            updateCart();
        });
        view.findViewById(R.id.fragment_cart_remove_button).setOnClickListener(view1 -> {
//                getDashboardActivity().onEvent(StatisticsEvent.VIEW_CART_REMOVE, "item_id", cartItem.shopItem.id, "old_count", String.valueOf(cartItem.count));
            if (cartItem.count == 1) {
                new android.support.v7.app.AlertDialog.Builder(getActivity())
                        .setMessage(getResources().getString(R.string.cart_remove_item_message))
                        .setPositiveButton(getResources().getString(R.string.cart_remove_item_positive_text), (dialogInterface, i) -> {
                            getApplication().getCart().addItem(cartItem.shopItem, -1);
                            updateCart();
                        })
                        .setNegativeButton(getResources().getString(R.string.cart_remove_item_negative_text), null).show();
            } else {
                getApplication().getCart().addItem(cartItem.shopItem, -1);
                updateCart();
            }
            Events.get(getActivity()).cart().onAddToCart(cartItem);
        });
        view.findViewById(R.id.fragment_cart_delete_button).setOnClickListener(view1 -> {
//                getDashboardActivity().onEvent(StatisticsEvent.VIEW_CART_REMOVE, "item_id", cartItem.shopItem.id, "old_count", String.valueOf(cartItem.count));
            new android.support.v7.app.AlertDialog.Builder(getActivity())
                    .setMessage(R.string.cart_remove_item_message)
                    .setPositiveButton(R.string.cart_remove_item_positive_text, (dialogInterface, i) -> {
                        getApplication().getCart().addItem(cartItem.shopItem, -cartItem.count);
                        updateCart();
                        Events.get(getActivity()).cart().onRemoveFromCart(cartItem);
                    })
                    .setNegativeButton(R.string.cart_remove_item_negative_text, null).show();
        });
        layout.addView(view);
    }


    @Override
    public void onStart() {
        super.onStart();
        Events.get(getActivity()).navigation().onPageOpen(OpenPageEvents.cart.name());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container1, Bundle savedInstanceState) {
        View res = inflater.inflate(R.layout.fragment_cart, container1, false);
        layout = (LinearLayout) res.findViewById(R.id.fragment_cart_items);
        bottomInfo = res.findViewById(R.id.fragment_cart_bottom_info);
        totalCost = (TextView) res.findViewById(R.id.fragment_cart_total_cost);
        buyButton = (Button) res.findViewById(R.id.fragment_cart_purchase);
        count = (TextView) res.findViewById(R.id.fragment_cart_count);
        freeDelivery = res.findViewById(R.id.free_delivery_view);

        buyButton.setOnClickListener(view -> checkProfile());

        items_wrapper = res.findViewById(R.id.fragment_cart_items_wrapper);
        hintView = res.findViewById(R.id.hintText);

        if (!Config.get().getApplicationData().getApplicationShopEnabled()) {
            totalCost.setVisibility(View.INVISIBLE);
        }
        updateCart();
        return res;
    }


    private void checkProfile() {
        if (Config.get().getApplicationData().getProfileEnabled() && ShopDataStorage.getInstance().getProfileAccessToken() == null) {
            CartAuthDialog.getDialog(getActivity(), this).show();
        } else {
            startOrderDialog();
        }
    }

    private void startOrderDialog() {
        ProcedureCheckoutFragment fragment = new ProcedureCheckoutFragment();
        fragment.setTargetFragment(this, RequestCodes.MAKE_ORDER);
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit)
                .replace(R.id.fragment_wrapper, fragment)
                .addToBackStack("")
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case RequestCodes.MAKE_ORDER:
                    SuccessOrderData orderData = (SuccessOrderData)
                            data.getSerializableExtra(SuccessOrderData.class.getName());

                    MainDashboardActivity activity = getDashboardActivity();

                    Events.get(activity).order().onMakeOrder(orderData, getApplication().getCart().getItems());

                    getApplication().onSuccessOrder(orderData);

                    getFragmentManager()
                            .beginTransaction()
                            .replace(
                                    R.id.fragment_wrapper,
                                    OrderSuccessFragment.getInstance(orderData)
                            )
                            .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.pop_enter, R.anim.pop_exit)
                            .addToBackStack("")
                            .commit();
                    PhoneUtils.vibrate(activity, 400);
                    break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (layout != null)
            layout.removeAllViews();
    }

    @Override
    public void onAuth() {
        LoginFragment fragment = new LoginFragment();
        fragment.setTargetFragment(this, RequestCodes.MAKE_ORDER);
        FragmentUtils.replace(getActivity(), fragment, true);
    }

    @Override
    public void onRegister() {
        FragmentUtils.doOpenFragmentDependsOnInternetAccess(getActivity(), RegistrationFragment.class, true);
    }

    @Override
    public void onProceed() {
        startOrderDialog();
    }
}
