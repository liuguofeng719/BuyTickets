package com.ticket.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.ui.base.BaseActivity;

import butterknife.InjectView;

public class PayMentModeActivity extends BaseActivity {

    @InjectView(R.id.ly_weixin_pay)
    LinearLayout ly_weixin_pay;
    @InjectView(R.id.ly_bao_pay)
    LinearLayout ly_bao_pay;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.btn_back)
    ImageView btn_back;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.pay_ment_mode;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_header_title.setText(getString(R.string.pay_mode_title));
        ly_weixin_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ly_bao_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
