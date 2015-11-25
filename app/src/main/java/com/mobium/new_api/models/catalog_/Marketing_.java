package com.mobium.new_api.models.catalog_;

import android.support.annotation.Nullable;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *  on 14.07.15.
 * http://mobiumapps.com/
 */

public class Marketing_ implements Serializable {
    private String title;
    private String description;
    private Type type;
    private String icon;
    private Data data;

    //todo parser


    public enum Type implements Serializable {
        lower_price, free_delivery, nothing
    }


    public static class Data {
        @Nullable private ArrayList<String> types;
        @Nullable private Integer lower_to;
        @Nullable private Integer lower_by;

        public Data() {
        }

        public void setTypes(@Nullable ArrayList<String> types) {
            this.types = types;
        }

        public void setLower_to(@Nullable Integer lower_to) {
            this.lower_to = lower_to;
        }

        public void setLower_by(@Nullable Integer lower_by) {
            this.lower_by = lower_by;
        }

        @Nullable
        public ArrayList<String> getTypes() {
            return types;
        }

        @Nullable
        public Integer getLower_to() {
            return lower_to;
        }

        @Nullable
        public Integer getLower_by() {
            return lower_by;
        }
    }

    public Marketing_() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
