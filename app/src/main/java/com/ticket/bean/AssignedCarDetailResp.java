package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/7/23.
 */
public class AssignedCarDetailResp<T> extends BaseInfoVo {

    @SerializedName("AssignedCarDetail")
    private T assignedCarDetail;

    public T getAssignedCarDetail() {
        return assignedCarDetail;
    }

    public void setAssignedCarDetail(T assignedCarDetail) {
        this.assignedCarDetail = assignedCarDetail;
    }
}
