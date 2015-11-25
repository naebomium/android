package com.mobium.config.prototype_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 *  on 16.11.15.
 */
public class LeftMenuModel {

    @SerializedName("menu_background_color")
    @Expose
    private String menuBackgroundColor;
    @SerializedName("menu_background_image")
    @Expose
    private String menuBackgroundImage;
    @SerializedName("menu_background_image_everywhere")
    @Expose
    private boolean menuBackgroundImageEverywhere;
    @SerializedName("menu_background_mode")
    @Expose
    private String menuBackgroundMode;
    @SerializedName("change_region_icon")
    @Expose
    private String changeRegionIcon;
    @SerializedName("change_region_title_color")
    @Expose
    private String changeRegionTitleColor;
    @SerializedName("change_region_title_text")
    @Expose
    private String changeRegionTitleText;
    @SerializedName("change_region_dialog_title")
    @Expose
    private String changeRegionDialogTitle;
    @SerializedName("change_region_dialog_message")
    @Expose
    private String changeRegionDialogMessage;
    @SerializedName("change_region_dialog_ok")
    @Expose
    private String changeRegionDialogOk;
    @SerializedName("change_region_dialog_cancel")
    @Expose
    private String changeRegionDialogCancel;
    @SerializedName("search_hint_text")
    @Expose
    private String searchHintText;
    @SerializedName("search_hint_color")
    @Expose
    private String searchHintColor;
    @SerializedName("search_text_color")
    @Expose
    private String searchTextColor;
    @SerializedName("navigation_color_cell")
    @Expose
    private String navigationColorCell;
    @SerializedName("navigation_color_section")
    @Expose
    private String navigationColorSection;
    @SerializedName("navigation_color_section_separator")
    @Expose
    private String navigationColorSectionSeparator;
    @SerializedName("navigation_sections")
    @Expose
    private List<NavigationSection> navigationSections = new ArrayList<NavigationSection>();

    /**
     *
     * @return
     * The menuBackgroundColor
     */
    public String getMenuBackgroundColor() {
        return menuBackgroundColor;
    }

    /**
     *
     * @param menuBackgroundColor
     * The menu_background_color
     */
    public void setMenuBackgroundColor(String menuBackgroundColor) {
        this.menuBackgroundColor = menuBackgroundColor;
    }

    /**
     *
     * @return
     * The menuBackgroundImage
     */
    public String getMenuBackgroundImage() {
        return menuBackgroundImage;
    }

    /**
     *
     * @param menuBackgroundImage
     * The menu_background_image
     */
    public void setMenuBackgroundImage(String menuBackgroundImage) {
        this.menuBackgroundImage = menuBackgroundImage;
    }

    /**
     *
     * @return
     * The menuBackgroundImageEverywhere
     */
    public boolean getMenuBackgroundImageEverywhere() {
        return menuBackgroundImageEverywhere;
    }

    /**
     *
     * @param menuBackgroundImageEverywhere
     * The menu_background_image_everyvere
     */
    public void setMenuBackgroundImageEverywhere(boolean menuBackgroundImageEverywhere) {
        this.menuBackgroundImageEverywhere = menuBackgroundImageEverywhere;
    }

    /**
     *
     * @return
     * The menuBackgroundMode
     */
    public String getMenuBackgroundMode() {
        return menuBackgroundMode;
    }

    /**
     *
     * @param menuBackgroundMode
     * The menu_background_mode
     */
    public void setMenuBackgroundMode(String menuBackgroundMode) {
        this.menuBackgroundMode = menuBackgroundMode;
    }

    /**
     *
     * @return
     * The changeRegionIcon
     */
    public String getChangeRegionIcon() {
        return changeRegionIcon;
    }

    /**
     *
     * @param changeRegionIcon
     * The change_region_icon
     */
    public void setChangeRegionIcon(String changeRegionIcon) {
        this.changeRegionIcon = changeRegionIcon;
    }

    /**
     *
     * @return
     * The changeRegionTitleColor
     */
    public String getChangeRegionTitleColor() {
        return changeRegionTitleColor;
    }

    /**
     *
     * @param changeRegionTitleColor
     * The change_region_title_color
     */
    public void setChangeRegionTitleColor(String changeRegionTitleColor) {
        this.changeRegionTitleColor = changeRegionTitleColor;
    }

    /**
     *
     * @return
     * The changeRegionTitleText
     */
    public String getChangeRegionTitleText() {
        return changeRegionTitleText;
    }

