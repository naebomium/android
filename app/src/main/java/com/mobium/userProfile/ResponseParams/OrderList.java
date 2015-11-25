package com.mobium.userProfile.ResponseParams;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *  on 16.07.15.
 * http://mobiumapps.com/
 */
public class OrderList {
    @SerializedName("orders")
    public  List<OrderProfile> orderProfiles;
    public  String errorMessage;

    public OrderList(List<OrderProfile> orderProfiles) {
        this.orderProfiles = orderProfiles;
        errorMessage = null;
    }

    public OrderList(String errorMessage) {
        this.errorMessage = errorMessage;
        orderProfiles = null;
    }

    public OrderList() {
    }

    public List<OrderProfile> getOrderProfiles() {
        return orderProfiles;
    }

    public void setOrderProfiles(List<OrderProfile> orderProfiles) {
        this.orderProfiles = orderProfiles;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
