package com.mobium.new_api.methodParameters;

/**
 *  on 19.06.15.
 * http://mobiumapps.com/
 */
public class RegisterAppParam {
    public final String platform;
    public final String os_version;
    public final String installation_id;
    public final String device_model;
    public final String device_id;
    public final String device_brand;

    public RegisterAppParam(String platform,
                            String os_version,
                            String installation_id,
                            String device_model,
                            String device_id,
                            String device_brand) {
        this.platform = platform;
        this.os_version = os_version;
        this.installation_id = installation_id;
        this.device_model = device_model;
        this.device_id = device_id;
        this.device_brand = device_brand;
    }
}
