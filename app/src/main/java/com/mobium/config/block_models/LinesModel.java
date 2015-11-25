package com.mobium.config.block_models;

import com.google.gson.annotations.SerializedName;

/**
 *  on 02.11.2015.
 */
public class LinesModel extends BlockModel {
    @SerializedName("SpaceBetweenLines")private int spaceBetweenLines;
    @SerializedName("Height") private int height;
    @SerializedName("Lines") private Line[] lines;

    public LinesModel() {
        super(BlockType.MENU);
    }

    public Line[] getLines() {
        return lines;
    }

    public void setLines(Line[] items) {
        this.lines = items;
    }

    public int getSpaceBetweenLines() {
        return spaceBetweenLines;
    }

    public void setSpaceBetweenLines(int spaceBetweenLines) {
        this.spaceBetweenLines = spaceBetweenLines;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    
    public static class LineItem {
        @SerializedName("BtnModel")
        private ButtonModel button;
        @SerializedName("Weight")
        private int weight;

        public LineItem() {
        }

        public ButtonModel getButton() {
            return button;
        }

        public void setButton(ButtonModel button) {
            this.button = button;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }

    public static class Line {
        @SerializedName("Elems")
        private LineItem[] lineElements;

        @SerializedName("Weight")
        private int lineWeight;

        @SerializedName("SpaceBetweenElems")
        private int spiceBettweenElems;

        public int getSpiceBettweenElems() {
            return spiceBettweenElems;
        }

        public void setSpiceBettweenElems(int spiceBettweenElems) {
            this.spiceBettweenElems = spiceBettweenElems;
        }

        public LineItem[] getLineElements() {
            return lineElements;
        }

        public void setLineElements(LineItem[] lineElements) {
            this.lineElements = lineElements;
        }

        public int getLineWeight() {
            return lineWeight;
        }

        public void setLineWeight(int lineWeight) {
            this.lineWeight = lineWeight;
        }
    }
}
