package com.mobium.reference.fragments.shop_info;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.annimon.stream.Stream;
import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.new_api.Api;
import com.mobium.new_api.Handler;
import com.mobium.new_api.methodParameters.SendMessageParam;
import com.mobium.new_api.models.MobiumError;
import com.mobium.new_api.models.ResponseBase;
import com.mobium.reference.R;
import com.mobium.reference.fragments.InjectAbstractFragment;
import com.mobium.reference.utils.FragmentActionHandler;
import com.mobium.config.common.Config;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;
import com.mobium.reference.utils.text.Validator;
import com.mobium.reference.views.HolderIconTextUnderText;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *  on 03.08.15.
 * http://mobiumapps.com/
 */

public class ContactsFragment extends InjectAbstractFragment {

    @Bind(R.id.fragment_contacts_static_contact_items)
    protected LinearLayout layout;

    @Bind(R.id.fragment_contacts_top_layout)
    protected LinearLayout topLayout;

    @Bind(R.id.fragment_contacts_message)
    protected EditText messageEditText;

    @Bind(R.id.fragment_contacts_email)
    protected EditText emailEditText;

    @Bind(R.id.fragment_contacts_button)
    protected Button button;

    @Bind(R.id.fragment_contacts_message_wrapper)
    protected TextInputLayout messageWrapper;

    @Bind(R.id.fragment_contacts_email_wrapper)
    protected TextInputLayout emailWrapper;


    private View[] input;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final LayoutInflater inflater = LayoutInflater.from(getActivity());

        Stream.of(getStaticContactItems(getActivity())).forEach(contactItem -> {
            View v = inflater.inflate(R.layout.view_icon_text_undertext, layout, false);
            HolderIconTextUnderText holder = new HolderIconTextUnderText(v);

            holder.clickArea.setOnClickListener(contactItem.listener);
            holder.imageView.setImageResource(contactItem.icon);
            holder.textView.setText(contactItem.text);
            holder.underText.setText(contactItem.underText);
            holder.deliver.setVisibility(View.VISIBLE);

            layout.addView(v);
        });
        input = new View[]{button, emailEditText, messageEditText};
//        messageWrapper.setErrorEnabled(true);
//        emailWrapper.setErrorEnabled(true);

        view.requestFocus();
    }

    @Override
    public void onStart() {
        super.onStart();
        Events.get(getActivity()).navigation().onPageOpen(OpenPageEvents.contacts.name());
    }

    @Override
    public String getTitle() {
        return "Контакты";
    }


    @OnClick(R.id.fragment_contacts_button)
    public void onSendMessageClick() {
        boolean correct = true;
        if (messageEditText.getText().length() < 10) {
            try {
                messageEditText.setError("Слишком короткое сообщение");
            } catch (Exception e) {
                e.printStackTrace();
            }
            correct = false;
        }
        if (!Validator.testEmail(emailEditText.getText().toString())) {
            try {
            emailEditText.setError("Неверно введен email адрес");
            } catch (Exception e) {
                e.printStackTrace();
            }
            correct = false;
        }
        if (!correct)
            return;

        sendMessage(emailEditText.getText().toString(), messageEditText.getText().toString());
    }


    private void sendMessage(final String email, final String message) {
        Stream.of(input).forEach(v -> v.setEnabled(false));

        Api.getInstance().SendMessage(message, email).invoke(new Handler<SendMessageParam, ResponseBase>() {
            @Override
            public void onResult(ResponseBase responseBase) {
                if (!isAdded() && getView() != null)
                    return;
                Events.get(getActivity()).connectivity().onSendFeedback(message, email);
                Stream.of(input).forEach(v -> v.setEnabled(true));
                messageEditText.getText().clear();
                emailEditText.getText().clear();


                Snackbar.make(getView(), "Сообщение отправлено", Snackbar.LENGTH_LONG)
                        .show();

                topLayout.requestFocus();
            }

            @Override
            public void onBadArgument(MobiumError mobiumError, SendMessageParam sendMessageParam) {
                if (!isAdded() && getView() != null)
                    return;
                Stream.of(input).forEach(v -> v.setEnabled(true));
                Snackbar.make(getView(), "Ошибка запроса, попробуйте позже", Snackbar.LENGTH_LONG)
                        .show();
                topLayout.requestFocus();
            }

            @Override
            public void onException(Exception ex) {
                if (!isAdded() && getView() != null)
                    return;

                Stream.of(input).forEach(v -> v.setEnabled(true));
                Snackbar.make(getView(), "Ошибка во время отправки " + ex.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                        .setAction("Повторить", view -> sendMessage(email, message))
                        .show();
                topLayout.requestFocus();
            }
        });

    }

    private static List<ContactItem> getStaticContactItems(@NonNull final FragmentActivity activity) {
        return Arrays.asList(
                new ContactItem(Config.get().getApplicationData().getShopPhone(),
                        "Позвоните нам",
                        R.drawable.phone,
                        (v) -> FragmentActionHandler.doAction(activity, new Action(ActionType.DO_CALL, Config.get().getApplicationData().getShopPhone())),
                        false
                ),
                new ContactItem("Адреса магазинов",
                        "Адреса магазинов " + Config.get().getStaticData().appName(),
                        R.drawable.shop2,
                        (v) -> FragmentActionHandler.doAction(activity, new Action(ActionType.OPEN_SHOPS, null)),
                        true
                ),
                new ContactItem("Адреса сервис-центров",
                        "Сервис-центры",
                        R.drawable.support,
                        (v) -> FragmentActionHandler.doAction(activity, new Action(ActionType.OPEN_SERVICES, null)),
                        true
                )
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_contacts;
    }

    private static class ContactItem {
        public final String text;
        public final String underText;
        public final
        @DrawableRes
        int icon;
        public final View.OnClickListener listener;
        public final boolean hasRow;

        private ContactItem(String text, String underText, int icon, View.OnClickListener listener, boolean hasRow) {
            this.text = text;
            this.underText = underText;
            this.icon = icon;
            this.listener = listener;
            this.hasRow = hasRow;
        }
    }

}
