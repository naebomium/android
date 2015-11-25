package com.mobium.reference.fragments;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mobium.client.LogicUtils;
import com.mobium.client.ShopDataStorage;
import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.config.common.Config;
import com.mobium.reference.R;
import com.mobium.reference.ReferenceApplication;
import com.mobium.reference.activity.RequestCodes;
import com.mobium.reference.fragments.order.CheckoutFragment;
import com.mobium.reference.fragments.order.OrderNewFragment;
import com.mobium.reference.utils.FragmentActionHandler;
import com.mobium.reference.utils.FragmentUtils;
import com.mobium.reference.utils.ProfileUtils;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;
import com.mobium.reference.views.MEditTextWithIcon;
import com.mobium.reference.views.RequestCodeViewController;
import com.mobium.userProfile.CallBack.ProfileCallBack;
import com.mobium.userProfile.ProfileApi;
import com.mobium.userProfile.Response;
import com.mobium.userProfile.ResponseParams.Activation;
import com.mobium.userProfile.ResponseParams.Authorization;
import com.mobium.userProfile.ResponseParams.ProfileInfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.RetrofitError;

/**
 *  on 13.07.15.
 * http://mobiumapps.com/
 */
public class LoginFragment extends BasicContentFragment implements
        LogicUtils.ProfileListener,
        FragmentUtils.HaveCustomAnimations {

    @Bind(R.id.fragment_login_email)
    MEditTextWithIcon loginView;
    @Bind(R.id.fragment_login_password)
    MEditTextWithIcon passWordView;
    @Bind(R.id.fragment_login_button_login)
    Button loginButton;



    private ProgressDialog progressDialog;
    private ProfileCallBack<Authorization> callBack;

    public static void onLogin(FragmentActivity activity, ReferenceApplication application) {
        activity.getSupportFragmentManager().popBackStack();
        ProfileUtils.onLogin(activity);
    }

    @Override
    protected String getTitle() {
        return "Авторизация";
    }


    @OnClick(R.id.fragment_login_button_register)
    protected void goToRegistrationFragment(View v) {
        FragmentUtils.doOpenFragmentDependsOnInternetAccess(getActivity(), RegistrationFragment.class, true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, root);

        passWordView.setRequired(true);
        loginView.setRequired(true);

        passWordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        loginButton.setOnClickListener(this::makeAuthorization);
////
//        loginView.getInput().setText("felidae@mail.ru");
//        passWordView.getInput().setText("194156");

        return root;
    }

    private void makeAuthorization(View loginBtn) {
        if (!loginView.isCorrect() || !passWordView.isCorrect())
            return;
        String userEmail = loginView.getValue();
        String passWord = passWordView.getValue();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Авторизация");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        callBack = new ProfileCallBack<Authorization>() {
            @Override
            public void onSuccess(Authorization data) {
                if (data.accessToken == null) {
                    if (data.confirmToken != null) {
                        onGetConfirmToken(data.confirmToken);
                        progressDialog.setOnDismissListener(null);
                    } else onAbort(Response.ResponseStatus.ERROR, data);
                }
            }

            @Override
            public void onAbort(Response.ResponseStatus type, @Nullable Authorization data) {
                if (type == Response.ResponseStatus.NEED_AUTH || type == Response.ResponseStatus.ERROR) {
                    String message = "Проверьте введенные данные";
                    if (data != null && data.errorMessage != null)
                        message = data.errorMessage;

                    progressDialog.cancel();

                    new AlertDialog.Builder(getDashboardActivity())
                            .setTitle("Ошибка во время авторизации")
                            .setMessage(message)
                            .setOnDismissListener(dialogInterface -> {
                                progressDialog.dismiss();
                            }).show();
                }
            }

            @Override
            public void onError(RetrofitError error) {
                if (FragmentUtils.isUiFragmentActive(LoginFragment.this))
                    new AlertDialog.Builder(getDashboardActivity())
                            .setTitle("Ошибка во время авторизации")
                            .setMessage(error.getLocalizedMessage())
                            .setPositiveButton("Повторить", (dialogInterface, i) -> {
                                progressDialog.show();
                                ProfileApi.getInstance().makeAuthorization(userEmail, passWord, this);
                            })
                            .setNegativeButton("Изменить данные", (dialogInterface1, i1) -> {
                                dialogInterface1.cancel();
                                progressDialog.dismiss();
                            }).show();
            }
        };

        ProfileApi.getInstance().makeAuthorization(userEmail, passWord, callBack);
        progressDialog.setOnDismissListener(dialogInterface -> callBack.cancel());
    }

    private void onGetConfirmToken(final String confirmToken) {
        final RequestCodeViewController requestCode = new RequestCodeViewController(getActivity());

        requestCode.initCodeButton(confirmToken);

        requestCode.sendCode.setOnClickListener(view -> {
            if (requestCode.input.isCorrect())
                makeActivation(confirmToken, requestCode.input.getValue());
        });

        new AlertDialog.Builder(getActivity())
                .setTitle("Подтверждение регистрации")
                .setMessage("Введите код из смс")
                .setView(requestCode.view)
                .show();

    }

    private void makeActivation(final String confirmToken, final String code) {
        progressDialog.show();
        ProfileCallBack<Activation> callBack = new ProfileCallBack<Activation>() {
            @Override
            public void onSuccess(Activation data) {
                //good
            }

            @Override
            public void onAbort(Response.ResponseStatus type, @Nullable Activation data) {
                if (type == Response.ResponseStatus.NEED_AUTH || type == Response.ResponseStatus.ERROR) {
                    String message = "Проверьте введенные данные";
                    if (data != null && data.errorMessage != null)
                        message = data.errorMessage;

                    progressDialog.cancel();

                    new AlertDialog.Builder(getDashboardActivity())
                            .setTitle("Ошибка во время авторизации")
                            .setMessage(message)
                            .setOnDismissListener(dialogInterface ->
                                            progressDialog.dismiss()
                            ).show();
                }
            }

            @Override
            public void onError(RetrofitError error) {
                new AlertDialog.Builder(getDashboardActivity())
                        .setTitle("Ошибка во время авторизации")
                        .setMessage(error.getLocalizedMessage())
                        .setPositiveButton("Повторить", (dialogInterface, i) -> {
                            dialogInterface.cancel();
                            makeActivation(confirmToken, code);
                        })
                        .setNegativeButton("Изменить данные", (dialogInterface1, i1) -> {
                            dialogInterface1.cancel();
                            progressDialog.dismiss();
                        }).show();
            }
        };
        ProfileApi.getInstance().makeActivation(confirmToken, code, callBack);
    }

    @Override
    public void onStart() {
        super.onStart();
        Events.get(getActivity()).navigation().onPageOpen(OpenPageEvents.login.name());
        ShopDataStorage.getInstance().addProfileListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        ShopDataStorage.getInstance().removeProfileListener(this);
        if (callBack != null)
            callBack.cancel();
    }

    @Override
    public void onLogin(String accessToken) {
        if (getTargetFragment() == null) {
            onLogin(getDashboardActivity(), getReferenceApplication());
        } else if (getTargetRequestCode() == RequestCodes.MAKE_ORDER) {
            loadProfileInfo();
        }
        progressDialog.dismiss();
    }


    private void loadProfileInfo() {
        ProfileApi.getInstance().getProfileInfo(new ProfileCallBack<ProfileInfo>() {

            @Override
            public void onSuccess(ProfileInfo data) {
                ShopDataStorage.getInstance().setProfileInfo(data);
                ShopDataStorage.getInstance().trySave();
                navigateToNewOrder();
            }

            @Override
            public void onAbort(Response.ResponseStatus type, @Nullable ProfileInfo data) {

            }

            @Override
            public void onError(RetrofitError error) {

            }
        });
    }

    private void navigateToNewOrder() {
        CheckoutFragment fragment = new CheckoutFragment();
        fragment.setTargetFragment(getTargetFragment(), getTargetRequestCode());
        FragmentUtils.replace(getActivity(), fragment, true);
    }

    @Override
    public void onExit() {

    }

    @Override
    public int enter() {
        return R.anim.abc_fade_in;
    }

    @Override
    public int exit() {
        return R.anim.abc_fade_out;
    }

    @Override
    public int popEnter() {
        return R.anim.abc_fade_in;
    }

    @Override
    public int popExit() {
        return R.anim.abc_fade_out;
    }

    @OnClick(R.id.pass_reset)
    protected void resetPass(View v) {
//        FragmentActionHandler.doAction(getActivity(), new Action(ActionType.OPEN_URL_EXTERNAL, Config.get().strings().getForgetPasswordUrl()));
    }
}
