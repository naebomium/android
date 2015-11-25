package com.mobium.new_api.models;

/**
 *  on 11.11.15.
 */
public class ResponseBase {
    private String error;
    protected String description;
    private boolean mayRetry;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isMayRetry() {
        return mayRetry;
    }

    public void setMayRetry(boolean mayRetry) {
        this.mayRetry = mayRetry;
    }

    public ResponseBase() {
    }
}
