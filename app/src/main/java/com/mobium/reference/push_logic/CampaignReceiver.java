package com.mobium.reference.push_logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import com.mobium.reference.ReferenceApplication;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 *
 *
 * Date: 22.11.13
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */
public class CampaignReceiver extends BroadcastReceiver {
    static final String TAG = CampaignReceiver.class.getSimpleName();
    public static final String REFERRER = "referrer";

    @Override
    public void onReceive(Context context, Intent intent) {

        String rawReferrer = intent.getStringExtra("referrer");

        Log.d(TAG, "received broadcast");

        if (rawReferrer != null) {
            Log.d(TAG, "raw: " + rawReferrer);

            String referrer = "";

            try {
                referrer = URLDecoder.decode(rawReferrer, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace(); // This should not happen.
            }

            Log.d(TAG, "decoded: " + referrer);

            SharedPreferences.Editor prefs = ReferenceApplication.getPrefs().edit();
            prefs.putString(REFERRER, referrer).apply();
        } else {
            Log.e(TAG, "referrer is null");
        }
    }
}
