package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/9/8.
 */
public class QRCodeVo extends BaseInfoVo {

    @SerializedName("QRCodeUrl")
    private String qrCodeUrl;

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }
}