    /**
     *
     * @param changeRegionTitleText
     * The change_region_title_text
     */
    public void setChangeRegionTitleText(String changeRegionTitleText) {
        this.changeRegionTitleText = changeRegionTitleText;
    }

    /**
     *
     * @return
     * The changeRegionDialogTitle
     */
    public String getChangeRegionDialogTitle() {
        return changeRegionDialogTitle;
    }

    /**
     *
     * @param changeRegionDialogTitle
     * The change_region_dialog_title
     */
    public void setChangeRegionDialogTitle(String changeRegionDialogTitle) {
        this.changeRegionDialogTitle = changeRegionDialogTitle;
    }

    /**
     *
     * @return
     * The changeRegionDialogMessage
     */
    public String getChangeRegionDialogMessage() {
        return changeRegionDialogMessage;
    }

    /**
     *
     * @param changeRegionDialogMessage
     * The change_region_dialog_message
     */
    public void setChangeRegionDialogMessage(String changeRegionDialogMessage) {
        this.changeRegionDialogMessage = changeRegionDialogMessage;
    }

    /**
     *
     * @return
     * The changeRegionDialogOk
     */
    public String getChangeRegionDialogOk() {
        return changeRegionDialogOk;
    }

    /**
     *
     * @param changeRegionDialogOk
     * The change_region_dialog_ok
     */
    public void setChangeRegionDialogOk(String changeRegionDialogOk) {
        this.changeRegionDialogOk = changeRegionDialogOk;
    }

    /**
     *
     * @return
     * The changeRegionDialogCancel
     */
    public String getChangeRegionDialogCancel() {
        return changeRegionDialogCancel;
    }

    /**
     *
     * @param changeRegionDialogCancel
     * The change_region_dialog_cancel
     */
    public void setChangeRegionDialogCancel(String changeRegionDialogCancel) {
        this.changeRegionDialogCancel = changeRegionDialogCancel;
    }

    /**
     *
     * @return
     * The searchHintText
     */
    public String getSearchHintText() {
        return searchHintText;
    }

    /**
     *
     * @param searchHintText
     * The search_hint_text
     */
    public void setSearchHintText(String searchHintText) {
        this.searchHintText = searchHintText;
    }

    /**
     *
     * @return
     * The searchHintColor
     */
    public String getSearchHintColor() {
        return searchHintColor;
    }

    /**
     *
     * @param searchHintColor
     * The search_hint_color
     */
    public void setSearchHintColor(String searchHintColor) {
        this.searchHintColor = searchHintColor;
    }

    /**
     *
     * @return
     * The searchTextColor
     */
    public String getSearchTextColor() {
        return searchTextColor;
    }

    /**
     *
     * @param searchTextColor
     * The search_text_color
     */
    public void setSearchTextColor(String searchTextColor) {
        this.searchTextColor = searchTextColor;
    }

    /**
     *
     * @return
     * The navigationColorCell
     */
    public String getNavigationColorCell() {
        return navigationColorCell;
    }

    /**
     *
     * @param navigationColorCell
     * The navigation_color_cell
     */
    public void setNavigationColorCell(String navigationColorCell) {
        this.navigationColorCell = navigationColorCell;
    }

    /**
     *
     * @return
     * The navigationColorSection
     */
    public String getNavigationColorSection() {
        return navigationColorSection;
    }

    /**
     *
     * @param navigationColorSection
     * The navigation_color_section
     */
    public void setNavigationColorSection(String navigationColorSection) {
        this.navigationColorSection = navigationColorSection;
    }

    /**
     *
     * @return
     * The navigationColorSectionSeparator
     */
    public String getNavigationColorSectionSeparator() {
        return navigationColorSectionSeparator;
    }

    /**
     *
     * @param navigationColorSectionSeparator
     * The navigation_color_section_separator
     */
    public void setNavigationColorSectionSeparator(String navigationColorSectionSeparator) {
        this.navigationColorSectionSeparator = navigationColorSectionSeparator;
    }

    /**
     *
     * @return
     * The navigationSections
     */
    public List<NavigationSection> getNavigationSections() {
        return navigationSections;
    }

    /**
     *
     * @param navigationSections
     * The navigation_sections
     */
    public void setNavigationSections(List<NavigationSection> navigationSections) {
        this.navigationSections = navigationSections;
    }

}