package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

public class PicturesVo {

    @SerializedName("PictureUrl")
    private String pictureUrl;

    @SerializedName("NavigateUrl")
    private String navigateUrl;

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getNavigateUrl() {
        return navigateUrl;
    }

    public void setNavigateUrl(String navigateUrl) {
        this.navigateUrl = navigateUrl;
    }

    @Override
    public String toString() {
        return "PicturesVo{" +
                "pictureUrl='" + pictureUrl + '\'' +
                ", navigateUrl='" + navigateUrl + '\'' +
                '}';
    }
}
