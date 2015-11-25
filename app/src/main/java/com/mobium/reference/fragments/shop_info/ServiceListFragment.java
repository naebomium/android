package com.mobium.reference.fragments.shop_info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobium.client.ShopDataStorage;
import com.mobium.new_api.Api;
import com.mobium.new_api.Handler;
import com.mobium.new_api.SimpleHandler;
import com.mobium.new_api.methodParameters.GetShopPointParam;
import com.mobium.new_api.models.LocationFilters;
import com.mobium.new_api.models.MobiumError;
import com.mobium.new_api.models.Region;
import com.mobium.new_api.models.ShopPoint;
import com.mobium.new_api.utills.ShopPointUtils;
import com.mobium.reference.R;
import com.mobium.reference.fragments.BasicContentFragment;
import com.mobium.reference.utils.Dialogs;
import com.mobium.reference.utils.FragmentUtils;
import com.mobium.config.common.Config;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;
import com.mobium.reference.views.ChangeRegionUtils;
import com.mobium.reference.views.VisibilityViewUtils;
import com.mobium.reference.views.adapters.ShopPointAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  on 24.07.15.
 * http://mobiumapps.com/
 */
public class ServiceListFragment extends BasicContentFragment {
    private Region currentRegion;
    private ShopPointAdapter adapter;
    private static LocationFilters filters;

    private boolean updated = false;

    @Bind(R.id.fragment_services_list)
    protected RecyclerView viewServiceList;

    @Bind(R.id.fragment_shops_region_title)
    protected TextView buttonRegion;

    @Bind(R.id.dontsave)
    protected TextView viewHint;

    @Bind(R.id.progress_view)
    protected View viewLoad;


    {
        currentRegion = ShopDataStorage.getInstance()
                .getCurrentRegion()
                .orElse(ShopDataStorage.getInstance().getRegions().get(0));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (filters == null)
            loadLocationFiltersAsync();
        else if (currentRegion.services == null)
            loadPointsAsync();
        else if (!updated)
            configureUi();
    }

    private void onRegionChanged(Region region) {
        this.currentRegion = region;

        if (region.services == null)
            loadPointsAsync();
        else
            configureUi();
        if (viewServiceList != null)
            viewServiceList.scrollToPosition(0);
    }

    private void configureUi() {
        buttonRegion.setText(currentRegion.getTitle());
        if (currentRegion.services == null || currentRegion.services.size() == 0) {
            viewServiceList.setVisibility(View.GONE);
            viewHint.setVisibility(View.VISIBLE);
            viewHint.setText("В данном регионе нет сервисных центров");
        } else {
            viewServiceList.setVisibility(View.VISIBLE);
            viewHint.setVisibility(View.GONE);
            if (adapter == null || viewServiceList.getAdapter() == null) {
                adapter = new ShopPointAdapter(currentRegion.services.entrySet(), getActivity());
                viewServiceList.setAdapter(adapter);

                adapter.setListener((name, list, position) -> {
                            String url = ShopPointUtils.getBrandIconUrl(filters, name).orElse("");
                            FragmentUtils.replace(getActivity(),
                                    ServiceFragment.getInstance(list, name, url, currentRegion),
                                    true
                            );
                        }
                );

            } else adapter.setItemsReplacingOld(new ArrayList<>(currentRegion.services.entrySet()));
        }
        updated = true;
        VisibilityViewUtils.hide(viewLoad, true);
    }

    
    private void loadLocationFiltersAsync() {
        VisibilityViewUtils.show(viewLoad, true);
        Api.getInstance().GetPointFilters().call(new SimpleHandler<LocationFilters>() {
            @Override
            public void onResult(LocationFilters f) {
                filters = f;
                if (!isFragmentUIActive())
                    return;
                if (currentRegion.services == null)
                    loadPointsAsync();
                else configureUi();
            }

            @Override
            public void onBadArgument(MobiumError mobiumError) {
                Dialogs.showExitScreenDialog(getDashboardActivity(), getFragmentManager()::popBackStack);
            }

            @Override
            public void onException(Exception ex) {
                Dialogs.showNetworkErrorDialog(getDashboardActivity(),
                        ex.getMessage(),
                        ServiceListFragment.this::loadLocationFiltersAsync,
                        getFragmentManager()::popBackStack);
            }
        });
    }
    private void loadPointsAsync() {
        VisibilityViewUtils.show(viewLoad, true);
        Api.getInstance().GetShopPoints(currentRegion, ShopPoint.ShopPointType.service, "", null)
                .invoke(new Handler<GetShopPointParam, ShopPoint.ShopPointList>() {
                    @Override
                    public void onResult(ShopPoint.ShopPointList shopPointList) {
                        currentRegion.services =
                                ShopPointUtils.buildServiceMap(shopPointList.getPoints());
                        if (!isFragmentUIActive())
                            return;
                        configureUi();
                    }

                    @Override
                    public void onBadArgument(MobiumError mobiumError, GetShopPointParam getShopPointParam) {
                        Dialogs.showExitScreenDialog(getDashboardActivity(), getFragmentManager()::popBackStack, mobiumError.description);
                    }

                    @Override
                    public void onException(Exception ex) {
                        Dialogs.showNetworkErrorDialog(getDashboardActivity(),
                                ex.getMessage(),
                                ServiceListFragment.this::loadPointsAsync,
                                getFragmentManager()::popBackStack);
                    }
                });
        updated = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        Events.get(getActivity()).navigation().onPageOpen(OpenPageEvents.services_list.name());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_services, container, false);
        ButterKnife.bind(this, root);
        updated = false;

        viewServiceList.setLayoutManager(new LinearLayoutManager(getActivity()));
        return root;
    }



    public static List<Region> getRegions() {
        return ShopDataStorage.getInstance().getRegions();
    }

    @OnClick(R.id.fragment_shops_region_title)
    protected void onChangeRegion() {
//        Dialogs.showSelectDialog(
//                getDashboardActivity(),
//                "Выбор Региона",
//                getRegions(),
//                Region::getTitle,
//                this::onRegionChanged);


        ChangeRegionUtils.showAutoCompleteDialog(getActivity(), new ChangeRegionUtils.DataProvider() {
            @Override
            public List<Region> regions() {
                return  getRegions();
            }

            @Override
            public String title() {
                return Config.get().strings().autoCompleteRegionTitle();
            }

            @Override
            public String message() {
                return Config.get().strings().autoCompleteRegionMessage();
            }
        }, this::onRegionChanged);
    }


    @Override
    protected String getTitle() {
        return "Сервис-центры";
    }

}
