package com.mobium.client.models;

/**
 *  IDEA.
 *
 * Date: 23.03.12
 * Time: 23:10
 * To change this template use File | Settings | File Templates.
 */
public class CharacteristicItem extends Extralable<CharacteristicItem> {
    private String keyTitle;
    private String valueTitle;

    public CharacteristicItem(String keyTitle, String valueTitle) {
        this.keyTitle = keyTitle;
        this.valueTitle = valueTitle;
    }

    public String getKeyTitle() {
        return keyTitle;
    }

    public void setKeyTitle(String keyTitle) {
        this.keyTitle = keyTitle;
    }

    public String getValueTitle() {
        return valueTitle;
    }

    public void setValueTitle(String valueTitle) {
        this.valueTitle = valueTitle;
    }
}
