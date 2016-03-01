package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityListResp<T> extends BaseInfoVo {

    @SerializedName("CityList")
    private T cityList;

    @SerializedName("ProvinceList")
    private List<ProvincesVo> provinceList;

    public List<ProvincesVo> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<ProvincesVo> provinceList) {
        this.provinceList = provinceList;
    }

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
