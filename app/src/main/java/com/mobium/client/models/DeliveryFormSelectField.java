package com.mobium.client.models;

/**
 *  on 07.12.13.
 * http://mobiumapps.com/
 */
public class DeliveryFormSelectField extends DeliverFormField {
    private SelectItem[] selectItems;

    public DeliveryFormSelectField(String id, String title, FieldType type, boolean required, SelectItem... items) {
        super(id, title, FieldType.SELECT, required);
        selectItems = items;
    }

    public DeliveryFormSelectField(String id, String title, boolean required, SelectItem... items) {
        super(id, title, FieldType.SELECT, required);
        selectItems = items;
    }

    public SelectItem[] getSelectItems() {
        return selectItems;
    }
}
