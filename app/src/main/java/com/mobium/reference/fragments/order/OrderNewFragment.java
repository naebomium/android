package com.mobium.reference.fragments.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.mobium.client.ShopDataStorage;
import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.client.models.SuccessOrderData;
import com.mobium.new_api.models.order.NewOrderData;
import com.mobium.new_api.models.order.OrderItem;
import com.mobium.new_api.Receiver;
import com.mobium.reference.R;
import com.mobium.reference.fragments.BasicContentFragment;
import com.mobium.reference.utils.FragmentActionHandler;
import com.mobium.reference.utils.Functional;
import com.mobium.config.common.Config;
import com.mobium.reference.utils.executing.ExecutingAsyncTask;
import com.mobium.reference.utils.executing.ExecutingException;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;
import com.mobium.reference.views.MEditTextWithIcon;
import com.mobium.userProfile.ResponseParams.ProfileInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *   31.03.15.
 * http://mobiumapps.com/
 * Fragment for Checkout with static field
 */

@Deprecated
public class OrderNewFragment extends BasicContentFragment
        implements Functional.ChangeListener<OrderNewFragment.State>, Receiver<ProfileInfo> {

    public static final String ORDER_DATA = "com.mobium.reference.fragments.order.OrderNewFragment::orderdata";

    public enum State {
        Loading, ShowContent
    }

    private MEditTextWithIcon name;
    private MEditTextWithIcon phone;
    private MEditTextWithIcon email;

    private View makeOrderButton;
    private View inputViews[];
    private View progressBar;


    private final static Date date;

    static {
        date = new Date();
    }

    private void setStateView(View view, boolean enabled) {
        view.setEnabled(enabled);
        view.setAlpha(enabled ? 1 : 0.6f);
    }

    @Override
    public void onChange(State newValue) {
        Function<Boolean, Void> changeState =
                b -> {
                    Stream.of(inputViews)
                            .forEach(view -> setStateView(view, b));
                    makeOrderButton.setEnabled(b);

                    return null;
                };

        switch (newValue) {
            case Loading:
                getDashboardActivity().updateTitle(getDashboardActivity().getString(R.string.data_exchange));
                changeState.apply(false);
                progressBar.setVisibility(View.VISIBLE);
                Stream.of(inputViews).forEach(View::clearFocus);
                break;
            case ShowContent:
                getDashboardActivity().updateTitle(getTitle());
                changeState.apply(true);
                progressBar.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_order, container, false);
        name = (MEditTextWithIcon) root.findViewById(R.id.fragment_order_name);
        phone = (MEditTextWithIcon) root.findViewById(R.id.fragment_order_phone);
        email = (MEditTextWithIcon) root.findViewById(R.id.fragment_order_email);

        progressBar = root.findViewById(R.id.fragment_order_progressBar);

        makeOrderButton = root.findViewById(R.id.fragment_order_buy);
        makeOrderButton
                .setOnClickListener(this::makeOrder);

        root.findViewById(R.id.fragment_order_callToShop).setOnClickListener(view -> FragmentActionHandler.doAction(getActivity(),
                new Action(ActionType.DO_CALL, Config.get().getApplicationData().getShopPhone())));

        name.setInputType(InputType.TYPE_CLASS_TEXT);
        phone.setInputType(InputType.TYPE_CLASS_PHONE);
        email.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        email.setCorrector(
                editText -> Patterns.EMAIL_ADDRESS
                        .matcher(editText.getText().toString())
                        .matches(),
                "Проверьте ваш адрес"
        );

        name.setRequired(true);
        phone.setRequired(true);
        email.setRequired(false);

        inputViews = new View[]{name.getInput(), phone.getInput(), email.getInput()};
        return root;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            loadProfileFromCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeOrder(View button) {
        long correct =
                Stream.of(name, phone, email)
                        .filter(MEditTextWithIcon::isCorrect)
                        .count();
        if (correct != 3) return;


        ProfileInfo profileInfo = new ProfileInfo();

        profileInfo.setEmail(email.getValue());
        profileInfo.setName(name.getValue());
        profileInfo.setPhone(phone.getValue());
        ShopDataStorage.getInstance().setProfileInfo(profileInfo);
        ShopDataStorage.getInstance().trySave();

        int cost = getApplication().getCart().getItemsCost();

        List<OrderItem> items = new ArrayList<>();

        Stream.of(getApplication().getCart().getItems())
                .forEach(cartItem -> items.add(
                                new OrderItem(cartItem.shopItem.id,
                                        cartItem.count,
                                        cartItem.shopItem.getModificationsMap())
                        )
                );

        HashMap<String, String> userInfo = new HashMap<>(3);
        userInfo.put("name", name.getValue());
        userInfo.put("phone", phone.getValue());
        userInfo.put("email", email.getValue());


        String regionId = "";
        try {
            regionId = getApplication().getShopData().getCurrentRegion().get().getId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        NewOrderData orderInfo =
                new NewOrderData(regionId, userInfo, items, cost);

        onChange(State.Loading);
        executeAsync(new ExecutingAsyncTask() {
            SuccessOrderData result;

            @Override
            public void onError(ExecutingException exception) {
                super.onError(exception);
                getActivity().runOnUiThread(() ->
                                onChange(State.ShowContent)
                );
            }

            @Override
            public void executeAsync() throws ExecutingException {
//                result = getApplication().getExecutor().makeOrder(orderInfo);
            }

            @Override
            public void afterExecute() {
                super.afterExecute();
                onChange(State.ShowContent);
                result.setTime(date.getTime());
                result.setCacheItems(new ArrayList<>(Arrays.asList(getApplication().getCart().getItems())));

                getFragmentManager().popBackStackImmediate();
                Intent resultIntent = new Intent();
                resultIntent.putExtra(ORDER_DATA, result);
                getTargetFragment()
                        .onActivityResult(
                                getTargetRequestCode(),
                                Activity.RESULT_OK,
                                resultIntent
                        );
            }
        });
    }

    @Override
    public void onResult(ProfileInfo profileInfo) {
        if (profileInfo != null) {
            email.getInput().setText(profileInfo.email);
            name.getInput().setText(profileInfo.name);
            phone.getInput().setText(profileInfo.phone);
        }
    }

    private void loadProfileFromCache() throws Exception {
        onResult(ShopDataStorage.getInstance().getProfileInfo());
    }

    public void onStart() {
        super.onStart();
        Events.get(getActivity()).navigation().onPageOpen(OpenPageEvents.preorder.name());
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        inputViews = null;
    }

    @Override
    protected String getTitle() {
        return "Оформление";
    }

}
