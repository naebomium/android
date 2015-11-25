package com.mobium.config.screen_models;

import com.google.gson.annotations.SerializedName;
import com.mobium.config.block_models.BlockModel;

/**
 *  on 29.10.15.
 */
public class Screen {
    private
    @SerializedName("Title")
    String title;

    private
    @SerializedName("Blocks")
    BlockModel[] blockModels;

    public Screen() {
    }

    public Screen(String title, BlockModel[] blockModels) {
        this.title = title;
        this.blockModels = blockModels;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BlockModel[] getBlockModels() {
        return blockModels;
    }

    public void setBlockModels(BlockModel[] blockModels) {
        this.blockModels = blockModels;
    }
}
