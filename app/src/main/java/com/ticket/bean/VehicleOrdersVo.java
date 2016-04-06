package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/4/6.
 */
public class VehicleOrdersVo implements Serializable {

    @SerializedName("OrderNumber")
    private String orderNumber;//订单号
    @SerializedName("PaymentDateTime")
    private String paymentDateTime;//支付时间
    @SerializedName("PaymentWay")
    private String paymentWay;//支付方式
    @SerializedName("OrderStatusString")
    private String orderStatusString;//订单状态描述
    @SerializedName("GoDate")
    private String goDate;//出发
    @SerializedName("GoDateTime")
    private String goDateTime;//发车时间

}
