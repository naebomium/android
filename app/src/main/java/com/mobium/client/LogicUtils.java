package com.mobium.client;

import com.mobium.client.models.CartItem;
import com.mobium.client.models.SuccessOrderData;
import com.mobium.client.models.ShopItem;
import com.mobium.reference.utils.Functional.ChangeListener;
import com.mobium.reference.utils.executing.ExecutingException;
import com.mobium.new_api.models.Region;
import com.mobium.userProfile.ResponseParams.OrderProfile;

import java.util.List;
import java.util.Set;

/**
 *  on 11.05.15.
 * http://mobiumapps.com/
 */
public class LogicUtils {
    public interface MobiumLifeCycleInterface {

        /**
         * Попытка загрузки сохраненных данных,
         * инициализация служб
         */
        void onAppStart();

        /**
         * Получение доступа в интернет
         */
        void onGotInternetAccess() throws ExecutingException;

        /**
         * Сохранение данных
         */
        void onAppPause();
    }

    public interface OnChangeRegionListener
            extends ChangeListener<Region> {
    }

    public interface OnChangeFavouriteListener
            extends ChangeListener<Set<ShopItem>> {
    }
    public interface OnChangeCartListener
            extends ChangeListener<Set<CartItem>> {
    }
    public interface OnChangeOrdersListener {
        void onChange(Set<SuccessOrderData> newValue, List<OrderProfile> profile);
    }

    public interface ProfileListener {
        void onLogin(String accessToken);
        void onExit();
    }
}
