package com.mobium.new_api.models.order;

/**
 *  on 13.10.15.
 */
public class Field {
    private String id, title, type;
    private boolean isRequired;


    public Field() {
    }

    public Field(String id, String title, String type, boolean isRequired) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.isRequired = isRequired;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setIsRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }

    @Override
    public String toString() {
        return "Field{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", type='"+ type+ '\'' +
                '}';
    }
}
