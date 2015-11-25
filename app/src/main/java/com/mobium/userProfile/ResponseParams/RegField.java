package com.mobium.userProfile.ResponseParams;

/**
 *  on 18.07.15.
 * http://mobiumapps.com/
 */

public class RegField {
    public RegFields.RegFieldType type;
    public String id;
    public boolean required;
    public String title;

    private String value;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
