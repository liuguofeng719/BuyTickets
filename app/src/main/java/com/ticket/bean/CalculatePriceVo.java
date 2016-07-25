package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/7/24.
 */
public class CalculatePriceVo implements Serializable {

    @SerializedName("CarTypeName")
    private String carTypeName; //车型名称
    @SerializedName("CarTypePicture")
    private String carTypePicture; //车型图片
    @SerializedName("AppearanceFeeContent")
    private String appearanceFeeContent; //时长费文本
    @SerializedName("AppearanceFee")
    private String appearanceFee; //时长费金额
    @SerializedName("TravelCostContent")
    private String travelCostContent; //里程费文本
    @SerializedName("TravelCost")
    private String travelCost; //里程费金额

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

    public String getCarTypePicture() {
        return carTypePicture;
    }

    public void setCarTypePicture(String carTypePicture) {
        this.carTypePicture = carTypePicture;
    }

    public String getAppearanceFeeContent() {
        return appearanceFeeContent;
    }

    public void setAppearanceFeeContent(String appearanceFeeContent) {
        this.appearanceFeeContent = appearanceFeeContent;
    }

    public String getAppearanceFee() {
        return appearanceFee;
    }

    public void setAppearanceFee(String appearanceFee) {
        this.appearanceFee = appearanceFee;
    }

    public String getTravelCostContent() {
        return travelCostContent;
    }

    public void setTravelCostContent(String travelCostContent) {
        this.travelCostContent = travelCostContent;
    }

    public String getTravelCost() {
        return travelCost;
    }

    public void setTravelCost(String travelCost) {
        this.travelCost = travelCost;
    }

}
