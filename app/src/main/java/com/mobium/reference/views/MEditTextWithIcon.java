package com.mobium.reference.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.annimon.stream.function.Predicate;
import com.mobium.reference.R;
import com.mobium.reference.utils.ImageUtils;


/**
 *  on 16.05.15.
 * http://mobiumapps.com/
 */
public class MEditTextWithIcon extends FrameLayout {
    private EditText input;
    private ImageView icon;
    private Predicate<EditText> corrector;
    private String errorText = "Проверьте поле";
    private boolean required;


    public MEditTextWithIcon(Context context, String hint, Predicate<EditText> corrector, int resId) {
        super(context);
        setUpUi(context, null);
        icon.setImageResource(resId);
        input.setHint(hint);
        this.corrector = corrector;
    }


    public MEditTextWithIcon(Context context) {
        super(context);
        setUpUi(context, null);
    }

    public MEditTextWithIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpUi(context, attrs);
    }

    public MEditTextWithIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpUi(context, attrs);
    }

    public String getValue() {
        return input.getText().toString();
    }


    private void setUpUi(Context context, @Nullable AttributeSet attrs) {
        View root = LayoutInflater.from(context).inflate(R.layout.view_icon_edit_text, this);
        input = (EditText) root.findViewById(R.id.view_icon_edit_text_input);
        icon = (ImageView) root.findViewById(R.id.view_icon_edit_text_icon);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ViewEditTextWithIcon);

        if (!isInEditMode()) {
            for (int i = 0; i < a.getIndexCount(); i++)
                setAtr(a, a.getIndex(i));

            a.recycle();
        }
    }

    private void setAtr(TypedArray a, int atrId) {
        switch (atrId) {
            case R.styleable.ViewEditTextWithIcon_hint:
                input.setHint(a.getString(atrId));
                break;
            case R.styleable.ViewEditTextWithIcon_image:
                icon.setImageResource(a.getResourceId(atrId, 0));
                break;
            case R.styleable.ViewEditTextWithIcon_image_leftMargin:
                FrameLayout.LayoutParams params = ((FrameLayout.LayoutParams) icon.getLayoutParams());
                float marginLeft = a.getDimension(atrId, ImageUtils.convertToPx(getContext(), 16));
                params.leftMargin = (int) marginLeft;
                break;
        }

    }


    public EditText getInput() {
        return input;
    }

    public ImageView getIcon() {
        return icon;
    }

    public void setInputType(int inputType) {
        input.setInputType(inputType);
    }

    public void setCorrector(Predicate<EditText> corrector, String errorText) {
        this.corrector = corrector;
        this.errorText = errorText;
    }


    public boolean isCorrect() {
        boolean empty = input.getText().length() == 0;
        if (required && empty) {
            input.setError("Обязательное поле");
            return false;
        }

        if (corrector != null) {
            if (corrector.test(input))
                return true;
            else {
                input.setError(errorText);
                return false;
            }
        }

        return true;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
