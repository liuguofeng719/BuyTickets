package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

public class CityVo {

    @SerializedName("CityID")
    private String cityId;
    @SerializedName("FirstLatter")
    private String firstLatter;
    @SerializedName("CityName")
    private String cityName;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getFirstLatter() {
        return firstLatter;
    }

    public void setFirstLatter(String firstLatter) {
        this.firstLatter = firstLatter;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "CityVo{" +
                "cityId='" + cityId + '\'' +
                ", firstLatter='" + firstLatter + '\'' +
                ", cityName='" + cityName + '\'' +
                '}';
    }
}
