package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/3/24.
 */
public class TravelRoutingVoResp<T> extends BaseInfoVo {

    @SerializedName("TravelPreviewList")
    public T travelPreviewList;

    public T getTravelPreviewList() {
        return travelPreviewList;
    }

    public void setTravelPreviewList(T travelPreviewList) {
        this.travelPreviewList = travelPreviewList;
    }

    @Override
    public String toString() {
        return "TravelRoutingVoResp{" +
                "travelPreviewList=" + travelPreviewList +
                '}';
    }
}
