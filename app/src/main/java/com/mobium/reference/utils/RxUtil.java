package com.mobium.reference.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import rx.Observable;

/**
 *  on 14.10.15.
 */
public class RxUtil {
    static private Handler handler = new Handler(Looper.getMainLooper());

    public static class EditTextEvent {
        public final EditText editText;
        public final String text;

        public EditTextEvent(EditText editText, String text) {
            this.editText = editText;
            this.text = text;
        }
    }

    public static Observable<EditTextEvent> completeInput(EditText v) {
        return Observable.create(subscriber -> {
            if (v.getText() != null && v.getText().length() > 0) {
                handler.postDelayed(() -> subscriber.onNext(new EditTextEvent(v, v.getText().toString())), 400);
            }
            v.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    subscriber.onNext(new EditTextEvent(v, s.toString()));
                }
            });
        });
    }
}
