package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/4/6.
 */
public class PlaceListResp<T> extends BaseInfoVo {

    @SerializedName("PlaceList")
    private T placeList;

    public T getPlaceList() {
        return placeList;
    }

    public void setPlaceList(T placeList) {
        this.placeList = placeList;
    }

    @Override
    public String toString() {
        return "PlaceListResp{" +
                "placeList=" + placeList +
                '}';
    }
}
