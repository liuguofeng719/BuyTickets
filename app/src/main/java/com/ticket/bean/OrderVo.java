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
    @SerializedName("OrderStatusContent")
    public String orderStatusContent;//订单状态说明
    @SerializedName("IsPaid")
    public boolean isPaid;//是否已支付

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

    public String getOrderStatusContent() {
        return orderStatusContent;
    }

    public void setOrderStatusContent(String orderStatusContent) {
        this.orderStatusContent = orderStatusContent;
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
                "orderNumber='" + orderNumber + '\'' +
                ", createOrderDate='" + createOrderDate + '\'' +
                ", passengerAmount='" + passengerAmount + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", trip='" + trip + '\'' +
                ", orderStatusContent='" + orderStatusContent + '\'' +
                ", isPaid='" + isPaid + '\'' +
                '}';
    }
}
