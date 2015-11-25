package com.mobium.reference.utils;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;


import com.annimon.stream.function.Supplier;
import com.mobium.client.ShopDataStorage;
import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.new_api.models.order.IOrder;
import com.mobium.reference.R;
import com.mobium.reference.ReferenceApplication;
import com.mobium.reference.activity.RequestCodes;
import com.mobium.reference.activity.ScanActivity;
import com.mobium.reference.anotations.NeedInternetAccess;
import com.mobium.reference.fragments.LaunchFragment;
import com.mobium.reference.fragments.goods.CartFragment;
import com.mobium.reference.fragments.goods.CatalogFragment_;
import com.mobium.reference.fragments.shop_info.ContactsFragment;
import com.mobium.reference.fragments.support.ContentPageFragment;
import com.mobium.reference.fragments.support.DevSettings;
import com.mobium.reference.fragments.goods.FavouritesFragment;
import com.mobium.reference.fragments.LoginFragment;
import com.mobium.reference.fragments.NewsFragment;
import com.mobium.reference.fragments.order.OrderObserveFragment;
import com.mobium.reference.fragments.order.OrdersListFragment;
import com.mobium.reference.fragments.goods.ProductFragment;
import com.mobium.reference.fragments.ProfileFragment;
import com.mobium.reference.fragments.goods.SearchFragment_;
import com.mobium.reference.fragments.shop_info.ServiceListFragment;
import com.mobium.reference.fragments.shop_info.ShopPointListFragment;
import com.mobium.reference.fragments.support.WebStaticFragment;
import com.mobium.reference.leftmenu.CategoryObserver;

import com.mobium.reference.model.ContentPageSource;
import com.mobium.reference.push_logic.PushUtils;
import com.mobium.config.common.Config;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;

/**
 *  on 31.07.15.
 * http://mobiumapps.com/
 */

public class FragmentActionHandler {

    //Получатель фрагмента и класс фрагмента
    private static class FragmentSupplier<T extends Fragment> {
        public final Supplier<? extends Fragment> supplier;
        public final Class<T> fragmentClass;

        FragmentSupplier(Supplier<? extends Fragment> supplier, Class<T> fragmentClass) {
            this.supplier = supplier;
            this.fragmentClass = fragmentClass;
        }
    }


    /**
     * Окрыть фрагмент, если есть интернет, иначе показывать ошибку
     * @param activity
     * @param supplier
     * @param <T>
     */
    private static <T extends Fragment> void navigateToFragmentDependsOnInternetAccess(final @NonNull FragmentActivity activity, @NonNull FragmentSupplier<T> supplier) {
        Runnable replaceCurrentFragment = () -> FragmentUtils.replace(activity, supplier.supplier.get(), true);
        //если для класса исполнителя екшена нужен интернет
        if (supplier.fragmentClass.isAnnotationPresent(NeedInternetAccess.class))
            //если есть интернет, выполнить, иначе сообщать об ошибке с предложением повторить попытку
            FragmentUtils.invokeIfHaveInternet(replaceCurrentFragment, activity);
        else
            replaceCurrentFragment.run();
    }

    /**
     * Ошибка регион не выбран
     * @param activity
     */
    private static void showError(FragmentActivity activity) {
        new AlertDialog.Builder(activity).
                setMessage(Config.get().strings().regionNotSelectedErrorMessage())
                .show();
    }


    /**
     * Пробовать открыть фрагмент, если выбран регион, иначе показать ошибку
     * @param supplier постащик fragment instance
     * @param classFragment  class<T>
     * @param <T> тип фрагмента
     */
    private static <T extends Fragment> void navigateToFragmentDependsOnRegion(final @NonNull FragmentActivity activity, Supplier<T> supplier, Class<T> classFragment) {
        if (!ShopDataStorage.getInstance().getCurrentRegion().isPresent())
            showError(activity);
        else
            navigateToFragmentDependsOnInternetAccess(activity, new FragmentSupplier<>(supplier, classFragment));
    }


    /**
     * Пробовать открыть фрагмент, если выбран регион, иначе показать ошибку
     * @param activity
     * @param <T> тип фрагмента
     */
    private static <T extends Fragment> void navigateToFragmentDependsOnRegion(final @NonNull FragmentActivity activity, Class<T> classFragment) {
        if (!ShopDataStorage.getInstance().getCurrentRegion().isPresent())
            showError(activity);
        else
            FragmentUtils.doOpenFragmentDependsOnInternetAccess(activity, classFragment, true);
    }
    
