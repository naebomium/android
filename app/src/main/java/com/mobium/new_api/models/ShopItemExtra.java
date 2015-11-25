package com.mobium.new_api.models;

import android.support.annotation.Nullable;

/**
 *  on 14.07.15.
 * http://mobiumapps.com/
 */
public class ShopItemExtra {
    private @Nullable String article;

    private @Nullable String categoryTitle;

    private @Nullable Boolean hasRelatedItems;

    private @Nullable Boolean hasOtherItems;

    private @Nullable String rating;

    public ShopItemExtra() {
    }

    public void setArticle(@Nullable String article) {
        this.article = article;
    }

    public void setCategoryTitle(@Nullable String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public void setHasRelatedItems(@Nullable Boolean hasRelatedItems) {
        this.hasRelatedItems = hasRelatedItems;
    }

    public void setHasOtherItems(@Nullable Boolean hasOtherItems) {
        this.hasOtherItems = hasOtherItems;
    }

    public void setRating(@Nullable String rating) {
        this.rating = rating;
    }

    @Nullable
    public String getArticle() {
        return article;
    }

    @Nullable
    public String getCategoryTitle() {
        return categoryTitle;
    }

    @Nullable
    public Boolean getHasRelatedItems() {
        return hasRelatedItems;
    }

    @Nullable
    public Boolean getHasOtherItems() {
        return hasOtherItems;
    }

    @Nullable
    public String getRating() {
        return rating;
    }
}
