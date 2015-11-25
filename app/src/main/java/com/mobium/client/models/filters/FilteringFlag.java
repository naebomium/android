package com.mobium.client.models.filters;

import android.support.annotation.NonNull;
import com.annimon.stream.Stream;
import com.mobium.client.models.ShopItem;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 *  IDEA.
 *
 * Date: 18.02.12
 * Time: 13:36
 * To change this template use File | Settings | File Templates.
 */
public class FilteringFlag extends Filtering {

    private LinkedHashMap<String, String> flags;


    public FilteringFlag(String id, String title, LinkedHashMap<String, String> flags) {
        super(id, title, Type.flags);
        this.flags = flags;
    }

    public FilteringFlag(String id, String title, LinkedHashMap<String, String> flags, boolean isCollapsed) {
        super(id, title, Type.flags, isCollapsed);
        this.flags = flags;
    }

    public String[] getKeys() {
        Set<String> var = flags.keySet();
        return var.toArray(new String[var.size()]);
    }

    public String getTitle(String id) {
        if (flags.containsKey(id)) {
            return flags.get(id);
        } else {
            return null;
        }
    }

    public static class FilterFlagState extends FilterState {

        //id - title
        private LinkedHashMap<String, String> flags;

        //id - select
        private HashMap<String, Boolean> selectedFlags;

        public FilterFlagState(FilteringFlag filteringFlag) {
            this.flags = filteringFlag.flags;
            selectedFlags = new HashMap<>(flags.size());
            for (String s: flags.keySet()) {
                selectedFlags.put(s, false);
            }
        }

        public LinkedHashMap<String, String> getFlags() {
            return flags;
        }

        public HashMap<String, Boolean> getSelectedFlags() {
            return selectedFlags;
        }


        @Override
        public Boolean filterOut(@NonNull ShopItem item) {
            for (Map.Entry<String, Boolean> flag : selectedFlags.entrySet())
                if (flag.getValue())
                    if (item.filtering.containsKey(flag.getKey()))
                        return false;
            return true;
        }

        @Override
        public Boolean isCustomState() {
            for (Boolean e : selectedFlags.values())
                if (e)
                    return true;
            return false;
        }

        @Override
        public void clear() {
            Stream.of(selectedFlags.entrySet())
                    .forEach(stringBooleanEntry -> stringBooleanEntry.setValue(false));
            notyfiListers();
        }
    }


    @Override
    public FilterState createState() {
        return new FilterFlagState(this);
    }
}
