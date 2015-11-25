package com.mobium.new_api.models;

/**
 *  on 19.06.15.
 * http://mobiumapps.com/
 *
 * Server response with response and extra fields
 */
public class ResponseExtra<Result extends ResponseBase, Extra>
        extends Response<Result> {
    public Extra extra;

    public ResponseExtra(Result response, Extra extra) {
        super(response);
        this.extra = extra;
    }

}
