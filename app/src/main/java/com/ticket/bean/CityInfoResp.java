package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/2/25.
 */
public class CityInfoResp<T> extends BaseInfoVo {

    @SerializedName("CityInfo")
    private T cityInfo;

    public T getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(T cityInfo) {
        this.cityInfo = cityInfo;
    }

    @Override
    public String toString() {
        return "CityInfoResp{" +
                "cityInfo=" + cityInfo +
                '}';
    }
}
