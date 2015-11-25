package com.mobium.new_api.methodParameters;

/**
 *  on 03.08.15.
 * http://mobiumapps.com/
 */

public class SendMessageParam {
    public final String email;
    public final String message;

    public SendMessageParam(String email, String message) {
        this.email = email;
        this.message = message;
    }
}
