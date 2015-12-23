package com.ticket.ui.activity;

import android.view.View;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.ticket.common.Constants;
import com.ticket.ui.base.BaseActivity;

public class WXPayActivity extends BaseActivity {

    private IWXAPI api;

    @Override
    protected int getContentViewLayoutID() {
        return 0;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        api = WXAPIFactory.createWXAPI(this, Constants.wxpay.APPID);
//        PayReq req = new PayReq();
//        req.appId = json.getString("appid");
//        req.partnerId = json.getString("partnerid");
//        req.prepayId = json.getString("prepayid");
//        req.nonceStr = json.getString("noncestr");
//        req.timeStamp = json.getString("timestamp");
//        req.packageValue = json.getString("package");
//        req.sign = json.getString("sign");
//        req.extData = "app data"; // optional
//        api.sendReq(req);
    }
}
