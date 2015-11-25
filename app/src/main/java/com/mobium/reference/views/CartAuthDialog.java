package com.mobium.reference.views;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.mobium.reference.R;

/**
 *  on 08.10.2015.
 */
public class CartAuthDialog extends AlertDialog {

    private ICartAuthDialog iCartAuthDialog;

    protected CartAuthDialog(Context context, ICartAuthDialog iCartAuthDialog) {
        super(context);

        this.iCartAuthDialog = iCartAuthDialog;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View rootView = inflater.inflate(R.layout.cart_auth_dialog, null);
        View authView = rootView.findViewById(R.id.cart_dialog_login);
        View registerView = rootView.findViewById(R.id.cart_dialog_register);
        View proceedView = rootView.findViewById(R.id.cart_dialog_proceed);
        setView(rootView);

        authView.setOnClickListener(this::onAuth);
        registerView.setOnClickListener(this::onRegister);
        proceedView.setOnClickListener(this::onProceed);
    }

    private void onAuth(View v) {
        iCartAuthDialog.onAuth();
        dismiss();
    }

    private void onRegister(View v) {
        iCartAuthDialog.onRegister();
        dismiss();
    }

    private void onProceed(View v) {
        iCartAuthDialog.onProceed();
        dismiss();
    }

    public static CartAuthDialog getDialog(@NonNull Context context, @NonNull ICartAuthDialog iCartAuthDialog) {
        return new CartAuthDialog(context, iCartAuthDialog);
    }

    public interface ICartAuthDialog {
        void onAuth();
        void onRegister();
        void onProceed();
    }

}
