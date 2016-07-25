package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/23.
 */
public class CarTypeListResp<T> extends BaseInfoVo {

    @SerializedName("CarTypeList")
    private T carTypeList;

    public T getCarTypeList() {
        return carTypeList;
    }

    public void setCarTypeList(T carTypeList) {
        this.carTypeList = carTypeList;
    }
}
