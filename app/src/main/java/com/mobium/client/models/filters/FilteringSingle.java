package com.mobium.client.models.filters;

import android.support.annotation.NonNull;
import com.mobium.client.models.ShopItem;

/**
 *
 *
 * Date: 04.10.13
 * Time: 14:50
 * To change this template use File | Settings | File Templates.
 */
public class FilteringSingle extends Filtering {

    public FilteringSingle(String id, String title) {
        super(id, title, Type.single);
    }

    @Override
    public FilterState createState() {
        return new SingleState(getId());
    }


    public static class SingleState extends FilterState {
        public boolean selected;
        private final String key;

        public SingleState(String key) {
            this.key = key;
        }


        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            notyfiListers();
        }

        @Override
        public Boolean filterOut(@NonNull ShopItem item) {
            return selected && !(item.filtering.containsKey(key));
        }

        @Override
        public Boolean isCustomState() {
            return selected;
        }

        @Override
        public void clear() {
            selected = false;
            notyfiListers();
        }
    }

}
