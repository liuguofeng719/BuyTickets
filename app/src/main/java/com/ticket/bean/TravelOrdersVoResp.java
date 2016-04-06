package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/4/6.
 */
public class TravelOrdersVoResp<T> extends BaseInfoVo {

    @SerializedName("TravelOrders")
    private T travelOrders;

    public T getTravelOrders() {
        return travelOrders;
    }

    public void setTravelOrders(T travelOrders) {
        this.travelOrders = travelOrders;
    }

    @Override
    public String toString() {
        return "LeasedVehicleVoResp{" +
                "travelOrders=" + travelOrders +
                '}';
    }
}
