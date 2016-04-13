package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/4/13.
 */
public class LeasedVehicleListResp<T> extends BaseInfoVo {

    @SerializedName("LeasedVehicleOrderList")
    public T leasedVehicleOrderList;

    public T getLeasedVehicleOrderList() {
        return leasedVehicleOrderList;
    }

    public void setLeasedVehicleOrderList(T leasedVehicleOrderList) {
        this.leasedVehicleOrderList = leasedVehicleOrderList;
    }

    @Override
    public String toString() {
        return "LeasedVehicleListResp{" +
                "leasedVehicleOrderList=" + leasedVehicleOrderList +
                '}';
    }
}
