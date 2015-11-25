package com.mobium.client.models;

/**
 *  IDEA.
 *
 * Date: 27.11.11
 * Time: 21:08
 * To change this template use File | Settings | File Templates.
 */
public class DeliverFormField extends Extralable<DeliverFormField> {

    public enum FieldType {
        TEXT, LABEL, SELECT
    }

    protected String id;
    protected String title;
    protected FieldType type;
    protected boolean isRequired;

    public DeliverFormField(String id, String title, FieldType type, boolean required) {
        this.id = id;
        this.title = title;
        this.type = type;
        isRequired = required;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public FieldType getType() {
        return type;
    }

    public boolean isRequired() {
        return isRequired;
    }
}
