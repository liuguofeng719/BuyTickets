package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

public class WXPayVo extends BaseInfoVo {

    //公众账号ID
    @SerializedName("appid")
    private String appId;
    //商户号
    @SerializedName("partnerid")
    private String partnerId;
    //预支付ID
    @SerializedName("prepayid")
    private String prepayId;
    //固定值Sign=WXPay
    @SerializedName("package")
    private String packageValue;
    //随机字符串32位
    @SerializedName("noncestr")
    private String nonceStr;
    //时间戳
    @SerializedName("timestamp")
    private String timeStamp;
    //签名
    @SerializedName("sign")
    private String sign;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "WXPayVo{" +
                "appId='" + appId + '\'' +
                ", partnerId='" + partnerId + '\'' +
                ", prepayId='" + prepayId + '\'' +
                ", packageValue='" + packageValue + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