    public static void doAction(final @NonNull FragmentActivity activity, final @NonNull Action action) {
//        // Потребитель пары < поставщик фрагмента, класс фрагметна>
//        // надо бы оформить в виде обычной функции
//        Consumer<Pair<Supplier<? extends Fragment>, Class<? extends Fragment>>> navigateTo = fragmentAndItsClass -> {
//
//            Runnable replaceCurrentFragment = () -> FragmentUtils.replace(activity, fragmentAndItsClass.first.get());
//
//            //если для класса исполнителя екшена нужен интернет
//            if (fragmentAndItsClass.second.isAnnotationPresent(NeedInternetAccess.class))
//                //если есть интернет, выполнить, иначе сообщать об ошибке с предложением повторить попытку
//                FragmentUtils.invokeIfHaveInternet(replaceCurrentFragment, activity);
//            else
//                replaceCurrentFragment.run();
//        };


        switch (action.getType()) {
            case OPEN_START:
                openStart(activity);
                break;

            case OPEN_CATALOG:
                navigateToFragmentDependsOnRegion(activity,  () -> CatalogFragment_.getInstanceToCategory(action.getActionData()), CatalogFragment_.class);
                Events.get(activity).navigation().onPageOpen(OpenPageEvents.catalog.name());
                break;

            case OPEN_CATEGORY:
                navigateToFragmentDependsOnRegion(activity,
                        () -> CatalogFragment_.getInstanceToCategory(action.getActionData()), CatalogFragment_.class);
                break;

            case OPEN_CATEGORY_BY_ALIAS:
                navigateToFragmentDependsOnRegion(activity,
                        () -> CatalogFragment_.getInstanceToAlias(action.getActionData()), CatalogFragment_.class);
                break;


            case OPEN_PRODUCT_BY_REAL_ID:
                navigateToFragmentDependsOnRegion(activity,
                        () -> ProductFragment.getInstanceForRealId(action.getActionData()), ProductFragment.class);
                break;

            case OPEN_PRODUCT:
                navigateToFragmentDependsOnRegion(activity,
                        () -> ProductFragment.getInstance(action.getActionData()), ProductFragment.class);
                break;

            case OPEN_PAGE:
                navigateToFragmentDependsOnRegion(activity,
                        () -> WebStaticFragment.getInstance(action.getActionData(), action.getActionTitle()), WebStaticFragment.class);
                break;


            case OPEN_ARTICLE:
                navigateToFragmentDependsOnRegion(activity,
                        () -> WebStaticFragment.getInstanceToNewsItem(action.getActionData(), action.getActionTitle()), WebStaticFragment.class);
                Events.get(activity).navigation().onPageOpen(OpenPageEvents.article.name());
                break;


            case GET_NEWS_ITEM:
                navigateToFragmentDependsOnRegion(activity,
                        () -> WebStaticFragment
                        .getInstanceToNewsItem(action.getActionData(), action.getActionTitle()), WebStaticFragment.class);
                Events.get(activity).navigation().onPageOpen(OpenPageEvents.news.name());
                break;

            case OPEN_URL:
                navigateToFragmentDependsOnRegion(activity,
                        () -> WebStaticFragment.getInstanceToUrl(action.getActionData(), action.getActionTitle()), WebStaticFragment.class);
                break;


            case OPEN_ARTICLES:
                navigateToFragmentDependsOnRegion(activity,
                        () -> NewsFragment.getInstance(action.getActionData()), NewsFragment.class);
                Events.get(activity).navigation().onPageOpen(OpenPageEvents.articles.name());
                break;

            case OPEN_DEV_SETTING:
                navigateToFragmentDependsOnRegion(activity, DevSettings.class);
                break;

            case OPEN_URL_EXTERNAL:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(action.getActionData()));
                    activity.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case DO_CALL:
                PhoneUtils.doCall(activity, action.getActionData());
                Events.get(activity).connectivity().onMakeCall(action.getActionData());
                break;

            case OPEN_SEARCH:
                FragmentUtils.invokeIfHaveInternet(
                        () -> doOpenSearch(activity, action.getActionData(), null), activity);
                break;

            case OPEN_SHOPS:
                navigateToFragmentDependsOnRegion(activity, ShopPointListFragment.class);
                break;

            case OPEN_CART:
                navigateToFragmentDependsOnRegion(activity, CartFragment.class);
                break;

            case OPEN_HISTORY:
                navigateToFragmentDependsOnRegion(activity, OrdersListFragment.class);
                break;

            case OPEN_FAVOURITES:
                navigateToFragmentDependsOnRegion(activity, FavouritesFragment.class);
                break;

            case OPEN_CONTACTS:
                navigateToFragmentDependsOnRegion(activity, ContactsFragment.class);
                break;

            case OPEN_DELIVERY_INFO:
//                final Action navAction2 = ReferenceApplication.getInstance().getConfiguredAction("mobium.delivery");
//                if (navAction2 != null) {
//                    if (action.getActionTitle() == null)
//                        action.setActionTitle(activity.getString(R.string.action_contacts_title));
//                    doAction(activity, navAction2);
//                }
                break;


            case POP_CATALOG_INSIDE_MENU:
                if (activity instanceof CategoryObserver)
                    ((CategoryObserver) activity).popCategory();
                break;

            case OPEN_CATALOG_INSIDE_MENU:
                if (activity instanceof CategoryObserver)
                    ((CategoryObserver) activity).viewCategory(action.getActionData());
                break;

            case OPEN_SERVICES:
                navigateToFragmentDependsOnRegion(activity, ServiceListFragment.class);
                break;

            case OPEN_LOGIN_SCREEN:
                navigateToFragmentDependsOnRegion(activity, LoginFragment.class);
                break;

            case OPEN_PROFILE_SCREEN:
                navigateToFragmentDependsOnRegion(activity, ProfileFragment.class);
                break;

            case SEND_TEST_PUSH:
                PushUtils.sendTestPush(activity, new Action(ActionType.OPEN_CATALOG, "0"));
                break;

            case OPEN_LOADED_CONTENT_PAGE:
                ContentPageSource.tryDeseriale(ReferenceApplication.getInstance().gson, action.getActionData())
                        .ifPresent(source ->
                                        navigateToFragmentDependsOnRegion(
                                                activity,
                                                () -> ContentPageFragment.getInstance(source),
                                                ContentPageFragment.class
                                        )
                        );
                break;
            case DO_OPEN_SCANNER:
                Events.get(activity).catalog().onOpenBarcodeScanner();
                activity.startActivityForResult(new Intent()
                        .setClass(activity, ScanActivity.class), RequestCodes.SCAN_CODE);
                break;

        }


