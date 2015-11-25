package com.mobium.config;

import com.mobium.config.block_models.BlockModel;
import com.mobium.config.block_models.ButtonModel;
import com.mobium.config.block_models.CatalogSearchModel;
import com.mobium.config.block_models.CollectionViewModel;
import com.mobium.config.block_models.ImagesPagerModel;
import com.mobium.config.block_models.LabelModel;
import com.mobium.config.block_models.LinesModel;
import com.mobium.config.block_views.BaseView;
import com.mobium.config.block_views.ButtonView;
import com.mobium.config.block_views.CatalogSearchView;
import com.mobium.config.block_views.CollectionView;
import com.mobium.config.block_views.ImagesPager;
import com.mobium.config.block_views.LabelView;
import com.mobium.config.block_views.LinesView;
import com.mobium.config.models.ItemWithCost;
import com.mobium.config.models.ItemWithName;
import com.mobium.reference.activity.MainDashboardActivity;

/**
 *  on 30.10.15.
 */
public class BlockViewFactory {
    public static BaseView<? extends BlockModel> build (BlockModel model) {
        if (model instanceof LabelModel)
            return new LabelView((LabelModel)model);
        else if (model instanceof ImagesPagerModel)
            return new ImagesPager((ImagesPagerModel) model);
        else if (model instanceof CatalogSearchModel)
            return new CatalogSearchView((CatalogSearchModel) model);
        else if (model instanceof LinesModel)
            return new LinesView((LinesModel) model);
        else if (model instanceof CollectionViewModel) {
            CollectionViewModel collectionViewModel = (CollectionViewModel) model;
            return CollectionView.get(collectionViewModel);
        }

        return null;
    }
}
