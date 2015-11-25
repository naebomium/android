package com.mobium.userProfile.ResponseParams;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.List;

/**
 *  on 11.06.15.
 * http://mobiumapps.com/
 */

public class ProfileInfo implements Serializable{
    public String errorMessage;
    public String email;
    public String deliveryType;
    public String cityId;
    public String cardNumber;
    public String phone;
    public String name;
    public ProfileInfo.Type type;
    public FavouriteShopPoints favorites;

    public Bonus bonus;


    //хз
    public String address;



    public static class Bonus {
        public int value;
        public String format;

        public boolean used() {
            return value != -1;
        }

        public String getRedable() {
            return format.replace("%s", String.valueOf(value));
        }
    }

    public enum Type {
        natural, legal
    }

    public ProfileInfo() {
    }

    public static class FavouriteShopPoints {
        public final List<Object> points;
        public String errorMessage;
        public FavouriteShopPoints(List<Object> points) {
            this.points = points;
        }
    }


    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setFavorites(FavouriteShopPoints favorites) {
        this.favorites = favorites;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }


}
