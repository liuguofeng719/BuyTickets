package com.ticket.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.BalanceVo;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.AppPreferences;
import com.ticket.utils.CommonUtils;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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
    @InjectView(R.id.tv_trade_total_amount)
    TextView tv_trade_total_amount;
    private Call<BalanceVo> balanceVoCall;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }
    //充值
    @OnClick(R.id.tv_recharge)
    public void recharge() {
        readyGo(RechargeActivity.class);
    }

    //提现
    @OnClick(R.id.tv_withdraw_deposit)
    public void withDrawDeposit() {
        Bundle bundle = new Bundle();
        bundle.putString("money",tv_trade_total_amount.getText().toString());
        readyGo(WithdrawActivity.class,bundle);
    }

    //收支明细
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
        balanceVoCall = getApis().getBanlance(AppPreferences.getString("userId")).clone();
        balanceVoCall.enqueue(new Callback<BalanceVo>() {
            @Override
            public void onResponse(Response<BalanceVo> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    BalanceVo balanceVo = response.body();
                    tv_trade_total_amount.setText(balanceVo.getBalance());
                } else {
                    if (response.body() != null) {
                        BalanceVo body = response.body();
                        CommonUtils.make(MyWalletActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                    } else {
                        CommonUtils.make(MyWalletActivity.this, CommonUtils.getCodeToStr(response.code()));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (balanceVoCall != null) {
            balanceVoCall.cancel();
        }
    }
}
