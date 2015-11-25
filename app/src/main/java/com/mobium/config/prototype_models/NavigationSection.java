package com.mobium.config.prototype_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 *  on 16.11.15.
 */

public class NavigationSection {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("cells")
    @Expose
    private List<Cell> cells = new ArrayList<>();

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The cells
     */
    public List<Cell> getCells() {
        return cells;
    }

    /**
     *
     * @param cells
     * The cells
     */
    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

}
