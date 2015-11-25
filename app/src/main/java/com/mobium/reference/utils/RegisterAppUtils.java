package com.mobium.reference.utils;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

import com.mobium.new_api.Api;
import com.mobium.new_api.Handler;
import com.mobium.new_api.Method;
import com.mobium.new_api.methodParameters.RegisterAppParam;
import com.mobium.new_api.models.MobiumError;

import java.io.StringWriter;

/**
 *  on 23.09.15.
 */
public class RegisterAppUtils {
    public static void registerAppAcync(Activity activity, AppIdReceiver appIdReceiver, Method<RegisterAppParam, Api.StringWrapper> registerAppMethod) {
        registerAppMethod.invoke(new Handler<RegisterAppParam, Api.StringWrapper>() {
            @Override
            public void onResult(Api.StringWrapper s) {
                appIdReceiver.onGetAppId(s.getValue());
            }

            @Override
            public void onBadArgument(MobiumError mobiumError, RegisterAppParam registerAppParam) {
               showError(mobiumError == null ? null : mobiumError.description);
            }

            @Override
            public void onException(Exception ex) {
                showError(ex.getMessage());
            }

            private void showError(String message) {
                new AlertDialog.Builder(activity)
                        .setTitle("Ошибка во время регистрации приложения")
                        .setMessage(message + "\n Некоторый функционал не будет работать")
                        .setCancelable(false)
                        .setPositiveButton("Повторить", (dialog, which) -> {
                            registerAppAcync(activity, appIdReceiver, registerAppMethod);
                        }).show();
            }
        });
    }


    public interface AppIdReceiver {
        void onGetAppId(String appId);
    }

}
