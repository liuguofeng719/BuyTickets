package com.ticket.wxapi;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.ticket.R;
import com.ticket.common.Constants;
import com.ticket.ui.activity.IndexActivity;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.TLog;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.pay_success;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        findViewById(R.id.btn_back).setVisibility(View.GONE);
        ((Button) findViewById(R.id.btn_back_home)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WXPayEntryActivity.this, IndexActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        ((Button) findViewById(R.id.btn_myorder)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WXPayEntryActivity.this, IndexActivity.class);
                intent.putExtra("order", 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        ((TextView) findViewById(R.id.tv_header_title)).setText("微信支付");
        api = WXAPIFactory.createWXAPI(this, Constants.wxpay.APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        //resp.errCode==  0 ：表示支付成功
        //resp.errCode== -1 ：表示支付失败
       // resp.errCode== -2 ：表示取消支付
        TLog.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        ImageView imgView = (ImageView) findViewById(R.id.iv_pay_icon);
        TextView textView = (TextView) findViewById(R.id.tv_pay_message);
        if (resp.errCode==0) {
            imgView.setImageResource(R.drawable.success);
            textView.setText("付款成功");
        } else if(resp.errCode==-1){
            imgView.setImageResource(R.drawable.pay_failed);
            textView.setText("付款失败");
        }else if(resp.errCode==-2) {
            imgView.setImageResource(R.drawable.pay_failed);
            textView.setText("取消支付,支付失败");
        }
    }
}