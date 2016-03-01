package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

public class ProvincesResp<T> extends BaseInfoVo {
    @SerializedName("ProvinceList")
    public T provinceList;

    public T getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(T provinceList) {
        this.provinceList = provinceList;
    }

    @Override
    public String toString() {
        return "ProvincesResp{" +
                "provinceList=" + provinceList +
                '}';
    }
}
