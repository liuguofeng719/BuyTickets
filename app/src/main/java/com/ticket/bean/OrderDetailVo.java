package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuguofeng719 on 2015/12/14.
 */
public class OrderDetailVo {
    @SerializedName("StartStationName")
    private String startStationName;// 起点站名称
    @SerializedName("StopStationName")
    private String stopStationName;// 终点站名称
    @SerializedName("GoDate")
    private String goDate;// 开车日期
    @SerializedName("GoTime")
    private String goTime;// 开车时间
    @SerializedName("OrderTotalPrice")
    private String orderTotalPrice;// 订单总金额
    @SerializedName("Passengers")
    private List<PassengerDetailVo> passengers;// 乘客列表

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
