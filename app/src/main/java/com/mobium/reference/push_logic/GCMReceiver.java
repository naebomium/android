package com.mobium.reference.push_logic;

import android.content.Context;
import com.google.android.gcm.GCMBroadcastReceiver;

/**
 *
 *
 * Date: 11.12.12
 * Time: 6:41
 */
public class GCMReceiver extends GCMBroadcastReceiver {
    @Override
    protected String getGCMIntentServiceClassName(Context context) {
        return GCMIntentService.class.getCanonicalName();
    }
}