package com.ticket.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.ui.base.BaseActivity;

import butterknife.InjectView;
import butterknife.OnClick;

public class CouponActivity extends BaseActivity {


    @InjectView(R.id.btn_back)
    ImageView btn_back;

    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.coupon_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText("优惠券");
    }
}
