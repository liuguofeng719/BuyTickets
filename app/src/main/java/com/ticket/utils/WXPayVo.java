package com.ticket.utils;

/**
 * Created by liuguofeng719 on 2015/12/10.
 */
public class WXPayVo {

    //微信公众号Id
    private String appId;
    //微信公众号密码
    private String secret;
    //微信商户ID
    private String partnerId;
    //微信商户key
    private String partnerKey;
    //回调Url
    private String notifyUrl;
    //预支付ID
    private String prepayId;
    //固定值Sign=WXPay
    private String packageValue;
    //随机字符串32位
    private String nonceStr;
    //时间戳
    private String timeStamp;
    //签名
    private String sign;

    public String getAppId() {
        return appId;
    }

    public void setAppid(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerKey() {
        return partnerKey;
    }

    public void setPartnerKey(String partnerKey) {
        this.partnerKey = partnerKey;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}
