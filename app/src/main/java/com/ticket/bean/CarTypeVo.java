package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/7/23.
 */
public class CarTypeVo implements Serializable {

    @SerializedName("CarTypeID")
    private String carTypeId;// 车型ID
    @SerializedName("CarTypeName")
    private String carTypeName;// 车型名称
    @SerializedName("PictureUrl")
    private String pictureUrl;// 车型图片

    public String getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(String carTypeId) {
        this.carTypeId = carTypeId;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
