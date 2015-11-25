package com.mobium.reference.views;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mobium.reference.R;

/**
 *  on 05.10.2015.
 */
public class RateAppDialog extends AlertDialog {
    private RatingBar ratingBar;
    private TextView title;
    private TextView positiveText;
    private TextView negativeText;

    private IRateAppDialog iRateAppDialog;

    protected RateAppDialog(Context context) {
        super(context);
    }

    protected RateAppDialog(Context context, IRateAppDialog iRateAppDialog) {
        super(context);
        this.iRateAppDialog = iRateAppDialog;
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.rating_dialog_layout, null);
        ratingBar = (RatingBar) rootView.findViewById(R.id.rating_dialog_ratingbar);
        title = (TextView) rootView.findViewById(R.id.title);
        negativeText = (TextView) rootView.findViewById(R.id.rating_dialog_negative_button);
        positiveText = (TextView) rootView.findViewById(R.id.rating_dialog_positive_button);
        setView(rootView);
        positiveText.setOnClickListener(view -> {
            if (iRateAppDialog != null) {
                iRateAppDialog.onVoting((int) ratingBar.getRating());
            }
            dismiss();
        });

        negativeText.setOnClickListener(view -> {
            if (iRateAppDialog != null) {
                iRateAppDialog.onShowLaterClick();
                dismiss();
            }
        });
    }

    public static RateAppDialog getDialog(Context context, IRateAppDialog dialog) {
        return new RateAppDialog(context, dialog);
    }

    public interface IRateAppDialog {
        void onVoting(int starCount);
        void onShowLaterClick();
    }
}
