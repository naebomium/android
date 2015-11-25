package com.mobium.reference.utils.statistics_new.data_receivers;

/**
 *  on 29.09.15.
 */
public interface IConnectivityDataReceiver {
    void onMakeCall(String cellNumber);
    void onSendFeedback(String message, String email);
}
