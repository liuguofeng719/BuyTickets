package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/4/6.
 */
public class PlaceVo implements Serializable {

    @SerializedName("PlaceID")
    private String placeId;
    @SerializedName("PlaceName")
    private String placeName;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    @Override
    public String toString() {
        return "PlaceVo{" +
                "placeId='" + placeId + '\'' +
                ", placeName='" + placeName + '\'' +
                '}';
    }
}
