package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/9/8.
 */
public class TravelOrderVo extends BaseInfoVo {

    @SerializedName("OrderID")
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
