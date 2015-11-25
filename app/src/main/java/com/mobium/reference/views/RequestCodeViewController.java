package com.mobium.reference.views;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mobium.reference.R;
import com.mobium.userProfile.CallBack.ProfileCallBack;
import com.mobium.userProfile.ProfileApi;
import com.mobium.userProfile.Response;

import java.util.Timer;
import java.util.TimerTask;

import retrofit.RetrofitError;

/**
 *  on 13.07.15.
 * http://mobiumapps.com/
 */

public class RequestCodeViewController {

    private static final int REQUEST_CODE_TICK = 1;

    //задержка отправки в секундах
    private static int REQUEST_CODE_TIMEOUT = 60;

    //здесь лежит обратный отсчет
    private static int requestCodeCount = REQUEST_CODE_TIMEOUT;

    private static final String TIMEOUT_MESSAGE = "Повторная отправка кода возможна через %d сек";

    private boolean isCanceled = false;

    public final MEditTextWithIcon input;
    public final Button sendCode;
    public final Button requestCode;
    public final View view;
    private TextView requestTimeoutMessageText;

    Timer requsetTimeoutTimer;
    TimerTask requestTimeoutTask;

    final Handler handler;
    public RequestCodeViewController(Context c) {
        view = View.inflate(c, R.layout.view_input_profile_code, null);
        sendCode = (Button) view.findViewById(R.id.profile_code_send_code_button);
        requestCode = (Button) view.findViewById(R.id.profile_code_request_code_button);
        input = (MEditTextWithIcon) view.findViewById(R.id.profile_code_input);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setRequired(true);
        input.getInput().setTextColor(c.getResources().getColor(R.color.white_background));
        requestTimeoutMessageText = (TextView)view.findViewById(R.id.code_request_message);
        initTimers();
        handler = new Handler(c.getMainLooper()) {
            @Override
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case REQUEST_CODE_TICK: {
                        requestCodeCount--;
                        if (requestTimeoutMessageText != null) {
                            if(requestCodeCount == 0) {
                                requestTimeoutMessageText.setVisibility(View.GONE);
                                requestTimeoutMessageText.setText("");
                                isCanceled = true;
                                requestCode.setEnabled(true);
                                dismiss();
                            } else {
                                requestTimeoutMessageText.setText(String.format(TIMEOUT_MESSAGE, requestCodeCount));
                            }
                        }
                    }
                }
            }
        };
    }

    private void initTimers () {
        requsetTimeoutTimer = new Timer();
        requestTimeoutTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(REQUEST_CODE_TICK);
            }
        };
    }

    public rx.Observable<String> codeOnClick() {
        return rx.Observable.create(subscriber ->
                        sendCode.setOnClickListener(view1 -> {
                            if (input.isCorrect())
                                try {
                                    subscriber.onNext(input.getValue());
                                } catch (Exception e) {
                                    subscriber.onError(e);
                                }
                        })
        );
    }

    private boolean isActive() {
        return sendCode != null && sendCode.getContext() != null;
    }

    public void initCodeButton(final String confirmToken) {
        requestCode.setEnabled(true);
        requestCode.setOnClickListener(v -> requestCode.postDelayed(() -> {
            requetCode(confirmToken);
            requestCode.setEnabled(false);
            if (isCanceled)
                initTimers();
            isCanceled = false;
            requsetTimeoutTimer.scheduleAtFixedRate(requestTimeoutTask, 0, 1000);
            requestTimeoutMessageText.setText("");
            requestTimeoutMessageText.setVisibility(View.VISIBLE);
        }, 50));
    }

    private void requetCode(final String confirmToken) {
        if (requestCode != null && requestCode.getContext() != null) {

            ProfileCallBack<Object> callBack = new ProfileCallBack<Object>() {
                @Override
                public void onSuccess(Object data) {
                    if (!isActive())
                        return;
                    new Handler().postDelayed(() -> {
                        requestCode.setVisibility(View.VISIBLE);
                    }, 1000 * 60);
                    Toast.makeText(requestCode.getContext(), "Код отправлен", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAbort(Response.ResponseStatus type, @Nullable Object data) {
                    if (!isActive())
                        return;
                    requestCode.setVisibility(View.VISIBLE);
                    Toast.makeText(requestCode.getContext(), "Ошибка при отправке кода", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(RetrofitError error) {
                    if (!isActive())
                        return;
                    requestCode.setVisibility(View.VISIBLE);
                    Toast.makeText(requestCode.getContext(), "Ошибка при отправке кода", Toast.LENGTH_SHORT).show();
                }
            };
            ProfileApi.getInstance().resendCode(confirmToken, callBack);
        }
    }

    public void dismiss() {
        try {
            requestCodeCount = REQUEST_CODE_TIMEOUT;
            requsetTimeoutTimer.cancel();
            requestTimeoutTask.cancel();
            isCanceled = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
