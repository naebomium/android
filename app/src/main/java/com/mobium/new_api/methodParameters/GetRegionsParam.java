package com.mobium.new_api.methodParameters;

/**
 *  on 23.06.15.
 * http://mobiumapps.com/
 */
public class GetRegionsParam {
    public final int minPoints;
    public final String googlePlacesId;
    public final String cityName;

    public GetRegionsParam(int minPoints, String googlePlacesId, String cityName) {
        this.minPoints = minPoints;
        this.googlePlacesId = googlePlacesId;
        this.cityName = cityName;
    }
}
