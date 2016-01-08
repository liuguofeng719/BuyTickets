package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

public class Pictures extends BaseInfoVo {

    @SerializedName("Pictures")
    public String[] pictures;

    public String[] getPictures() {
        return pictures;
    }

    public void setPictures(String[] pictures) {
        this.pictures = pictures;
    }
}
