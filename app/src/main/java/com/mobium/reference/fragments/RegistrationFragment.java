package com.mobium.reference.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.mobium.reference.R;
import com.mobium.reference.anotations.NeedInternetAccess;
import com.mobium.reference.utils.Dialogs;
import com.mobium.reference.utils.FragmentUtils;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;
import com.mobium.reference.views.RequestCodeViewController;
import com.mobium.reference.views.VisibilityViewUtils;
import com.mobium.reference.views.registration.RegFieldView;
import com.mobium.userProfile.ProfileApi;
import com.mobium.userProfile.Response;
import com.mobium.userProfile.ResponseParams.Activation;
import com.mobium.userProfile.ResponseParams.RegField;
import com.mobium.userProfile.ResponseParams.Registration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.view.ViewObservable;
import rx.schedulers.Schedulers;

/**
 *  on 18.07.15.
 * http://mobiumapps.com/
 */

@NeedInternetAccess
public class RegistrationFragment extends InjectAbstractFragment {
    private Subscription registerFieldSubscription;
    private List<Subscription> subsctriptionList = new LinkedList<>();
    private AlertDialog codeForm;

    @Bind(R.id.fragment_registration_form)
    protected ViewGroup regFieldsContainer;
    @Bind(R.id.fragment_registration_button)
    protected View registerButton;
    @Bind(R.id.fragment_registration_progress)
    protected View loadBar;
    @Bind(R.id.fragment_registration_type_text)
    protected TextView typeOfAccount;
    @Bind(R.id.fragment_registration_type_button)
    protected Switch typeButton;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerButton.setEnabled(false);
    }

    private void createSubscription() {
        if (registerFieldSubscription == null) {
            registerFieldSubscription =
                    ProfileApi.getInstance().getRegFields()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnError(throwable -> {
                                if (throwable instanceof RetrofitError &&
                                        ((RetrofitError) throwable).getKind() == RetrofitError.Kind.NETWORK) {
                                    registerFieldSubscription.unsubscribe();
                                    registerFieldSubscription = null;
                                    Dialogs.showNetworkErrorDialog(
                                            RegistrationFragment.this.getActivity(),
                                            throwable.getLocalizedMessage(),
                                            RegistrationFragment.this::createSubscription,
                                            RegistrationFragment.this.getFragmentManager()::popBackStack
                                    );
                                } else Dialogs.showExitScreenDialog(
                                        RegistrationFragment.this.getActivity(),
                                        RegistrationFragment.this.getFragmentManager()::popBackStack,
                                        throwable.getLocalizedMessage()
                                );
                            })
                            .map(regFieldsResponse -> regFieldsResponse.data.fields)
                            .subscribe(regFieldList -> {
                                regFieldsContainer.removeAllViews();

                                registerButton.setVisibility(View.VISIBLE);
                                //объеденение корректности епт
                                List<Observable<Boolean>> correctAND = new ArrayList<>();


                                for (int i = 0; i < regFieldList.size(); i++) {
                                    RegFieldView regFieldView = new RegFieldView();
                                    regFieldView.showRegFieldView(regFieldsContainer, regFieldList.get(i));
                                    correctAND.add(regFieldView.correct());
                                    if (i == 0) {
                                        showKeyboard(regFieldView.getEditText());
                                    }
                                }


                                subsctriptionList.add(
                                        Observable.combineLatest(correctAND, args -> {
                                            for (Object o : args)
                                                if (!(boolean) o) return false;
                                            return true;
                                        }).subscribe(registerButton::setEnabled)
                                );

                                subsctriptionList.add(
                                        ViewObservable.clicks(registerButton).subscribe(event -> {
                                            makeRegistration(regFieldList)
                                                    .subscribe(activation -> {
                                                                if (isAdded() && isVisible())
                                                                    LoginFragment.onLogin(getActivity(), null);
                                                            },
                                                            this::showError);
                                        }));
                                showContent();
                            }, this::showError);
        }
    }

    private void showKeyboard(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(editText.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        new Handler().postDelayed(editText::requestFocus, AnimationUtils.loadAnimation(getActivity(), R.anim.abc_fade_in).getDuration() + 50);
    }

    @Override
    public void onStart() {
        super.onStart();
        createSubscription();
        Events.get(getActivity()).navigation().onPageOpen(OpenPageEvents.registration.name());
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_registration;
    }

    private void showError(Throwable throwable) {
        if (!isFragmentUIActive())
            return;
        showContent();
        if (throwable instanceof RetrofitError) {
            RetrofitError e = (RetrofitError) throwable;
            if (e.getKind() == RetrofitError.Kind.NETWORK)
                Dialogs.showNetworkErrorDialog(
                        getActivity(),
                        e.getLocalizedMessage(),
                        registerButton::performClick,
                        getFragmentManager()::popBackStack
                );
            else {
                Dialogs.showExitScreenDialog(
                        getActivity(),
                        getFragmentManager()::popBackStack,
                        throwable.getLocalizedMessage()
                );
            }
        }
    }

    private boolean isFragmentUIActive() {
        return FragmentUtils.isUiFragmentActive(this);
    }

    private void showErrors(List<Registration.FieldError> errors) {
        if (!isFragmentUIActive())
            return;
        new AlertDialog.Builder(getActivity()).
                setMessage(errors.size() > 1 ? "Произошли ошибки" : "Произошла ошибка" +
                                " во время регистрации\n" + Registration.FieldError.format(errors)).show();
    }

    private Observable<Activation> makeRegistration(List<RegField> filledRegFields) {
        //бич-контроллер диалога
        final RequestCodeViewController requestCode =
                new RequestCodeViewController(getActivity());
        codeForm = new AlertDialog.Builder(getActivity())
                .setTitle("Подтверждение регистрации")
                .setMessage("Введите код из смс")
                .setOnDismissListener( d -> requestCode.dismiss())
                .setView(requestCode.view).create();


        //конфирм токЕн из сети
        Observable<String> confirmToken =
                ProfileApi.getInstance().makeRegistration(filledRegFields)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnRequest(aLong -> showLoadBar())
                        .doOnCompleted(this::showContent)
                        .doOnError(this::showError)
                        .doOnNext(registrationResponse -> {
                            RegFieldView.password.clear();
                            if (registrationResponse.getType() == Response.ResponseStatus.ERROR) {
                                codeForm.dismiss();
                                if (registrationResponse.data.errorMessage != null)
                                    showErrors(Collections.singletonList(
                                                    new Registration.FieldError("", registrationResponse.data.errorMessage))
                                    );
                                else if (registrationResponse.data.errors != null)
                                    showErrors(registrationResponse.data.errors);
                            } else if (registrationResponse.data.accessToken != null) {
                                LoginFragment.onLogin(getActivity(), null);
                            } else if (registrationResponse.data.confirmToken == null) {
                                Dialogs.showExitScreenDialog(
                                        getActivity(),
                                        getFragmentManager()::popBackStack, "Ошибка во время регистрации");
                            }
                        })
                        .map(registrationResponse -> registrationResponse.data.confirmToken)
                        .filter(token -> token != null)
                        .doOnNext(token -> {
                            codeForm.show();
                            requestCode.initCodeButton(token);
                        });

        //актульная пара код - токен
        return Observable.combineLatest(requestCode.codeOnClick(), confirmToken, Pair::<String, String>create)
                //выдупливаем пары с пустыми кодами
                .filter(p -> p.second != null && p.first != null)                         //активируем профайл, аксесс токен вдуплиться сам внутри апей
                .flatMap(codeAndToken ->
                                ProfileApi.getInstance().makeActivation(
                                        codeAndToken.second,
                                        codeAndToken.first)
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnRequest(l -> {
                    Events.get(getActivity()).navigation().onPageOpen(OpenPageEvents.registration_confirmation.name());
                    showLoadBar();
                    codeForm.hide();
                })
                .doOnCompleted(() -> {
                    codeForm.hide();
                    showContent();
                })
                .filter(activationResponse -> {
                    //вывод ошибок и фильтрация данных потока без асес токена
                    if (activationResponse.data == null ||
                            activationResponse.data.errorMessage != null ||
                            activationResponse.data.accessToken == null) {

                        Dialogs.showNetworkErrorDialog(
                                getActivity(),
                                Optional.ofNullable(activationResponse.data)
                                        .map(activation -> activation.errorMessage).orElse("Ошибка во время подтверждения"),
                                () -> {
                                    showContent();
                                    codeForm.show();
                                },
                                getFragmentManager()::popBackStack
                        );

                        return false;
                    } else
                        return true;
                })
                .map(activationResponse -> activationResponse.data);
    }

    @Override
    public boolean onBackPressed() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Прервать регистрацию?")
                .setMessage("Введенные данные будут утеряны")
                .setCancelable(false)
                .setPositiveButton("Прервать", (d, i) -> getFragmentManager().popBackStack())
                .setNegativeButton("Нет", (d, i) -> {
                })
                .show();
        return true;
    }

    public void showContent() {
        getActivity().runOnUiThread(() -> VisibilityViewUtils.hide(loadBar, true));

    }

    public void showLoadBar() {
        getActivity().runOnUiThread(() -> VisibilityViewUtils.show(loadBar, false));
    }

    @Override
    public void onStop() {
        super.onStop();
        if (registerFieldSubscription != null)
            registerFieldSubscription.unsubscribe();
        if (subsctriptionList != null) {
            Stream.of(subsctriptionList).forEach(Subscription::unsubscribe);
            subsctriptionList.clear();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (RegFieldView.password != null)
            RegFieldView.password.clear();
        if (codeForm != null)
            codeForm.dismiss();
    }


    @Override
    public String getTitle() {
        return "Регистрация";
    }
}
