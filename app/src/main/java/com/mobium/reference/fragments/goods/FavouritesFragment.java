package com.mobium.reference.fragments.goods;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mobium.client.LogicUtils;
import com.mobium.client.models.ShopItem;
import com.mobium.reference.R;
import com.mobium.reference.fragments.BasicContentFragment;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;
import com.mobium.reference.views.adapters.CatalogRecyclerAdapter;

import java.util.List;

/**
 *
 *
 * Date: 28.11.12
 * Time: 16:48
 */
public class FavouritesFragment extends BasicContentFragment {
    private RecyclerView productsView;
    private View hintWrapper;
    private CatalogRecyclerAdapter adapter;
    private LogicUtils.OnChangeFavouriteListener listener;


    @Override
    protected String getTitle() {
        return getString(R.string.favourites_title);
    }

    private void updateData(){
        new Handler().post(() -> {
            if (getActivity() == null)
                return;
            List<ShopItem> favourites = getApplication().getShopData().getFavourites();
            boolean showItems = !favourites.isEmpty();
            hintWrapper.setVisibility(showItems ? View.GONE : View.VISIBLE);
            productsView.setVisibility(showItems ? View.VISIBLE : View.GONE);
            adapter.setShopItems(favourites);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getReferenceApplication().getShopData().removeFavouriteListener(listener);
    }


    @Override
    public void onStart() {
        super.onStart();
        Events.get(getActivity()).navigation().onPageOpen(OpenPageEvents.favorites.name());
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View res = inflater.inflate(R.layout.fragment_favourites, container, false);
        View hintView = res.findViewById(R.id.hintText);
        productsView = (RecyclerView) res.findViewById(R.id.products);
        hintWrapper = res.findViewById(R.id.fragment_catalog_hintWrapper);

        productsView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        productsView.setItemAnimator(itemAnimator);

        adapter = new CatalogRecyclerAdapter(getDashboardActivity());
        productsView.setAdapter(adapter);

        listener = newValue -> updateData();

        getReferenceApplication().getShopData().addFavouriteListener(listener);

        return res;
    }
}
