package com.mobium.reference.fragments.order;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Optional;
import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.new_api.models.order.IOrder;
import com.mobium.reference.R;
import com.mobium.reference.fragments.InjectAbstractFragment;
import com.mobium.reference.utils.BundleUtils;
import com.mobium.reference.utils.FragmentActionHandler;
import com.mobium.config.common.Config;

import butterknife.Bind;


/**
 * fragment for success order
 */

public class OrderSuccessFragment extends InjectAbstractFragment {

    private static final String TAG = "SuccessOrderFragment";
    private static final String TITLE = "Спасибо за заказ!\nИнформация о заказе выслана на ваш электронный адрес %s. " +
            "Менеджер интернет-магазина свяжется с Вами в ближайшее время.";

    protected
    @Bind(R.id.message_to_user)
    TextView messageToUserTextView;
    protected
    @Bind(R.id.order_fields)
    LinearLayout fieldsLayout;

    @Bind(R.id.social_message)
    TextView socialMessage;

    private Optional<IOrder> order = Optional.empty();

    public static OrderSuccessFragment getInstance(IOrder order) {
        final OrderSuccessFragment orderFragment = new OrderSuccessFragment();
        orderFragment.setArguments(BundleUtils.toBundle(new Bundle(1), order, IOrder.class));
        return orderFragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        order.ifPresent(o -> {
            createField("Номер заказа", o.getId());
            o.getDate().ifPresent(time -> {
                String timeFormatString = android.text.format.DateFormat.format("dd-MM-yyyy", time).toString();
                createField("Дата", timeFormatString);
            });
            Optional<String> email = Optional.ofNullable(o.getUserData().get("email"));
            if (email.isPresent()) {
                messageToUserTextView.setText(String.format(TITLE, email.get()));
            }
        });


        initSocialButtons(view);
        String message = String.format(getString(R.string.social_message), Config.get().strings().getMainPageTitle());
        socialMessage.setText(message);

        Spannable doCallLabel = new SpannableString(" \t\tПОЗВОНИТЬ НАМ");
        doCallLabel.setSpan(new RelativeSizeSpan(1.2f), 0, doCallLabel.length(), 0);
        doCallLabel.setSpan(new ImageSpan(getContext(), R.drawable.phone_black, ImageSpan.ALIGN_BOTTOM), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Button doCallButton = (Button) view.findViewById(R.id.fragment_product_do_call);
        doCallButton.setText(doCallLabel);
        doCallButton.setOnClickListener(v -> FragmentActionHandler.doAction(getActivity(), new Action(ActionType.DO_CALL, Config.get().getApplicationData().getShopPhone())));
    }

    private View createField(String key, String value) {
        View viewField = getActivity().getLayoutInflater().inflate(R.layout.order_success_text_item, null);
        TextView titleText = (TextView) viewField.findViewById(R.id.order_success_title);
        EditText valueText = (EditText) viewField.findViewById(R.id.order_success_value);
        titleText.setText(key);
        valueText.setText(value);
        fieldsLayout.addView(viewField);
        return viewField;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_order_success;
    }

    @Override
    public String getTitle() {
        return "Заказ оформлен" + order.map(IOrder::getId).map(id -> " #" + id).orElse("");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IOrder o = BundleUtils.fromBundle(getArguments(), IOrder.class);
        order = Optional.ofNullable(o);
    }

    private void initSocialButtons(View v) {
        v.findViewById(R.id.social_vk).setOnClickListener(view ->
                FragmentActionHandler.doAction(getActivity(), new Action(ActionType.OPEN_URL_EXTERNAL, Config.get().strings().getVkUrl())));
        v.findViewById(R.id.social_fb).setOnClickListener(view ->
                FragmentActionHandler.doAction(getActivity(), new Action(ActionType.OPEN_URL_EXTERNAL, Config.get().strings().getFbUrl())));
        v.findViewById(R.id.social_instagram).setOnClickListener(view ->
                FragmentActionHandler.doAction(getActivity(), new Action(ActionType.OPEN_URL_EXTERNAL, Config.get().strings().getInstagammUrl())));
        v.findViewById(R.id.social_youtube).setOnClickListener(view ->
                FragmentActionHandler.doAction(getActivity(), new Action(ActionType.OPEN_URL_EXTERNAL, Config.get().strings().getYoutubeUrl())));
    }
}
