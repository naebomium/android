package com.mobium.reference.fragments.shop_info;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.mobium.new_api.Api;
import com.mobium.new_api.Handler;
import com.mobium.new_api.methodParameters.GetRegionsParam;
import com.mobium.new_api.methodParameters.GetShopPointParam;
import com.mobium.new_api.models.MobiumError;
import com.mobium.new_api.models.Region;
import com.mobium.new_api.models.RegionList;
import com.mobium.new_api.models.ShopPoint;
import com.mobium.new_api.utills.ShopPointUtils;
import com.mobium.reference.R;
import com.mobium.reference.anotations.NeedInternetAccess;
import com.mobium.reference.fragments.BasicContentFragment;
import com.mobium.reference.utils.Dialogs;
import com.mobium.reference.utils.FragmentUtils;
import com.mobium.reference.utils.MapUtils;
import com.mobium.config.common.Config;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;
import com.mobium.reference.views.ChangeRegionUtils;
import com.mobium.reference.views.adapters.ShopPointsAdapter;

import java.util.List;

/**
 *  on 11.07.15.
 * http://mobiumapps.com/
 */

@NeedInternetAccess
public class ShopPointListFragment extends BasicContentFragment {
    private Region currentRegion;
//    private ShopPoint selectedPoint;


    private static List<Region> regionsWithPoints;

    private RecyclerView pointsRecyclerView;
    private ShopPointsAdapter adapter;
    private TextView regionButton;
    private MapView mapView;

    private GoogleMap map;

    private TextView hint;
    private View loadView;

    private State state;

    private MenuItem mapIcon;
    private MenuItem listIcon;


    private boolean hasUiBeenUpdated = false;


    private enum State {
        MAP, LIST;
        public static State next(State state) {
            return state == LIST ? MAP : LIST;
        }
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View res = inflater.inflate(R.layout.fragment_shops, container, false);
        pointsRecyclerView = (RecyclerView) res.findViewById(R.id.fragment_shops_shop_list);
        regionButton = (TextView) res.findViewById(R.id.fragment_shops_region_title);
        mapView = (MapView) res.findViewById(R.id.fragment_shops_map);
        pointsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        hint = (TextView) res.findViewById(R.id.fragment_shops_hint);
        loadView = res.findViewById(R.id.fragment_shops_progress);

        regionButton.setOnClickListener(view ->{
            ChangeRegionUtils.showAutoCompleteDialog(getActivity(), new ChangeRegionUtils.DataProvider() {
                @Override
                public List<Region> regions() {
                    return regionsWithPoints;
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



//            Dialogs.showSelectDialog(
//                    getActivity(),
//                    "Выбор Региона",
//                    regionsWithPoints,
//                    Region::getTitle,
//                    r -> {
//
//                    }
//            )
        });

        mapView.onCreate(savedInstanceState);

        MapsInitializer.initialize(this.getActivity());


        getDashboardActivity().invalidateOptionsMenu();
        hasUiBeenUpdated = false;
        map = null;

        return res;
    }

    private void onRegionChanged(Region region) {
        regionButton.setText(region.getTitle());
        currentRegion = region;
        if (currentRegion.getPoints() == null)
            loadPointsAcynh();
        else
            updateUi();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Events.get(getActivity()).navigation().onPageOpen(OpenPageEvents.shops.name());
    }

    private void loadPointsAcynh() {
        final Region copy = currentRegion;
        hasUiBeenUpdated = false;

        loadView.setVisibility(View.VISIBLE);
        if (mapIcon != null) {
            mapIcon.setVisible(false);
            listIcon.setVisible(false);
        }

        Api.getInstance().GetShopPoints(currentRegion, null).invoke(new Handler<GetShopPointParam, ShopPoint.ShopPointList>() {
            @Override
            public void onResult(ShopPoint.ShopPointList shopPointList) {
                if (!isFragmentUIActive())
                    return;
                copy.setPoints(ShopPointUtils.filter(
                                shopPointList.getPoints(),
                                ShopPoint.ShopPointType.pickupShop,
                                ShopPoint.ShopPointType.noPickupShop)
                );
                updateUi();
            }

            @Override
            public void onBadArgument(MobiumError mobiumError, GetShopPointParam getShopPointParam) {
                Dialogs.showExitScreenDialog(getDashboardActivity(), getFragmentManager()::popBackStack);
            }

            @Override
            public void onException(Exception ex) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Ошбика соединения")
                        .setMessage("Повторить попытку?")
                        .setPositiveButton("Повторить", (dialogInterface, i) -> {
                            loadPointsAcynh();
                        }).setNegativeButton("Отмена", (dialogInterface1, i1) -> {
                    getFragmentManager().popBackStack();
                }).show();
            }
        });
    }


    private void loadRegionWithPointsAcync() {
        loadView.setVisibility(View.VISIBLE);
        if (mapIcon != null) {
            mapIcon.setVisible(false);
            listIcon.setVisible(false);
        }
        hasUiBeenUpdated = false;

        Runnable update = () -> {
            Stream.of(regionsWithPoints)
                    .filter(region -> currentRegion.getId().equals(region.getId()))
                    .findFirst()
                    .ifPresent(r ->
                                    currentRegion = r
                    );
            if (currentRegion.getPoints() == null)
                loadPointsAcynh();
            else
                updateUi();
        };
        if (regionsWithPoints == null)
            Api.getInstance().GetRegions(1, null, null).invoke(new Handler<GetRegionsParam, RegionList>() {
                @Override
                public void onResult(RegionList regionList) {
                    if (!isFragmentUIActive())
                        return;
                    regionsWithPoints = regionList.getRegions();
                    update.run();
                }

                @Override
                public void onBadArgument(MobiumError mobiumError, GetRegionsParam getRegionsParam) {
                    Dialogs.showExitScreenDialog(getDashboardActivity(), getFragmentManager()::popBackStack);
                }

                @Override
                public void onException(Exception ex) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Ошбика соединения")
                            .setMessage("Повторить попытку?")
                            .setPositiveButton("Повторить", (dialogInterface, i) -> {
                                loadRegionWithPointsAcync();
                            }).setNegativeButton("Отмена", (dialogInterface1, i1) -> {
                        getFragmentManager().popBackStack();
                    }).show();
                }
            });
        else {
            update.run();
        }

    }


