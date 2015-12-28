package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

public class PassengerDetailVo {

    @SerializedName("OrderDetailID")
    private String orderDetailID;
    @SerializedName("PassengerName")
    private String passengerName;
    @SerializedName("IDCard")
    private String idCard;
    @SerializedName("MobileNumber")
    private String mobileNumber;
    @SerializedName("TicketNumber")
    private String ticketNumber;
    @SerializedName("HasRefund")
    private String hasRefund;//是否存在退票
    @SerializedName("CanbeRefund")
    private boolean canbeRefund;//是否可退票（当值为true时，表示可以退票）
    @SerializedName("RefundDescription")
    private String refundDescription;//退票状态说明

    public String getHasRefund() {
        return hasRefund;
    }

    public void setHasRefund(String hasRefund) {
        this.hasRefund = hasRefund;
    }

    public boolean isCanbeRefund() {
        return canbeRefund;
    }

    public void setCanbeRefund(boolean canbeRefund) {
        this.canbeRefund = canbeRefund;
    }

    public String getRefundDescription() {
        return refundDescription;
    }

    public void setRefundDescription(String refundDescription) {
        this.refundDescription = refundDescription;
    }

    public String getOrderDetailID() {
        return orderDetailID;
    }

    public void setOrderDetailID(String orderDetailID) {
        this.orderDetailID = orderDetailID;
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    @Override
    public String toString() {
        return "PassengerDetailVo{" +
                "orderDetailID='" + orderDetailID + '\'' +
                ", passengerName='" + passengerName + '\'' +
                ", idCard='" + idCard + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", ticketNumber='" + ticketNumber + '\'' +
                '}';
    }
}
