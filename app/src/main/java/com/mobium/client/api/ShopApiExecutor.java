package com.mobium.client.api;

import android.content.Context;


import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.mobium.client.api.networking.ApiExecutor;
import com.mobium.client.api.networking.ApiInterface;
import com.mobium.client.api.networking.NetworkingException;
import com.mobium.client.models.Action;
import com.mobium.client.models.CartItem;
import com.mobium.client.models.CharacteristicItem;
import com.mobium.client.models.CharacteristicsGroup;
import com.mobium.client.models.DeliverFormField;
import com.mobium.client.models.DeliveryFormSelectField;
import com.mobium.client.models.Extralable;
import com.mobium.client.models.Marketing;
import com.mobium.client.models.modifications.Modification;
import com.mobium.client.models.NewsRecord;
import com.mobium.client.models.Opinion;
import com.mobium.client.models.Opinions;
import com.mobium.client.models.Price;
import com.mobium.client.models.SearchResult;
import com.mobium.client.models.SelectItem;
import com.mobium.client.models.ShopCategory;
import com.mobium.client.models.ShopItem;
import com.mobium.client.models.ShoppingCart;
import com.mobium.client.models.Sorting;
import com.mobium.client.models.filters.FilteringFlag;
import com.mobium.client.models.filters.FilteringRange;
import com.mobium.client.models.filters.FilteringSingle;

import com.mobium.client.models.resources.Graphics;
import com.mobium.client.models.resources.MapGraphics;
import com.mobium.client.models.resources.UrlGraphics;
import com.mobium.new_api.models.Region;
import com.mobium.new_api.models.ShopPoint;
import com.mobium.reference.R;
import com.mobium.reference.utils.executing.ExecutingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import static com.mobium.reference.utils.Functional.createOptional;
import static com.mobium.reference.utils.Functional.optionalJsonArray;
import static com.mobium.reference.utils.Functional.optionalStream;
import static com.mobium.reference.utils.Functional.parseJSON;
import static com.mobium.reference.utils.Functional.parseNullableJSONArray;


/**
 *
 *
 * Date: 27.11.12
 * Time: 20:54
 */
public class ShopApiExecutor {
    public static final String LOAD_CATEGORY_ITEMS = "multishop_load_category_items";
    public static final String HAS_MORE_SUBCATEGORIES = "multishop_has_more_subcategories";

    private static final String ROOT_CATEGORY = "0";

    private ApiExecutor executor;
    private Context context;




    public ShopApiExecutor(Context context, ApiExecutor executor) {
        this.executor = executor;
        this.context = context;
    }

    private JSONArray createOrderItems(ShoppingCart cart) throws JSONException {
        JSONArray res = new JSONArray();
        CartItem[] items = cart.getItems();
        for (CartItem item : items) {
            res.put(new JSONObject()
                    .put("id", item.shopItem.getId())
                    .put("count", item.count));
        }
        return res;
    }


