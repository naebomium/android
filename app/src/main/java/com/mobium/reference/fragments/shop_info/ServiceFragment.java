package com.mobium.reference.fragments.shop_info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.new_api.Api;
import com.mobium.new_api.Handler;
import com.mobium.new_api.methodParameters.GetShopPointParam;
import com.mobium.new_api.models.MobiumError;
import com.mobium.new_api.models.Region;
import com.mobium.new_api.models.ShopPoint;
import com.mobium.new_api.utills.ShopPointUtils;
import com.mobium.reference.R;
import com.mobium.reference.fragments.BasicContentFragment;
import com.mobium.reference.model.ServiceModel;
import com.mobium.reference.presenter.ServicePresenter;
import com.mobium.reference.utils.BundleUtils;
import com.mobium.reference.utils.Dialogs;
import com.mobium.reference.utils.FragmentActionHandler;
import com.mobium.config.common.Config;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;
import com.mobium.reference.views.ChangeRegionUtils;
import com.mobium.reference.view.ServiceView;
import com.mobium.reference.view.ServiceViewImp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  on 24.07.15.
 * http://mobiumapps.com/
 */
public class ServiceFragment extends BasicContentFragment {
    private ServiceView screen;
    private ServiceModel model;
    private ServicePresenter presenter;


    public static ServiceFragment getInstance(List<ShopPoint> points, String title, String url, Region region) {
        if (!(points instanceof ArrayList))
            points = new ArrayList<>(points);

        ServiceFragment fragment = new ServiceFragment();
        fragment.setArguments(BundleUtils.toBundle(new Bundle(1), new ServiceModel((ArrayList<ShopPoint>) points, title, url, region)));
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_service, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        screen = new ServiceViewImp(view, presenter);
        presenter.setScreen(screen);
        presenter.showModel(model);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = BundleUtils.fromBundle(getArguments(), ServiceModel.class);
        presenter = new PresenterImpl();
    }

    @Override
    public void onStart() {
        super.onStart();
        Events.get(getActivity()).navigation().onPageOpen(OpenPageEvents.service_center.name());
    }

    private class PresenterImpl implements ServicePresenter {

        private ServiceView screen;
        private Region region;


        private void loadPointsAsync() {
            screen.showProgress(true);
            Api.getInstance().GetShopPoints(region, ShopPoint.ShopPointType.service)
                    .invoke(new Handler<GetShopPointParam, ShopPoint.ShopPointList>() {
                        @Override
                        public void onResult(ShopPoint.ShopPointList shopPointList) {
                            region.services =
                                    ShopPointUtils.buildServiceMap(shopPointList.getPoints());
                            updateUi();
                            screen.showContent(true);
                        }

                        @Override
                        public void onBadArgument(MobiumError mobiumError, GetShopPointParam getShopPointParam) {
                            Dialogs.showExitScreenDialog(getDashboardActivity(), getFragmentManager()::popBackStack);
                        }

                        @Override
                        public void onException(Exception ex) {
                            Dialogs.showNetworkErrorDialog(getDashboardActivity(),
                                    ex.getMessage(),
                                    PresenterImpl.this::loadPointsAsync,
                                    getFragmentManager()::popBackStack);
                        }
                    });
        }

        private void updateUi() {
            List<ShopPoint> services = region.services.get(model.title);
            if (services == null)
                services = Collections.emptyList();
            ServiceModel newModel = new ServiceModel(new ArrayList<>(services),model.title, model.urlIcon, region);
            showModel(newModel);
        }


        private void onRegionChanged(Region region) {
            this.region = region;
            if (region.services == null)
               loadPointsAsync();
            else
                updateUi();
        }

        @Override
        public void onPhoneClick(String phone) {
            FragmentActionHandler.doAction(getActivity(), new Action(ActionType.DO_CALL, phone));
        }

        @Override
        public void onButtonClick(View view) {
//            Dialogs.showSelectDialog(
//                    getDashboardActivity(),
//                    "Выбор Региона",
//                    ServiceListFragment.getRegions(),
//                    Region::getTitle,
//                    this::onRegionChanged);

            ChangeRegionUtils.showAutoCompleteDialog(getActivity(), new ChangeRegionUtils.DataProvider() {
                @Override
                public List<Region> regions() {
                    return ServiceListFragment.getRegions();
                }

                @Override
                public String title() {
                    return "Смена региона";
                }

                @Override
                public String message() {
                    return "Начните вводить регион";
                }
            }, this::onRegionChanged);
        }

        @Override
        public void showModel(ServiceModel model) {
            screen.setUpButton("В других городах", this::onButtonClick);
            screen.showTitle(model.title);
            screen.showPicture(model.urlIcon);
            screen.showServices(model.services);
            screen.showRegionTitle(model.region.getTitle());
        }


        public PresenterImpl setScreen(ServiceView screen) {
            this.screen = screen;
            return this;
        }
    }


    @Override
    protected String getTitle() {
        return "О сервис-центре";
    }

}
