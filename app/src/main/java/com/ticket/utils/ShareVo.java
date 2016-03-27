package com.ticket.utils;

/**
 * Created by liuguofeng719 on 2016/3/25.
 */
public class ShareVo {

    //title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
    private String title;
    //titleUrl是标题的网络链接，仅在人人网和QQ空间使用
    private String titleUrl;
    //text是分享文本，所有平台都需要这个字段
    private String text;
    //url仅在微信（包括好友和朋友圈）中使用
    private String url;
    //comment是我对这条分享的评论，仅在人人网和QQ空间使用
    private String comment;
    //site是分享此内容的网站名称，仅在QQ空间使用
    private String site;
    //siteUrl是分享此内容的网站地址，仅在QQ空间使用
    private String siteUrl;
    //平台类型
    private String type;
    //微信分享类型
    private int shareType;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getShareType() {
        return shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public enum platform {
        QQ, WECHAT
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleUrl() {
        return titleUrl;
    }

    public void setTitleUrl(String titleUrl) {
        this.titleUrl = titleUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    @Override
    public String toString() {
        return "ShareVo{" +
                "title='" + title + '\'' +
                ", titleUrl='" + titleUrl + '\'' +
                ", text='" + text + '\'' +
                ", url='" + url + '\'' +
                ", comment='" + comment + '\'' +
                ", site='" + site + '\'' +
                ", siteUrl='" + siteUrl + '\'' +
                '}';
    }
}