        if (activity instanceof CustomActionHandler) {
            CustomActionHandler handler = (CustomActionHandler) activity;
            if (handler.isHandling(action))
                handler.handleAction(action);
        }
    }

    private static void openStart(FragmentActivity activity) {
        if (activity.getSupportFragmentManager().getBackStackEntryCount() == 1 &&
                activity.getSupportFragmentManager().getFragments().get(0).getClass().equals(LaunchFragment.class)) {
            activity.getSupportFragmentManager().popBackStack();
        } else if (activity.getSupportFragmentManager()
                .findFragmentById(R.id.fragment_wrapper) == null ||
                !activity.getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_wrapper).getClass().equals(LaunchFragment.class)) {
            FragmentUtils.replace(activity, FragmentUtils.find(LaunchFragment.class, activity.getSupportFragmentManager().getFragments()).orElseGet(LaunchFragment::new), false, false);
        }
        FragmentUtils.closeMenu(activity);
    }

    private static void doOpenSearch(@NonNull FragmentActivity activity, @Nullable String query, @Nullable String categoryId) {
        if (query == null || query.isEmpty())
            return;

        Events.get(activity).search().onSearch(query, categoryId);

        Fragment f = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_wrapper);

        if (f instanceof SearchFragment_) {
            ((SearchFragment_) f).setSearchQuery(query, categoryId);
        } else {
            FragmentUtils.replace(activity, SearchFragment_.getInstance(query, categoryId), true);
        }
    }



    public interface CustomActionHandler {
        boolean isHandling(@NonNull final Action action);

        void handleAction(@NonNull final Action action);
    }

    public static void doOpenSuccessOrder(final @NonNull FragmentActivity activity, final IOrder order) {
        navigateToFragmentDependsOnRegion(activity, () -> OrderObserveFragment.getInstance(order), OrderObserveFragment.class);
    }
}
