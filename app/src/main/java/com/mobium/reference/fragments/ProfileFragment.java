package com.mobium.reference.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.annimon.stream.Stream;
import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirGetCallback;
import com.anupcowkur.reservoir.ReservoirPutCallback;
import com.mobium.client.ShopDataStorage;
import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.new_api.Receiver;
import com.mobium.reference.R;
import com.mobium.reference.utils.Dialogs;
import com.mobium.reference.utils.FragmentActionHandler;
import com.mobium.reference.utils.FragmentUtils;
import com.mobium.reference.utils.ProfileUtils;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;
import com.mobium.reference.views.TextViewWithImageAndSubTitle;
import com.mobium.reference.views.TitledTextController;
import com.mobium.userProfile.CallBack.ProfileCallBack;
import com.mobium.userProfile.ProfileApi;
import com.mobium.userProfile.Response;
import com.mobium.userProfile.ResponseParams.ProfileInfo;

import retrofit.RetrofitError;

/**
 *  on 13.07.15.
 * http://mobiumapps.com/
 */
public class ProfileFragment extends BasicContentFragment implements Receiver<ProfileInfo> {

    private ProfileInfo info;
    private View progressView;
    private LinearLayout personalDataList;
    private LinearLayout productsList;
    private Button exitButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        progressView = view.findViewById(R.id.progress);
        personalDataList = (LinearLayout) view.findViewById(R.id.fragment_profile_personal_data);
        productsList = (LinearLayout) view.findViewById(R.id.fragment_profile_personal_product_list);
        exitButton = (Button) view.findViewById(R.id.fragment_profile_personal_product_exit);
        exitButton.setOnClickListener(v -> doExit());
        return view;
    }


    @Override
    public void onResult(final ProfileInfo profileInfo) {
        Stream.of(Pair.create("Логин", profileInfo.name),
                Pair.create("Адрес", profileInfo.address),
                Pair.create("Телефон", profileInfo.phone),
                Pair.create("E-mail", profileInfo.email)

        ).map(pair ->
                        pair.second == null || pair.second.trim().length() == 0 ?
                                Pair.create(pair.first, "не задано") :
                                pair
        ).map(pair -> {
            TitledTextController controller = new TitledTextController(getDashboardActivity());
            controller.init(pair.first, pair.second);
            return controller;
        }).forEach(personalDataList::addView);


        View list1 = productsList.findViewById(R.id.fragment_profile_personal_product_list_item1);
        View list2 = productsList.findViewById(R.id.fragment_profile_personal_product_list_item2);

        new TextViewWithImageAndSubTitle(list1).configure("Мои Заказы", "Все оформленнные заказы", R.drawable.order_list);
        new TextViewWithImageAndSubTitle(list2).configure("Избранное", "Товары, добавленные в избранное", R.drawable.favorite);

        list1.setOnClickListener(v -> FragmentActionHandler.doAction(getActivity(), new Action(ActionType.OPEN_HISTORY, null)));
        list2.setOnClickListener(v -> FragmentActionHandler.doAction(getActivity(), new Action(ActionType.OPEN_FAVOURITES, null)));

        progressView.setVisibility(View.GONE);
        ShopDataStorage.getInstance().setProfileInfo(profileInfo);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (checkPendingExit())
            onSuccessExit();
        else if (info != null) {
            if (personalDataList.getChildCount() == 0)
                onResult(info);
        } else loadPersonalInfoTask();
        Events.get(getActivity()).navigation().onPageOpen(OpenPageEvents.account.name());
    }

    private void loadPersonalInfoTask() {
        progressView.setVisibility(View.VISIBLE);
        if (!loadFromCache())
            loadFromWeb();
    }

    private boolean loadFromCache() {
        boolean objectExists = false;
        try {
            objectExists = Reservoir.contains(ProfileUtils.PROFILE_INFO_TAG);
            if (objectExists) {
                Reservoir.getAsync(ProfileUtils.PROFILE_INFO_TAG, ProfileInfo.class, new ReservoirGetCallback<ProfileInfo>() {
                    @Override
                    public void onSuccess(ProfileInfo profileInfo) {
                        info = profileInfo;
                        onResult(profileInfo);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        loadFromWeb();
                    }
                });
            }
        } catch (Exception e) {
        }
        return objectExists;
    }


    private void loadFromWeb() {
        ProfileApi.getInstance().getProfileInfo(new ProfileCallBack<ProfileInfo>() {
            @Override
            public void onSuccess(ProfileInfo data) {
                info = data;
                onResult(data);
                Reservoir.putAsync(ProfileUtils.PROFILE_INFO_TAG, data, new ReservoirPutCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }

            @Override
            public void onAbort(Response.ResponseStatus type, @Nullable ProfileInfo data) {
                if (type == Response.ResponseStatus.NEED_AUTH) {
                    onSuccessExit();
                    FragmentUtils.doOpenFragmentDependsOnInternetAccess(getActivity(), LoginFragment.class, true);
                } else {
                    Dialogs.showExitScreenDialog(getDashboardActivity(), ProfileFragment.this::onSuccessExit);
                }
            }

            @Override
            public void onError(RetrofitError error) {
                if (error.getKind() != RetrofitError.Kind.NETWORK)
                    Dialogs.showExitScreenDialog(getActivity(), () -> getFragmentManager().popBackStack());
                else
                    new AlertDialog.Builder(getDashboardActivity())
                            .setTitle("Ошибка подключения")
                            .setPositiveButton("Повторить", (dialogInterface, i) -> {
                                loadFromWeb();
                            })
                            .setNegativeButton("Назад", (dialogInterface1, i1) -> {
                                getDashboardActivity().doOpenStart();
                            }).show();

            }
        });
    }


    private void onSuccessExit() {
        try {
            Reservoir.delete(ProfileUtils.PROFILE_INFO_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ProfileApi.getInstance().setAccessToken(null);
        ShopDataStorage.getInstance().clearProfileToken();
        getFragmentManager().popBackStack();
        ShopDataStorage.getInstance().setProfileInfo(null);
        ShopDataStorage.getInstance().trySave();
    }

    private void doExit() {
        progressView.setVisibility(View.VISIBLE);
        ProfileApi.getInstance().makeExit(new ProfileCallBack<Object>() {
            @Override
            public void onSuccess(Object data) {
                onSuccessExit();
            }

            @Override
            public void onAbort(Response.ResponseStatus type, @Nullable Object data) {
                onSuccessExit();
            }

            @Override
            public void onError(RetrofitError error) {
                new AlertDialog.Builder(getDashboardActivity())
                        .setMessage(error.getLocalizedMessage())
                        .setTitle("Ошибка во время выхода")
                        .setPositiveButton("Повторить", (dialogInterface, i) -> {
                            doExit();
                        })
                        .setNegativeButton("Отмена", (dialogInterface1, i1) -> {
                            onSuccessExit();
                        }).show();
            }

        });
    }

    private boolean checkPendingExit() {
        return ShopDataStorage.getInstance().getProfileAccessToken() == null;
    }


    @Override
    protected void onSaveState(Bundle outState) {
        super.onSaveState(outState);
        outState.putSerializable(ProfileUtils.PROFILE_INFO_TAG, info);
    }


    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        super.onRestoreState(savedInstanceState);
        info = (ProfileInfo) savedInstanceState.getSerializable(ProfileUtils.PROFILE_INFO_TAG);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        info = null;
    }
}
