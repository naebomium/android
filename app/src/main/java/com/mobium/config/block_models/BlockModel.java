package com.mobium.config.block_models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 *  on 27.10.15.
 */
public abstract class BlockModel {
    public final BlockType type;
    private @SerializedName("StickyType") StickyType stickyType;
    private @SerializedName("Insets") Insets margin;
    private @SerializedName("Padding") Insets padding;

    {
        stickyType = StickyType.NONE;
        margin = Insets.defaultInsets();
        padding = Insets.defaultInsets();
    }

    public BlockModel(BlockType type) {
        this.type = type;
    }

    public BlockModel(BlockType type, StickyType stickyType) {
        this.stickyType = stickyType;
        this.type = type;
    }

    public BlockModel(BlockType blockType, StickyType stickyType,  Insets margin) {
        this(blockType, stickyType);
        setMargin(margin);
    }

    public BlockType getType() {
        return type;
    }

    public StickyType getStickyType() {
        return stickyType;
    }

    public BlockModel setStickyType(StickyType stickyType) {
        this.stickyType = stickyType;
        return this;
    }

    public @NonNull Insets getMargin() {
        return margin;
    }

    public BlockModel setMargin(Insets margin) {
        this.margin = margin;
        return this;
    }

    public @NonNull Insets getPadding() {
        return padding;
    }

    public BlockModel setPadding(Insets padding) {
        this.padding = padding;
        return this;
    }
}
