package com.mobium.new_api.models.order;

import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 *  on 13.10.15.
 */
public enum PaymentType implements Serializable {
    cash, online;

    public static @Nullable PaymentType nullablePaymentType(String str) {
        try {
            return valueOf(str);
        } catch (Exception e) {
            return null;
        }
    }

    public int toInt() {
        int result = 0;
        switch (this) {
            case cash:
                result = 0;
                break;
            case online:
                result = 1;
                break;
        }
        return result;
    }

    @Override
    public String toString() {
        String result = "";
        switch (this) {
            case cash:
                result = "Наличными";
                break;
            case online:
                result = "Онлайн";
                break;
        }
        return result;
    }
}
