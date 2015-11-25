package com.mobium.new_api.models.order;

import com.annimon.stream.Optional;
import com.google.android.gms.common.api.Api;
import com.mobium.new_api.models.ResponseBase;

import java.io.Serializable;

/**
 *  on 13.10.15.
 */
public class PaymentTypesInfo extends ResponseBase implements Serializable {
    private PaymentTypeService[] services;



    public PaymentTypesInfo(PaymentTypeService[] services) {
        this.services = services;
    }

    public PaymentTypesInfo() {
    }

    public Optional<PaymentTypeService[]> getServices() {
        return services == null || services.length == 0 ? Optional.empty() : Optional.of(services);
    }

    public void setServices(PaymentTypeService[] services) {
        this.services = services;
    }
}
