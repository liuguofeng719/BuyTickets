package com.ticket.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.ui.base.BaseActivity;

import butterknife.InjectView;

public class PaySuccessActivity extends BaseActivity {

    @InjectView(R.id.btn_back_home)
    Button btn_back_home;
    @InjectView(R.id.btn_myorder)
    Button btn_myorder;
    @InjectView(R.id.tv_pay_message)
    TextView tv_pay_message;
    @InjectView(R.id.iv_pay_icon)
    ImageView iv_pay_icon;

    private Bundle extras;

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

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
        ((TextView) findViewById(R.id.tv_header_title)).setText("支付结果");
        String msg = extras.getString("msg");
        tv_pay_message.setText(msg);
        String status = extras.getString("status");
        if ("0".equals(status)) {
            iv_pay_icon.setImageResource(R.drawable.pay_failed);
        }

        btn_back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaySuccessActivity.this, IndexActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        btn_myorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaySuccessActivity.this, IndexActivity.class);
                intent.putExtra("current", 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}
