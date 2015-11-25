package com.mobium.reference.fragments.order;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Optional;
import com.mobium.client.ShopDataStorage;
import com.mobium.new_api.models.Region;
import com.mobium.new_api.models.ShopPoint;
import com.mobium.new_api.models.order.DeliveryMethod;
import com.mobium.new_api.models.order.Field;
import com.mobium.new_api.models.order.PaymentType;
import com.mobium.reference.R;
import com.mobium.reference.fragments.InjectAbstractFragment;
import com.mobium.reference.presenter.CheckoutPresenterImpl;
import com.mobium.reference.utils.BundleUtils;
import com.mobium.reference.utils.CheckOutUtils;
import com.mobium.reference.utils.Dialogs;
import com.mobium.reference.utils.Functional;
import com.mobium.reference.utils.RxUtil;
import com.mobium.config.common.Config;
import com.mobium.reference.view.CheckoutView;
import com.mobium.reference.views.ChangeRegionUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 *  on 13.10.15.
 */
public class CheckoutFragment extends InjectAbstractFragment implements CheckoutView {
    protected
    @Bind(R.id.message_to_user_text)
    TextView messageToUser;
    protected
    @Bind(R.id.static_fields_place)
    LinearLayout static_fields_place;
    protected
    @Bind(R.id.region_place)
    LinearLayout regionPlace;
    protected
    @Bind(R.id.delivery_place)
    LinearLayout deliveryPlace;
    protected
    @Bind(R.id.payment_place)
    LinearLayout payment_place;
    protected
    @Bind(R.id.fragment_order_buy)
    View makeOrder;

    private CheckoutPresenter presenter;
    Functional.Receiver<Region> regionReceiver = p -> {};
    Functional.ChangeListener<ShopPoint> shopPointReceiver = p -> {};

    @Override
    public Observable<CheckoutPresenterImpl.FilledMethod> showDeliveryMethods(DeliveryMethod[] methods) {
        return Observable.empty();
    }

    @Override
    public Observable<Map<Field, String>> showStaticFields(Field[] field, @Nullable String[] defaultValues) {
        static_fields_place.removeAllViews();
        EditText[] editTexts = new EditText[field.length];
        for (int i = 0; i < field.length; i++) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.view_edit_text, static_fields_place, false);
            editTexts[i] = (EditText) view.findViewById(R.id.edit_text);
            editTexts[i].setTag(field[i]);
            editTexts[i].setHint(field[i].getTitle());
            final int finalI = i;
            if (defaultValues != null && finalI < defaultValues.length)
                Optional.ofNullable(defaultValues).map(values -> values[finalI]).ifPresent(editTexts[finalI]::setText);
            static_fields_place.addView(view);
        }

        Observable<EditText> input = Observable.from(editTexts);
        return input
                .flatMap(RxUtil::completeInput)// stream of EdtText events
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(events ->
                                input.filter(e -> CheckOutUtils.checkFieldAndShowErrorIfIncorrect(e, (Field) e.getTag()))
                                        .toMap(e -> (Field) e.getTag(), e -> e.getText().toString())
                                        .filter(map -> map.size() == field.length)
                );

