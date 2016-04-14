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
    @SerializedName("StartCity")
    private String startCity;
    @SerializedName("StopCity")
    private String stopCity;
    @SerializedName("StartCityPlace")
    private String startCityPlace;//行程（城市名-城市名）
    @SerializedName("StopCityPlace")
    private String stopCityPlace;//行程（发车地址-下车地址）
    @SerializedName("IsPaid")
    private boolean isPaid; //是否支付

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getStopCity() {
        return stopCity;
    }

    public void setStopCity(String stopCity) {
        this.stopCity = stopCity;
    }

    public String getStartCityPlace() {
        return startCityPlace;
    }

    public void setStartCityPlace(String startCityPlace) {
        this.startCityPlace = startCityPlace;
    }

    public String getStopCityPlace() {
        return stopCityPlace;
    }

    public void setStopCityPlace(String stopCityPlace) {
        this.stopCityPlace = stopCityPlace;
    }

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
                ", startCity='" + startCity + '\'' +
                ", stopCity='" + stopCity + '\'' +
                ", startCityPlace='" + startCityPlace + '\'' +
                ", stopCityPlace='" + stopCityPlace + '\'' +
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
}
