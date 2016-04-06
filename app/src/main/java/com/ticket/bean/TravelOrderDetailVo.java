package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/4/6.
 */
public class TravelOrderDetailVo implements Serializable{
    @SerializedName("OrderNumber")
    private String orderNumber;
    @SerializedName("PaymentDateTime")
    private String paymentDateTime;//支付时间
    @SerializedName("PaymentWay")
    private String paymentWay;//支付方式
    @SerializedName("CarModel")
    private String carModel;//车型
    @SerializedName("PassengerPaidAmount")
    private String passengerPaidAmount;//已支付人数
    @SerializedName("DealSeatAmont")
    private String dealSeatAmont;//达成出行人数
    @SerializedName("OrderStatusString")
    private String orderStatusString;//订单状态描述
    @SerializedName("GoDateTime")
    private String goDateTime;//发车时间
    @SerializedName("TripOfCity")
    private String tripOfCity;//行程（城市名-城市名）
    @SerializedName("TripOfPlace")
    private String tripOfPlace;//行程（发车地址-下车地址）

    @Override
    public String toString() {
        return "TravelOrderDetailVo{" +
                "orderNumber='" + orderNumber + '\'' +
                ", paymentDateTime='" + paymentDateTime + '\'' +
                ", paymentWay='" + paymentWay + '\'' +
                ", carModel='" + carModel + '\'' +
                ", passengerPaidAmount='" + passengerPaidAmount + '\'' +
                ", dealSeatAmont='" + dealSeatAmont + '\'' +
                ", orderStatusString='" + orderStatusString + '\'' +
                ", goDateTime='" + goDateTime + '\'' +
                ", tripOfCity='" + tripOfCity + '\'' +
                ", tripOfPlace='" + tripOfPlace + '\'' +
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

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getPassengerPaidAmount() {
        return passengerPaidAmount;
    }

    public void setPassengerPaidAmount(String passengerPaidAmount) {
        this.passengerPaidAmount = passengerPaidAmount;
    }

    public String getDealSeatAmont() {
        return dealSeatAmont;
    }

    public void setDealSeatAmont(String dealSeatAmont) {
        this.dealSeatAmont = dealSeatAmont;
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

    public String getTripOfCity() {
        return tripOfCity;
    }

    public void setTripOfCity(String tripOfCity) {
        this.tripOfCity = tripOfCity;
    }

    public String getTripOfPlace() {
        return tripOfPlace;
    }

    public void setTripOfPlace(String tripOfPlace) {
        this.tripOfPlace = tripOfPlace;
    }
}
