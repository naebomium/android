package com.mobium.reference.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.TextView;

import com.mobium.client.LogicUtils;
import com.mobium.client.models.Action;
import com.mobium.client.models.ActionType;
import com.mobium.client.models.CartItem;
import com.mobium.client.models.ShopCategory;
import com.mobium.client.models.ShopItem;
import com.mobium.client.models.ShoppingCart;
import com.mobium.reference.R;
import com.mobium.reference.ReferenceApplication;
import com.mobium.reference.fragments.goods.CartFragment;
import com.mobium.reference.fragments.goods.FavouritesFragment;
import com.mobium.reference.fragments.goods.PreCartFragment;
import com.mobium.config.common.ConfigUtils;
import com.mobium.reference.utils.FragmentActionHandler;
import com.mobium.reference.utils.FragmentUtils;
import com.mobium.reference.utils.ShareUtils;
import com.mobium.config.common.Config;
import com.mobium.reference.utils.statistics_new.Events;
import com.mobium.reference.utils.statistics_new.OpenPageEvents;
import com.mobium.reference.view.IRatingBar;
import com.mobium.reference.views.adapters.CatalogRecyclerAdapter;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;

import java.util.Set;


/**
 *  on 15.04.15.
 * http://mobiumapps.com/
 */
public class Ui_configurator {
    private volatile static Ui_configurator instance;

    public static Ui_configurator getInstance(Context context) {
        Ui_configurator localInstance = instance;
        if (localInstance == null)
            synchronized (Ui_configurator.class) {
                localInstance = instance;
                if (localInstance == null)
                    instance = localInstance = new Ui_configurator(context);
            }
        return localInstance;
    }

    private  final View.OnClickListener addToCardClickListener;
    private  final View.OnClickListener favoriteClickListener;
    private  final View.OnClickListener shareClickListener;
    private  final View.OnClickListener shopItemsClickListener;
    private  final CatalogRecyclerAdapter catalogAdapter;
    private  final ReferenceApplication application;

    private Ui_configurator(Context context) {
        addToCardClickListener = new AddToCardItemClickListener();
        favoriteClickListener  = new FavoriteClickListener();
        shareClickListener     = new ShareButtonClickListener();
        shopItemsClickListener = new OpenCatalogClickListener();


        catalogAdapter = new CatalogRecyclerAdapter(context);

        application = ReferenceApplication.getInstance();

        WebImageView.setErrorImage(getErrorImage(context));
    }

    public CatalogRecyclerAdapter getCatalogAdapter() {
        return catalogAdapter;
    }

    public void configureCategoryItem(final ShopCategory shopCategory, TextView title, WebImageView icon) {
        if (shopCategory == null)
            return;
        if (title != null)
            title.setText(shopCategory.title);
        if (icon != null) {
            icon.setUrl(shopCategory.icon.getUrl("sd"));
            icon.setVisibility(View.VISIBLE);
        }

    }

    public void configureShopItemButtons(
            final ShopItem shopItem,
            View shareButton,
            View doFavouriteButton,
            TextView doFavouriteText
    ) {
        configureShopItemCard(shopItem, null, null, null, null, null, null, doFavouriteText, doFavouriteButton, shareButton);
    }

    public void configureShopItemInfo(
            @NonNull final ShopItem shopItem,
            @Nullable WebImageView icon,
            @Nullable TextView title,
            @Nullable TextView cost,
            @Nullable TextView oldCost,
            @Nullable IRatingBar ratingBar,
            @Nullable TextView buyButton
    ) {
        configureShopItemCard(shopItem, icon, title, cost, oldCost, ratingBar, buyButton, null, null, null);
    }


