package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2015/12/14.
 */
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
}
