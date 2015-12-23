package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailVo {
    @SerializedName("StartStationName")
    private String startStationName;// 起点站名称
    @SerializedName("StopStationName")
    private String stopStationName;// 终点站名称
    @SerializedName("StartStationCityName")
    private String startStationCityName; //出发城市名称
    @SerializedName("StopStationCityName")
    private String stopStationCityName;// 终点城市名称
    @SerializedName("GoDate")
    private String goDate;// 开车日期
    @SerializedName("GoTime")
    private String goTime;// 开车时间
    @SerializedName("OrderTotalPrice")
    private String orderTotalPrice;// 订单总金额
    @SerializedName("OrderNumber")
    public String orderNumber;//订单号
    @SerializedName("PayDateTime")
    private String payDateTime;//支付时间
    @SerializedName("PayFuncation")
    private String payFuncation;//支付方式
    @SerializedName("OrderStatus")
    private String orderStatus;//订单状态
    @SerializedName("TotalTicketPrice")
    private String totalTicketPrice;//总的票价
    @SerializedName("TotalServicePrice")
    private String totalServicePrice;//服务费
    @SerializedName("TotalInsurancePrice")
    private String totalInsurancePrice;//保险费
    @SerializedName("Passengers")
    private List<PassengerDetailVo> passengers;// 乘客列表

    public String getStartStationCityName() {
        return startStationCityName;
    }

    public void setStartStationCityName(String startStationCityName) {
        this.startStationCityName = startStationCityName;
    }

    public String getStopStationCityName() {
        return stopStationCityName;
    }

    public void setStopStationCityName(String stopStationCityName) {
        this.stopStationCityName = stopStationCityName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPayDateTime() {
        return payDateTime;
    }

    public void setPayDateTime(String payDateTime) {
        this.payDateTime = payDateTime;
    }

    public String getPayFuncation() {
        return payFuncation;
    }

    public void setPayFuncation(String payFuncation) {
        this.payFuncation = payFuncation;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getTotalTicketPrice() {
        return totalTicketPrice;
    }

    public void setTotalTicketPrice(String totalTicketPrice) {
        this.totalTicketPrice = totalTicketPrice;
    }

    public String getTotalServicePrice() {
        return totalServicePrice;
    }

    public void setTotalServicePrice(String totalServicePrice) {
        this.totalServicePrice = totalServicePrice;
    }

    public String getTotalInsurancePrice() {
        return totalInsurancePrice;
    }

    public void setTotalInsurancePrice(String totalInsurancePrice) {
        this.totalInsurancePrice = totalInsurancePrice;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public void setStartStationName(String startStationName) {
        this.startStationName = startStationName;
    }

    public String getStopStationName() {
        return stopStationName;
    }

    public void setStopStationName(String stopStationName) {
        this.stopStationName = stopStationName;
    }

    public String getGoDate() {
        return goDate;
    }

    public void setGoDate(String goDate) {
        this.goDate = goDate;
    }

    public String getGoTime() {
        return goTime;
    }

    public void setGoTime(String goTime) {
        this.goTime = goTime;
    }

    public String getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(String orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public List<PassengerDetailVo> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerDetailVo> passengers) {
        this.passengers = passengers;
    }

    @Override
    public String toString() {
        return "OrderDetailVo{" +
                "startStationName='" + startStationName + '\'' +
                ", stopStationName='" + stopStationName + '\'' +
                ", goDate='" + goDate + '\'' +
                ", goTime='" + goTime + '\'' +
                ", orderTotalPrice='" + orderTotalPrice + '\'' +
                ", passengers=" + passengers +
                '}';
    }
}
