package com.mobium.config.block_models;

import com.google.gson.annotations.SerializedName;

/**
 *  on 27.10.15.
 */
public class Insets {
    public @SerializedName("Top")
    Inset top;
    public @SerializedName("Bottom")
    Inset bottom;
    public @SerializedName("Right")
    Inset right;
    public @SerializedName("Left")
    Inset left;

    public Insets(Inset top, Inset bottom, Inset right, Inset left) {
        this.top = top;
        this.bottom = bottom;
        this.right = right;
        this.left = left;
    }

    public Insets() {
    }

    public Insets(Inset all) {
        this.top = all;
        this.bottom  = all;
        this.left = all;
        this.right = all;
    }

    public Insets setTop(Inset top) {
        this.top = top;
        return this;
    }

    public Insets setBottom(Inset bottom) {
        this.bottom = bottom;
        return this;
    }

    public Insets setRight(Inset right) {
        this.right = right;
        return this;
    }

    public Insets setLeft(Inset left) {
        this.left = left;
        return this;
    }

    public static class InsetsBuilder {
        public Inset top;
        public Inset bottom;
        public Inset right;
        public Inset left;

        private InsetsBuilder() {
        }

        public static InsetsBuilder anInsets() {
            return new InsetsBuilder();
        }

        public InsetsBuilder withTop(Inset top) {
            this.top = top;
            return this;
        }

        public InsetsBuilder withBottom(Inset bottom) {
            this.bottom = bottom;
            return this;
        }

        public InsetsBuilder withRight(Inset right) {
            this.right = right;
            return this;
        }

        public InsetsBuilder withLeft(Inset left) {
            this.left = left;
            return this;
        }

        public InsetsBuilder but() {
            return anInsets().withTop(top).withBottom(bottom).withRight(right).withLeft(left);
        }

        public Insets build() {
            Insets insets = new Insets();
            insets.setTop(top);
            insets.setBottom(bottom);
            insets.setRight(right);
            insets.setLeft(left);
            return insets;
        }
    }

    public static Insets defaultInsets() {
        return new InsetsBuilder()
                .withBottom(Inset.NONE)
                .withRight(Inset.NONE)
                .withTop(Inset.NONE)
                .withLeft(Inset.NONE)
                .build();
    }
}
