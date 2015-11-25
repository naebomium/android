package com.mobium.client;

import android.content.Context;
import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.mobium.client.models.SuccessOrderData;
import com.mobium.client.models.ShopItem;
import com.mobium.new_api.models.Region;
import com.mobium.reference.utils.persistence.SingletonContextPersistence;
import com.mobium.userProfile.ResponseParams.OrderProfile;
import com.mobium.userProfile.ResponseParams.ProfileInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mobium.client.LogicUtils.OnChangeFavouriteListener;
import static com.mobium.client.LogicUtils.OnChangeOrdersListener;
import static com.mobium.client.LogicUtils.OnChangeRegionListener;
import static com.mobium.client.LogicUtils.ProfileListener;
import static com.mobium.reference.utils.Functional.Receiver;
import static com.mobium.reference.utils.Functional.equalsMapPredicate;

/**
 *  on 11.05.15.
 * http://mobiumapps.com/
 *
 *
 * HashSet<Region> regions - сохраняется в Serializable виде,
 * список регионов  обновляется при запуске приложения
 *
 * HashMap<String, HashSet<ShopItem>> - сохраняется в Serializable виде
 * {(regionID, {favoriteItem}}

 */
//todo:: обновление фаворитов

public class ShopDataStorage extends SingletonContextPersistence implements OnChangeRegionListener,
                                                                   Receiver<List<Region>>
{

    private static volatile ShopDataStorage instance;

    public static ShopDataStorage getInstance() {
        ShopDataStorage localInstance = instance;
        if (localInstance == null) {
            synchronized (ShopDataStorage.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ShopDataStorage();
                }
            }
        }
        return localInstance;
    }
    
    /** список регионов: сохраняется в Serializable виде, обновляется при запуске приложения*/
    private List<Region> regions = new ArrayList<>();

    private HashSet<ShopItem> favourites = new HashSet<>();

    /** сет заказов */
    private Set<SuccessOrderData> orders = new HashSet<>();
    private List<OrderProfile> profileOrderProfiles = new ArrayList<>();

    private final transient Set<OnChangeRegionListener> regionListeners = new HashSet<>();
    private final transient Set<OnChangeFavouriteListener> favouriteListeners = new HashSet<>();
    private final transient Set<OnChangeOrdersListener> ordersListeners = new HashSet<>();
    private final transient Set<ProfileListener> profileListeners = new HashSet<>();

    private boolean googlePlacesEnabled = false;

    private boolean isReferrerSent;

    private Region currentRegion;

    private String profileAccessToken;

    private ProfileInfo profileInfo;

    public ProfileInfo getProfileInfo() {
        return profileInfo;
    }

    public void setProfileInfo(ProfileInfo profileInfo) {
        this.profileInfo = profileInfo;
    }

    /**
     * Попытка восстановить данные с устройства:
     * если избранных в данном регионе нет, добавить пустой сет израбнных
     * @return удачная загрузка
     */
    @Override
    public boolean tryLoad() {
        boolean result = super.tryLoad();
        return result;
    }

    @Override
    public boolean trySave() {
        boolean result;
        result = super.trySave();
        return result;
    }

    private ShopDataStorage(Context context) {
        super(context);
    }

    /**
     * изменен регион:
     * изменить текущий регоин на новый;
     * очистить favouriteListeners;
     * оповестить regionListeners;
     * @param newValue - новый текущий регион
     */
    @Override
    public void onChange(Region newValue) {
        synchronized (this) {
            currentRegion = newValue;
            Stream.of(regionListeners).forEach(listener -> listener.onChange(newValue));
        }
    }


    public synchronized void onChangeOrders(List<SuccessOrderData> ordersFromServer) {
        Stream.of(ordersListeners).forEach(listener -> listener.onChange(orders, profileOrderProfiles));
    }


    /**
     * Получены список регионов с сервера:
     * изменить список регионов;
     * проверить, есть ли в списке регион, эквивалентный текущему;
     * если такого региона нет, попробовать найти регион, имеющий один id с текущим
     * если и такого региона нет, изменить текущий регион на первый из списка.
     * @param data список новых регионов
     */
    @Override
    public void onReceive(List<Region> data) {
        synchronized (this) {
                regions.clear();
                regions.addAll(data);

                //обновление текущего региона
                if(currentRegion == null)
                    return;

                //регион, соответствующий текущему
                Optional<Region> newCurrentRegion =
                        Stream.of(data)
                                .filter(r -> r.equals(currentRegion))
                                .findFirst();

                //если такого региона нет
                if (!newCurrentRegion.isPresent()) {
                    //регион, имеющий id текущего
                    Optional<Region> newRegionById =
                            Stream.of(data)
                                    .filter(equalsMapPredicate(currentRegion.getId(), Region::getId))
                                    .findFirst();
                    if (newRegionById.isPresent())
                        onChange(newRegionById.get());
                    else
                        currentRegion = null;
                }
        }
    }

    public synchronized void addFavouriteListener(OnChangeFavouriteListener listener) {
        favouriteListeners.add(listener);
    }

    public synchronized void removeFavouriteListener(OnChangeFavouriteListener listener) {
        favouriteListeners.remove(listener);
    }

    private synchronized Set<ShopItem> getCurrentFavourites() {
        return favourites;
    }

    public synchronized List<ShopItem> getFavourites() {
        return Stream.of(getCurrentFavourites())
                .sorted((item, t1) -> item.cost.getCurrentConst() - t1.cost.getCurrentConst())
                .collect(Collectors.toList());
    }

    public synchronized List<Region> getRegions() {
        return regions;
    }

    public synchronized Optional<Region> getCurrentRegion() {
        return Optional.ofNullable(currentRegion);
    }

    /**
     * сделать ShopItem shopItem избранным товаром
     * @param item товар
     */
    public synchronized void makeFavourite(@NonNull ShopItem item) {
        Set<ShopItem> f = getCurrentFavourites();

        if (!f.contains(item)) {
            f.add(item);
            for (OnChangeFavouriteListener l : favouriteListeners)
                l.onChange(f);
        }
    }

    public synchronized void removeFavourite(ShopItem item) {
        Set<ShopItem> f = getCurrentFavourites();
        if (f.contains(item)) {
            f.remove(item);
            for (OnChangeFavouriteListener l : favouriteListeners)
                l.onChange(f);
        }
    }

    public synchronized boolean isFavourite(ShopItem item) {
        return getCurrentFavourites().contains(item);
    }

    public synchronized void addOrder(@NonNull SuccessOrderData orderData) {
        orders.add(orderData);
        Stream.of(ordersListeners).forEach(listener -> listener.onChange(orders, profileOrderProfiles));
    }

    public synchronized void addOrder(@NonNull OrderProfile orderProfileData) {
        profileOrderProfiles.add(orderProfileData);
        Stream.of(ordersListeners).forEach(listener -> listener.onChange(orders, profileOrderProfiles));
    }

    public synchronized void setProfileOrderProfiles(@NonNull List<OrderProfile> orderProfileData) {
        profileOrderProfiles.clear();
        profileOrderProfiles.addAll(orderProfileData);
        Stream.of(ordersListeners).forEach(listener -> listener.onChange(orders, profileOrderProfiles));
    }



    public synchronized void removeRegionListner(OnChangeRegionListener listener) {
        if (regionListeners.contains(listener))
            regionListeners.remove(listener);
    }

    public synchronized void addRegionListener(OnChangeRegionListener listener) {
        synchronized (this) {
            if (!regionListeners.contains(listener))
                regionListeners.add(listener);
        }
    }

    public synchronized void setReferrerSent(boolean isReferrerSent) {
        this.isReferrerSent = isReferrerSent;
    }

    public synchronized boolean isReferrerSent() {
        return isReferrerSent;
    }

    public @NonNull  Set<SuccessOrderData> getOrders() {
        return orders;
    }


    private ShopDataStorage() {
        super();
    }

    public String getProfileAccessToken() {
        return profileAccessToken;
    }

    public void setProfileAccessToken(String profileAccessToken) {
        this.profileAccessToken = profileAccessToken;
        Stream.of(profileListeners).forEach(listener -> listener.onLogin(profileAccessToken));
    }

    public void clearProfileToken() {
        profileAccessToken = null;
        setProfileOrderProfiles(Collections.<OrderProfile>emptyList());
        Stream.of(profileListeners).forEach(ProfileListener::onExit);
    }

    public void addProfileListener(ProfileListener listener) {
        profileListeners.add(listener);
    }

    public void removeProfileListener(ProfileListener listener) {
        profileListeners.remove(listener);
    }


    public void addOrderListener(OnChangeOrdersListener listener) {
        ordersListeners.add(listener);
    }

    public void removeOrderListener(OnChangeOrdersListener listener) {
        ordersListeners.remove(listener);
    }

    public List<OrderProfile> getProfileOrderProfiles() {
        return profileOrderProfiles;
    }

    public static String getRegionId() {
        return getInstance().getCurrentRegion().map(Region::getId).orElse("");
    }

    public boolean isGooglePlacesEnabled() {
        return googlePlacesEnabled;
    }

    public void setGooglePlacesEnabled(boolean googlePlacesEnabled) {
        this.googlePlacesEnabled = googlePlacesEnabled;
    }

    private static ArrayList<Region> getDefaultRegions() {
        ArrayList<Region> defaultRegions = new ArrayList<>(1);
        defaultRegions.add(Region.getRussiaRegion());

        return defaultRegions;
    }

    public boolean hasCustomRegions() {
        return regions != null && regions.size() > 0 && !regions.equals(getDefaultRegions());
    }

    public void setDefaultRegionList() {
        onReceive(getDefaultRegions());
    }
}
