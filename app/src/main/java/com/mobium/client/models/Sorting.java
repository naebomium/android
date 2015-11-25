package com.mobium.client.models;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 *  IDEA.
 *
 * Date: 04.12.11
 * Time: 23:56
 */

public class Sorting implements Serializable {

    public enum Type {
        STRING, NUMBER, DEFAULT
    }

    private String id;
    private String title;
    private Type type;

    public Sorting(String id, String title, Type type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }

    public Sorting() {
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

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(Type type) {
        this.type = type;
    }
}