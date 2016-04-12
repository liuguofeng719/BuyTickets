package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/3/24.
 */
public class TravelRoutingListVo implements Serializable {

    @SerializedName("TravelID")
    private String travelId;
    //起点名称
    @SerializedName("StartPlaceName")
    private String startPlaceName;
    //终点名称
    @SerializedName("StopPlaceName")
    private String stopPlaceName;
    //出发城市名称
    @SerializedName("StartCity")
    private String startCity;
    // 终点城市名称
    @SerializedName("StopCity")
    private String stopCity;
    //出发日期
    @SerializedName("GoDate")
    private String goDate;
    //出发时间
    @SerializedName("GoTime")
    private String goTime;
    //学生票价
    @SerializedName("StudentPrice")
    private String studentPrice;
    //成人票价
    @SerializedName("NormalPrice")
    private String normalPrice;
    //保险费
    @SerializedName("InsurcePrice")
    private String insurcePrice;
    //服务费
    @SerializedName("ServicePrice")
    private String servicePrice;
    //座位数
    @SerializedName("SeatAmount")
    private String seatAmount;
    //剩余座位数
    @SerializedName("SurplusSeat")
    private String surplusSeat;
    //达成出行座位数
    @SerializedName("ReachSeatAmount")
    private String reachSeatAmount;
    //车型
    @SerializedName("CarName")
    private String carName;
    //承运公司
    @SerializedName("CompanyName")
    private String companyName;
    //发布类型
    @SerializedName("PublishedType")
    private String publishedType;
    //状态
    @SerializedName("State")
    private String State;
    //发布人
    @SerializedName("CreateUser")
    private String createUser;

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getStopCity() {
        return stopCity;
    }

    public void setStopCity(String stopCity) {
        this.stopCity = stopCity;
    }

    public String getTravelId() {
        return travelId;
    }

    public void setTravelId(String travelId) {
        this.travelId = travelId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getStartPlaceName() {
        return startPlaceName;
    }

    public void setStartPlaceName(String startPlaceName) {
        this.startPlaceName = startPlaceName;
    }

    public String getStopPlaceName() {
        return stopPlaceName;
    }

    public void setStopPlaceName(String stopPlaceName) {
        this.stopPlaceName = stopPlaceName;
    }

    public String getGoDate() {
        return goDate;
    }

    public void setGoDate(String goDate) {
        this.goDate = goDate;
    }

    public String getGoTime() {
        return goTime;
    }

    public void setGoTime(String goTime) {
        this.goTime = goTime;
    }

    public String getStudentPrice() {
        return studentPrice;
    }

    public void setStudentPrice(String studentPrice) {
        this.studentPrice = studentPrice;
    }

    public String getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(String normalPrice) {
        this.normalPrice = normalPrice;
    }

    public String getInsurcePrice() {
        return insurcePrice;
    }

    public void setInsurcePrice(String insurcePrice) {
        this.insurcePrice = insurcePrice;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getSeatAmount() {
        return seatAmount;
    }

    public void setSeatAmount(String seatAmount) {
        this.seatAmount = seatAmount;
    }

    public String getSurplusSeat() {
        return surplusSeat;
    }

    public void setSurplusSeat(String surplusSeat) {
        this.surplusSeat = surplusSeat;
    }

    public String getReachSeatAmount() {
        return reachSeatAmount;
    }

    public void setReachSeatAmount(String reachSeatAmount) {
        this.reachSeatAmount = reachSeatAmount;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPublishedType() {
        return publishedType;
    }

    public void setPublishedType(String publishedType) {
        this.publishedType = publishedType;
    }

    @Override
    public String toString() {
        return "TravelRoutingListVo{" +
                "startPlaceName='" + startPlaceName + '\'' +
                ", stopPlaceName='" + stopPlaceName + '\'' +
                ", goDate='" + goDate + '\'' +
                ", goTime='" + goTime + '\'' +
                ", studentPrice='" + studentPrice + '\'' +
                ", normalPrice='" + normalPrice + '\'' +
                ", insurcePrice='" + insurcePrice + '\'' +
                ", servicePrice='" + servicePrice + '\'' +
                ", seatAmount='" + seatAmount + '\'' +
                ", surplusSeat='" + surplusSeat + '\'' +
                ", reachSeatAmount='" + reachSeatAmount + '\'' +
                ", carName='" + carName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", publishedType='" + publishedType + '\'' +
                '}';
    }
}
