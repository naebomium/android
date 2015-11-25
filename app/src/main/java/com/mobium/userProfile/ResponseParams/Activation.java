package com.mobium.userProfile.ResponseParams;

import org.parceler.Parcel;

/**
 *  on 11.06.15.
 * http://mobiumapps.com/
 */

public class Activation {
    public String accessToken;
    public String errorMessage;

    public Activation() {
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}


