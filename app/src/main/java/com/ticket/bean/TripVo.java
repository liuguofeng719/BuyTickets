package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/4/6.
 */
public class TripVo implements Serializable {
    @SerializedName("Day")
    private String day;
    @SerializedName("StartCity")
    private String startCity;
    @SerializedName("EndCity")
    private String endCity;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getEndCity() {
        return endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }
}
