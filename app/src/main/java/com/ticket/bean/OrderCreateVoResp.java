package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

public class OrderCreateVoResp extends BaseInfoVo {
    @SerializedName("OrderID")
    public String orderId;//订单号
    @SerializedName("OrderNumber")
    public String orderNumber;//订单号

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return "OrderCreateVoResp{" +
                "orderId='" + orderId + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                '}';
    }
}
