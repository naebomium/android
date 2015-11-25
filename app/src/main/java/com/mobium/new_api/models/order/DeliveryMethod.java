package com.mobium.new_api.models.order;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.io.Serializable;
import java.util.List;

/**
 *  on 13.10.15.
 */
public class DeliveryMethod implements Serializable {
    private boolean isPickup;
    private int deliveryCost;
    private int totalCost;
    private String id;
    private String payment;
    private String title;
    private Field[] fields;

    public void setIsPickUp(boolean isPickUp) {
        this.isPickup = isPickUp;
    }

    public void setDeliveryCost(int deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public List<PaymentType> getPaymentTypes() {
        return Stream.of(payment.split(","))
                .map(PaymentType::nullablePaymentType)
                .filter(p -> p != null)
                .collect(Collectors.toList());
    }

    public boolean isPickup() {
        return isPickup;
    }

    public int getDeliveryCost() {
        return deliveryCost;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public String getId() {
        return id;
    }

    public Field[] getFields() {
        return fields;
    }

    public DeliveryMethod setFields(Field[] fields) {
        this.fields = fields;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public DeliveryMethod setTitle(String title) {
        this.title = title;
        return this;
    }
}
