package com.mobium.new_api.models.catalog_;

import android.support.annotation.Nullable;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *  on 15.07.15.
 * http://mobiumapps.com/
 */

public class Characteristics_ implements Serializable {
    private ArrayList<Groop_> groops;



    static class Groop_ implements Serializable {
        private String id;
        private String title;
        private ArrayList<Item_> items;

        public Groop_() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public ArrayList<Item_> getItems() {
            return items;
        }

        public void setItems(ArrayList<Item_> items) {
            this.items = items;
        }
    }

    static class Item_ implements Serializable {
        private String keyTitle;
        private String valueTitle;

        private @Nullable String id;
        private @Nullable Extra extra;

        public static class Extra {
            private String suffix;
        }


        public Item_() {
        }

        public String getKeyTitle() {
            return keyTitle;
        }

        public void setKeyTitle(String keyTitle) {
            this.keyTitle = keyTitle;
        }

        public String getValueTitle() {
            return valueTitle;
        }

        public void setValueTitle(String valueTitle) {
            this.valueTitle = valueTitle;
        }

        @Nullable
        public String getId() {
            return id;
        }

        public void setId(@Nullable String id) {
            this.id = id;
        }

        @Nullable
        public Extra getExtra() {
            return extra;
        }

        public void setExtra(@Nullable Extra extra) {
            this.extra = extra;
        }
    }

    public Characteristics_() {
    }

    public ArrayList<Groop_> getGroops() {
        return groops;
    }

    public void setGroops(ArrayList<Groop_> groops) {
        this.groops = groops;
    }
}
