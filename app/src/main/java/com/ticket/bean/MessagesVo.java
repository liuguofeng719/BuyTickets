package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/4/6.
 */
public class MessagesVo implements Serializable{
    //留言ID
    @SerializedName("PublishUserID")
    private String publishUserId;
    //留言用户昵称
    @SerializedName("NickName")
    private String nickName;
    //用户头像
    @SerializedName("HeadPicture")
    private String headPicture;
    //发布日期
    @SerializedName("PublishDateTime")
    private String publishDateTime;
    //留言内容
    @SerializedName("Content")
    private String content;

    @Override
    public String toString() {
        return "MessagesVo{" +
                "publishUserId='" + publishUserId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", headPicture='" + headPicture + '\'' +
                ", publishDateTime='" + publishDateTime + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(String publishUserId) {
        this.publishUserId = publishUserId;
    }

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

    public String getPublishDateTime() {
        return publishDateTime;
    }

    public void setPublishDateTime(String publishDateTime) {
        this.publishDateTime = publishDateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