    private Extralable loadExtras(Extralable extralable, JSONObject object) {
        createOptional(object::getJSONObject, "extra")
                .ifPresent(
                        extra ->
                                Stream.of(extra.keys()).forEach(
                                        (String key) -> {
                                            try {
                                                Object value = extra.get(key);
                                                if (value instanceof String) {
                                                    extralable.putExtra(key, (String) value);
                                                } else if (value instanceof Integer) {
                                                    extralable.putExtra(key, (Integer) value);
                                                } else if (extra.get(key) instanceof Boolean) {
                                                    extralable.putExtra(key, (Boolean) value);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                )
                );
        return extralable;
    }

    private void checkError(JSONObject response) throws ExecutingException {
        try {
            JSONObject resp = response.getJSONObject("response");
            if (resp.has("error")) {
                String description = context.getString(R.string.error_internal_server_error);
                boolean mayRepeat = true;
                if (resp.has("description")) {
                    description = resp.getString("description");
                    if (description.trim().equals("")) {
                        description = context.getString(R.string.error_internal_server_error);
                    }
                }

                if (resp.has("mayRetry")) {
                    mayRepeat = resp.getBoolean("mayRetry");
                }

                throw new ExecutingException(description, mayRepeat);
            }
        } catch (JSONException e) {
            // Ignore
            e.printStackTrace();
        }
    }

    private Graphics decodeResource(JSONObject object) throws JSONException {
        String type = object.getString("type");
        String def = object.getString("default");

        Graphics res = new UrlGraphics(def);

        switch (type) {
            case "map":
                HashMap<String, String> urls = new HashMap<>();

                if (object.has("url_map")) {
                    JSONObject urlMap = object.getJSONObject("url_map");
                    Iterator keys = urlMap.keys();
                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        urls.put(key, urlMap.getString(key));
                    }
                }

                res = new MapGraphics(urls, def);
                break;
            case "raw_url":
                res = new UrlGraphics(def);
                break;
            case "null":
                res = new UrlGraphics("null");
                break;
        }

        return res;
    }

    private ShopItem decodeItem(JSONObject itm) throws JSONException {
        Graphics icon = Graphics.EMPTY;

        if (itm.get("icon") != null) {
            icon = decodeResource(itm.getJSONObject("icon"));
        }


        HashMap<String, Serializable> sorting = new HashMap<>();
        HashMap<String, Serializable> filterings = new HashMap<>();

        if (itm.getInt("cost") > 1000) {
            filterings.put("more_1000", true);
        }

        if (itm.getInt("cost") > 2000) {
            filterings.put("more_2000", true);
        }


        if (itm.get("sortings") != null) {
            JSONObject sorts = (JSONObject) itm.get("sortings");
            Iterator keys = sorts.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                sorting.put(key, (Serializable) sorts.get(key));
            }
        }


        parseNullableJSONArray(
                itm::getJSONArray,
                "filterings",
                jsonObject -> jsonObject,
                (JSONObject filter) -> {
                    try {
                        String filterId = filter.getString("id");
                        switch (filter.getString("type")) {
                            case "range":
                                Double value = filter.getDouble("value");
                                filterings.put(filterId, value);
                                break;
                            case "single":
                                if (filter.getBoolean("value"))
                                    filterings.put(filterId, true);
                                break;
                            case "flags":
                                JSONArray values = filter.getJSONArray("values");
                                for (int j = 0; j < values.length(); j++) {
                                    String key = values.getString(j);
                                    filterings.put(key, true);
                                }
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        );


        ShopItem res = new ShopItem(
                itm.getString("id"),
                itm.getString("title"),
                icon,
                new Price(itm.optInt("cost")),
                sorting,
                filterings);

        if (itm.has("description"))
            res.setDescription(itm.getString("description"));

        optionalStream(
                    optionalJsonArray(itm::getJSONArray, "media"),
                    object -> new ShopItem.Media(
                        object.getString("type"),
                        object.getString("url")
                    )
        ).ifPresent(mediaStream ->
             res.setMedia(
                     mediaStream.collect(
                             Collectors.toList()
                     )
             )
        );


        if (itm.has("extra")) {
            ShopItem.DetailsInfo detailsInfo = new ShopItem.DetailsInfo();
            JSONObject extra = itm.getJSONObject("extra");


            if (extra.has("rating")) {
                res.setRating(extra.optInt("rating", 0));
            }

            if (extra.has("hasRelatedItems"))
                detailsInfo.hasRelatedItems = Boolean.parseBoolean(extra.getString("hasRelatedItems"));
            if (extra.has("hasOtherItems"))
                detailsInfo.hasOtherItems = Boolean.parseBoolean(extra.getString("hasOtherItems"));
            if (extra.has("categoryId"))
                detailsInfo.categoryId = extra.getString("categoryId");
            if (extra.has("categoryTitle"))
                detailsInfo.categoryTitle = extra.getString("categoryTitle");
            
            if (extra.has("shortDescription"))
                detailsInfo.shortDescription = extra.getString("shortDescription");
            if (extra.has("description2"))
                detailsInfo.description2 = extra.getString("description2");
            if (extra.has("vendor"))
                detailsInfo.vendor = extra.getString("vendor");
            if (extra.has("typePrefix"))
                detailsInfo.typePrefix = extra.getString("typePrefix");
            if (extra.has("model"))
                detailsInfo.model = extra.getString("model");
            if (extra.has("article"))
                detailsInfo.realId = extra.getString("article");
            if (extra.has("warranty"))
                detailsInfo.warranty = extra.getString("warranty");
            res.detailsInfo = detailsInfo;
        }

        if (itm.has("marketing") && itm.get("marketing") instanceof JSONArray) {
            try {
                res.setMarketing(Marketing.deserialize(itm.getJSONArray("marketing")));
                res.applyMarketing();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (itm.has("modifications") && itm.get("modifications") instanceof JSONArray) {
            try {
                res.setModifications(Modification.deserialize(itm.getJSONArray("modifications")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        optionalStream(
                optionalJsonArray(itm::getJSONArray, "images"),
                this::decodeResource
        ).ifPresent(mediaStream ->
                 res.setImages(mediaStream.collect(Collectors.toList()))
        );

        try {
            if (itm.has("characteristics")) {
                JSONArray groups = itm.getJSONObject("characteristics").getJSONArray("groups");
                CharacteristicsGroup[] groupItems = new CharacteristicsGroup[groups.length()];
                for (int i = 0; i < groups.length(); i++) {
                    JSONObject group = groups.getJSONObject(i);
                    String title = group.optString("title", null);
                    JSONArray rawItems = group.getJSONArray("items");
                    CharacteristicItem[] items = new CharacteristicItem[rawItems.length()];
                    for (int j = 0; j < items.length; j++) {
                        JSONObject ch = rawItems.getJSONObject(j);
                        items[j] = new CharacteristicItem(ch.getString("keyTitle"), ch.getString("valueTitle"));
                        loadExtras(items[j], ch);
                    }

                    groupItems[i] = new CharacteristicsGroup(title, items);
                }

                res.setCharacteristics(groupItems);
            }
        } catch (Exception e) {

        }


        return res;
    }




    private ShopCategory decodeCategory(JSONObject categoryItem, boolean isSearch) throws JSONException {
        Graphics iconResource = Graphics.EMPTY;

        if (categoryItem.get("icon") != null) {
            iconResource = decodeResource(categoryItem.getJSONObject("icon"));
        }

        ShopCategory category = new ShopCategory(
                categoryItem.getString("id"),
                categoryItem.getString("title"),
                iconResource);

        loadExtras(category, categoryItem);

        if (!isSearch) {
            if (categoryItem.has("sorts")) {
                JSONArray array = (JSONArray) categoryItem.get("sorts");
                for (int s = 0; s < array.length(); s++) {
                    JSONObject object = (JSONObject) array.get(s);
                    String id = object.getString("id");
                    String type = object.getString("sortingType");
                    String direction = object.getString("direction");
                    String title = object.getString("title");
                    if (type.equals("number")) {
                        category.sortings.add(new Sorting(id, title, Sorting.Type.NUMBER));
                    } else if (type.equals("string")) {
                        category.sortings.add(new Sorting(id, title, Sorting.Type.STRING));
                    }
                }
            }

            if (categoryItem.has("filterings")) {
                JSONArray filterings = categoryItem.getJSONArray("filterings");
                for (int i = 0; i < filterings.length(); i++) {
                    JSONObject filter = filterings.getJSONObject(i);
                    String filterId = filter.getString("id");
                    boolean isCollapsed = false;

                    if (filter.getString("type").equals("range")) {
                        String title = filter.getString("title");
                        Double min = filter.getDouble("min");
                        Double max = filter.getDouble("max");
                        Double step = filter.getDouble("step");
                        String suffix = filter.optString("suffix", "");
                        if (filter.has("extra") && filter.getJSONObject("extra").has("isCollapsed")) {
                            isCollapsed = filter.getJSONObject("extra").getBoolean("isCollapsed");
                        }
                        category.filterings.add(new FilteringRange(filterId, title, min, max, step, suffix, isCollapsed));
                    }

                    if (filter.getString("type").equals("flags")) {
                        LinkedHashMap<String, String> flags = new LinkedHashMap<>();
                        JSONArray flagsJson = filter.getJSONArray("flags");

                        for (int j = 0; j < flagsJson.length(); j++) {
                            JSONObject flagJson = flagsJson.getJSONObject(j);
                            flags.put(flagJson.getString("id"), flagJson.getString("title"));
                        }

                        if (filter.has("extra") && filter.getJSONObject("extra").has("isCollapsed")) {
                            isCollapsed = filter.getJSONObject("extra").getBoolean("isCollapsed");
                        }

                        if (filter.getString("type").equals("flags")) {
                            category.filterings.add(new FilteringFlag(filterId, filter.getString("title"), flags, isCollapsed));
                        }
                    }

                    if (filter.getString("type").equals("single")) {
                        String title = filter.getString("title");
                        category.filterings.add(new FilteringSingle(filterId, title));
                    }
                }
            }
        }

        return category;
    }

    public ShopItem[] getItemsById(List<String> items_id) throws ExecutingException {
        JSONObject response;

        JSONArray Ides = new JSONArray();
        for (String id : items_id)
            Ides.put(id);
        try {
            response = DoApiRequest("GetItemsById", new JSONObject().put("Ides", Ides));
        } catch (NetworkingException | JSONException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        }
        checkError(response);
        return decodeItems(response);
    }

    public ShopItem[] getItemsByRealId(List<String> items_id, String regionId) throws ExecutingException {
        JSONObject response;

        JSONArray RealIdes = new JSONArray();
        for (String id : items_id)
            RealIdes.put(id);
        try {
            response = DoApiRequest("GetItemsByRealId", new JSONObject()
                            .put("RealIdes", RealIdes)
                            .put(ApiInterface.EXTRA_REGION_ID, regionId)
            );
        } catch (NetworkingException | JSONException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        }
        checkError(response);
        return decodeItems(response);
    }



    public ShopItem[] loadItems(ShopCategory category, int offset, int limit, String regionId) throws ExecutingException {
        JSONObject response = null;
        try {
            response = DoApiRequest("GetItems", "plain_id", category.id, "#!offset", offset + "", "#!limit", limit + "", ApiInterface.EXTRA_REGION_ID, regionId);
        } catch (NetworkingException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        }
        try {
            category.totalCount = response.getJSONObject("response").getInt("totalCount");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return decodeItems(response);
    }

    public ShopItem[] loadItems(ShopCategory category, String regionId) throws ExecutingException {
        JSONObject response = null;
        try {
            response = DoApiRequest("GetItems", "plain_id", category.id, ApiInterface.EXTRA_REGION_ID, regionId);
        } catch (NetworkingException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        }

        try {
            category.totalCount = response.getJSONObject("response").getInt("totalCount");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return decodeItems(response);
    }

    private ShopItem[] decodeItems(JSONObject response) throws ExecutingException {
        try {

            checkError(response);
            JSONArray itemsArray = response.getJSONObject("response").getJSONArray("items");
            ShopItem items[] = new ShopItem[itemsArray.length()];
            for (int i = 0; i < itemsArray.length(); i++)
                items[i] = decodeItem((JSONObject) itemsArray.get(i));

            return items;
        } catch (ExecutingException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExecutingException(context.getString(R.string.error_internal_server_error));
        }
    }


    public String getCategoryIdByAlias(String alias) throws ExecutingException {
        JSONObject response = null;
        try {
            response = DoApiRequest("GetCategoryIdByAlias", "plain_id", alias);
        } catch (NetworkingException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        }

        try {
            return response.get("response").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            throw new ExecutingException(context.getString(R.string.error_internal_server_error));
        }
    }

    public ShopCategory loadCategories(ShopCategory root, String regionId) throws ExecutingException {
        return loadCategories(root, root.id, regionId);
    }

    public ShopCategory loadCategories(ShopCategory root, String rootId, String regionId) throws ExecutingException {
        JSONObject response = null;
        try {
            response = DoApiRequest("GetCategories", "plain_id", rootId, ShopApiInterface.EXTRA_REGION_ID, regionId);
        } catch (NetworkingException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        }

        if (response == null)
            throw new ExecutingException(context.getString(R.string.error_connection));

        try {
            checkError(response);
            JSONObject categories = response.getJSONObject("response");
            JSONArray list = categories.getJSONArray("categories");

            HashMap<String, ShopCategory> categoryMap = new HashMap<>();

            for (int i = 0; i < list.length(); i++) {
                JSONObject categoryItem = (JSONObject) list.get(i);

                ShopCategory category = decodeCategory(categoryItem, false);

                if (category.id.equals(rootId)) {
                    root.sortings = category.sortings;
                    root.filterings = category.filterings;
                    root.icon = category.icon;
                    root.title = category.title;
                    root.setExtraParams(category.getExtraParams());
                }

                String parentId = categoryItem.getString("parentId");
                if (!parentId.equals(root.id)) {
                    if (categoryMap.containsKey(parentId)) {
                        categoryMap.get(parentId).childs.add(category);
                    }
                } else {
                    root.childs.add(category);
                }

                categoryMap.put(category.id, category);
            }

            /*if (response.getJSONObject("extra").has("categoryTitle")) {
                root.title = response.getJSONObject("extra").getString("categoryTitle");
            }*/

            return root;
        } catch (ExecutingException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExecutingException(context.getString(R.string.error_internal_server_error));
        }
    }


    public ShopCategory loadCategory(String id, String regionId) throws ExecutingException {
        ShopCategory root = new ShopCategory(id, "Каталог", new UrlGraphics(""));
        root.putExtra(LOAD_CATEGORY_ITEMS, false);
        return loadCategories(root, ROOT_CATEGORY, regionId);
    }

    public ShopItem loadItem(String itemId, String region_id) throws ExecutingException {
        JSONObject response = null;
        try {
            response = DoApiRequest("GetItem", "plain_id", itemId, ApiInterface.EXTRA_REGION_ID, region_id);
        } catch (NetworkingException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        }

        try {
            checkError(response);
            JSONObject answerItem = response.getJSONObject("response");
            ShopItem shopItem =  decodeItem(answerItem);
            loadDetails(shopItem, region_id);
            return shopItem;
        } catch (ExecutingException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExecutingException(context.getString(R.string.error_internal_server_error));
        }
    }

    public SearchResult search(String query, String categoryId) throws ExecutingException {
        JSONObject response;
        try {
            response = DoApiRequest("Search", "plain_id", query, "#!extra_categoryId", categoryId);
        } catch (NetworkingException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        }

        return getSearchResult(response);
    }

    public SearchResult search(String query) throws ExecutingException {
        JSONObject response;
        try {
            response = DoApiRequest("Search", "plain_id", query);
        } catch (NetworkingException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        }

        return getSearchResult(response);
    }

    public SearchResult getSearchResult(JSONObject response) throws ExecutingException {
        try {
             JSONObject searchResult = response.getJSONObject("response");

            JSONArray categoriesList = searchResult.getJSONArray("categories");
            ShopCategory[] categories = new ShopCategory[categoriesList.length()];
            for (int i = 0; i < categories.length; i++) {
                categories[i] = decodeCategory(categoriesList.getJSONObject(i), true);
            }
            JSONArray list = searchResult.getJSONArray("items");
            ShopItem[] items = new ShopItem[list.length()];
            for (int i = 0; i < items.length; i++) {
                items[i] = decodeItem(list.getJSONObject(i));
            }

            return new SearchResult(items, categories);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new ExecutingException(context.getString(R.string.error_internal_server_error));
        }
    }

    private DeliverFormField createField(JSONObject object) throws JSONException {
        String type = object.getString("type");
        if (type.equals("text")) {
            return (DeliverFormField) loadExtras(new DeliverFormField(object.getString("id"),
                    object.getString("title"), DeliverFormField.FieldType.TEXT, object.getBoolean("isRequired")), object);
        }
        if (type.equals("label")) {
            return (DeliverFormField) loadExtras(new DeliverFormField(object.getString("id"),
                    object.getString("title"), DeliverFormField.FieldType.LABEL, object.getBoolean("isRequired")), object);
        }
        if (type.equals("select")) {
            return (DeliverFormField) loadExtras(
                    new DeliveryFormSelectField(object.getString("id"),
                            object.getString("title"), DeliverFormField.FieldType.SELECT, object.getBoolean("isRequired"),
                            createSelectItems(object.getJSONArray("items"))),
                    object);
        }
        return null;
    }

    private SelectItem[] createSelectItems(JSONArray jsonItems) {
        ArrayList<SelectItem> selectItems = new ArrayList<>();
        for (int i = 0; i < jsonItems.length(); i++) {
            try {
                JSONObject shop = jsonItems.getJSONObject(i);
                String id = shop.getString("id");
                String title = shop.getString("title");
                SelectItem selectItem = new SelectItem(id, title);
                selectItems.add(selectItem);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return selectItems.toArray(new SelectItem[selectItems.size()]);
    }

    public void doRegisterGCM(String tokenId) throws NetworkingException, ExecutingException {
        DoApiRequest("RegisterPushToken", "service", "GCM", "token", tokenId);
    }



    public String getHtml(String key) throws ExecutingException {
        JSONObject response = null;
        try {
            response = DoApiRequest("GetHtml", "key", key);
            checkError(response);
            return response.getJSONObject("response").getString("value");
        } catch (NetworkingException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        } catch (JSONException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        }
    }

    public String getNewsRecord(String key) throws ExecutingException {
        JSONObject response = null;
        try {
            response = DoApiRequest("GetNewsItem", "plain_id", key);
            checkError(response);
            return response.getJSONObject("response").getString("content");
        } catch (NetworkingException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        } catch (JSONException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        }
    }

    public JSONObject customRequest(String key, String... args) throws ExecutingException {
        JSONObject response = null;
        try {
            JSONObject jArgs = new JSONObject();
            jArgs.put("request", key);
            for (int i = 0; i < args.length / 2; i++) {
                jArgs.put(args[i * 2], args[i * 2 + 1]);
            }
            response = DoApiRequest("CustomRequest", jArgs);
            checkError(response);
            return response;
        } catch (NetworkingException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        } catch (JSONException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        }
    }

    public JSONObject customRequest(String key, JSONObject args) throws ExecutingException {
        JSONObject response = null;
        try {
            JSONObject jArgs = new JSONObject();
            jArgs.put("request", key);
            jArgs.put("param", args);
            response = DoApiRequest("CustomRequest", jArgs);
            checkError(response);
            return response;
        } catch (NetworkingException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        } catch (JSONException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        }
    }

    public NewsRecord[] getNews() throws ExecutingException {
        JSONArray response = null;
        try {
            response = DoApiRequest("GetNewsList").getJSONObject("response").getJSONArray("items");
        } catch (NetworkingException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        } catch (JSONException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        }

        ArrayList<NewsRecord> res = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject item = response.getJSONObject(i);
                int id = item.getInt("id");
                String contentId = item.getString("id");
                if (!item.getString("contentData").equals("null")) {
                    contentId = item.getString("contentData");
                }

                Action action = new Action(item.getString("contentType"), contentId);
                action.setActionTitle(item.getString("title"));
                NewsRecord record = new NewsRecord(
                        id,
                        item.getString("title"),
                        action,
                        decodeResource(item.getJSONObject("icon")),
                        item.getString("description"),
                        item.getInt("date"));
                loadExtras(record, item);

                res.add(record);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return res.toArray(new NewsRecord[res.size()]);
    }

    public ShopItem[] loadRelatedItems(String productId, String region_id) throws ExecutingException {
        try {
            return decodeItems(DoApiRequest("GetRelatedItems", "plain_id", productId, ApiInterface.EXTRA_REGION_ID, region_id));
        } catch (NetworkingException e) {
            e.printStackTrace();
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        }
    }

    public ShopItem[] loadOtherItems(String productId, String region_id) throws ExecutingException {
        JSONArray itemsArray;
        try {
            itemsArray = DoApiRequest("GetOtherItems", "plain_id", productId, ApiInterface.EXTRA_REGION_ID, region_id).getJSONObject("response").getJSONArray("items");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new ExecutingException(context.getString(R.string.error_internal_server_error), e);
        } catch (NetworkingException e) {
            e.printStackTrace();
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        }

        return getItems(itemsArray);
    }



    public ShopItem[] getItems(JSONArray itemsArray) throws ExecutingException {
        try {
            ArrayList<ShopItem> relatedItems = new ArrayList<>();

            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject itm = (JSONObject) itemsArray.get(i);
                relatedItems.add(decodeItem(itm));
            }
            return relatedItems.toArray(new ShopItem[relatedItems.size()]);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExecutingException(context.getString(R.string.error_internal_server_error));
        }
    }



    private void loadDetails(ShopItem item, String regionId) throws NetworkingException {
        JSONObject response = null;
        ShopItem[] items;

        if (item.hasOtherItems()) {
            try {
                items = loadOtherItems(item.getId(), regionId);

                if (items != null && items.length > 0)
                    item.detailsInfo.otherItems = items;
                else
                    item.detailsInfo.hasOtherItems = false;

            } catch (ExecutingException e) {
                e.printStackTrace();
                item.detailsInfo.hasOtherItems = false;
            }
        }
        if (item.hasRelatedItems()) {
            try {
                items = loadRelatedItems(item.getId(), regionId);

                if (items != null && items.length > 0)
                    item.detailsInfo.relatedItems = items;
                else
                    item.detailsInfo.hasRelatedItems = false;
            } catch (ExecutingException e) {
                e.printStackTrace();
                item.detailsInfo.hasRelatedItems = false;
            }
        }
    }


    public boolean sendReferrer(String referrer) throws ExecutingException {
        boolean isSend = true;
        try {
            DoApiRequest("GetOtherItems", "referrer", referrer);
        } catch (NetworkingException e) {
            e.printStackTrace();
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        }

        return isSend;
    }

    private JSONObject DoApiRequest(String method, String... args) throws NetworkingException, ExecutingException {
        String[] processed = new String[2 * (args.length / 2) + 2];
        System.arraycopy(args, 0, processed, 0, 2 * (args.length / 2));
        processed[processed.length - 2] = "#!guid";
        processed[processed.length - 1] = UUID.randomUUID().toString();
        return executor.DoApiRequest(method, processed);
    }

    private JSONObject DoApiRequest(String method, JSONObject args) throws NetworkingException, ExecutingException {
        try {
            args.put("#!guid", UUID.randomUUID().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return executor.DoApiRequest(method, args);
    }




    // ORDER API _________________________________________________________________________


    public ShopItem[] loadDiscounts() throws ExecutingException {
        JSONObject response = null;
        try {
            response = DoApiRequest("GetMarketingItems", "types", "LOWER_PRICE");
        } catch (NetworkingException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        }
        ShopItem[] result = decodeItems(response);

        return result;
    }

    public ShopCategory[] loadPopularCategories(String regionId) throws ExecutingException {
            JSONObject response = null;
            try {
                response = DoApiRequest("GetPopularCategories", ShopApiInterface.EXTRA_REGION_ID, regionId);
            } catch (NetworkingException e) {
                throw new ExecutingException(context.getString(R.string.error_connection), e);
            }

            if (response == null)
                throw new ExecutingException(context.getString(R.string.error_connection));
            try {
                checkError(response);
                JSONArray list = response.getJSONObject("response"). getJSONArray("categories");

                ShopCategory[] result = new ShopCategory[list.length()];
                for (int i = 0; i < list.length(); i++) {
                    JSONObject categoryItem = (JSONObject) list.get(i);
                    result[i] = decodeCategory(categoryItem, false);
                }
                return result;
            } catch (ExecutingException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                throw new ExecutingException(context.getString(R.string.error_internal_server_error));
            }
    }


    public Opinions loadOpinions(String objId, String objType, int limit, int offset) throws ExecutingException {
        JSONObject responseBody = new JSONObject();
        int count = 0;
        try {
            JSONObject respons = DoApiRequest("GetOpinions", "objId", objId, "objType", objType, "limit", String.valueOf(limit), "offset", String.valueOf(offset));
            checkError(respons);
            responseBody = respons.getJSONObject("response");
            count = responseBody.getInt("count");
        } catch (NetworkingException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject finalResponseBody = responseBody;
        List<Opinion> opinions =  parseJSON(
                () -> finalResponseBody.getJSONArray("opinions"),
                Opinion.deserialize
        ).collect(Collectors.toList());


        return count > 0 ? new Opinions(count, opinions) : null;
    }

    public ShopItem getItemByRealId(String realId, String region_id) throws ExecutingException {
        JSONObject response = null;
        try {
            response = DoApiRequest("GetItemByRealId", "plain_id", realId, ShopApiInterface.EXTRA_REGION_ID, region_id);
        } catch (NetworkingException e) {
            throw new ExecutingException(context.getString(R.string.error_connection), e);
        }

        try {
            checkError(response);
            JSONObject answerItem = response.getJSONObject("response");
            ShopItem shopItem =  decodeItem(answerItem);
            loadDetails(shopItem, region_id);
            return shopItem;
        } catch (ExecutingException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExecutingException(context.getString(R.string.error_internal_server_error));
        }
    }
}