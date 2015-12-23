package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2015/12/14.
 */
public class OrderVoResp<T> extends BaseInfoVo {

    @SerializedName("Orders")
    private T orders;

    public T getOrders() {
        return orders;
    }

    public void setOrders(T orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "OrderVoResp{" +
                "orders=" + orders +
                '}';
    }
}
