package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/4/6.
 */
public class VehicleOrdersVo implements Serializable {

    @SerializedName("OrderNumber")
    private String orderNumber;//订单号
    @SerializedName("PaymentDateTime")
    private String paymentDateTime;//支付时间
    @SerializedName("PaymentWay")
    private String paymentWay;//支付方式
    @SerializedName("OrderStatusString")
    private String orderStatusString;//订单状态描述
    @SerializedName("GoDateTime")
    private String goDateTime;//发车时间

    @Override
    public String toString() {
        return "VehicleOrdersVo{" +
                "orderNumber='" + orderNumber + '\'' +
                ", paymentDateTime='" + paymentDateTime + '\'' +
                ", paymentWay='" + paymentWay + '\'' +
                ", orderStatusString='" + orderStatusString + '\'' +
                ", goDateTime='" + goDateTime + '\'' +
                '}';
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPaymentDateTime() {
        return paymentDateTime;
    }

    public void setPaymentDateTime(String paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
    }

    public String getPaymentWay() {
        return paymentWay;
    }

    public void setPaymentWay(String paymentWay) {
        this.paymentWay = paymentWay;
    }

    public String getOrderStatusString() {
        return orderStatusString;
    }

    public void setOrderStatusString(String orderStatusString) {
        this.orderStatusString = orderStatusString;
    }

    public String getGoDateTime() {
        return goDateTime;
    }

    public void setGoDateTime(String goDateTime) {
        this.goDateTime = goDateTime;
    }
}
