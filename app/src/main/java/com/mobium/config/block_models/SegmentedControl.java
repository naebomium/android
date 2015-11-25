package com.mobium.config.block_models;

import com.google.gson.annotations.SerializedName;
import com.mobium.config.Util;

/**
 *  on 11/9/15.
 * MobiumApps http://mobiumapps.com/
 */
public class SegmentedControl {

    @SerializedName("SelectedSegment")
    private int selectedSegment;

    @SerializedName("Borders")
    private Borders[] borders;

    @SerializedName("Tabs")
    private Tabs[] tabs;

    public enum DATA_TYPE {
        DESCRIPTION, SPECIFICATIONS, COMMENTS, OTHER_ITEMS, RELATED_ITEMS
    }

    public Tabs[] getTabs() {
        return tabs;
    }

    public void setTabs(Tabs[] tabs) {
        this.tabs = tabs;
    }

    public int getSelectedSegment() {
        return selectedSegment;
    }

    public void setSelectedSegment(int selectedSegment) {
        this.selectedSegment = selectedSegment;
    }

    public Borders[] getBorders() {
        return borders;
    }

    public void setBorders(Borders[] borders) {
        this.borders = borders;
    }

    public class Borders {

        @SerializedName("BorderWidth")
        private int width;

        @SerializedName("BorderRadius")
        private int radius;

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }
    }

    public class Tabs {

        @SerializedName("Title")
        private String descriptionTitle;

        @SerializedName("Colors")
        private Colors colors;

        @SerializedName("DataType")
        private DATA_TYPE dataType;

        @SerializedName("Weight")
        private int weight;

        public String getDescriptionTitle() {
            return descriptionTitle;
        }

        public void setDescriptionTitle(String descriptionTitle) {
            this.descriptionTitle = descriptionTitle;
        }

        public Colors getColors() {
            return colors;
        }

        public void setColors(Colors colors) {
            this.colors = colors;
        }

        public DATA_TYPE getDataType() {
            return dataType;
        }

        public void setDataType(DATA_TYPE dataType) {
            this.dataType = dataType;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public class Colors {

            @SerializedName("BackgroundColor")
            private String backgroundColor;

            @SerializedName("SelectedBackgroundColor")
            private String selectedBackgroundColor;

            @SerializedName("SelectedTextColor")
            private String selectedTextColor;

            @SerializedName("TextColor")
            private String textColor;

            @SerializedName("BorderColor")
            private String borderColor;

            public int getBackgroundColor(int defaultColor) {
                return Util.colorFromString(backgroundColor, defaultColor);
            }

            public void setBackgroundColor(String backgroundColor) {
                this.backgroundColor = backgroundColor;
            }

            public int getSelectedBackgroundColor(int defaultColor) {
                return Util.colorFromString(selectedBackgroundColor, defaultColor);
            }

            public void setSelectedBackgroundColor(String selectedBackgroundColor) {
                this.selectedBackgroundColor = selectedBackgroundColor;
            }

            public int getSelectedTextColor(int defaultColor) {
                return Util.colorFromString(selectedTextColor, defaultColor);
            }

            public void setSelectedTextColor(String selectedTextColor) {
                this.selectedTextColor = selectedTextColor;
            }

            public int getTextColor(int defaultColor) {
                return Util.colorFromString(textColor, defaultColor);
            }

            public void setTextColor(String textColor) {
                this.textColor = textColor;
            }

            public int getBorderColor(int defaultColor) {
                return Util.colorFromString(borderColor, defaultColor);
            }

            public void setBorderColor(String borderColor) {
                this.borderColor = borderColor;
            }
        }


    }
}
