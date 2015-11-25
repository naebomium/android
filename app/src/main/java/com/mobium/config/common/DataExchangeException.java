package com.mobium.config.common;

/**
 *  on 02.11.15.
 */
public class DataExchangeException extends Exception {
    private final String message;
    public final boolean canRepeat;

    public DataExchangeException(String message, boolean canRepeat) {
        this.message = message;
        this.canRepeat = canRepeat;
    }

    @Override
    public String getMessage() {
        return message == null ? super.getMessage() : message;
    }

    public boolean isCanRepeat() {
        return canRepeat;
    }
}
