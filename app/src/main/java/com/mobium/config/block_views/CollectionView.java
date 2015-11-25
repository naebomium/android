package com.mobium.config.block_views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mobium.client.models.Action;
import com.mobium.config.block_models.CollectionViewModel;
import com.mobium.config.common.Handler;
import com.mobium.config.common.HaveActions;
import com.mobium.config.common.UpdatableLoadableView;
import com.mobium.config.common.Updater;
import com.mobium.config.common_views.OffsetAdapter;
import com.mobium.config.common_views.collection_view_items.ItemWithCostItemSupport;
import com.mobium.config.models.ItemWithCost;
import com.mobium.config.models.ItemWithName;
import com.mobium.reference.views.adapters.LoadAdapter;

import java.util.List;

/**
 *  on 03.11.2015.
 */
public class CollectionView<T> extends BaseView<CollectionViewModel> implements UpdatableLoadableView<List<T>>, HaveActions {

    private RecyclerView recyclerView;
    private FrameLayout layout;
    private Updater updater;
    private OffsetAdapter adapter;
    private Handler<Action> actionHandler;


    public CollectionView(CollectionViewModel model) {
        super(model);
    }

    @Override
    protected View buildView(Context context, @NonNull ViewGroup viewGroup, boolean add) {
        layout = new FrameLayout(context);
        recyclerView = new RecyclerView(context);
        final LinearLayoutManager layoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new LoadAdapter(model.getLimit(), model));
        layout.addView(recyclerView, getDefaultParams());
        updater.requestData(model.getLimit(), 0);
        if (add)
            viewGroup.addView(layout);
        return layout;
    }


    @Override
    public boolean hasData() {
        return adapter != null;
    }

    @Override
    public void onEmptyData(int offset) {
        if (offset == 0)
            recyclerView.setVisibility(View.GONE);
    }


    @Override
    public void setUpdater(Updater updater) {
        this.updater = updater;
    }

    @Override
    public void setData(List<T> data, int offset) {
        if (model.getContentType().equals(CollectionViewModel.CONTENT_TYPE.ITEMS)) {
            List<ItemWithCost> itemWithCosts = (List<ItemWithCost>) data;
            if (adapter == null) {
                adapter = new OffsetAdapter<>
                        (itemWithCosts, ItemWithCostItemSupport.get(model), actionHandler);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.addData(data, offset);
            }
        } else if (model.getContentType().equals(CollectionViewModel.CONTENT_TYPE.CATEGORIES)) {
            List<ItemWithName> itemWithNames = (List<ItemWithName>) data;

        }
    }

    @Override
    public void setActionHandler(Handler<Action> handler) {
        this.actionHandler = handler;
    }
    public static <T> CollectionView<T> get(CollectionViewModel model) {
        if (model.getContentType().equals(CollectionViewModel.CONTENT_TYPE.ITEMS))
            return (CollectionView<T>) new CollectionView<ItemWithCost>(model);
        else if (model.getContentType().equals(CollectionViewModel.CONTENT_TYPE.ITEMS))
            return (CollectionView<T>) new CollectionView<ItemWithName>(model);
        return null;
    }
}
