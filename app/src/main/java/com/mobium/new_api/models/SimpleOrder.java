package com.mobium.new_api.models;

import com.annimon.stream.Optional;
import com.mobium.new_api.models.order.IOrder;

import java.util.Map;

/**
 *  on 23.10.2015.
 */
public class SimpleOrder implements IOrder {
    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setDate(Optional<Long> date) {
        this.date = date;
    }

    public void setUserData(Map<String, String> userData) {
        this.userData = userData;
    }

    private String status;
    private String id;
    private int cost;
    private int count;
    private Optional<Long> date;
    private Map<String, String> userData;



    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Optional<Long> getDate() {
        return date;
    }

    @Override
    public Map<String, String> getUserData() {
        return userData;
    }
}
