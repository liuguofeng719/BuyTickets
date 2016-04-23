package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/4/23.
 */
public class ShareMessageVo extends BaseInfoVo {

    @SerializedName("ShareMessage")
    private String shareMessage;
    @SerializedName("NavigateUrl")
    private String navigateUrl;

    public String getNavigateUrl() {
        return navigateUrl;
    }

    public void setNavigateUrl(String navigateUrl) {
        this.navigateUrl = navigateUrl;
    }

    public String getShareMessage() {
        return shareMessage;
    }

    public void setShareMessage(String shareMessage) {
        this.shareMessage = shareMessage;
    }
}
