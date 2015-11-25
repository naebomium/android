package com.mobium.client.models;

import com.annimon.stream.Optional;
import com.mobium.client.models.filters.FilterState;
import com.mobium.client.models.filters.Filtering;
import com.mobium.client.models.resources.Graphics;
import com.mobium.reference.model.SortingDescriptor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *  IDEA.
 *
 * Date: 23.09.11
 * Time: 0:11
 * To change this template use File | Settings | File Templates.
 */
public class ShopCategory extends Extralable<ShopCategory> {
    public String title;
    public Graphics icon;
    public String id;
    public int totalCount;

    public ArrayList<Sorting> sortings = new ArrayList<>();
    public ArrayList<Filtering> filterings = new ArrayList<>();

    public transient HashMap<Filtering, FilterState> filterMap;
    public transient SortingDescriptor selectedSorting;


    public ArrayList<ShopCategory> childs = new ArrayList<>();

    public ShopCategory(String id, String title, Graphics icon) {
        this.title = title;
        this.id = id;
        this.icon = icon;
    }

    public Optional<Graphics> getIcon(){
        return Optional.ofNullable(icon);
    }
}
