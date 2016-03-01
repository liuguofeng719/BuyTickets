package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

public class CityVo {

    @SerializedName("CityID")
    private String cityId;
    @SerializedName("FirstLatter")
    private String firstLatter;
    @SerializedName("CityName")
    private String cityName;
    @SerializedName("ProvinceID")
    private String provinceId;
    @SerializedName("ProvinceName")
    private String provinceName;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

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
                ", provinceId='" + provinceId + '\'' +
                ", provinceName='" + provinceName + '\'' +
                '}';
    }
}
