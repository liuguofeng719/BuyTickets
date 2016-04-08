package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuguofeng719 on 2016/4/6.
 */
public class TravelOrderDetailVoResp extends BaseInfoVo {

    @SerializedName("Passengers")
    List<PassengerVo> passengers;
    @SerializedName("Messages")
    List<MessagesVo> messages;
    @SerializedName("TravelOrderDetail")
    TravelOrderDetailVo travelOrderDetail;

    public List<PassengerVo> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerVo> passengers) {
        this.passengers = passengers;
    }

    public List<MessagesVo> getMessages() {
        return messages;
    }

    public void setMessages(List<MessagesVo> messages) {
        this.messages = messages;
    }

    public TravelOrderDetailVo getTravelOrderDetail() {
        return travelOrderDetail;
    }

    public void setTravelOrderDetail(TravelOrderDetailVo travelOrderDetail) {
        this.travelOrderDetail = travelOrderDetail;
    }

    @Override
    public String toString() {
        return "TravelOrderDetailsVo{" +
                "passengers=" + passengers +
                ", messages=" + messages +
                ", travelOrderDetail=" + travelOrderDetail +
                '}';
    }
}
