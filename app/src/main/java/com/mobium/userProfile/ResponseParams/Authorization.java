package com.mobium.userProfile.ResponseParams;

/**
 *  on 11.06.15.
 * http://mobiumapps.com/
 */
public class Authorization {
    public String accessToken;
    public String confirmToken;
    public String errorMessage;



    public Authorization() {
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setConfirmToken(String confirmToken) {
        this.confirmToken = confirmToken;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
