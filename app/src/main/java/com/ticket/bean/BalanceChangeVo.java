package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/3/25.
 */
public class BalanceChangeVo implements Serializable {

    @SerializedName("ChangePrice")
    private String changePrice;
    @SerializedName("ChangeDateTime")
    private String changeDateTime;
    @SerializedName("Description")
    private String description;
    @SerializedName("BalanceTotalPrice")
    private String balanceTotalPrice;

    public String getBalanceTotalPrice() {
        return balanceTotalPrice;
    }

    public void setBalanceTotalPrice(String balanceTotalPrice) {
        this.balanceTotalPrice = balanceTotalPrice;
    }

    public String getChangePrice() {
        return changePrice;
    }

    public void setChangePrice(String changePrice) {
        this.changePrice = changePrice;
    }

    public String getChangeDateTime() {
        return changeDateTime;
    }

    public void setChangeDateTime(String changeDateTime) {
        this.changeDateTime = changeDateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
