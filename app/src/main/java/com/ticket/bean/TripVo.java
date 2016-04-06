package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/4/6.
 */
public class TripVo implements Serializable {
    @SerializedName("Day")
    private String day;
    @SerializedName("TripContent")
    private String tripContent;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTripContent() {
        return tripContent;
    }

    public void setTripContent(String tripContent) {
        this.tripContent = tripContent;
    }
}
