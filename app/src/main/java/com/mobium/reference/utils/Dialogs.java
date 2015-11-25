package com.mobium.reference.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.config.common.Config;
import com.mobium.config.prototype.ILeftMenu;
import com.mobium.reference.R;

import java.util.List;

/**
 *  on 11.07.15.
 * http://mobiumapps.com/
 */
public class Dialogs {
    public static void showExitScreenDialog(Context context, Runnable exit) {
        showExitScreenDialog(context, exit, "Данная функциональность недоступна в данный момент");
    }

    public static void showExitScreenDialog(Context context, Runnable exit, String text) {
        new AlertDialog.Builder(context)
                .setTitle("Ошибка сервера")
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton("ok", (dialog, which) -> {
                    exit.run();
                })
                .create().show();
    }

    public static void showExitScreenDialog(Context context, Runnable exit, String title, String text) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(text)
                .setCancelable(false)
                .setOnDismissListener(dialogInterface ->
                                exit.run()
                ).create().show();
    }

    public static <T> void showSelectDialog(
            @NonNull Context context,
            @NonNull String title,
            @NonNull List<T> list,
            @NonNull Function<T, String> map,
            @NonNull Functional.ChangeListener<T> listener) {
        List<String> titles = Stream.of(list).map(map).collect(Collectors.toList());
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setItems(titles.toArray(new CharSequence[titles.size()]), (dialogInterface, i) -> {
                    listener.onChange(list.get(i));
                }).show();
    }

    public static void showNetworkErrorDialog(@NonNull Context context,
                                              @Nullable String text,
                                              Runnable onRepeat,
                                              Runnable onExit) {
        new AlertDialog.Builder(context)
                .setTitle("Ошибка")
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton("Повторить", (dialogInterface1, i) -> onRepeat.run())
                .setNegativeButton("Отмена", (d, i) -> onExit.run())
                .show();
    }

    public static void alertNotHaveInternet(@NonNull Context context) {
        new android.support.v7.app.AlertDialog.Builder(context)
                .setMessage(context.getString(R.string.not_have_internet_access))
                .setPositiveButton("Ок", (dialogInterface, i) -> {
                })
                .show();
    }

    public static void doCallWithDialog(final Context context, final String phoneNumber) {
        new android.support.v7.app.AlertDialog.Builder(context)
                .setMessage("Позвонить на номер?")
                .setCancelable(true)
                .setPositiveButton("Позвонить", (dialogInterface, i) -> {
                    FragmentActionHandler.doAction((FragmentActivity) context, new Action(ActionType.DO_CALL, phoneNumber));
                    dialogInterface.dismiss();
                }).setNegativeButton(android.R.string.no, (dialogInterface, i) -> {
            dialogInterface.cancel();
        }).show();
    }

    public static void doCallWithDialog(final Context context, final String phoneNumber, final String info) {
        new android.support.v7.app.AlertDialog.Builder(context)
                .setMessage("Позвонить на номер" + "\n" + info + "?")
                .setCancelable(true)
                .setPositiveButton("Позвонить", (dialogInterface, i) -> {
                    FragmentActionHandler.doAction((FragmentActivity) context, new Action(ActionType.DO_CALL, phoneNumber));
                    dialogInterface.dismiss();
                }).setNegativeButton(android.R.string.no, (dialogInterface, i) -> {
            dialogInterface.cancel();
        }).show();
    }

    public static void showError(Context context, String title, String message, String applyTitle, DialogInterface.OnClickListener apply, String cancelTitle, DialogInterface.OnClickListener cancel) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(applyTitle, apply)
                .setNegativeButton(cancelTitle, cancel)
                .show();
    }

    public static void showOnlyRepeatDialog(Context context, String message, Runnable onApply) {
        new AlertDialog.Builder(context)
                .setTitle("Критическая ошибка")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("повторить", (d, s) -> onApply.run())
                .show();
    }

    public static void showChangeRegionDialogInLeftMenu(Context context, Runnable runnable) {
        ILeftMenu iLeftMenu = Config.get().design().getLeftMenu();
        String title = iLeftMenu.getRegionDialogTitle(context);
        String message = iLeftMenu.getRegionDialogMessage(context);
        String ok = iLeftMenu.getRegionDialogOk(context);
        String cancel = iLeftMenu.getRegionDialogCancel(context);

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(ok, (dialog, which) -> runnable.run())
                .setNegativeButton(cancel, null)
                .show();
    }

}
