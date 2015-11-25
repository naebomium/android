package com.mobium.client.models;

import java.io.Serializable;

/**
 *  IDEA.
 *
 * Date: 20.11.11
 * Time: 21:40
 * To change this template use File | Settings | File Templates.
 */
public class Price implements Serializable {
    public static final int NONE_COST = 0;

    private int currentConst;
    private int oldCost;
    {
        currentConst = NONE_COST;
        oldCost = NONE_COST;
    }

    public int getCurrentConst() {
        return currentConst;
    }

    public Price(int currentConst) {
        this.currentConst = currentConst;
    }

    public Price(int currentConst, int oldCost) {
        this.currentConst = currentConst;
        this.oldCost = oldCost;
    }
    public void change(int cost, int oldCost) {
        this.currentConst = cost;
        this.oldCost = oldCost;
    }


    public int getOldCost() {
        return oldCost;
    }

    public void setOldCost(int oldCost) {
        this.oldCost = oldCost;
    }
}
