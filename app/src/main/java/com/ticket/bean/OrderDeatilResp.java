package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2015/12/14.
 */
public class OrderDeatilResp<T> extends BaseInfoVo {
    @SerializedName("OrderDetailMessage")
    T orderDetailMessage;

    public T getOrderDetailMessage() {
        return orderDetailMessage;
    }

    public void setOrderDetailMessage(T orderDetailMessage) {
        this.orderDetailMessage = orderDetailMessage;
    }

    @Override
    public String toString() {
        return "OrderDeatilResp{" +
                "orderDetailMessage=" + orderDetailMessage +
                '}';
    }
}
