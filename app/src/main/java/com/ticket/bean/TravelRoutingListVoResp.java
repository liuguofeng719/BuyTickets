package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/3/24.
 */
public class TravelRoutingListVoResp<T> extends BaseInfoVo {

    @SerializedName("TravelRoutingList")
    public T travelRoutingList;

    public T getTravelRoutingList() {
        return travelRoutingList;
    }

    public void setTravelRoutingList(T travelRoutingList) {
        this.travelRoutingList = travelRoutingList;
    }

    @Override
    public String toString() {
        return "TravelRoutingListVoResp{" +
                "travelRoutingList=" + travelRoutingList +
                '}';
    }
}
