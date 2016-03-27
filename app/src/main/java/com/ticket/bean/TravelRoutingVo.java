package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/3/24.
 */
public class TravelRoutingVo implements Serializable {

    //出发城市ID
    @SerializedName("StartCityID")
    private String startCityID;
    //到达城市ID
    @SerializedName("StopCityID")
    private String stopCityID;
    //出发城市名称
    @SerializedName("StartCityName")
    private String startCityName;
    //到达城市名称
    @SerializedName("StopCityName")
    private String stopCityName;
    //出发日期
    @SerializedName("GoDate")
    private String goDate;
    //线路班次数
    @SerializedName("TravelAmount")
    private String travelAmount;

    public String getStartCityID() {
        return startCityID;
    }

    public void setStartCityID(String startCityID) {
        this.startCityID = startCityID;
    }

    public String getStopCityID() {
        return stopCityID;
    }

    public void setStopCityID(String stopCityID) {
        this.stopCityID = stopCityID;
    }

    public String getStartCityName() {
        return startCityName;
    }

    public void setStartCityName(String startCityName) {
        this.startCityName = startCityName;
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

    public String getTravelAmount() {
        return travelAmount;
    }

    public void setTravelAmount(String travelAmount) {
        this.travelAmount = travelAmount;
    }

    @Override
    public String toString() {
        return "TravelRoutingVo{" +
                "startCityID='" + startCityID + '\'' +
                ", stopCityID='" + stopCityID + '\'' +
                ", startCityName='" + startCityName + '\'' +
                ", stopCityName='" + stopCityName + '\'' +
                ", goDate='" + goDate + '\'' +
                ", travelAmount='" + travelAmount + '\'' +
                '}';
    }
}
