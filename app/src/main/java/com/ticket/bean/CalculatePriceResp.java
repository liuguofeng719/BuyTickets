package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/25.
 */
public class CalculatePriceResp<T> extends BaseInfoVo {

    @SerializedName("PriceDetails")
    private T priceDetails;
    @SerializedName("TotalPrice")
    private double totalPrice;

    public T getPriceDetails() {
        return priceDetails;
    }

    public void setPriceDetails(T priceDetails) {
        this.priceDetails = priceDetails;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
