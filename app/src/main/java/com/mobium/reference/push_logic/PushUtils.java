package com.mobium.reference.push_logic;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import com.annimon.stream.function.Consumer;
import com.google.android.gcm.GCMRegistrar;
import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.new_api.Api;
import com.mobium.reference.R;
import com.mobium.reference.ReferenceApplication;
import com.mobium.reference.activity.MainDashboardActivity;
import com.mobium.config.common.Config;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Random;

/**
 *  on 31.07.15.
 * http://mobiumapps.com/
 */
public class PushUtils {

    public static int NOTIFICATION_ID = 1;
    private static int attempt;
    private static OkHttpClient okClient;

    public static void receivePush(@NonNull Context context, @NonNull Intent intent) {
        try {
            final String taskId = intent.getStringExtra("push_task_id");
            Api.getInstance().PushReceived(taskId).invokeWithoutHandling(15, 5000);


            final String actionType = intent.getStringExtra("actionId");
            final String actionData = intent.getStringExtra("actionData");
            final String actionTitle = intent.getStringExtra("actionTitle");
        //openContentPage
            if (!ActionType.OPEN_CONTENT_PAGE.getName().equals(actionData))
                showNotification(context, intent, new Action(actionType, actionData, actionTitle));
            else
                loadContentPageAndShowIt(context, intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadContentPageAndShowIt(Context context, Intent intent) {
        attempt++;
        if (okClient == null)
            okClient = ReferenceApplication.getInstance().okHttpClient;

        final String actionType = intent.getStringExtra("actionId");
        final String actionData = intent.getStringExtra("actionData");
        final String actionTitle = intent.getStringExtra("actionTitle");

        final Request request = new Request.Builder()
                .url(actionData)
                .get()
                .addHeader("Accept-Encoding", "utf-8")
                .build();

        okClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    if (attempt < 20) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        loadContentPageAndShowIt(context, intent);
                    }
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    showNotification(context, intent, new Action(ActionType.OPEN_LOADED_CONTENT_PAGE, response.body().string(), actionTitle));
                    attempt = 0;
                }
            });
    }

    public static void sendTestPush(Context context, Action action) {
        Intent intent = new Intent();
        intent.putExtra("contentText", "test");
        intent.putExtra("contentTitle", "test");
        intent.putExtra("contentTicker", "test");

        showNotification(context, intent, action);
    }

    public static void sendTestPushContent(Context context, Action action) {
        Intent intent = new Intent();
        intent.putExtra("actionData", action.getActionData());
        intent.putExtra("actionTitle", action.getActionTitle());
        loadContentPageAndShowIt(context, intent);
    }

    public static void showNotification(Context context, Intent intent, Action action) {
        final String taskId = intent.getStringExtra("push_task_id");
        final String contentText   = intent.getStringExtra("contentText");
        final String contentTitle  = intent.getStringExtra("contentTitle");
        final String contentTicker = intent.getStringExtra("contentTicker");

        PendingIntent contentIntent = PendingIntent.getActivity(context,
                new Random().nextInt(),
                new Intent()
                        .setClass(context, MainDashboardActivity.class)
                        .setAction(MainDashboardActivity.ACTION_CUSTOM_ACTION)
                        .putExtra("push_task_id", taskId)
                        .putExtra("action", action)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentText(contentText)
                .setContentTitle(contentTitle)
                .setTicker(contentTicker)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(contentIntent)
                .setWhen(System.currentTimeMillis());

        Notification notification = builder.build();


                ((NotificationManager)
                        context.getSystemService(Application.NOTIFICATION_SERVICE))
                .notify(NOTIFICATION_ID, notification);
    }

    private static int getNotificationIcon() {
        return 0;
    }

    public static void registerForPushAsync(Context context, Consumer<Boolean> token) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    GCMRegistrar.checkDevice(context);
                    GCMRegistrar.checkManifest(context);
                    final String regId = GCMRegistrar.getRegistrationId(context);
                    if (regId.equals("")) {
                        GCMRegistrar.register(context, Config.get().getApplicationData().getSenderId());
                    } else {
                        ReferenceApplication.getInstance().onGcmToken(regId);
                    }
                    token.accept(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    token.accept(false);
                }
                return null;
            }
        }.execute(new Object());
    }
}
