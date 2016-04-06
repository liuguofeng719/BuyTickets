package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/4/6.
 */
public class QuoteVo implements Serializable {

    @SerializedName("QuoteID")
    private String quoteId;//报价ID
    @SerializedName("CompanyName")
    private String companyName;//客运公司名称
    @SerializedName("CarTypeName")
    private String carTypeName;//车型描述
    @SerializedName("QuotePrice")
    private String quotePrice;//报价金额
    @SerializedName("CarPicture")
    private String carPicture;//车型图片
    @SerializedName("IsChoosed")
    private boolean isChoosed;//用户是否已选择此报价

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

    public String getQuotePrice() {
        return quotePrice;
    }

    public void setQuotePrice(String quotePrice) {
        this.quotePrice = quotePrice;
    }

    public String getCarPicture() {
        return carPicture;
    }

    public void setCarPicture(String carPicture) {
        this.carPicture = carPicture;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setIsChoosed(boolean isChoosed) {
        this.isChoosed = isChoosed;
    }

    @Override
    public String toString() {
        return "QuoteVo{" +
                "quoteId='" + quoteId + '\'' +
                ", companyName='" + companyName + '\'' +
                ", carTypeName='" + carTypeName + '\'' +
                ", quotePrice='" + quotePrice + '\'' +
                ", carPicture='" + carPicture + '\'' +
                ", isChoosed=" + isChoosed +
                '}';
    }
}
