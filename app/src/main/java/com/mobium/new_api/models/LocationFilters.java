package com.mobium.new_api.models;


import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 *  on 28.07.15.
 * http://mobiumapps.com/
 */
public class LocationFilters  extends ResponseBase implements Serializable {
    public LocationFilter filters[];

    public LocationFilters() {
    }

    public void setFilters(LocationFilter[] filters) {
        this.filters = filters;
    }

    public static class LocationFilter {
        public String id;
        public String title;
        public Value[] values;

        @SerializedName("for")
        private String forString;

        private Optional<List<ShopPoint.ShopPointType>> getTypes() {
            return Optional.ofNullable(
                    forString != null ?
                            Stream.of(forString.replace(" ", "").split(","))
                                    .map(ShopPoint.ShopPointType::valueOf)
                                    .collect(Collectors.toList())
                            : null
            );
        }

        public LocationFilter() {
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setValues(Value[] values) {
            this.values = values;
        }

        public void setForString(String forString) {
            this.forString = forString;
        }
    }

    public static class Value {
        public String title;
        private String id;
        private String icon;

        public Value() {
        }

        public Optional<String> getId() {
            return Optional.ofNullable(id);
        }

        public Optional<String> getIcon() {
            return Optional.ofNullable(icon);
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }



}
