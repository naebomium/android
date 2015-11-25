package com.mobium.userProfile.ResponseParams;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *  on 11.06.15.
 * http://mobiumapps.com/
 */
public class RegFields {
    public List<RegField> fields;
    public String errorMessage;

    public RegFields(List<RegField> fields) {
        this.fields = fields;
    }

    public enum RegFieldType {
        @SerializedName("text")
        TEXT,
        @SerializedName("password")
        PASSWORD,
        @SerializedName("email")
        EMAIL,
        @SerializedName("phone")
        PHONE,
        @SerializedName("number")
        NUMBER
    }

    public RegFields() {
    }



    public void setFields(List<RegField> fields) {
        this.fields = fields;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
