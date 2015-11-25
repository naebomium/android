package com.mobium.reference.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.reference.R;
import com.mobium.reference.activity.MainDashboardActivity;
import com.mobium.reference.utils.FragmentActionHandler;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *   04.03.15.
 * http://mobiumapps.com/
 */

public class AdvancedSearchView extends LinearLayout {

    @Bind(R.id.scanImage)
    ImageView scanIcon;
    @Bind(R.id.microphoneIcon)
    ImageView microphoneIcon;
    @Bind(R.id.searchField)
    EditText viewSearchField;

    public AdvancedSearchView(Context context) {
        super(context, null);
        init(context);
    }

    public AdvancedSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private MainDashboardActivity getActivity() {
        if (getContext() instanceof MainDashboardActivity)
            return (MainDashboardActivity) getContext();
        return null;
    }

    private void init(Context context) {
        View view = inflate(getContext(), R.layout.search_view_advanced, this);
        ButterKnife.bind(this, view);

        if (getActivity() != null) {
            scanIcon.setOnClickListener(v -> getActivity().doOpenScanner(null));
            microphoneIcon.setOnClickListener(v -> getActivity().doVoiceSearch());

            viewSearchField.setOnEditorActionListener((textView, actionId, keyEvent) -> {
                boolean result = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                    String query = viewSearchField.getText().toString();
                    FragmentActionHandler.doAction(getActivity(), new Action(ActionType.OPEN_SEARCH, query));
                    result = true;
                }
                viewSearchField.getText().clear();
                return result;
            });
        }
        microphoneIcon.setVisibility(GONE);
    }

    public ImageView getScanIcon() {
        return scanIcon;
    }

    public ImageView getMicrophoneIcon() {
        return microphoneIcon;
    }

    public EditText getViewSearchField() {
        return viewSearchField;
    }
}
   
