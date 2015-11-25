package com.mobium.reference.leftmenu;

import android.graphics.drawable.Drawable;

import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;

/**
 *   04.03.15.
 * http://mobiumapps.com/
 */
public class LeftMenuButton implements LeftMenuListView.LeftMenuItem {
    private Action action;

    private int iconResurse;
    private String title;
    private String url;
    private String categoryId;

    private Drawable iconDrawable = null;

    public LeftMenuButton(Action action, String title, int iconRecurse) {
        this.action = action;
        this.iconResurse = iconRecurse;
        this.title = title;
    }

    public LeftMenuButton(Action action, String title) {
        this.title = title;
        this.action = action;
    }

    public LeftMenuButton(Action action, String title, Drawable icon) {
        this.title = title;
        this.action = action;
        this.iconDrawable = icon;
    }

    public LeftMenuButton(Action action, String title, String url) {
        this.action = action;
        this.url = url;
        this.title = title;
    }

    public LeftMenuButton(String categoryId, String title, String url) {
        this.title = title;
        this.url = url;
        this.categoryId = categoryId;
    }

    public LeftMenuButton(String categoryId, String title, int iconRecurse) {
        this.title = title;
        this.iconResurse = iconRecurse;
        this.categoryId = categoryId;
    }


    @Override
    public String getViewTag() {
        return "LEFT_MENU_ITEM";
    }

    @Override
    public boolean isEnabled() {
        return getFirstAction() != null;
    }

    @Override
    public Action getFirstAction() {
        if (categoryId == null)
            return action;
        else return new Action(ActionType.OPEN_CATALOG_INSIDE_MENU, categoryId);
    }

    @Override
    public Action getSecondAction() {
        if (categoryId == null)
            return null;
        else return new Action(ActionType.OPEN_CATEGORY, categoryId);
    }

    @Override
    public Drawable getDrawable() {
        return iconDrawable;
    }

    public int getIconResurse() {
        return iconResurse;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
