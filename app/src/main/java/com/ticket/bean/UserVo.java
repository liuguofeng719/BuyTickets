package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

public class UserVo extends BaseInfoVo {

    @SerializedName("UserID")
    private String userId;
    @SerializedName("NickName")
    private String nickName;
    @SerializedName("HeadPicture")
    private String headPicture;
    @SerializedName("IsBindingQQ")
    private boolean isBindingQQ;
    @SerializedName("QQOpenID")
    private String qqOpenId;
    @SerializedName("IsBindingWeiXin")
    private boolean isBindingWeiXin;
    @SerializedName("WeixinOpenID")
    private String weixinOpenId;
    @SerializedName("IsBindingPhoneNumber")
    private boolean isBindingPhoneNumber;
    @SerializedName("PhoneNumber")
    private String phoneNumber;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPicture() {
        return headPicture;
    }

    public void setHeadPicture(String headPicture) {
        this.headPicture = headPicture;
    }

    public boolean getIsBindingQQ() {
        return isBindingQQ;
    }

    public void setIsBindingQQ(boolean isBindingQQ) {
        this.isBindingQQ = isBindingQQ;
    }

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId;
    }

    public boolean getIsBindingWeiXin() {
        return isBindingWeiXin;
    }

    public void setIsBindingWeiXin(boolean isBindingWeiXin) {
        this.isBindingWeiXin = isBindingWeiXin;
    }

    public String getWeixinOpenId() {
        return weixinOpenId;
    }

    public void setWeixinOpenId(String weixinOpenId) {
        this.weixinOpenId = weixinOpenId;
    }

    public boolean getIsBindingPhoneNumber() {
        return isBindingPhoneNumber;
    }

    public void setIsBindingPhoneNumber(boolean isBindingPhoneNumber) {
        this.isBindingPhoneNumber = isBindingPhoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserVo{" +
                "userId='" + userId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", headPicture='" + headPicture + '\'' +
                ", isBindingQQ=" + isBindingQQ +
                ", qqOpenId='" + qqOpenId + '\'' +
                ", isBindingWeiXin=" + isBindingWeiXin +
                ", weixinOpenId='" + weixinOpenId + '\'' +
                ", isBindingPhoneNumber=" + isBindingPhoneNumber +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
