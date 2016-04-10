package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/4/6.
 */
public class TravelOrdersVo implements Serializable {

    @SerializedName("OrderID")
    private String orderId;
    @SerializedName("OrderNumber")
    private String orderNumber;
    @SerializedName("Trip")
    private String trip;
    @SerializedName("OrderTotalPrice")
    private String orderTotalPrice;
    @SerializedName("IsPaid")
    private boolean isPaid;
    @SerializedName("TravelStatus")
    private String travelStatus;//0：询价中;1：等待达成出行;2：达成出行;
    @SerializedName("GoDateTime")
    private String goDateTime;
    @SerializedName("OrderStatusString")
    public String orderStatusString;//订单状态说明
    @SerializedName("PassengerAmount")
    private String passengerAmount;

    public String getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(String orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public String getOrderStatusString() {
        return orderStatusString;
    }

    public void setOrderStatusString(String orderStatusString) {
        this.orderStatusString = orderStatusString;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

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

    public boolean isPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public String getTravelStatus() {
        return travelStatus;
    }

    public void setTravelStatus(String travelStatus) {
        this.travelStatus = travelStatus;
    }

    public String getGoDateTime() {
        return goDateTime;
    }

    public void setGoDateTime(String goDateTime) {
        this.goDateTime = goDateTime;
    }

    public String getPassengerAmount() {
        return passengerAmount;
    }

    public void setPassengerAmount(String passengerAmount) {
        this.passengerAmount = passengerAmount;
    }

    @Override
    public String toString() {
        return "TravelOrdersVo{" +
                "orderId='" + orderId + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", trip='" + trip + '\'' +
                ", orderTotalPrice='" + orderTotalPrice + '\'' +
                ", isPaid=" + isPaid +
                ", travelStatus='" + travelStatus + '\'' +
                ", goDateTime='" + goDateTime + '\'' +
                ", orderStatusString='" + orderStatusString + '\'' +
                ", passengerAmount='" + passengerAmount + '\'' +
                '}';
    }
}
