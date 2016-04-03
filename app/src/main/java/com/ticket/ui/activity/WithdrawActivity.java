package com.ticket.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.BankAccountVo;
import com.ticket.bean.BankAccountVoResp;
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
public class WithdrawActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.tv_total_amount)
    TextView tv_total_amount;
    @InjectView(R.id.ll_content)
    LinearLayout ll_content;

    @InjectView(R.id.tv_withdraw_action)
    TextView tv_withdraw_action;
    @InjectView(R.id.edit_car_number)
    EditText edit_car_number;
    @InjectView(R.id.edit_bank)
    EditText edit_bank;
    @InjectView(R.id.edit_home_name)
    EditText edit_home_name;
    @InjectView(R.id.edit_withraw_money)
    EditText edit_withraw_money;

    String money;
    private Call<BankAccountVoResp<BankAccountVo>> bankAccountVoRespCall;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @OnClick(R.id.tv_withdraw_action)
    public void withDrawAction() {
        String validate = validate();
        if (TextUtils.isEmpty(validate)) {
            CommonUtils.make(this, validate);
            return;
        }
    }

    private String validate() {
        if (TextUtils.isEmpty(edit_car_number.getText())) {
            return "请输入卡号";
        }
        if (TextUtils.isEmpty(edit_bank.getText())) {
            return "请输入开户行";
        }
        if (TextUtils.isEmpty(edit_home_name.getText())) {
            return "请输入户名";
        }
        if (TextUtils.isEmpty(edit_withraw_money.getText())) {
            return "请输体现金额";
        }
        if (Double.parseDouble(edit_withraw_money.getText().toString()) > Double.parseDouble(money)) {
            return "体现金额不能大于总金额";
        }
        return null;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        money = extras.getString("money");
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.withdraw;
    }

    @Override
    protected View getLoadingTargetView() {
        return ll_content;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText("提现");
        tv_total_amount.setText(money);
        showLoading("数据加载中");
        bankAccountVoRespCall = getApis().getBankAccount(AppPreferences.getString("userId"));
        bankAccountVoRespCall.enqueue(new Callback<BankAccountVoResp<BankAccountVo>>() {
            @Override
            public void onResponse(Response<BankAccountVoResp<BankAccountVo>> response, Retrofit retrofit) {
                hideLoading();
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    BankAccountVo bankAccount = response.body().getBankAccount();
                    if (bankAccount != null) {
                        edit_car_number.setText(bankAccount.getBankAccount());
                        edit_bank.setText(bankAccount.getBankName());
                        edit_home_name.setText(bankAccount.getBankRealName());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bankAccountVoRespCall != null) {
            bankAccountVoRespCall.cancel();
        }
    }
}