    public static boolean inCart(String productId) {
        return ReferenceApplication
                .getInstance()
                .getCart()
                .getItem(productId)
                .isPresent();
    }
    public void configureShopItemCard(
            @NonNull final ShopItem shopItem,
            @Nullable WebImageView icon,
            @Nullable TextView title,
            @Nullable TextView cost,
            @Nullable TextView oldCost,
            @Nullable IRatingBar ratingBar,
            @Nullable TextView buyButton,

            final TextView doFavoriteText,
            View doFavouriteButton,
            View shareButton
    ) {

//        assert shopItem != null;
//        assert activity != null;

        final boolean showPrices = Config.get().getApplicationData().getShowPrices();

        if (cost != null) {
            if (showPrices) {
                cost.setText(ConfigUtils.formatCurrency(shopItem.cost.getCurrentConst()));
                cost.setVisibility(View.VISIBLE);
            } else {
                cost.setVisibility(View.INVISIBLE);
            }
        }

        if (oldCost != null) {
            if (showPrices && shopItem.isSale()) {
                oldCost.setVisibility(View.VISIBLE);
                oldCost.setText(ConfigUtils.formatCurrency(shopItem.cost.getOldCost()));
                oldCost.setPaintFlags(oldCost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                oldCost.setVisibility(View.INVISIBLE);
            }
        }

        if (ratingBar != null) {
            if (shopItem.getRating().isPresent()) {
                ratingBar.setValue(shopItem.getRating().get() / 25);
            } else {
                ratingBar.setInvisible();
            }
        }

        if (buyButton != null) {
            if (showPrices) {

                buyButton.setText(inCart(shopItem.id) ?
                    "В корзине" : "В корзину");

                buyButton.setTag(R.id.TAGShopItem, shopItem);
                buyButton.setOnClickListener(addToCardClickListener);
            } else {
                buyButton.setVisibility(View.INVISIBLE);
            }
        }

        if (doFavouriteButton != null) {
            // assert favoriteClickListener != null;

            doFavouriteButton.setTag(R.id.TAGFavourite, shopItem);
            doFavouriteButton.setOnClickListener(favoriteClickListener);

            if (doFavoriteText != null) {

                doFavoriteText.setOnClickListener(favoriteClickListener);
                doFavoriteText.setTag(R.id.TAGFavourite, shopItem);
                doFavouriteButton.setOnClickListener(v -> doFavoriteText.performLongClick());

                boolean isFavourite = application.getShopData().isFavourite(shopItem);
                doFavoriteText.setText(
                        isFavourite ?
                                doFavoriteText.getContext().getString(R.string.remove_from_favourite) :
                                doFavoriteText.getContext().getString(R.string.add_to_favourite)
                );
            }

        }
        if (shareButton != null) {
            shareButton.setOnClickListener(shareClickListener);
            shareButton.setTag(R.id.TAGShare, shopItem);
        }
        if (icon != null) {
            if (shopItem.getIcon().isPresent()) {
                icon.setUrl(shopItem.getIcon().get().getUrl("sd"));
                icon.setMemoryPolicy(MemoryPolicy.NO_CACHE);
                icon.setNetworkPolicy(NetworkPolicy.NO_CACHE);
            } else {
                icon.setImageDrawable(Ui_configurator.getErrorImage(icon.getContext()));
            }
        }
        if (title != null) {
            title.setText(shopItem.title);
        }
    }

    public void configureOnShopItemClicks(View view, Object productOrCategory) {
        ((OpenCatalogClickListener)this.shopItemsClickListener).configureOnShopItemClicks(view, productOrCategory);
    }

    private static final class FavoriteClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Object object = view.getTag(R.id.TAGFavourite);
            if (object == null)
                return;
            ReferenceApplication application = ReferenceApplication.getInstance();
            ShopItem shopItem = (ShopItem) object;
            Activity activity = (Activity) view.getContext();

            if (application.getShopData().isFavourite(shopItem)) {
                application.getShopData().removeFavourite(shopItem);
                Events.get(activity).favourites().onFavoritesRemove(shopItem);
            }
            else {
                application.getShopData().makeFavourite(shopItem);
                Events.get(activity).favourites().onFavoritesAdd(shopItem);
            }

            if (view instanceof TextView) {
                final boolean isFavourite = application.getShopData().isFavourite(shopItem);
                ((TextView) view).setText(
                        isFavourite ?
                                view.getContext().getString(R.string.remove_from_favourite) :
                                view.getContext().getString(R.string.add_to_favourite)
                );
            }


        }
    }

    private static final class AddToCardItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Object object = view.getTag(R.id.TAGShopItem);
            if (object == null)
                return;

            final FragmentActivity activity = (FragmentActivity) view.getContext();
            final ShopItem shopItem = (ShopItem) object;
            final ShoppingCart cart = ReferenceApplication.getInstance().getCart();

            boolean inCart = inCart(shopItem.id);


            if (!inCart) {
                cart.addItem(shopItem);
                Events.get(activity).cart().onAddToCart(new CartItem(shopItem, cart.getItemCount(shopItem.id)));
                Events.get(activity).navigation().onPageOpen(OpenPageEvents.item_added.name());
            }


            Fragment current = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_wrapper);

            if (current != null && !current.getClass().equals(CartFragment.class)) {
                if (!inCart)
                    cart.getItem(shopItem.id)
                            .map(PreCartFragment::getInstance)
                            .ifPresent(fragment ->
                                            FragmentUtils.replace(activity, fragment, true, true)
                            );
                else
                    FragmentActionHandler.doAction(activity, new Action(ActionType.OPEN_CART, null));
            }


        }
    }

    private static final class ShareButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Object object = view.getTag(R.id.TAGShare);
            if (object == null)
                return;

            ShopItem shopItem = (ShopItem) object;
            ShareUtils.shareProduct(view.getContext(), shopItem);
        }
    }

    private static final class OpenCatalogClickListener implements View.OnClickListener {
        public void configureOnShopItemClicks(View view, Object productOrCategory){
            if (view == null || productOrCategory == null)
                return;
            if (productOrCategory instanceof ShopItem)
                view.setTag(R.id.TAGShopItem, ((ShopItem)productOrCategory).id);
            else if (productOrCategory instanceof ShopCategory)
                view.setTag(R.id.TagShopCategory, ((ShopCategory)productOrCategory).id);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Object object = view.getTag(R.id.TagShopCategory);

            if (object != null) {
                FragmentActionHandler.doAction((FragmentActivity) view.getContext(), new Action(ActionType.OPEN_CATEGORY, (String) object));
            } else {
                object = view.getTag(R.id.TAGShopItem);
                if (object != null) {
                    String id = (String) object;
                    //проверка источника клика
                    ShopItem item = ReferenceApplication.getInstance().getShopCache().fetchItem(id);
                    if (item != null && ReferenceApplication.getInstance().getShopData().isFavourite(item))
                        if (((FragmentActivity) view.getContext()).getSupportFragmentManager().findFragmentById(R.id.fragment_wrapper) instanceof FavouritesFragment)
                            Events.get((Activity) view.getContext()).favourites().onOpenFromFavourites(item);
                    FragmentActionHandler.doAction((FragmentActivity) view.getContext(), new Action(ActionType.OPEN_PRODUCT, id));
                }
            }
        }
    }



    public static Drawable getLoadingImage(Context context) {
        return ResourcesCompat.getDrawable(context.getResources(), R.drawable.logo, null);
    }

    public static Drawable getErrorImage(Context context) {
        return ResourcesCompat.getDrawable(context.getResources(), R.drawable.logo, null);
    }


}
