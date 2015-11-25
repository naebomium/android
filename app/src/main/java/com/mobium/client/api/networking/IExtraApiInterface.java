package com.mobium.client.api.networking;

import com.mobium.reference.utils.executing.ExecutingException;

import org.json.JSONObject;

/**
 *  on 15.10.15.
 */
public interface IExtraApiInterface extends ApiInterface {
    JSONObject DoApiRequest(String method, JSONObject args, JSONObject extra) throws NetworkingException, ExecutingException;
    String plain_id = "plain_id";
}
