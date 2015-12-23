package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

public class FrequencyListResp<T> extends BaseInfoVo {

    @SerializedName("Routings")
    private T routings;

    public T getRoutings() {
        return routings;
    }

    public void setRoutings(T routings) {
        this.routings = routings;
    }

    @Override
    public String toString() {
        return "FrequencyListResp{" +
                "routings=" + routings +
                '}';
    }
}
