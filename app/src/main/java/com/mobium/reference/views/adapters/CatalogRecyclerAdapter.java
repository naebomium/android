package com.mobium.reference.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobium.client.models.ShopCategory;
import com.mobium.client.models.ShopItem;
import com.mobium.reference.R;
import com.mobium.reference.utils.ImageUtils;
import com.mobium.reference.utils.ShareUtils;
import com.mobium.reference.view.IRatingBar;
import com.mobium.reference.views.Ui_configurator;
import com.mobium.reference.views.WebImageView;

import java.util.ArrayList;
import java.util.List;

/**
 *  on 06.07.15.
 * http://mobiumapps.com/
 */
public class CatalogRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_CATEGORY = 1;
    public static final int TYPE_CATEGORY_SALE = 2;
    public static final int TYPE_PRODUCT = 3;
    public static final int TYPE_PROGRESS = 4;

    private final int topMargin4dp;
    private final int topMargin16dp;

    private List<ShopCategory> categoryList = new ArrayList<>();
    private List<ShopItem> productList = new ArrayList<>();

    private boolean isFavourites = false;

    private boolean isLoad;
    private boolean showCategoryAsCard;


    public boolean isShowCategoryAsCard() {
        return showCategoryAsCard;
    }

    public CatalogRecyclerAdapter setShowCategoryAsCard(boolean showCategoryAsCard) {
        this.showCategoryAsCard = showCategoryAsCard;
        notifyDataSetChanged();
        return this;
    }

    public void setIsLoad(boolean isLoad) {
        this.isLoad = isLoad;
        notifyDataSetChanged();
    }

    public CatalogRecyclerAdapter(Context context) {
        topMargin4dp = ImageUtils.convertToPx(context, 4);
        topMargin16dp = ImageUtils.convertToPx(context, 16);
    }


    @Override
    public int getItemViewType(int position) {
        //if loading is enable and current position is last then return progress
        if (isLoad && position == getItemCount() - 1)
            return TYPE_PROGRESS;

        //if current position is category return type depends on view mode
        if (position < categoryList.size())
            return showCategoryAsCard ? TYPE_CATEGORY_SALE : TYPE_CATEGORY;

        //else return product view category
        return TYPE_PRODUCT;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    private Object getItem(int position) {
        //progress
        if (isLoad && position == getItemCount() - 1)
            return new Object();

        //category
        if (position < categoryList.size())
            return categoryList.get(position);

        //shopItem
        return productList.get(position - categoryList.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        switch (type) {
            case TYPE_PROGRESS:
                return new ProgressViewHolder(createProgressBar(viewGroup));
            case TYPE_CATEGORY:
                return new CategoryViewHolder(inflater.inflate(R.layout.item_shop_category_singe_line, viewGroup, false));
            case TYPE_CATEGORY_SALE:
                return new CategorySaleViewHolder(inflater.inflate(R.layout.item_shop_category_sale_card, viewGroup, false));
            case TYPE_PRODUCT:
                return new ProductViewHolder(inflater.inflate(R.layout.item_shop_product_large_card, viewGroup, false));
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder abstractViewHolder, final int position) {
        int type = getItemViewType(position);

        Ui_configurator ui_configurator = Ui_configurator.getInstance(abstractViewHolder.itemView.getContext());
        ShopCategory category;
        switch (type) {
            case TYPE_CATEGORY:
                category = (ShopCategory) getItem(position);
                CategoryViewHolder categoryViewHolder = (CategoryViewHolder) abstractViewHolder;
                ui_configurator.configureOnShopItemClicks(categoryViewHolder.itemView, category);

                ui_configurator.configureCategoryItem(category, categoryViewHolder.title, null);

                categoryViewHolder.icon.setVisibility(View.GONE);

                if (position == categoryList.size() - 1)
                    categoryViewHolder.deliver.setVisibility(View.INVISIBLE);
                else
                    categoryViewHolder.deliver.setVisibility(View.VISIBLE);

                break;

            case TYPE_CATEGORY_SALE:
                final CategorySaleViewHolder saleItemHolder = (CategorySaleViewHolder) abstractViewHolder;
                saleItemHolder.saleMarketingLabel.setVisibility(View.VISIBLE);
                category = (ShopCategory) getItem(position);

                ui_configurator.configureCategoryItem(
                        category, saleItemHolder.title, saleItemHolder.icon
                );

                ui_configurator.configureOnShopItemClicks(saleItemHolder.itemView, category);
                ui_configurator.configureOnShopItemClicks(saleItemHolder.saleViewButton, category);

                if(saleItemHolder.cartButtons != null)
                    saleItemHolder.cartButtons.setVisibility(View.GONE);

                saleItemHolder.saleShareButton.setOnClickListener(v -> ShareUtils.shareCategory(v.getContext(), category));

                break;

            case TYPE_PRODUCT:
                ShopItem item = (ShopItem) getItem(position);
                final ProductViewHolder shopItemHolder = (ProductViewHolder) abstractViewHolder;
                ui_configurator.configureShopItemInfo(
                        item,
                        shopItemHolder.icon,
                        shopItemHolder.title,
                        shopItemHolder.cost,
                        shopItemHolder.oldCost,
                        shopItemHolder.ratingBar,
                        shopItemHolder.buyButton
                );

                ui_configurator.configureShopItemButtons(
                        item,
                        shopItemHolder.shareButton,
                        shopItemHolder.doFavoriteButton,
                        shopItemHolder.addToFavoriteButton
                );

                shopItemHolder.marketingLabel.setVisibility(item.getMarketing().isPresent() ? View.VISIBLE : View.GONE);

                ui_configurator.configureOnShopItemClicks(shopItemHolder.itemView, item);

                break;
            case TYPE_PROGRESS:
                break;
            default:
                throw new IllegalArgumentException();
        }


        ViewGroup.LayoutParams p = abstractViewHolder.itemView.getLayoutParams();

        if (p instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginParam = ((ViewGroup.MarginLayoutParams) p);
            marginParam.topMargin = position == 0 ? topMargin4dp : 0;
            if (productList != null && productList.size() > 0)
                marginParam.bottomMargin = position == categoryList.size() - 1 ? topMargin16dp : 0;
        }
    }

    public abstract static class AbstractCatalogItemViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public volatile WebImageView icon;
        public View clickableView;

        public AbstractCatalogItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class CategoryViewHolder extends AbstractCatalogItemViewHolder {
        View deliver;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_shop_category_title);
            icon = (WebImageView) itemView.findViewById(R.id.item_shop_category_icon);
            deliver = itemView.findViewById(R.id.item_shop_category_deliver);
            clickableView = itemView;
        }
    }

    // progress bar
    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }

    // sale
    public class CategorySaleViewHolder extends AbstractCatalogItemViewHolder {
        View saleViewButton;
        View saleDoFavoriteButton;
        TextView addToFavoriteButton;
        View cartButtons;
        View saleShareButton;
        View saleMarketingLabel;


        public CategorySaleViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.sale_title);
            icon = (WebImageView) itemView.findViewById(R.id.saleIcon);
            saleViewButton = itemView.findViewById(R.id.category_view_btn);

            saleDoFavoriteButton = itemView.findViewById(R.id.favourite_btn);
            addToFavoriteButton = (TextView) itemView.findViewById(R.id.addToFavoriteText);
            saleShareButton = itemView.findViewById(R.id.share_btn);
            saleMarketingLabel = itemView.findViewById(R.id.sale_marketing_label);

            saleShareButton = itemView.findViewById(R.id.share_btn);
            saleMarketingLabel = itemView.findViewById(R.id.sale_marketing_label);

            clickableView = itemView;
        }
    }

    // product
    public class ProductViewHolder extends AbstractCatalogItemViewHolder {
        TextView cost;
        TextView oldCost;
        IRatingBar ratingBar;
        TextView buyButton;
        View doFavoriteButton;
        TextView addToFavoriteButton;
        View shareButton;
        View marketingLabel;

        public ProductViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            cost = (TextView) itemView.findViewById(R.id.cost);
            oldCost = (TextView) itemView.findViewById(R.id.oldCost);
            ratingBar = (IRatingBar) itemView.findViewById(R.id.rating);
            buyButton = (TextView) itemView.findViewById(R.id.sale_to_cart_btn);

            icon = (WebImageView) itemView.findViewById(R.id.icon);
            doFavoriteButton = itemView.findViewById(R.id.favourite_btn);
            addToFavoriteButton = (TextView) itemView.findViewById(R.id.addToFavoriteText);
            shareButton = itemView.findViewById(R.id.share_btn);
            marketingLabel = itemView.findViewById(R.id.marketing_label);
            clickableView = itemView;
        }

    }

    @Override
    public int getItemCount() {
        return categoryList.size() + productList.size() + (isLoad ? 1 : 0);
    }

    private View createProgressBar(ViewGroup viewGroup) {
        ProgressBar progressBar = new ProgressBar(viewGroup.getContext());
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressBar.setBackgroundColor(viewGroup.getContext().getResources().getColor(R.color.white_background));
        progressBar.setPadding(0, topMargin16dp, 0, topMargin16dp);
        progressBar.setLayoutParams(params);
        return progressBar;
    }


    public void addCategories(List<ShopCategory> categories) {
        if (categories != null)
            categoryList.addAll(categories);
        notifyDataSetChanged();
    }

    public void addShopItems(List<ShopItem> items) {
        if (items != null)
            productList.addAll(items);
        notifyDataSetChanged();
    }

    public void setCategories(List<ShopCategory> categories) {
        categoryList = new ArrayList<>();
        addCategories(categories);
    }

    public void setShopItems(List<ShopItem> items) {
        productList = new ArrayList<>();
        addShopItems(items);
    }

}
