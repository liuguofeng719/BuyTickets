package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/9/8.
 */
public class TravelVehicleOrdersVo extends BaseInfoVo {

    @SerializedName("VehicleOrdersDetails")
    private VehicleOrdersDetails vehicleOrdersDetails;
    @SerializedName("AssignedCarDetail")
    private AssignedCarDetail assignedCarDetail;

    public VehicleOrdersDetails getVehicleOrdersDetails() {
        return vehicleOrdersDetails;
    }

    public void setVehicleOrdersDetails(VehicleOrdersDetails vehicleOrdersDetails) {
        this.vehicleOrdersDetails = vehicleOrdersDetails;
    }

    public AssignedCarDetail getAssignedCarDetail() {
        return assignedCarDetail;
    }

    public void setAssignedCarDetail(AssignedCarDetail assignedCarDetail) {
        this.assignedCarDetail = assignedCarDetail;
    }

    public static class VehicleOrdersDetails {
        @SerializedName("OrderNumber")
        private String orderNumber;
        @SerializedName("PaymentDateTime")
        private String paymentDateTime;
        @SerializedName("PaymentWay")
        private String paymentWay;
        @SerializedName("OrderStatusString")
        private String orderStatusString;
        @SerializedName("GoDateTime")
        private String goDateTime;

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getPaymentDateTime() {
            return paymentDateTime;
        }

        public void setPaymentDateTime(String paymentDateTime) {
            this.paymentDateTime = paymentDateTime;
        }

        public String getPaymentWay() {
            return paymentWay;
        }

        public void setPaymentWay(String paymentWay) {
            this.paymentWay = paymentWay;
        }

        public String getOrderStatusString() {
            return orderStatusString;
        }

        public void setOrderStatusString(String orderStatusString) {
            this.orderStatusString = orderStatusString;
        }

        public String getGoDateTime() {
            return goDateTime;
        }

        public void setGoDateTime(String goDateTime) {
            this.goDateTime = goDateTime;
        }
    }

    public static class AssignedCarDetail {

        @SerializedName("CarID")
        private String carID;
        @SerializedName("FullName")
        private String fullName;
        @SerializedName("DrivingExperience")
        private String drivingExperience;
        @SerializedName("TravelKilometers")
        private String travelKilometers;
        @SerializedName("PhoneNumber")
        private String phoneNumber;
        @SerializedName("CarTypeName")
        private String carTypeName;
        @SerializedName("RegistrationNumber")
        private String registrationNumber;
        @SerializedName("CarTypePicture")
        private String carTypePicture;

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

        public String getDrivingExperience() {
            return drivingExperience;
        }

        public void setDrivingExperience(String drivingExperience) {
            this.drivingExperience = drivingExperience;
        }

        public String getTravelKilometers() {
            return travelKilometers;
        }

        public void setTravelKilometers(String travelKilometers) {
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
}
