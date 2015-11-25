package com.mobium.reference.utils;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;

/**
 *  on 03.11.15.
 */
public class Actions {
    public static void doOpenScanner(FragmentActivity activity) {
        doAction(activity, new Action(ActionType.DO_OPEN_SCANNER));
    }

    private static void doAction(FragmentActivity activity, Action action) {
        FragmentActionHandler.doAction(activity, action);
    }

    public static void doOpenVoiceSearch(FragmentActivity activity) {

    }

    public static void doSearch(FragmentActivity activity, String query, @Nullable String shopCategory) {
        doAction(activity, new Action(ActionType.OPEN_SEARCH, query));
    }


    public static void doUrlExternal(FragmentActivity context, String url) {
        doAction(context, new Action(ActionType.OPEN_URL_EXTERNAL, url));
    }
}
