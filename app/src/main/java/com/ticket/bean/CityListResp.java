package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

public class CityListResp<T> extends BaseInfoVo {

    @SerializedName("CityList")
    private T cityList;

    public T getCityList() {
        return cityList;
    }

    public void setCityList(T cityList) {
        this.cityList = cityList;
    }

    @Override
    public String toString() {
        return "CityListResp{" +
                "cityList=" + cityList +
                '}';
    }
}
