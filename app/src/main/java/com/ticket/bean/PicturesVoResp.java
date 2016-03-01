package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

public class PicturesVoResp<T> extends BaseInfoVo {

    @SerializedName("Pictures")
    public T pictures;

    public T getPictures() {
        return pictures;
    }

    public void setPictures(T pictures) {
        this.pictures = pictures;
    }

    @Override
    public String toString() {
        return "PicturesVoResp{" +
                "pictures=" + pictures +
                '}';
    }
}
