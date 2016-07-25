package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/7/23.
 */
public class AssignedCarVo implements Serializable {

    @SerializedName("CarID")
    private String carID;//车ID
    @SerializedName("FullName")
    private String fullName;//司机名称
    @SerializedName("DrivingExperience")
    private int drivingExperience;//驾龄
    @SerializedName("TravelKilometers")
    private int travelKilometers;//安全行驶距离 万为单位
    @SerializedName("PhoneNumber")
    private String phoneNumber;//司机电话
    @SerializedName("CarTypeName")
    private String carTypeName;//车型名称
    @SerializedName("RegistrationNumber")
    private String registrationNumber;// 车牌号
    @SerializedName("CarTypePicture")
    private String carTypePicture;//车型图片

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getDrivingExperience() {
        return drivingExperience;
    }

    public void setDrivingExperience(int drivingExperience) {
        this.drivingExperience = drivingExperience;
    }

    public int getTravelKilometers() {
        return travelKilometers;
    }

    public void setTravelKilometers(int travelKilometers) {
        this.travelKilometers = travelKilometers;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getCarTypePicture() {
        return carTypePicture;
    }

    public void setCarTypePicture(String carTypePicture) {
        this.carTypePicture = carTypePicture;
    }
}
