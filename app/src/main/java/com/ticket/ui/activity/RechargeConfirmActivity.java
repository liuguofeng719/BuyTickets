package com.ticket.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.ui.base.BaseActivity;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by liuguofeng719 on 2016/3/29.
 */
public class RechargeConfirmActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.tv_recharge_ok)
    TextView tv_recharge_ok;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    //确认充值
    @OnClick(R.id.tv_recharge_ok)
    public void rechargeOk() {
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.recharge_confirm;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {

    }
}
