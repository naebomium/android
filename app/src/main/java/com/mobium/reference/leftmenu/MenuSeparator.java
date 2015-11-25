package com.mobium.reference.leftmenu;

import android.graphics.drawable.Drawable;

import com.mobium.client.models.Action;

/**
 *   05.03.15.
 * http://mobiumapps.com/
 */
public class MenuSeparator implements LeftMenuListView.LeftMenuItem {
    private String name;
    private String url;
    private int resource;

    public MenuSeparator(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public MenuSeparator(int resource, String name) {
        this.resource = resource;
        this.name = name;
    }

    public MenuSeparator(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getResource() {
        return resource;
    }

    @Override
    public String getViewTag() {
        return "MENU_SEPARATOR";
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public Action getFirstAction() {
        return null;
    }

    @Override
    public Action getSecondAction() {
        return null;
    }

    @Override
    public Drawable getDrawable() {
        return null;
    }
}
