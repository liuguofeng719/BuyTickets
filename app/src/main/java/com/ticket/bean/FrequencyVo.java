package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FrequencyVo implements Serializable {

    // 路线编号
    @SerializedName("RoutingID")
    private String routingId;
    // 出发站点名称
    @SerializedName("StartStationName")
    private String startStationName;
    //出发城市名称
    @SerializedName("StartCityName")
    private String startCityName;
    // 终点站点名称
    @SerializedName("StopStationName")
    private String stopStationName;
    // 终点城市名称
    @SerializedName("StopCityName")
    private String stopCityName;
    // 开车日期
    @SerializedName("GoDate")
    private String goDate;
    // 发车时间
    @SerializedName("GoTime")
    private String goTime;
    // 车票价格
    @SerializedName("TicketPrice")
    private String ticketPrice;
    //服务费
    @SerializedName("ServicePrice")
    private String servicePrice;
    //保险费
    @SerializedName("InsurancePrice")
    private String insurancePrice;
    // 剩余票数
    @SerializedName("RemainingTicketsAmount")
    private String remainingTicketsAmount;

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(String insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public String getRoutingId() {
        return routingId;
    }

    public void setRoutingId(String routingId) {
        this.routingId = routingId;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public void setStartStationName(String startStationName) {
        this.startStationName = startStationName;
    }

    public String getStartCityName() {
        return startCityName;
    }

    public void setStartCityName(String startCityName) {
        this.startCityName = startCityName;
    }

    public String getStopStationName() {
        return stopStationName;
    }

    public void setStopStationName(String stopStationName) {
        this.stopStationName = stopStationName;
    }

    public String getStopCityName() {
        return stopCityName;
    }

    public void setStopCityName(String stopCityName) {
        this.stopCityName = stopCityName;
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

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getRemainingTicketsAmount() {
        return remainingTicketsAmount;
    }

    public void setRemainingTicketsAmount(String remainingTicketsAmount) {
        this.remainingTicketsAmount = remainingTicketsAmount;
    }

    @Override
    public String toString() {
        return "FrequencyVo{" +
                "routingId='" + routingId + '\'' +
                ", startStationName='" + startStationName + '\'' +
                ", startCityName='" + startCityName + '\'' +
                ", stopStationName='" + stopStationName + '\'' +
                ", stopCityName='" + stopCityName + '\'' +
                ", goDate='" + goDate + '\'' +
                ", goTime='" + goTime + '\'' +
                ", ticketPrice='" + ticketPrice + '\'' +
                ", servicePrice='" + servicePrice + '\'' +
                ", insurancePrice='" + insurancePrice + '\'' +
                ", remainingTicketsAmount='" + remainingTicketsAmount + '\'' +
                '}';
    }
}
