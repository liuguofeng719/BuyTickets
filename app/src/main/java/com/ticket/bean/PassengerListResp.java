package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

public class PassengerListResp<T> extends BaseInfoVo {

    @SerializedName("Passengers")
    public T passengerList;

    public T getPassengerList() {
        return passengerList;
    }

    public void setPassengerList(T passengerList) {
        this.passengerList = passengerList;
    }

    @Override
    public String toString() {
        return "PassengerListResp{" +
                "passengerList=" + passengerList +
                '}';
    }
}
