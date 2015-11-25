package com.mobium.config.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mobium.config.block_models.BlockModel;
import com.mobium.config.block_models.BlockType;
import com.mobium.config.block_models.CatalogSearchModel;
import com.mobium.config.block_models.CollectionViewModel;
import com.mobium.config.block_models.ImagesPagerModel;
import com.mobium.config.block_models.LabelModel;
import com.mobium.config.block_models.LinesModel;

import java.lang.reflect.Type;


/**
 *  on 29.10.15.
 */
public class BlockConverter implements JsonDeserializer<BlockModel> {

    @Override
    public BlockModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String typestr = json.getAsJsonObject().get("BlockType").getAsString();
        BlockType type = null;
        try {
            type = BlockType.valueOf(typestr);
        } catch (NullPointerException e) {
            return null;
        }
        switch (type) {
            case COLLECTION_VIEW:
                return context.deserialize(json, CollectionViewModel.class);
            case IMAGES_PAGER:
                return context.deserialize(json, ImagesPagerModel.class);
            case LABEL:
                return context.deserialize(json, LabelModel.class);
            case SEARCH_BAR:
                return context.deserialize(json, CatalogSearchModel.class);
            case MENU:
                return context.deserialize(json, LinesModel.class);
        }
        return null;
    }
}
