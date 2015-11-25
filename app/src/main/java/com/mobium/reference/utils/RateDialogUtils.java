package com.mobium.reference.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.mobium.reference.views.RateAppDialog;
import com.mobium.reference.views.SubmitReportDialog;

/**
 *  on 05.10.2015.
 */
public class RateDialogUtils {
    private static final String TAG = "RateDialogUtils";

    private static final int APP_START_COUNT_TO_SHOW_DIALOG = 10;

    public static void updateAppStartCount(Context context) {
        RatingStorage storage = RatingStorage.getInstance(context);
        storage.updateStartCount();
    }

    public static void checkAppStartCount(Context context) {
        RatingStorage storage = RatingStorage.getInstance(context);
        if (!storage.isNeedShowVoting())
            return;
        if (storage.getStartCount() != APP_START_COUNT_TO_SHOW_DIALOG)
            return;

        RateAppDialog.getDialog(context, new RateAppDialog.IRateAppDialog() {
            @Override
            public void onVoting(int rate) {
                if (rate < 4) {
                    SubmitReportDialog dialog = new SubmitReportDialog(context);
                    dialog.show();
                } else {
                    final String appPackageName = context.getPackageName().replace(".test", "");
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
                storage.setNeedShowVoting(false);
            }

            @Override
            public void onShowLaterClick() {
                storage.setStartCount(0);
                storage.setNeedShowVoting(true);
            }
        }).show();
    }

    private static final class RatingStorage {
        private final SharedPreferences prefs;

        private static final String needShowVotingKey = RatingStorage.class.getSimpleName()+"needShowVoting";
        private static final String startCountKey = RatingStorage.class.getSimpleName()+"startCount";

        private RatingStorage(Context context) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        }

        public int getStartCount() {
            return prefs.getInt(startCountKey, 0);
        }

        public void setStartCount(int startCount) {
            prefs.edit().putInt(startCountKey, startCount).apply();
        }

        public boolean isNeedShowVoting() {
            return prefs.getBoolean(needShowVotingKey, true);
        }

        public void setNeedShowVoting(boolean needShowVoting) {
            prefs.edit().putBoolean(needShowVotingKey, needShowVoting).apply();
        }

        public void updateStartCount() {
            int startCount = getStartCount();
            if (startCount < APP_START_COUNT_TO_SHOW_DIALOG) {
                startCount += 1;
                setStartCount(startCount);
            }
        }

        private static volatile RatingStorage instance;
        public static RatingStorage getInstance(Context context) {
            RatingStorage localInstance = instance;
            if (localInstance == null) {
                synchronized (RatingStorage.class) {
                    localInstance = instance;
                    if (localInstance == null) {
                        instance = localInstance = new RatingStorage(context);
                    }
                }
            }
            return localInstance;
        }

    }

}
