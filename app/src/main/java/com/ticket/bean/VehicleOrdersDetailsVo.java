package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuguofeng719 on 2016/4/6.
 */
public class VehicleOrdersDetailsVo extends BaseInfoVo {

    @SerializedName("LeasedVehicleOrderTripList")
    List<TripVo> leasedVehicleOrderTripList;
    @SerializedName("CompanyQuote")
    List<QuoteVo> companyQuote;
    @SerializedName("VehicleOrdersDetails")
    VehicleOrdersVo vehicleOrdersDetails;

    public List<TripVo> getLeasedVehicleOrderTripList() {
        return leasedVehicleOrderTripList;
    }

    public void setLeasedVehicleOrderTripList(List<TripVo> leasedVehicleOrderTripList) {
        this.leasedVehicleOrderTripList = leasedVehicleOrderTripList;
    }

    public List<QuoteVo> getCompanyQuote() {
        return companyQuote;
    }

    public void setCompanyQuote(List<QuoteVo> companyQuote) {
        this.companyQuote = companyQuote;
    }

    public VehicleOrdersVo getVehicleOrdersDetails() {
        return vehicleOrdersDetails;
    }

    public void setVehicleOrdersDetails(VehicleOrdersVo vehicleOrdersDetails) {
        this.vehicleOrdersDetails = vehicleOrdersDetails;
    }

    @Override
    public String toString() {
        return "VehicleOrdersDetailsVo{" +
                "leasedVehicleOrderTripList=" + leasedVehicleOrderTripList +
                ", companyQuote=" + companyQuote +
                ", vehicleOrdersDetails=" + vehicleOrdersDetails +
                '}';
    }
}
