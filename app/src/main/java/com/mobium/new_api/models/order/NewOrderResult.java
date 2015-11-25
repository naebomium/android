package com.mobium.new_api.models.order;


import com.mobium.new_api.models.ResponseBase;

import java.io.Serializable;

/**
 *  on 13.10.15.
 */
public class NewOrderResult extends ResponseBase implements Serializable {
    private String num;
    private String phone;
    private int serverCost;

    public NewOrderResult() {
    }

    public String getNum() {
        return num;
    }

    public NewOrderResult setNum(String num) {
        this.num = num;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public NewOrderResult setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public int getServerCost() {
        return serverCost;
    }

    public NewOrderResult setServerCost(int serverCost) {
        this.serverCost = serverCost;
        return this;
    }

    public boolean isSuccess() {
        return getNum() != null && !getNum().isEmpty();
    }
}
