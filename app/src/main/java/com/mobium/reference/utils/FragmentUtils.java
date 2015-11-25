package com.mobium.reference.utils;

import android.content.Context;
import android.support.annotation.AnimRes;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.widget.Toast;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Supplier;
import com.mobium.config.prototype.INavigationBar;
import com.mobium.reference.R;
import com.mobium.reference.anotations.NeedInternetAccess;
import com.mobium.reference.fragments.BasicLoadableFragment;
import com.mobium.reference.fragments.LaunchFragment;
import com.mobium.config.common.Config;

import java.util.List;

/**
 *  on 31.07.15.
 * http://mobiumapps.com/
 */

public class FragmentUtils {

    private final static int enteranim = R.anim.enter;
    private final static int exitanim = R.anim.exit;
    private final static int popEnteranim = R.anim.pop_enter;
    private final static int popExitanim = R.anim.pop_exit;


    public static void closeMenu(Context context) {
        if (context instanceof HasMenu && ((HasMenu) context).isMenuOpen())
            ((HasMenu) context).showContent();
    }


    /**
     * нахожит объект типа T в списке
     */
    public static <T extends Fragment> Optional<T> find(Class<T> fragmentClass, List<Fragment> list) {
        Optional<T> result = Stream.of(list)
                .filter(fragment -> fragment != null)
                .filter(fragment -> fragment.getClass().equals(fragmentClass))
                .findFirst().map(value -> (T) value);

        return result;
    }

    //

    public static  <T extends Fragment>  Optional<T> findOrCreate(FragmentActivity activity, Class<T> t) {
        Optional<T> findResult = find(t, activity.getSupportFragmentManager().getFragments());
        if (findResult.isPresent())
            return findResult;
        try {
            return Optional.ofNullable(t.newInstance());
        } catch (Exception e) {
            Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
           return Optional.empty();
        }
    }


    public static <T extends Fragment> void doOpenFragmentDependsOnInternetAccess(@NonNull FragmentActivity activity,
                                                                                  @NonNull Class<T> fragmentClass,
                                                                                  boolean animated) {
        if (activity.getSupportFragmentManager()
                .findFragmentById(R.id.fragment_wrapper) == null ||
                !activity.getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_wrapper).getClass().equals(fragmentClass)) {

                Runnable replace = () -> findOrCreate(activity, fragmentClass).ifPresent(fragment ->
                    replace(activity, fragment, animated)
                );

                if (fragmentClass.isAnnotationPresent(NeedInternetAccess.class))
                    invokeIfHaveInternet(replace, activity);
                else
                    replace.run();
            } else closeMenu(activity);
        }


    public static void replace(@NonNull FragmentActivity activity,
                               @NonNull final Fragment fragment,
                               boolean isAnimated,
                               boolean addToBackStack) {

        if (fragment instanceof BasicLoadableFragment && !NetworkUtils.isOnline(activity)
                && !(fragment instanceof LaunchFragment)) {
            Dialogs.alertNotHaveInternet(activity);
            return;
        }

        FragmentManager fm = activity.getSupportFragmentManager();

        int enter = enteranim;
        int exit = exitanim;
        int popEnter = popEnteranim;
        int popExit = popExitanim;

        if (fragment instanceof HaveCustomAnimations) {
            HaveCustomAnimations animations = (HaveCustomAnimations) fragment;
            enter = animations.enter();
            exit = animations.exit();
            popEnter = animations.popEnter();
            popExit = animations.popExit();
        }

        if (activity instanceof HasMenu && ((HasMenu) activity).isMenuOpen() || !isAnimated) {
            enter = R.anim.abc_fade_in;
            exit = R.anim.abc_fade_out;
        }

        FragmentTransaction transaction =
        fm.beginTransaction()
                .setCustomAnimations(enter, exit, popEnter, popExit)
                .replace(R.id.fragment_wrapper, fragment);

        if (addToBackStack)
            transaction.addToBackStack("");

        transaction.commit();

        afterTransaction(fragment, activity);
    }


    public static void replace(@NonNull FragmentActivity activity,
                               @NonNull final Fragment fragment,
                               boolean isAnimated) {

        replace(activity, fragment, isAnimated, true);
    }

    private static void afterTransaction(Fragment fragment, FragmentActivity activity) {

        closeMenu(activity);

        KeyboardUtils.hideKeyboard(activity);

        if (activity instanceof ActionBarView && fragment instanceof TitleChanger)
            ((ActionBarView) activity).updateTitle(((TitleChanger) fragment).getTitle());
    }


    public interface HaveCustomAnimations {
        @AnimRes
        int enter();

        @AnimRes
        int exit();

        @AnimRes
        int popEnter();

        @AnimRes
        int popExit();
    }

    public interface HasMenu {
        boolean isMenuOpen();

        void showMenu();

        void showContent();
    }


    public interface HasCollapsingSearch {
        void clearSearchEdit();

        void collaps();

        void expend();

        void setSearchQuery(final String query);
    }

    public interface TitleChanger {
        String getTitle();
    }

    public interface ActionBarView {
        enum Mode {
            text, icon, icon_with_text
        }
        enum Gravity {
            center, left, right
        }

        void updateTitle(String string);
        void setMode(Mode mode);
        void setGravity(Gravity gravity);
        void updateLogo(int res);
        void setIconColor(@ColorRes int color);
        void configure(INavigationBar model);
    }

    public interface BackButtonHandler {
        boolean onBackPressed();
    }

    public static boolean isUiFragmentActive(@Nullable Fragment fragment) {
        return fragment != null && fragment.isAdded() && !fragment.isDetached() && !fragment.isRemoving();
    }

    static <T> Optional<T> invokeIfHaveInternet(Supplier<T> supplier, Context context) {
        return Functional.providedSupplier(
                () -> NetworkUtils.isOnline(context),
                supplier,
                () -> Dialogs.alertNotHaveInternet(context));
    }

    //выполнить если есть интернет
    public static void invokeIfHaveInternet(Runnable runnable, Context context) {
            Functional.providedSupplier(
                    () -> NetworkUtils.isOnline(context),
                    () -> {
                        runnable.run();
                        return null;
                    },
                    () -> Dialogs.showNetworkErrorDialog(
                            context,
                            Config.get().strings().noInternetErrorMessage(),
                            /*try repeat check of internet access on repeat button*/
                            () -> invokeIfHaveInternet(runnable, context),
                            () -> {/*do nothing on negative button*/}
                    )

            );
    }

    public static void disabledMenuItems(Menu menu) {
        menu.findItem(R.id.top_filter).setVisible(false);
        menu.findItem(R.id.top_cart).setVisible(true);
        menu.findItem(R.id.top_share).setVisible(false);
        menu.findItem(R.id.top_points_list).setVisible(false);
        menu.findItem(R.id.top_map).setVisible(false);
        menu.findItem(R.id.top_search).setVisible(false);
    }

}
