package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/4/14.
 */
public class MessageListResp<T> extends BaseInfoVo {

    @SerializedName("Messages")
    public T messages;

    public T getMessages() {
        return messages;
    }

    public void setMessages(T messages) {
        this.messages = messages;
    }
}
