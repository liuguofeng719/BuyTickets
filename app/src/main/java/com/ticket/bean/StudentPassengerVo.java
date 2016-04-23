package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/4/12.
 */
public class StudentPassengerVo {

    @SerializedName("PassengerName")
    private String passengerName;
    @SerializedName("IDCard")
    private String idCard;
    @SerializedName("PhoneNumber")
    private String phoneNumber;
    @SerializedName("OrderItemID")
    private String orderItemId;
    @SerializedName("IsRefund")
    private boolean isRefund;//是否可退票（当值为true时，表示可以退票）

    public boolean isRefund() {
        return isRefund;
    }

    public void setRefund(boolean refund) {
        isRefund = refund;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
