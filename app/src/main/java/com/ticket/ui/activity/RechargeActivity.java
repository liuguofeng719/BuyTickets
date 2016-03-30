package com.ticket.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.CommonUtils;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by liuguofeng719 on 2016/3/29.
 */
public class RechargeActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.tv_recharge_ok)
    TextView tv_recharge_ok;
    @InjectView(R.id.edit_withraw_money)
    EditText edit_withraw_money;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    //提交充值
    @OnClick(R.id.tv_recharge_ok)
    public void rechargeOk() {
        if (TextUtils.isEmpty(edit_withraw_money.getText())) {
            CommonUtils.make(this, "充值金额不能为空!");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("money", edit_withraw_money.getText().toString());
        readyGo(RechargeConfirmActivity.class, bundle);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.recharge;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText("充值页面");
    }
}
