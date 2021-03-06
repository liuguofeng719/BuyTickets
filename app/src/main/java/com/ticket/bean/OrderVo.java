package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

public class OrderVo {
    @SerializedName("OrderID")
    public String orderId;//订单号
    @SerializedName("OrderNumber")
    public String orderNumber;//订单号
    @SerializedName("CreateOrderDate")
    public String createOrderDate;//订单创建时间
    @SerializedName("GoDate")
    public String goDate;//发车时间
    @SerializedName("PassengerAmount")
    public String passengerAmount;//乘客人数
    @SerializedName("TotalPrice")
    public String totalPrice;//订单总金额
    @SerializedName("Trip")
    public String trip;//行程
    @SerializedName("OrderStatusDescription")
    public String orderStatusDescription;//订单状态说明
    @SerializedName("OrderStatusCode")//订单状态码 (NOT_PAY：未支付，GENERATING：正在出票中,REFUNDED：出票失败，退款已完成,REFUNDING：出票失败，正在退款中,SUCCESS：出票成功,UNKONW：未知状态)
    public String orderStatusCode;
    @SerializedName("IsPaid")
    public boolean isPaid;//是否已支付

    public String getOrderStatusDescription() {
        return orderStatusDescription;
    }

    public void setOrderStatusDescription(String orderStatusDescription) {
        this.orderStatusDescription = orderStatusDescription;
    }

    public String getOrderStatusCode() {
        return orderStatusCode;
    }

    public void setOrderStatusCode(String orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGoDate() {
        return goDate;
    }

    public void setGoDate(String goDate) {
        this.goDate = goDate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCreateOrderDate() {
        return createOrderDate;
    }

    public void setCreateOrderDate(String createOrderDate) {
        this.createOrderDate = createOrderDate;
    }

    public String getPassengerAmount() {
        return passengerAmount;
    }

    public void setPassengerAmount(String passengerAmount) {
        this.passengerAmount = passengerAmount;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }


    public boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    @Override
    public String toString() {
        return "OrderVo{" +
                "orderId='" + orderId + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", createOrderDate='" + createOrderDate + '\'' +
                ", goDate='" + goDate + '\'' +
                ", passengerAmount='" + passengerAmount + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", trip='" + trip + '\'' +
                ", orderStatusDescription='" + orderStatusDescription + '\'' +
                ", orderStatusCode='" + orderStatusCode + '\'' +
                ", isPaid=" + isPaid +
                '}';
    }
}
