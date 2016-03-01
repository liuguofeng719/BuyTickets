package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

public class ProvincesVo {

    @SerializedName("ProvinceID")
    private String provinceId;
    @SerializedName("ProvinceName")
    private String provinceName;

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Override
    public String toString() {
        return "ProvincesVo{" +
                "provinceId='" + provinceId + '\'' +
                ", provinceName='" + provinceName + '\'' +
                '}';
    }
}
