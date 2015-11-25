package com.mobium.reference.fragments.goods;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mobium.client.api.ShopApiExecutor;
import com.mobium.client.api.ShopCache;
import com.mobium.client.models.ShopCategory;
import com.mobium.client.models.ShopItem;
import com.mobium.reference.R;
import com.mobium.reference.ReferenceApplication;
import com.mobium.reference.activity.RequestCodes;
import com.mobium.reference.fragments.BasicLoadableFragment;
import com.mobium.reference.utils.FragmentUtils;
import com.mobium.reference.utils.Functional;
import com.mobium.reference.utils.ImageUtils;
import com.mobium.reference.utils.executing.ExecutingException;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;
import com.mobium.reference.views.Ui_configurator;
import com.mobium.reference.views.VisibilityViewUtils;
import com.mobium.reference.views.WebImageView;
import com.mobium.reference.views.adapters.CatalogRecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *  on 07.07.15.
 * http://mobiumapps.com/
 */
public class CatalogFragment_ extends BasicLoadableFragment implements
        Functional.ChangeListener<CatalogFragment_.State> {

    public static final byte SORTING_REQUEST = RequestCodes.FILTER_FRAGMENT_CODE;

    private static final int ITEM_COUNT_FOR_FIRST_LOAD = 50;
    private static final int ITEM_COUNT_FOR_NEXT_LOADS = 30;

    private String aliasId;
    private String categoryId;

    private CatalogRecyclerAdapter catalogAdapter;

    private ArrayList<ShopItem> shopItems;
    private ArrayList<ShopItem> filteredShopItems;

    private ArrayList<ShopCategory> shopSubcategories;

    private ShopCategory shopCategory;
    private RecyclerView catalogItemsList;
    private View progressBar;
    private TextView categoryName;

    private int offset;
    private int limit;

    private int currentIndex;

    private MenuItem topFilterMenuItem;

    private State state = State.List;

    @Override
    public void onChange(State newValue) {
        state = newValue;

        switch (newValue) {
            case List:
                if (topFilterMenuItem != null)
                    topFilterMenuItem.setVisible(shopItems != null && shopItems.size() > 0);
                catalogItemsList.setVisibility(View.VISIBLE);
                VisibilityViewUtils.hide(progressBar, true);
                break;
            case Load:
                catalogItemsList.setVisibility(View.GONE);

                VisibilityViewUtils.show(progressBar, false);
                if (topFilterMenuItem != null)
                    topFilterMenuItem.setVisible(false);
                break;
        }
    }


    public boolean showCategoriesAsCard() {
        return aliasId != null && aliasId.equals("actions");
    }


    public enum State {
        List, Load
    }


    public void setAliasId(String aliasId) {
        this.aliasId = aliasId;
    }

    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    protected String getTitle() {
        return isActions() ? "Акции" : "Каталог";
    }

    @Override
    public void onStart() {
        super.onStart();
        Events.get(getActivity()).navigation().onPageOpen(OpenPageEvents.catalog.name());
    }


    public boolean isActions() {
        return aliasId != null && aliasId.equals("actions");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_catalog_, container, false);

        if (categoryId == null && aliasId == null)
            categoryId = "0";

        progressBar = rootView.findViewById(R.id.progress);
        catalogItemsList = (RecyclerView) rootView.findViewById(R.id.catalogItems);
        categoryName = (TextView) rootView.findViewById(R.id.categoryName);
        LinearLayoutManager manager =
                new LinearLayoutManager(getDashboardActivity(), LinearLayoutManager.VERTICAL, false);
        catalogItemsList.setLayoutManager(manager);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            catalogItemsList.setElevation(ImageUtils.convertToPx(getActivity(), 4));
            progressBar.setElevation(ImageUtils.convertToPx(getActivity(), 4));
        }

        catalogItemsList.addOnScrollListener(new WebImageView.StopLoadScrollListener());
        catalogAdapter = Ui_configurator.getInstance(getActivity()).getCatalogAdapter();

        catalogAdapter.setShowCategoryAsCard(showCategoriesAsCard());
        catalogAdapter.setShowCategoryAsCard(isActions());

        catalogItemsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0)
                    if (manager.findFirstVisibleItemPosition() > 0.75 * shopItems.size())
                        loadMoreItemsAsync();
            }
        });


        return rootView;
    }

    @Override
    protected boolean needLoading() {
        // id категории не определен(нужно получить по алиасу) или категории нет в кеше
        boolean result =
                shopCategory == null ||
                !getApplication().getCategories().containsKey(categoryId);
        if (result)
            onChange(State.Load);
        return result;
    }


    @Override
    protected void loadInBackground() throws ExecutingException {
        ReferenceApplication application = getReferenceApplication();

        //получение id категории
        if (aliasId != null)
            categoryId = application.getExecutor().getCategoryIdByAlias(aliasId);

        //загрузка категоии
        if (application.getCategories().containsKey(categoryId))
            shopCategory = application.getCategories().get(categoryId);
        else
            shopCategory = application.loadCategory(categoryId);

        //загрузка субкатегорий
        if (shopCategory.getExtraBoolean(ShopApiExecutor.HAS_MORE_SUBCATEGORIES)
                &&  shopCategory.childs.isEmpty())
            application.loadCategories(application.getCategories().get(categoryId));


        shopSubcategories = new ArrayList<>();
        //субкатегории
        if (!shopCategory.childs.isEmpty())
            shopSubcategories.addAll(shopCategory.childs);
        shopItems = new ArrayList<>(Arrays.asList(loadItems(ITEM_COUNT_FOR_FIRST_LOAD, 0)));
        offset = shopItems.size();


        Log.d("CATALOG", "Total items" + shopCategory.totalCount + " loaded" + shopItems.size());
    }

    private void fillUi() {
        Events.get(getActivity()).catalog().onCategoryOpened(shopCategory);
        catalogItemsList.setAdapter(catalogAdapter);
        catalogAdapter.setCategories(shopSubcategories);
        if (showCategoryName()) {
            categoryName.setText(shopCategory.title);
            categoryName.setVisibility(View.VISIBLE);
        } else
            categoryName.setVisibility(View.GONE);
    }

    private boolean showCategoryName() {
        return !isActions() && !(categoryId != null && categoryId.equals("0"));
    }

    @Override
    protected void afterLoaded() {
        super.afterLoaded();
        catalogAdapter.setShopItems(shopItems);
        fillUi();
        onChange(State.List);
    }

    @Override
    protected void doesntNeedLoading() {
        super.doesntNeedLoading();

        shopItems = new ArrayList<>(Arrays.asList(
                getReferenceApplication().getShopCache().fetchCategoryItems(categoryId)
        ));
        shopSubcategories = shopCategory.childs;
        catalogAdapter.setShopItems(shopItems);
        fillUi();
    }

    @Override
    protected void alreadyLoaded() {
        super.alreadyLoaded();
        if (catalogItemsList.getAdapter() != null)
            return;

        if (filteredShopItems == null)
            catalogAdapter.setShopItems(shopItems);
        else
            catalogAdapter.setShopItems(filteredShopItems);

        fillUi();
    }

    @Override
    protected void afterPrepared() {
        super.afterPrepared();
        topFilterMenuItem.setVisible(
                shopItems.size() > 0 &&
                        state == State.List
        );
    }

    private ShopItem[] loadItems(int limit, int offset) throws ExecutingException, OutOfMemoryError {
        return getReferenceApplication().getShopCache().fetchItemsFromServer(shopCategory, offset, limit);
    }

    private boolean loading = false;

    private void loadMoreItemsAsync() {
        if (loading)
            return;

        loading = true;
        int itemsInCategory = shopCategory.totalCount;
        if (offset >= itemsInCategory)
            return;

        if (offset == 0) {
            limit = ITEM_COUNT_FOR_FIRST_LOAD;
        } else {
            limit = ITEM_COUNT_FOR_NEXT_LOADS;
        }

        catalogAdapter.setIsLoad(true);

        new AsyncTask<Void, Void, ShopItem[]>() {
            @Override
            protected ShopItem[] doInBackground(Void... voids) {
                try {
                    return loadItems(limit, offset);
                } catch (ExecutingException e) {
                    e.printStackTrace();
                }  catch (OutOfMemoryError e) {
                    getReferenceApplication().getShopCache().clearCache();
                    return doInBackground();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ShopItem[] items) {
                if (items == null || !FragmentUtils.isUiFragmentActive(CatalogFragment_.this))
                    return;

                catalogAdapter.setIsLoad(false);

                Collections.addAll(shopItems, items);
                catalogAdapter.addShopItems(Arrays.asList(items));
                offset = shopItems.size();

                Log.d("CATALOG", "loaded" + items.length +" items, total: " + shopItems.size());

                loading = false;
            }

        }.execute();

    }

    private void onFilterButtonClick() {
        Events.get(getActivity()).catalog().onFilterOpened(shopCategory);
        if (shopCategory.totalCount > shopItems.size()) {
            limit = shopCategory.totalCount - shopItems.size();
            offset = shopItems.size();
            onChange(State.Load);

            new AsyncTask<Void, Void, ShopItem[]>() {
                @Override
                protected ShopItem[] doInBackground(Void... voids) {
                    try {
                        return loadItems(limit, offset);
                    } catch (ExecutingException e) {
                        e.printStackTrace();
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Ошбика во время загрузки")
                                .setMessage("Повторить попытку?")
                                .setPositiveButton("Повторить", (dialogInterface, i) -> {
                                    onFilterButtonClick();
                                }).setNegativeButton("Отмена", (dialogInterface1, i1) -> {
                        }).show();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(ShopItem[] items) {
                    if (items == null) {
                        onChange(State.List);
                        return;
                    }
                    Collections.addAll(shopItems, items);
                    offset = shopItems.size();
                    if (isFragmentUIActive()) {
                        showFilterView();
                    }
                    else
                        onChange(State.List);
                    Log.d("CATALOG", "Load for filters " + shopItems.size());

                }
            }.execute();
        } else {
            catalogItemsList.scrollToPosition(0);
            showFilterView();
        }
    }

    private void showFilterView() {
        FilterFragment fragment = FilterFragment.newInstance(shopCategory);

        ShopCache shopCache = getApplication().getShopCache();

        for (ShopItem item : shopItems)
            if (!getApplication().getShopCache().hasItem(item.id))

        fragment.setTargetFragment(this, SORTING_REQUEST);

//        fragment.show(getFragmentManager(), FilterFragment.ID);

//        getFragmentManager().beginTransaction()
//                .replace(R.id.fragment_wrapper, fragment, FilterFragment.ID)
//                .addToBackStack(FilterFragment.ID)
//                .commit();
        FragmentUtils.replace(getActivity(), fragment, true);
        onChange(State.List);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SORTING_REQUEST:
                    filteredShopItems = (ArrayList<ShopItem>)
                            data.getSerializableExtra(FilterFragment.ITEMS_ID);
                    catalogAdapter.setShopItems(filteredShopItems);
                    if (filteredShopItems == null || filteredShopItems.size() == 0)
                        Toast.makeText(getContext(), "По выбранным фильтрам ничего не найдено", Toast.LENGTH_LONG);
                    break;
            }
        }
    }



    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.top_search).setVisible(false);

        topFilterMenuItem = menu.findItem(R.id.top_filter);
        if (shopItems != null)
            topFilterMenuItem.setVisible(shopItems.size() > 0 && state == State.List);

        topFilterMenuItem.setOnMenuItemClickListener(menuItem -> {
            onFilterButtonClick();
            return true;
        });
    }


    @Override
    public boolean onBackPressed() {
       if  (state == State.Load) {
            loading = false;
            if (shopItems == null || shopItems.size() == 0) {
                getFragmentManager().popBackStack();
                return true;
            }
            onChange(State.List);
            return true;
        }
        return super.onBackPressed();
    }


    public static CatalogFragment_ getInstanceToCategory(String categoryId) {
        CatalogFragment_ result = new CatalogFragment_();
        result.setCategoryId(categoryId);
        return result;
    }

    public static CatalogFragment_ getInstanceToAlias(String aliasId) {
        CatalogFragment_ result = new CatalogFragment_();
        result.setAliasId(aliasId);
        return result;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        catalogItemsList.clearOnScrollListeners();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        shopItems = filteredShopItems = null;
        shopSubcategories = null;
        if (catalogItemsList != null)
            catalogItemsList.setAdapter(null);
        catalogAdapter = null;
    }
}
