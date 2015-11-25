package com.mobium.client.models;

/**
 *
 *
 * Date: 26.04.12
 * Time: 20:19
 * To change this template use File | Settings | File Templates.
 */
public class CharacteristicsGroup extends Extralable<CharacteristicsGroup> {
    private String groupTitle;

    private CharacteristicItem[] characteristics;

    public String getGroupTitle() {
        return groupTitle;
    }

    public CharacteristicItem[] getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(CharacteristicItem... characteristics) {
        this.characteristics = characteristics;
    }

    public CharacteristicsGroup(String groupTitle, CharacteristicItem... characteristics) {
        this.groupTitle = groupTitle;
        this.characteristics = characteristics;
    }
}
