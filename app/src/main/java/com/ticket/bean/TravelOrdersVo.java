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
    private int travelStatus;
    @SerializedName("GoDateTime")
    private String goDateTime;
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

    public String getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(String orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public int getTravelStatus() {
        return travelStatus;
    }

    public void setTravelStatus(int travelStatus) {
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
        return "LeasedVehicleVo{" +
                "orderId='" + orderId + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", trip='" + trip + '\'' +
                ", orderTotalPrice='" + orderTotalPrice + '\'' +
                ", isPaid=" + isPaid +
                ", travelStatus=" + travelStatus +
                ", goDateTime='" + goDateTime + '\'' +
                ", passengerAmount='" + passengerAmount + '\'' +
                '}';
    }
}
