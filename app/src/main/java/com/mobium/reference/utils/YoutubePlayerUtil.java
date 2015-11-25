package com.mobium.reference.utils;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.google.android.youtube.player.YouTubeInitializationResult;

/**
 *  on 12.10.15.
 */
public class YoutubePlayerUtil {

    private static void goToYouTubeApplication(FragmentActivity activity) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.google.android.youtube"));
        activity.startActivity(intent);
    }


    public static void handleError(FragmentActivity activity, YouTubeInitializationResult youTubeInitializationResult) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle("Ошибка при инициализация плеeра")
                .setNegativeButton("Пропустить", (dialog1, which1) -> {
                dialog1.dismiss();
                });

        switch (youTubeInitializationResult) {
            case SERVICE_MISSING:
                builder.setMessage("Для просмотра видео необходимо приложение youtube")
                        .setPositiveButton("Установить", (dialog, which) -> {
                            goToYouTubeApplication(activity);
                        });
                break;
            case SERVICE_VERSION_UPDATE_REQUIRED:
                builder.setMessage("Для просмотра видео необходимо обновить приложение youtube")
                        .setPositiveButton("Обновить", (dialog, which) -> {
                            goToYouTubeApplication(activity);
                        });
                break;
            default:
                builder.setMessage("Невозможно отобразить видео, ошибка " + youTubeInitializationResult);
                break;
        }
        builder.show();
    }
}
