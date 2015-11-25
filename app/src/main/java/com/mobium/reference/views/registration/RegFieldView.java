package com.mobium.reference.views.registration;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.mobium.reference.R;
import com.mobium.reference.utils.text.Validator;
import com.mobium.userProfile.ResponseParams.RegField;
import com.mobium.userProfile.ResponseParams.RegFields;

import java.util.ArrayList;
import java.util.HashSet;

import rx.Observable;
import rx.android.widget.WidgetObservable;

/**
 *  on 19.07.15.
 * http://mobiumapps.com/
 */
public class RegFieldView implements IRegFieldView {
    private RegField regField;
    private EditText editText;
    private TextView title;
    public static HashSet<RegFieldView> password = new HashSet<>(2);
    private boolean correct;


    private void addListener() {
        editText.setOnFocusChangeListener((v, hasFocus) ->
                        title.setTextColor(v.getContext().getResources().getColor(
                                        hasFocus ? android.R.color.black : R.color.dark_alpha
                                )
                        )
        );
    }

    @Override
    public void showRegFieldView(final ViewGroup where, RegField field) {
        regField = field;
        LayoutInflater inflater = LayoutInflater.from(where.getContext());
        View result = inflater.inflate(R.layout.view_reg_field, where, false);
        editText = (EditText) result.findViewById(R.id.view_reg_field_edit_text);
        title = (TextView) result.findViewById(R.id.view_reg_field_title);
        title.setText(field.title);
        if (field.getValue() == null)
            editText.getText().clear();
        else
            editText.setText(field.getValue());

        addListener();

        switch (field.type) {
            case EMAIL:
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case NUMBER:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case PASSWORD:
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
                password.add(this);
                break;
            case PHONE:
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case TEXT:
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setFilters(new InputFilter[]{
                        (src, start, end, dst, dstart, dend) -> {
                            if (src.equals("")) {
                                return src;
                            }
                            if (src.toString().matches("[a-zA-Za-яА-Я ]+")) {
                                return src;
                            }
                            return "";
                        }
                });
                editText.setHint("Только латиница и цифры");
                break;
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                regField.setValue(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        where.addView(result);
    }

    private void showHint(final String s) {
        editText.setError(s);
    }

    @Override
    public rx.Observable<Pair<String, RegField>> changeText() {
        return WidgetObservable.text(editText)
                .map(s -> Pair.create(s.text().toString().trim(), regField));
    }


    @Override
    public Observable<Boolean> correct() {
        if (regField.type != RegFields.RegFieldType.PASSWORD)
            return WidgetObservable.text(editText)
                    .map(event -> event.text().toString().trim())
                    .map(s -> {
                        if (!regField.required)
                            return true;
                        boolean result = true;

                        String hint = "Проверьте поле";

                        if (s.length() == 0)
                            result = false;
                        else {
                            switch (regField.type) {
                                case TEXT:
                                    result = true;
                                    break;
                                case NUMBER:
                                    result = android.text.TextUtils.isDigitsOnly(s);
                                    break;
                                case EMAIL:
                                    result = Validator.testEmail(s);
                                    break;
                                default:
                                    result = true;
                            }
                        }
                        if (!result) showHint(hint);

                        return result;
                    });

        return Observable.combineLatest(
                Stream.of(password.iterator()).map(RegFieldView::changeText).collect(Collectors.toList()),
                args -> {
                    ArrayList<String> list = new ArrayList<>();
                    for (Object o : args) {
                        Pair<String, RegField> pair = (Pair<String, RegField>) o;
                        list.add(pair.first);
                    }
                    return list;
                }
        ).map(list -> {
            if (editText.getText().toString().length() < 5) {
                showHint("пароль меньше 5 символов");
                return false;
            }
            String first = list.get(0);
            for (String s : list)
                if (!s.equals(first)) {
                    showHint("пароли не совпадают");
                    return false;
                }

            return true;
        });

    }

    public EditText getEditText() {
        return editText;
    }
}
