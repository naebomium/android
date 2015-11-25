package com.mobium.reference.push_logic;

import android.content.Context;
import android.content.Intent;
import com.google.android.gcm.GCMBaseIntentService;
import com.mobium.reference.ReferenceApplication;
import com.mobium.config.common.Config;

/**
 *
 *
 * Date: 11.12.12
 * Time: 6:19
 */
public class GCMIntentService extends GCMBaseIntentService {
    @Override
    protected void onMessage(Context context, Intent intent) {
        PushUtils.receivePush(getApplication(), intent);
    }

    @Override
    protected void onError(Context context, String s) {

    }

    @Override
    protected void onRegistered(Context context, String s) {
        ((ReferenceApplication) getApplication()).onGcmToken(s);
    }

    @Override
    protected void onUnregistered(Context context, String s) {

    }

    @Override
    protected String[] getSenderIds(Context context) {
        return Config.get().getApplicationData().getSenderId();
    }
}
