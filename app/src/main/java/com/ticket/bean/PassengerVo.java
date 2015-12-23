package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

public class PassengerVo {

    @SerializedName("PassengerID")
    private String passengerId;
    @SerializedName("FullName")
    private String fullName;
    @SerializedName("IDCard")
    private String idCard;
    @SerializedName("MobileNumber")
    private String mobileNumber;

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PassengerVo that = (PassengerVo) o;

        if (passengerId != null ? !passengerId.equals(that.passengerId) : that.passengerId != null)
            return false;
        if (fullName != null ? !fullName.equals(that.fullName) : that.fullName != null)
            return false;
        if (idCard != null ? !idCard.equals(that.idCard) : that.idCard != null) return false;
        return !(mobileNumber != null ? !mobileNumber.equals(that.mobileNumber) : that.mobileNumber != null);

    }

    @Override
    public int hashCode() {
        int result = passengerId != null ? passengerId.hashCode() : 0;
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (idCard != null ? idCard.hashCode() : 0);
        result = 31 * result + (mobileNumber != null ? mobileNumber.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PassengerVo{" +
                "passengerId='" + passengerId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", idCard='" + idCard + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }

}
