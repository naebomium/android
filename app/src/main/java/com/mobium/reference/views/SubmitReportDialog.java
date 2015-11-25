package com.mobium.reference.views;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobium.new_api.Api;
import com.mobium.reference.R;

/**
 *  on 06.10.2015.
 */
public class SubmitReportDialog extends AlertDialog {

    EditText emailEditText;
    TextView emailError;

    EditText messageEditText;
    TextView messageError;
    Button sendButton;

    public SubmitReportDialog(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View rootView = inflater.inflate(R.layout.submit_report_dialog, null);
        emailEditText = (EditText) rootView.findViewById(R.id.report_email);
        emailError = (TextView) rootView.findViewById(R.id.email_error);
        messageEditText = (EditText) rootView.findViewById(R.id.report_message);
        messageError = (TextView) rootView.findViewById(R.id.message_error);
        sendButton = (Button) rootView.findViewById(R.id.report_submit);
        setView(rootView);
        sendButton.setOnClickListener(view -> {
            if(checkField(messageEditText, messageError) && checkField(emailEditText, emailError)) {
                Api.getInstance().SendMessage(messageEditText.getText().toString(), messageEditText.getText().toString());
                dismiss();
            }
        });
    }

    private boolean checkField(EditText field, TextView messageError) {
        if(field.getText().toString().isEmpty()) {
            messageError.setText("Поле не может быть пустым");
            return false;
        }
        return true;
    }
}
