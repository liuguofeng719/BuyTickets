package com.ticket.utils;

import android.content.Context;

import com.ticket.onekeyshare.OnekeyShare;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;

/**
 * Created by liuguofeng719 on 2016/3/25.
 */
public class ShareUtils {

    private Context context;

    public static void showShare(Context context, ShareVo shareVo) {

        OnekeyShare oks = new OnekeyShare();
        HashMap<String, Object> params = oks.getParams();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // text是分享文本，所有平台都需要这个字段
        oks.setText(shareVo.getText());
        if (ShareVo.platform.WECHAT.name().equals(shareVo.getType())) {
            oks.setUrl(shareVo.getUrl());
            oks.setTitle(shareVo.getTitle());
            params.put("shareType", Platform.SHARE_WEBPAGE);
        }
        if (ShareVo.platform.QQ.name().equals(shareVo.getType())) {
            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
            oks.setTitle(shareVo.getTitle());
            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
            oks.setTitleUrl(shareVo.getTitleUrl());
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
            oks.setComment(shareVo.getComment());
            // site是分享此内容的网站名称，仅在QQ空间使用
            oks.setSite(shareVo.getSite());
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSiteUrl(shareVo.getSiteUrl());
        }
        // 启动分享GUI
        oks.show(context);
    }
}
