package com.ticket.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.ui.base.BaseActivity;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by liuguofeng719 on 2016/3/28.
 */
public class MyWalletActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.tv_income_details)
    TextView tv_income_details;
    @InjectView(R.id.tv_recharge)
    TextView tv_recharge;
    @InjectView(R.id.tv_withdraw_deposit)
    TextView tv_withdraw_deposit;

    //充值
    @OnClick(R.id.tv_recharge)
    public void recharge() {
        readyGo(RechargeActivity.class);
    }

    //提现
    @OnClick(R.id.tv_withdraw_deposit)
    public void withdrawDeposit() {
        readyGo(WithdrawActivity.class);
    }

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @OnClick(R.id.tv_income_details)
    public void toIncomeDetails() {
        readyGo(ExpensesDetailsActivity.class);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.my_wallet;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText("我的钱包");
    }
}
