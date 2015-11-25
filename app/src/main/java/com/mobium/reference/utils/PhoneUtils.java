package com.mobium.reference.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;

import com.mobium.reference.utils.statistics_new.Events;

/**
 *  on 01.08.15.
 * http://mobiumapps.com/
 */
public class PhoneUtils {
    public static void vibrate(final Context context, long time) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        try {
            vibrator.vibrate(time);
        } catch (NoSuchMethodError error) {
            error.printStackTrace();
        }
    }
    public static void doCall(final Context context, String number) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + number));
            context.startActivity(callIntent);
        } catch (ActivityNotFoundException activityException) {
            activityException.printStackTrace();
        }
    }
}
