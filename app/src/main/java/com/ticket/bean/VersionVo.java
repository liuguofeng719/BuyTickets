package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/5/18.
 */
public class VersionVo extends BaseInfoVo {

    @SerializedName("VersionCode")
    private int versionCode;
    @SerializedName("AppUrl")
    private String appUrl;
    @SerializedName("IsForcingUpdate")
    private boolean isForcingUpdate;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public boolean isForcingUpdate() {
        return isForcingUpdate;
    }

    public void setForcingUpdate(boolean forcingUpdate) {
        isForcingUpdate = forcingUpdate;
    }
}