//        return Observable.from(editTexts)
//                .flatMap(RxUtil::completeInput)// stream of EdtText events
//                .debounce(500, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext((onTextChangeEvent) -> { // show error, if user made mistake
//                    if (!CheckOutUtils.isInputCorrect(onTextChangeEvent))
//                        onTextChangeEvent.editText.setError("Проверьте поле");
//                })
//                .filter(CheckOutUtils::isInputCorrect) // only valid fields
//                .toMap(editTextEvent -> (Field) editTextEvent.editText.getTag(), editTextEvent -> editTextEvent.text) // called on every change
//                .doOnNext(fieldStringMap -> {
//                    Log.v("map", fieldStringMap.toString()); // never called
//                }); // map of it
    }

    @Override
    public Observable<PaymentType> showPaymentTypes(PaymentType[] types) {
        return Observable.empty();
    }

    @Override
    public Observable<Region> showRegions(List<Region> regions, @Nullable Region current) {

        TextView regionLabel = new TextView(getContext());
        regionLabel.setText("Ваш Регион: ");

        TextView regionSelection = new TextView(getContext());
        regionSelection.setText("Выбрать");
        regionSelection.setOnClickListener(v ->
                ChangeRegionUtils.showAutoCompleteDialog(getActivity(), new ChangeRegionUtils.DataProvider() {
            @Override
            public List<Region> regions() {
                return ShopDataStorage.getInstance().getRegions();
            }

            @Override
            public String title() {
                return "Изменить регион";
            }

            @Override
            public String message() {
                return "Начните вводить регион";
            }
        }, regionReceiver));

        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        regionPlace.setLayoutParams(linearParams);
        regionPlace.addView(regionLabel);
        regionPlace.addView(regionSelection);

        return Observable.create(region -> regionReceiver = region::onNext);
    }


    @Override
    public Observable<ShopPoint> showPoints(ShopPoint[] points) {

        TextView textView = null;

        textView.setOnClickListener(v ->
                        Dialogs.showSelectDialog(getContext(), "Выберете точку получения", Arrays.asList(points), ShopPoint::getAddress, shopPointReceiver)
        );
        return Observable.create(subscriber -> shopPointReceiver = subscriber::onNext);
    }

    @Override
    public void showMessageToUser(CharSequence message) {
        messageToUser.setText(message);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_checkout;
    }


    @Override
    public void exit() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        getFragmentManager().popBackStack();
    }

    @Override
    public void exitViewAlert(String title, String message) {
        Dialogs.showExitScreenDialog(getActivity(), () -> getFragmentManager().popBackStack(), title, message);
    }

    @Override
    public void showError(String title, String message, String applyTitle, DialogInterface.OnClickListener apply, String cancelTitle, DialogInterface.OnClickListener cancel) {
        Dialogs.showError(getActivity(), title, message, applyTitle, apply, cancelTitle, cancel);
    }


    private ProgressDialog progressDialog;

    @Override
    public void showProgress(boolean animated) {
        if (progressDialog == null)
            progressDialog = createProgressBar();
        progressDialog.show();
    }

    @Override
    public void showContent(boolean animated) {
        if (progressDialog != null)
            progressDialog.hide();
    }

    private ProgressDialog createProgressBar() {
        ProgressDialog bar = new ProgressDialog(getContext());
        bar.setTitle("Обмен данными");
        bar.setCancelable(false);
        bar.setCanceledOnTouchOutside(false);
        return bar;
    }


    public static CheckoutFragment getInstance(GoodsModel model) {
        CheckoutFragment fragment = new CheckoutFragment();
        fragment.setArguments(BundleUtils.toBundle(new Bundle(1), model));
        return fragment;
    }

    @Override
    public String getTitle() {
        return "Оформление заказа";
    }


    public static class GoodsModel implements Serializable {
        public final HashMap<String, Integer> counts;
        public final int cost;

        public GoodsModel(int cost, HashMap<String, Integer> counts) {
            this.cost = cost;
            this.counts = counts;
        }

    }

    public interface CheckoutPresenter {
        void saveState(@NonNull Bundle bundle);

        void loadState(@NonNull Bundle bundle);

        void viewCreated(View root);

        void loadStatus(LoadStatus status);

        void onBuyButtonClick(View button);

        enum LoadStatus {
            mayLoading, mayNotLoading
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoodsModel model = BundleUtils.fromBundle(getArguments(), GoodsModel.class);
        presenter = new CheckoutPresenterImpl(model, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.loadStatus(CheckoutPresenter.LoadStatus.mayLoading);
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.loadStatus(CheckoutPresenter.LoadStatus.mayNotLoading);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        makeOrder.setOnClickListener(presenter::onBuyButtonClick);
        presenter.viewCreated(view);
    }

}
