package com.mobium.config.block_models;

import com.mobium.client.models.ShopCategory;
import com.mobium.client.models.ShopItem;

/**
 *  on 13.10.15.
 * abstract classes for resource of Result data
 */
public class DataResource<Result> {
    public final MethodName methodName;
    
    private DataResource(MethodName methodName) {
        this.methodName = methodName;
    }

    enum MethodName {
        getItemsById,
        getItemsByRealId,
        getCategoryById,
        getCategoryByRealId,
        getCategories,
        getCategoriesByRealId,
        getPopularCategories,
        getMarketingItems,
        getMarketingCategories
    }

    /**
     * Method with @param <Arg> returning @param <Result>
     */
    private static class ArgMethod<Result, Arg> extends DataResource<Result> {
        public final Arg arg;
        public ArgMethod(MethodName methodName, Arg arg) {
            super(methodName);
            this.arg = arg;
        }
    }

    /**
     * Method with listArg param returning @param <Result>
     */
    private static class DataFromListId<Result> extends ArgMethod<Result, String[]> {
        public DataFromListId(MethodName methodName, String[] id) {
            super(methodName, id);
        }
    }

    /**
     * Method with single id param returning @param <Result>
     */
    public static class DataFromId<Result> extends ArgMethod<Result, String> {
        public DataFromId(MethodName methodName, String id) {
            super(methodName, id);
        }
    }
    

    public final static class GetItemsById extends DataFromListId<ShopItem> {
        public GetItemsById(String[] id) {
            super(MethodName.getItemsById, id);
        }
    }

    public final static class GetItemsByRealId extends DataFromListId<ShopItem> {
        public GetItemsByRealId(String[] ids) {
            super(MethodName.getItemsByRealId, ids);
        }
    }

    public final static class GetCategoryById extends DataFromId<ShopItem[]> {
        public GetCategoryById(String id) {
            super(MethodName.getCategoryById, id);
        }
    }

    public final static class GetCategoryByRealId extends DataFromId<ShopItem[]> {
        public GetCategoryByRealId(String id) {
            super(MethodName.getCategoryByRealId, id);
        }
    }

    public final static class GetCategorisById extends DataFromListId<ShopCategory> {
        public GetCategorisById(String[] ids) {
            super(MethodName.getCategories, ids);
        }
    }

    public final static class GetCategoriesByRealId extends DataFromListId<ShopCategory> {
        public GetCategoriesByRealId(String[] ids) {
            super(MethodName.getCategoriesByRealId, ids);
        }
    }

    public final static class GetPopularCategories extends DataResource<ShopCategory> {
        public GetPopularCategories() {
            super(MethodName.getPopularCategories);
        }
    }

    public final static class GetMarketingCategories extends DataResource<ShopCategory[]> {
        public GetMarketingCategories() {
            super(MethodName.getMarketingCategories);
        }
    }

    public final static class GetMarketingItems extends DataResource<ShopItem[]> {
        public GetMarketingItems() {
            super(MethodName.getMarketingItems);
        }
    }

}
