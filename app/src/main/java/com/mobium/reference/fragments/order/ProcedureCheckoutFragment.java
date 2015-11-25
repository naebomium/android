package com.mobium.reference.fragments.order;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.client.models.CartItem;
import com.mobium.new_api.models.order.OrderItem;
import com.mobium.client.models.SuccessOrderData;
import com.mobium.client.models.ShoppingCart;
import com.mobium.google_places_api.models.AutoCompleteResult;
import com.mobium.new_api.Api;
import com.mobium.new_api.Handler;
import com.mobium.new_api.KeyType;
import com.mobium.new_api.KeyValueStorage;
import com.mobium.new_api.methodParameters.GetShopPointParam;
import com.mobium.new_api.methodParameters.NewOrderParam;
import com.mobium.new_api.models.MobiumError;
import com.mobium.new_api.models.Region;
import com.mobium.new_api.models.ShopPoint;
import com.mobium.new_api.models.order.DeliveryMethod;
import com.mobium.new_api.models.order.DeliveryMethods;
import com.mobium.new_api.models.order.Field;
import com.mobium.new_api.models.order.NewOrderResult;
import com.mobium.new_api.models.order.PaymentType;
import com.mobium.new_api.utills.RegionUtils;
import com.mobium.reference.R;
import com.mobium.reference.ReferenceApplication;
import com.mobium.reference.activity.AutoRegionActivity;
import com.mobium.reference.fragments.InjectAbstractFragment;
import com.mobium.reference.utils.CheckOutUtils;
import com.mobium.reference.utils.Dialogs;
import com.mobium.reference.utils.FragmentActionHandler;
import com.mobium.reference.utils.FragmentUtils;
import com.mobium.config.common.Config;
import com.mobium.reference.views.ChangeRegionUtils;
import com.mobium.reference.views.holders.TitleAutoCompleteHolder;
import com.mobium.reference.views.holders.TitleTextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

import static com.mobium.reference.utils.CheckOutUtils.buildParam;

/**
 *  on 21.10.15.
 */
public class ProcedureCheckoutFragment extends InjectAbstractFragment {
    private static final String TAG = "PCheckoutFragment";
    protected
    @Bind(R.id.message_to_user_text_)
    TextView messageToUser;

    private TitleAutoCompleteHolder regionHolder; // holder для выбора региона
    private TitleTextHolder deliveryHolder; //holder для способа доставки
    private TitleTextHolder pointHolder;
    private TitleTextHolder paymentHolder;

    View shopPointsView;

    protected
    @Bind(R.id.payment_place_)
    LinearLayout payment_place;

    private LinearLayout fieledsLayout;

    private Region selectedRegion;//выбранный регион
    private DeliveryMethod selectedDeliveryMethod; // выбранный способ доставки, обнуляется при смене региона
    private HashMap<String, String> form; // форма заказа(см метод), обнуляется при смене региона или способа доставки
    private PaymentType selectedPaymentType; // выбранный способ оплаты(пока не актуально)
    private ShopPoint selectedPoint; // выбранная точка самовывоза, обнуляется при смене региноа, актуально при выборе доставки самовывоз

    private LayoutInflater inflater;

    HashMap<String, String> orderInfoMap = new HashMap<>();

    EditText[] textFields;

    private void showRegions(Region currentRegion) {
        selectedRegion = currentRegion;
        selectedDeliveryMethod = null;
        form = null;
        selectedPaymentType = PaymentType.cash;
        selectedPoint = null;
        regionHolder.autoCompleteTextView.setText(currentRegion.getTitle());
        Api.getInstance().getDeliveryMethods(buildParam(ReferenceApplication.getInstance().getCart(), currentRegion.getId()))
                .doOnRequest(l -> showProgress(true))
                .doOnCompleted(() -> showContent(true))
                .subscribe(this::showDeliveryMethods, throwable -> {
                    if (FragmentUtils.isUiFragmentActive(ProcedureCheckoutFragment.this))
                        new AlertDialog.Builder(getContext())
                                .setTitle("Ошибка во время загрузки региона")
                                .setMessage(throwable.getMessage())
                                .show();
                    regionHolder.autoCompleteTextView.setText("");
                });
    }


