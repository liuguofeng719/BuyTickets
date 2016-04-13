package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/4/13.
 */
public class LeasedVehicleOrder implements Serializable {

    @SerializedName("OrderID")
    private String orderId;
    @SerializedName("OrderNumber")
    private String orderNumber;
    @SerializedName("Trip")
    private String trip;
    @SerializedName("TotalPrice")
    private String totalPrice;
    @SerializedName("IsPaid")
    private boolean isPaid;
    @SerializedName("State")
    private String state;//0：询价中;1：等待达成出行;2：达成出行;
    @SerializedName("GoDateTime")
    private String goDateTime;
    @SerializedName("OrderStatusString")
    public String orderStatusString;//订单状态说明
    @SerializedName("PassengerAmount")
    private String passengerAmount;

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

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGoDateTime() {
        return goDateTime;
    }

    public void setGoDateTime(String goDateTime) {
        this.goDateTime = goDateTime;
    }

    public String getOrderStatusString() {
        return orderStatusString;
    }

    public void setOrderStatusString(String orderStatusString) {
        this.orderStatusString = orderStatusString;
    }

    public String getPassengerAmount() {
        return passengerAmount;
    }

    public void setPassengerAmount(String passengerAmount) {
        this.passengerAmount = passengerAmount;
    }
}