    private void updateUi() {
        if (currentRegion.getPoints().size() == 0) {
            pointsRecyclerView.setVisibility(View.GONE);
            mapView.setVisibility(View.GONE);

            hint.setVisibility(View.VISIBLE);

            hint.setText("В данном регионе нет точек продаж");
        } else {
            hint.setVisibility(View.GONE);
            switch (state) {
                case LIST:
                    mapView.setVisibility(View.GONE);
                    if (pointsRecyclerView.getAdapter() == null) {
                        adapter = new ShopPointsAdapter(
                                currentRegion.getPoints(),
                                getDashboardActivity(),
                                onShopPointClickListener
                        );
                        pointsRecyclerView.setAdapter(adapter);
                    } else {
                        adapter.setCopyOfItems(currentRegion.getPoints());
                    }
                    pointsRecyclerView.setVisibility(View.VISIBLE);
                    break;
                case MAP:
                   if (map == null) {
                       waitForMap();
                       return;
                   }
                    pointsRecyclerView.setVisibility(View.GONE);

                    MapUtils.updateMapState(onShopPointClickListenerMap::onClick, map, currentRegion.getPoints());

                    mapView.setVisibility(View.VISIBLE);
            }
        }
        loadView.setVisibility(View.GONE);
        hasUiBeenUpdated = true;
        updateIcons();
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        getDashboardActivity().invalidateOptionsMenu();

        regionButton.setText(currentRegion == null ? "Выбрать" : currentRegion.getTitle());

        if (currentRegion == null)
            getDashboardActivity().doOpenRegionsScreen();
        else if (regionsWithPoints == null)
            loadRegionWithPointsAcync();
        else if (currentRegion.getPoints() == null)
            loadPointsAcynh();
        else if (!hasUiBeenUpdated) {
            updateUi();
        }

    }

    private void waitForMap() {
        loadView.setVisibility(View.VISIBLE);
        hasUiBeenUpdated = false;
        if (mapIcon != null) {
            mapIcon.setVisible(false);
            listIcon.setVisible(false);
        }
        mapView.getMapAsync(googleMap -> {
            map = googleMap;
            updateUi();
        });
    }


    @Override
    protected void onSaveState(Bundle outState) {
        super.onSaveState(outState);
        outState.putSerializable("Region", currentRegion);
        outState.putInt("State", state.ordinal());
    }

    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        super.onRestoreState(savedInstanceState);
        currentRegion = (Region) savedInstanceState.getSerializable("Region");
        state = State.values()[(savedInstanceState.getInt("State", 0))];
    }

    @Override
    protected void onFirstTimeLaunched() {
        super.onFirstTimeLaunched();
        try {
            currentRegion = getApplication().getShopData().getCurrentRegion().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private ShopPointsAdapter.OnShopPointClickListener onShopPointClickListener = shopPoint -> {
        AlertDialog.Builder builder =
        new AlertDialog.Builder(getDashboardActivity())
                .setTitle("Выберите действие")
                .setMessage(shopPoint.getAddress())
                .setPositiveButton("Подробная информация", (dialogInterface1, i1) ->
                        FragmentUtils.replace(getActivity(), ShopPointFragment.getInstance(shopPoint), true)
                );
        shopPoint.getCoordinates().ifPresent(coordinates ->
                        builder.setNegativeButton("Показать на карте", (dialogInterface, i) -> {
                            state = State.MAP;
                            updateMap(shopPoint);
                        })
        );

        new android.os.Handler().postDelayed(builder::show, 200);
    };

    private ShopPointsAdapter.OnShopPointClickListener onShopPointClickListenerMap = shopPoint -> {
        FragmentUtils.replace(getActivity(), ShopPointFragment.getInstance(shopPoint), true);
    };

    private void updateMap(ShopPoint point) {
        mapView.getMapAsync(googleMap -> {
            map = googleMap;
            updateUi();
            MapUtils.updateMapState(onShopPointClickListenerMap::onClick, map, currentRegion.getPoints(), point);
        });

    }

    @Override
    protected String getTitle() {
        return "Магазины";
    }


    public void updateIcons() {
        if (mapIcon != null && loadView.getVisibility() != View.VISIBLE) {
            mapIcon.setVisible(state == State.LIST);
            listIcon.setVisible(state == State.MAP);

        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem.OnMenuItemClickListener stateLisnter = menuItem -> {
            changeState();
            return true;
        };

        mapIcon = menu.findItem(R.id.top_map);
        mapIcon.setVisible(false);
        mapIcon.setOnMenuItemClickListener(stateLisnter);

        listIcon = menu.findItem(R.id.top_points_list);
        listIcon.setVisible(false);
        listIcon.setOnMenuItemClickListener(stateLisnter);
        updateIcons();
    }


    private void changeState() {
        state = State.next(state);
        updateUi();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        hasUiBeenUpdated = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onBackPressed() {
        if (state == State.LIST) {
            changeState();
            return true;
        }
        return super.onBackPressed();
    }
}
