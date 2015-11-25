package com.mobium.reference.model;

/**
 *
 *
 * Date: 20.12.12
 * Time: 18:31
 */
public class WindowConfiguration {
    private boolean showSearch;
    private boolean showBack;

    private boolean showBigSearch;
    private boolean showLogo;

    private boolean titleGravityLeft;

    public WindowConfiguration(boolean showSearch, boolean showBack, boolean showBigSearch, boolean showLogo, boolean titleGravityLeft) {
        this.showSearch = showSearch;
        this.showBack = showBack;
        this.showBigSearch = showBigSearch;
        this.showLogo = showLogo;
        this.titleGravityLeft = titleGravityLeft;
    }

    public boolean isTitleGravityLeft() {
        return titleGravityLeft;
    }

    public boolean isShowSearch() {
        return showSearch;
    }

    public boolean isShowBack() {
        return showBack;
    }

    public boolean isShowBigSearch() {
        return showBigSearch;
    }

    public boolean isShowLogo() {
        return showLogo;
    }
}
