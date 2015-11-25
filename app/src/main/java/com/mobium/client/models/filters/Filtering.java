package com.mobium.client.models.filters;

import java.io.Serializable;

/**
 *  IDEA.
 *
 * Date: 04.12.11
 * Time: 23:57
 * To change this template use File | Settings | File Templates.
 */
public abstract class Filtering implements Serializable{
    public enum Type {
        flags, range, single
    }

    private String id;
    private String title;
    private Type type;
    private boolean isCollapsed = false;

    public boolean isCollapsed() {
        return isCollapsed;
    }

    public void setCollapsed(boolean collapsed) {
        isCollapsed = collapsed;
    }

    public Filtering(String id, String title, Type type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }

    public Filtering(String id, String title, Type type, boolean isCollapsed) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.isCollapsed = isCollapsed;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }


    public Type getType() {
        return type;
    }


    public abstract FilterState createState();
}
