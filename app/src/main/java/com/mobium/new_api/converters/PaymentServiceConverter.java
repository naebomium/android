package com.mobium.new_api.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mobium.new_api.models.order.CloudePaymant;
import com.mobium.new_api.models.order.PaymentTypeService;

import java.lang.reflect.Type;

/**
 *  on 13.10.15.
 */
public class PaymentServiceConverter implements JsonDeserializer<PaymentTypeService> {
    @Override
    public PaymentTypeService deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String id = json.getAsJsonObject().get("id").getAsString();
        switch (id) {
            case CloudePaymant.cloudpaymentsId:
                String paymentTypeId = json.getAsJsonObject().get("data").getAsJsonObject().get("publicId").getAsString();
                return new CloudePaymant(paymentTypeId);
            default:
                return new PaymentTypeService(id);
        }
    }
}
