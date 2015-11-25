package com.mobium.new_api.models.order;

/**
 *  on 13.10.15.
 */
public class CloudePaymant extends PaymentTypeService {
    public String publicId;
    public static final String cloudpaymentsId = "cloudpayments";

    public CloudePaymant(String publicId) {
        super(cloudpaymentsId);
        this.publicId = publicId;
    }
}
