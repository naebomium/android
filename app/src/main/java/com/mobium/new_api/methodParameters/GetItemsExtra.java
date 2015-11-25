package com.mobium.new_api.methodParameters;

/**
 *  on 15.07.15.
 * http://mobiumapps.com/
 */
public class GetItemsExtra {
    private Integer limit;
    private Integer offset;
    private String regionId;

    public GetItemsExtra(Integer limit, Integer offset, String regionId) {
        this.limit = limit;
        this.offset = offset;
        this.regionId = regionId;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
}
