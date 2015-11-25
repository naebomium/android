package com.mobium.client.models.filters;

import android.support.annotation.NonNull;
import com.mobium.client.models.ShopItem;

/**
 *
 *
 * Date: 26.07.13
 * Time: 13:57
 * To change this template use File | Settings | File Templates.
 */
public class FilteringRange extends Filtering {
    private double minValue;
    private double maxValue;
    private double stepValue;
    private String suffix;

    public FilteringRange(String id, String title, double min, double max, double step, String suffix) {
        super(id, title, Filtering.Type.range);
        this.minValue = min;
        this.maxValue = max;
        this.stepValue = step;
        this.suffix = suffix;
    }

    public FilteringRange(String id, String title, double min, double max, double step, String suffix, boolean isCollapsed) {
        super(id, title, Filtering.Type.range, isCollapsed);
        this.minValue = min;
        this.maxValue = max;
        this.stepValue = step;
        this.suffix = suffix;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getStepValue() {
        return stepValue;
    }

    public String getSuffix() {
        return suffix;
    }

    @Override
    public FilterState createState() {
        return new FilterRangeState(this);
    }


    public static class FilterRangeState extends FilterState {
        private final FilteringRange range;

        private Double currentMaxValue;
        private Double currentMinValue;

        public FilterRangeState(FilteringRange range) {
            this.range = range;
            currentMaxValue = range.getMaxValue();
            currentMinValue = range.getMinValue();
        }


        public void setCurrentMaxValue(double currentMaxValue) {
            this.currentMaxValue = currentMaxValue;
            notyfiListers();
        }

        public void setCurrentMinValue(double currentMinValue) {
            this.currentMinValue = currentMinValue;
            notyfiListers();
        }

        public Double getCurrentMaxValue() {
            return currentMaxValue;
        }

        public Double getCurrentMinValue() {
            return currentMinValue;
        }

        public String getTitle() {
            return range.getTitle();
        }


        @Override
        public Boolean  filterOut(@NonNull ShopItem item) {
            boolean result = false;
            if (item.filtering.containsKey(range.getId())) {
                Double value = (Double) item.filtering.get(range.getId());
                if (value > currentMaxValue || value < currentMinValue)
                    result = true;
            }
            return result;
        }

        @Override
        public Boolean isCustomState() {
            return currentMaxValue != range.maxValue || currentMinValue != range.minValue;
        }

        @Override
        public void clear() {
            currentMaxValue = range.maxValue;
            currentMinValue = range.minValue;
            notyfiListers();
        }
    }


}