    private void showDeliveryMethods(DeliveryMethods methods) {
        deliveryHolder.text.setText("Выберите способ доставки");


        View.OnClickListener listener = v -> CheckOutUtils.buildDeliveryMethodsDialog(getActivity(), Arrays.asList(methods.methods), method -> {

            shopPointsView.setVisibility(View.GONE);

            deliveryHolder.text.setText(method.getTitle());
            //пользватель выбрал новый способ доставки.
            //"выбрать" сменить на название метода method.getTitle();
            method.setIsPickUp(true);
            //сохранить метод в private selectedDeliveryMethod
            selectedDeliveryMethod = method;
            //показать выбор способа оплаты, показать форму для ввода полей
            //есть общие поля для ввода, есть специальные поля у каждого метода
            ProcedureCheckoutFragment.this.showForm(methods.staticFields, method.getFields());
            ProcedureCheckoutFragment.this.showPaymentType(method.getPaymentTypes());
            if (method.isPickup()) {
                Api.getInstance().GetShopPoints(selectedRegion, ShopPoint.ShopPointType.pickupShop, method.getId(), ReferenceApplication.getInstance().getCart()).invoke(new Handler<GetShopPointParam, ShopPoint.ShopPointList>() {
                    @Override
                    public void onResult(ShopPoint.ShopPointList shopPointList) {
                        deliveryHolder.text.setText(method.getTitle());
                        if (shopPointList != null && shopPointList.points != null &&
                                shopPointList.points.length > 0) {
                            showPoints(shopPointList.points);
                        } else {
                            shopPointsView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onBadArgument(MobiumError mobiumError, GetShopPointParam param) {
                        Log.w(TAG, mobiumError.toString() + "  " + param.toString());
                    }

                    @Override
                    public void onException(Exception ex) {
                        Log.w(TAG, ex.toString());
                    }
                });
            }
        }).show();

        deliveryHolder.text.setOnClickListener(listener);
    }


    public void showPoints(ShopPoint[] points) {
        shopPointsView.setVisibility(View.VISIBLE);
        pointHolder.text.setOnClickListener(view -> {
            Dialogs.showSelectDialog(
                    getContext(),
                    "Выберете точку получения",
                    Arrays.asList(points),
                    ShopPoint::getAddress,
                    newShopPoint -> {
                        this.selectedPoint = newShopPoint;
                        pointHolder.text.setText(newShopPoint.getTitle());
                    });
        });

    }

    private void showForm(Field[] staticFields, Field[] specificFields) {
        fieledsLayout.removeAllViews();
        //тут надо объеденить эти массивы и создать таблицу для ввода данных.
        List<Field> fields = new ArrayList<Field>(Arrays.asList(staticFields));
        fields.addAll(Arrays.asList(specificFields));

        Optional<Map<String, String>> savedFieldsValues = KeyValueStorage.getInstance(getContext()).getMap(KeyType.deliveryInfo);


        int size = fields.size();
        textFields = new EditText[size];
        for (int i = 0; i < size; i++) {
            View fieldView = inflater.inflate(R.layout.checkout_field_item, (ViewGroup) getView(), false);
            fieledsLayout.addView(fieldView);
            TextView titleText = (TextView) fieldView.findViewById(R.id.title);
            EditText valueText = (EditText) fieldView.findViewById(R.id.value);

            final Field field = fields.get(i);

            valueText.setTag(field);
            textFields[i] = valueText;

            switch (field.getId()) {
                case "email":
                    valueText.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    break;
                case "phone":
                    valueText.setInputType(InputType.TYPE_CLASS_PHONE);
                    break;
                default:
                    valueText.setInputType(InputType.TYPE_CLASS_TEXT);
            }

            titleText.setText(field.getTitle());

            valueText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String userInput = s.toString();
                    orderInfoMap.put(field.getId(), userInput);
                }
            });
            if (savedFieldsValues.isPresent() && i < 3) {
                String savedValue = savedFieldsValues.get().get(field.getId());
                if (savedValue != null) {
                    valueText.setText(savedValue);
                }
            }
        }
    }

    private void showPaymentType(List<PaymentType> types) {
        paymentHolder = TitleTextHolder.bind(payment_place);
        paymentHolder.title.setText("Cпособ оплаты");
        if (selectedPaymentType != null) {
            paymentHolder.text.setText(selectedPaymentType.toString());
        } else {
            paymentHolder.text.setText("выбрать");
        }
        payment_place.setVisibility(View.VISIBLE);

        paymentHolder.text.setOnClickListener(view -> {
            Dialogs.showSelectDialog(
                    getContext(),
                    "Выберите cпособ оплаты",
                    types,
                    PaymentType::toString,
                    paymentType -> {
                        this.selectedPaymentType = paymentType;
                        paymentHolder.text.setText(paymentType.toString());
                    });
        });
    }

    @OnClick(R.id.fragment_order_buy)
    protected void onCheckOutButtonClick() {

        if (selectedRegion == null) {
            showErrorMessage(regionHolder.autoCompleteTextView, "Выберите регион");
            return;
        }

        if (selectedDeliveryMethod == null) {
            showErrorMessage(deliveryHolder.text, "Выберите способ доставки");
            return;
        }

        if (shopPointsView.getVisibility() == View.VISIBLE && selectedPoint == null) {
            showErrorMessage(pointHolder.text, "Выберите точку получения");
            return;
        }

        if (selectedPaymentType == null) {
            showErrorMessage(paymentHolder.text, "Выберите способ оплаты");
            return;
        }
        if (Stream.of(textFields).map(f -> {
            Field field = (Field) f.getTag();
            if (field.isRequired() && f.getText().length() == 0) {
                showErrorMessage(f, "Проверьте поле");
                return false;
            }
            return true;
        }).collect(Collectors.toList()).contains(false)) {
            return;
        }

        ShoppingCart cart = ReferenceApplication.getInstance().getCart();

        int cost = cart.getItemsCost();

        NewOrderParam param = new NewOrderParam();

        param.setDeliveryType(selectedDeliveryMethod.getId());
        param.setOrderInfo(orderInfoMap);
        param.setPaymentType(selectedPaymentType.toInt());
        param.setPrice(cost);
        param.setRegionId(selectedRegion.getId());
        if (selectedPoint != null) {
            param.setPointId(selectedPoint.getId());
        }

        NewOrderParam.Item[] items = mapItemsToOrderParam(cart.getItems());

        param.setItems(items);

        Api.getInstance().newOrder(param)
                .doOnRequest(l -> showProgress(true))
                .doOnCompleted(() -> showContent(true))
                .subscribe(result -> onNewOrderResult(result, items), throwable -> {
                    showContent(true);
                    if (FragmentUtils.isUiFragmentActive(ProcedureCheckoutFragment.this))
                        new AlertDialog.Builder(getContext())
                                .setTitle("Ошибка при оформлении заказа")
                                .setMessage(throwable.getMessage())
                                .show();
                });

        //проверить наличие введенных данных
        //нужно:
        // регион
        // метод доставки
        // способ оплаты
        // HashMap с размером равным сумме полей(все поля заполнены см метод)

        //если все ок, то формирует аргумент запроса и посылаем на сервер
        //из ответа и введенных данных нужно сформировать SimpleOrder : IOrder
        //логика в том, что нужно нужно хранить на устройстве список товаров, выбранный способ доставки и id заказа с сервера

    }

    private static NewOrderParam.Item[] mapItemsToOrderParam(CartItem[] cartItems) {
        return Stream.of(cartItems)
                .map(i -> new NewOrderParam.Item(i.shopItem.getId(), i.count, i.shopItem.getModificationsMap()))
                .collect(Collectors.toList()).toArray(new NewOrderParam.Item[0]);

    }

    private static OrderItem[] mapParamToItem(NewOrderParam.Item[] items) {
        return Stream.of(items).map(item -> new OrderItem(item.id, item.count, item.modifications))
                .collect(Collectors.toList()).toArray(new OrderItem[0]);
    }

    private void onNewOrderResult(NewOrderResult result, NewOrderParam.Item[] items) {
        if (result.isSuccess()) {
            ShoppingCart cart = ReferenceApplication.getInstance().getCart();


            SuccessOrderData data = new SuccessOrderData(
                    result.getNum(),
                    result.getServerCost(),
                    SuccessOrderData.newOrder
            );

            data.setUserData(orderInfoMap);
            data.setTime(new Date().getTime());
            data.setCacheItems(new ArrayList<>(Arrays.asList(ReferenceApplication.getInstance().getCart().getItems())));
            data.setItemCounts(Arrays.asList(mapParamToItem(items)));
            ReferenceApplication.getInstance().onSuccessOrder(data);

            getFragmentManager().popBackStackImmediate();
            Intent resultIntent = new Intent();
            resultIntent.putExtra(SuccessOrderData.class.getName(), data);
            getTargetFragment()
                    .onActivityResult(
                            getTargetRequestCode(),
                            Activity.RESULT_OK,
                            resultIntent
                    );

            saveFields();
        }
    }




    private void saveFields() {
        KeyValueStorage.getInstance(getContext()).put(KeyType.deliveryInfo, orderInfoMap);
    }

    private void showErrorMessage(TextView textView, String message) {
        textView.setError(message);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_checkout_;
    }

    @Override
    public String getTitle() {
        return "Оформление заказа";
    }

    private ProgressDialog progressDialog;

    public void showProgress(boolean animated) {
        if (progressDialog == null)
            progressDialog = createProgressBar();
        progressDialog.show();
    }

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

    @OnClick(R.id.fragment_order_callToShop)
    protected void doCall() {
        FragmentActionHandler.doAction(getActivity(),
                new Action(ActionType.DO_CALL, Config.get().getApplicationData().getShopPhone()));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //configure regions
        View regionView = view.findViewById(R.id.fragment_checkout_region);
        regionHolder = TitleAutoCompleteHolder.bind(regionView);
        regionHolder.autoCompleteTextView.setAdapter(new AutoRegionActivity.AutoAdapter(ChangeRegionUtils.getApi(getContext(), Config.get().getApplicationData().getApiKeyGooglePlaceApi(), Config.get().logDebugInfo())));
        regionHolder.title.setText("Выбор региона");
        regionHolder.autoCompleteTextView.setLoadingIndicator(regionHolder.progressBar);

        regionHolder.autoCompleteTextView.setOnItemClickListener((parent, view2, position, id) -> {
            AutoCompleteResult.Item currentItem = (AutoCompleteResult.Item) regionHolder.autoCompleteTextView.getAdapter().getItem(position);
            RegionUtils.loadRegionsByAutoCompleteRegionWithDialog(getActivity(), currentItem, data -> showRegions(data.region));
        });
        showRegions(ReferenceApplication.getInstance().getRegion().orElse(Region.getRussiaRegion()));

        regionHolder.autoCompleteTextView.setPadding(8,
                regionHolder.autoCompleteTextView.getPaddingTop(),
                regionHolder.autoCompleteTextView.getPaddingRight(),
                regionHolder.autoCompleteTextView.getPaddingBottom());

        //configure delivery
        View deliveryView = view.findViewById(R.id.fragment_checkout_delivery_method);
        deliveryHolder = TitleTextHolder.bind(deliveryView);
        deliveryHolder.title.setText("Способ доставки");
        deliveryHolder.text.setText("Вначале выберите регион");

        shopPointsView = view.findViewById(R.id.fragment_checkout_shop_items);
        pointHolder = TitleTextHolder.bind(shopPointsView);
        pointHolder.title.setText("Пункт выдачи");
        pointHolder.text.setText("выбрать");


        fieledsLayout = (LinearLayout) view.findViewById(R.id.fields_place);

        inflater = LayoutInflater.from(getContext());

    }
}
