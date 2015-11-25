package com.mobium.reference;

import android.content.SharedPreferences;

import android.os.Build;
import android.preference.PreferenceManager;

import com.annimon.stream.Optional;
import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.Gson;
import com.mobium.client.LogicUtils.MobiumLifeCycleInterface;
import com.mobium.client.ShopDataStorage;
import com.mobium.client.api.Info;
import com.mobium.client.api.ShopApiExecutor;
import com.mobium.client.api.ShopApiInterface;
import com.mobium.client.api.ShopCache;
import com.mobium.client.api.networking.ApiExecutor;
import com.mobium.client.api.networking.ExtraApiInterface;
import com.mobium.client.api.networking.NetworkingException;
import com.mobium.client.models.SuccessOrderData;
import com.mobium.client.models.ShopCategory;
import com.mobium.client.models.ShoppingCart;
import com.mobium.new_api.Api;
import com.mobium.new_api.Method;
import com.mobium.new_api.methodParameters.RegisterAppParam;
import com.mobium.new_api.models.Region;
import com.mobium.config.common.Config;
import com.mobium.reference.utils.executing.ExecutingException;
import com.mobium.reference.utils.executing.backoff.BackOffException;
import com.mobium.reference.utils.executing.backoff.BackOffExecuting;
import com.mobium.reference.utils.executing.backoff.BackOffRunnable;
import com.mobium.reference.utils.executing.backoff.ExponentialBackOff;
import com.mobium.reference.utils.storage.AppIdProvider;
import com.mobium.userProfile.ProfileApi;
import com.squareup.okhttp.OkHttpClient;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


/**
 *  IDEA.
 *
 * Date: 24.11.11
 * Time: 23:40
 */
public class ReferenceApplication extends BaseApplication implements MobiumLifeCycleInterface {
    @Inject
    public OkHttpClient okHttpClient;
    @Inject
    public Gson gson;

    private static ReferenceApplication instance;
    private static SharedPreferences prefs;


    @Override
    public void onGotInternetAccess() {
        final String getDevice = getDeviceId();
        Api.getInstance().AppStart(getDevice).invokeWithoutHandling(5, 1500);
    }

    @Override
    public void onAppPause() {
        shopData.trySave();
    }

    private ShopDataStorage shopData;
    private ShoppingCart cart;

    private HashMap<String, ShopCategory> categories = new HashMap<>();

    private ShopApiExecutor executor;
    private ShopCache shopCache;

    public static ReferenceApplication getInstance() {
        return instance;
    }

    public static SharedPreferences getPrefs() {
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(getInstance());
        }

        return prefs;
    }


    @Override
    public void onAppStart() {
        try {
            Reservoir.init(this, 1024 * 1024 * 10); //in bytes
        } catch (Exception e) {
            e.printStackTrace();
        }

        cart = new ShoppingCart(this);

        ShopDataStorage.getInstance().init(this);

        shopData = ShopDataStorage.getInstance();

        shopData.tryLoad();

        shopData.addRegionListener(newValue -> cart.clear());

        String appId = AppIdProvider.getAppId(this).orElse(null);


        Info info = new Info(getApplicationVersionName(),
                Config.get().getStaticData().mobiumBuildId(),
                "android_5",
                "android",
                "1.7",
                "d7ea9ac1-8eb0-44f8-809d-bff6944db6c7",
                isDebug()
                );

//        final ExtraApiInterface extraApiInterface =
//                new ExtraApiInterface(
//                        info,
//                        Config.get().getShopUrl(),
//                        appId);


        final ShopApiInterface apiInterface = new ShopApiInterface(
                this,
                isDebug(),
                Config.get().getStaticData().mobiumApiUrl(),
                getDeviceId(),
                getInstallationId(),
                getApplicationVersionName(),
                Config.get().getStaticData().mobiumBuildId(),
                "android_3",
                appId
        );



        executor = new ShopApiExecutor(this, new ApiExecutor(apiInterface));
        shopCache = new ShopCache(executor);

        Api.getInstance()
                .init(this,
                        "1",
                        getApplicationVersionName(),
                        Config.get().getStaticData().mobiumApiProtocol(),
                        Config.get().getStaticData().mobiumBuildId(),
                        Config.get().getStaticData().mobiumApiKey(),
                        Config.get().getStaticData().mobiumApiUrl(),
                        appId,
                        Config.get().logDebugInfo()
                );
        if (Config.get().getApplicationData().getProfileEnabled())
            ProfileApi.init(
                    appId,
                    "android",
                    Config.get().getApplicationData().getProfileBaseUrl(),
                    shopData.getProfileAccessToken(),
                    Config.get().logDebugInfo()
            );




        AppIdProvider.addListener(id -> {
            if (Config.get().getApplicationData().getProfileEnabled())
                ProfileApi.getInstance().setAppId(id);
            Api.getInstance().setAppId(id);
            apiInterface.setAppId(id);
//            extraApiInterface.setAppId(id);
        });


    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        inject(this);
        onAppStart();
    }


    private ShopCategory processCategory(ShopCategory category) {
        if (!categories.containsKey(category.id)) {
            categories.put(category.id, category);
        }
        for (ShopCategory c : category.childs) {
            if (c.childs.size() != 0) {
                processCategory(c);
            } else {
                if (!categories.containsKey(c.id)) {
                    categories.put(c.id, c);
                }
            }
        }
        return category;
    }

    public void loadCategories(ShopCategory category) throws ExecutingException {
        processCategory(getExecutor().loadCategories(category, ShopDataStorage.getRegionId()));
    }

    public ShopCategory loadCategory(String id) throws ExecutingException {
        return processCategory(getExecutor().loadCategory(id, ShopDataStorage.getRegionId()));
    }


    public void onSuccessOrder(SuccessOrderData order) {
        getCart().clear();
        shopData.addOrder(order);
    }


    public void onGcmToken(final String token) {
        new Thread() {
            @Override
            public void run() {
                new BackOffExecuting(new ExponentialBackOff()).execute(new BackOffRunnable() {
                    @Override
                    public void run() throws BackOffException {
                        try {
                            executor.doRegisterGCM(token);
                        } catch (NetworkingException e) {
                            e.printStackTrace();
                            throw new BackOffException();
                        } catch (ExecutingException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();
    }


    public ShopDataStorage getShopData() {
        return shopData;
    }


    public boolean regionNotSelect() {
        return !shopData.getCurrentRegion().isPresent();
    }

    public Optional<Region> getRegion() {
        return shopData.getCurrentRegion();
    }


    public ShoppingCart getCart() {
        return cart;
    }

    public ShopApiExecutor getExecutor() {
        return executor;
    }

    public Map<String, ShopCategory> getCategories() {
        return categories;
    }


    public ShopCache getShopCache() {
        return shopCache;
    }


    public Method<RegisterAppParam, Api.StringWrapper> RegAppAPI() {
        return Api.getInstance().RegisterApp("android", "5.0", getInstallationId(), Build.MODEL, getDeviceId(), Build.BRAND);
    }

}