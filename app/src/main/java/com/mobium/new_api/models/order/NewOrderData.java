package com.mobium.new_api.models.order;

import com.annimon.stream.Stream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 *  on 16.05.15.
 * http://mobiumapps.com/
 * класс, описывающий информацию о предстоящем заказе
 * покупаемые товары и их количества, введенные контанстные данные, id региона и стоимость заказа
 */
public class NewOrderData {
    public List<OrderItem> orderItems;
    public final HashMap<String, String> userInputData;
    private static final int CASH = 0;
    private static final int CARD = 1;


    private String regionId;
    private int cost;
    private final static boolean IS_FAKE = false;

    public NewOrderData(String regionId, HashMap<String, String> userInputData, List<OrderItem> items, int cost) {
        this.regionId = regionId;
        this.userInputData = userInputData;
        this.orderItems = items;
        this.cost = cost;
        userInputData.put("region_id", regionId);
    }

    public JSONObject toJsonObject() throws JSONException {
        JSONObject userInputDataJson = new JSONObject();


        Stream.of(userInputData.keySet())
                .forEach(key -> {
                    try {
                        userInputDataJson.put(key, userInputData.get(key));
                    } catch (JSONException exception) {
                        exception.printStackTrace();
                    }
                });
        JSONArray itemsJson = new JSONArray();


        Stream.of(orderItems)
                .forEach(orderItem -> {
                    JSONObject item = new JSONObject();
                    try {
                        item.put("id", orderItem.itemId);
                        item.put("count", orderItem.count);
                        if (orderItem.modifications != null && !orderItem.modifications.isEmpty()) {
                            JSONObject mod = new JSONObject();
                            Stream.of(orderItem.modifications.keySet()).forEach(
                                    key -> {
                                        try {
                                            mod.put(key, orderItem.modifications.get(key));
                                        }
                                        catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                            );
                            item.put("modifications", mod);
                        }
                        itemsJson.put(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
        } );

        return new JSONObject()
                .put("items", itemsJson)
                .put("price", cost)
                .put("orderInfo", userInputDataJson)
                .put("fake", IS_FAKE)
                .put("regionId", regionId)
                .put("deliveryType", "post")
                .put("paymentType", CASH);
    }
}
