package com.mobium.new_api.models.order;

import com.annimon.stream.Optional;
import com.mobium.new_api.models.ResponseBase;

import java.io.Serializable;

/**
 *  on 13.10.15.
 */
public class DeliveryMethods  extends ResponseBase implements Serializable {
    public Field[] staticFields;
    public DeliveryMethod[] methods;

    public DeliveryMethods() {
    }

    public void setStaticFields(Field[] staticFields) {
        this.staticFields = staticFields;
    }

    public void setMethods(DeliveryMethod[] methods) {
        this.methods = methods;
    }
}
