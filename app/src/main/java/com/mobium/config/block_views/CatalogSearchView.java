package com.mobium.config.block_views;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobium.config.Util;
import com.mobium.config.block_models.CatalogSearchModel;
import com.mobium.config.common.CanSearch;
import com.mobium.config.common.Handler;
import com.mobium.reference.R;

/**
 *  on 30.10.15.
 */
public class CatalogSearchView extends BaseView<CatalogSearchModel> implements CanSearch {
    private View.OnClickListener onScanClick;
    private View.OnClickListener onVoiceClick;
    private Handler<String> queryHandler;
    public static final int backgroundColorDefault = 0x333333;
    public static final int TextAreaBackgroundDefauld = Color.WHITE;
    public static final int textColorDefault = Color.BLACK;

    public CatalogSearchView(CatalogSearchModel model) {
        super(model);
    }

    @Override
    protected View buildView(Context context, @NonNull ViewGroup viewGroup, boolean add) {
        LinearLayout layout = new LinearLayout(context);

        layout.setLayoutParams(getDefaultParams());
        layout.getLayoutParams().height = (int) context.getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        AppCompatEditText editText = new AppCompatEditText(context);

        editText.setHint(model.getHint());
        editText.setSingleLine(true);
        editText.setBackgroundResource(0);
        editText.setPadding(convertDpToPx(context, 4), 0, 0, 0);
        editText.setLayoutParams(new LinearLayout.LayoutParams(getDefaultParams()) {
            {
                width = 0;
                weight = 1;
                height = convertDpToPx(context, 28);
                gravity = Gravity.CENTER;
            }
        });

        editText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            boolean result = false;
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                String query = editText.getText().toString();
                if (queryHandler != null)
                    queryHandler.onData(query);
                editText.getText().clear();
                result = true;
            }
            return result;
        });

        layout.addView(editText);
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        layout.setBackgroundColor(Util.colorFromString(model.getBackgroundColor(), backgroundColorDefault));
        editText.setBackgroundColor(Util.colorFromString(model.getTextAreaBackgroundColor(), TextAreaBackgroundDefauld));
        editText.setTextColor(Util.colorFromString(model.getTextColor(), textColorDefault));
        editText.setHintTextColor(model.getHintColor(textColorDefault));



        if (model.isBarcodeEnabled()) {
            ImageView scanView = getImageView(context);
            scanView.setOnClickListener(v -> {
                if (onScanClick != null)
                    onScanClick.onClick(v);
            });
            scanView.setImageDrawable(Util.drawableByName(model.getScanIcon(), context, 0));
            layout.addView(scanView);
        }

        if (model.isVoiceSearchEnabled()) {
            ImageView voiceSearch = getImageView(context);
            voiceSearch.setOnClickListener(v -> {
                if (onVoiceClick != null)
                    onVoiceClick.onClick(v);
            });
            voiceSearch.setImageDrawable(Util.drawableByName(model.getVoiceIcon(), context, 0));
            layout.addView(voiceSearch);
        }

        if (add)
            viewGroup.addView(layout);

        return layout;
    }


    public ImageView getImageView(Context context) {
        return new ImageView(context) {
            {
                setScaleType(ScaleType.FIT_XY);
                setLayoutParams(new LinearLayout.LayoutParams(0, 0) {
                    {
                        gravity = Gravity.CENTER_VERTICAL;
                        height = WRAP_CONTENT;
                        width = WRAP_CONTENT;
                        leftMargin = convertDpToPx(context, 16);
                    }
                });
            }
        };
    }

    @Override
    public void setSearchHandler(Handler<String> queryHandler) {
        this.queryHandler = queryHandler;
    }

    @Override
    public void setOnVoiceSearchListneter(View.OnClickListener listener) {
        onVoiceClick = listener;
    }

    @Override
    public void setOnScannerClickListener(View.OnClickListener listener) {
        onScanClick = listener;
    }

    public View.OnClickListener getOnScanClick() {
        return onScanClick;
    }

    public View.OnClickListener getOnVoiceClick() {
        return onVoiceClick;
    }

    public Handler<String> getQueryHandler() {
        return queryHandler;
    }
}
