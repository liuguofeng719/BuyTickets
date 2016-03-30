package com.ticket.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
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
    @InjectView(R.id.edit_withraw_money)
    TextView edit_withraw_money;
    @InjectView(R.id.rl_cbo_wenxin_selected)
    FrameLayout rl_cbo_wenxin_selected;
    @InjectView(R.id.rl_cbo_pay_selected)
    FrameLayout rl_cbo_pay_selected;
    @InjectView(R.id.cbo_wenxin_selected)
    CheckBox cbo_wenxin_selected;
    @InjectView(R.id.cbo_qq_selected)
    CheckBox cbo_qq_selected;

    private String money;

    @OnClick({R.id.rl_cbo_wenxin_selected, R.id.rl_cbo_pay_selected})
    public void cboSelected(View view) {
        switch (view.getId()) {
            case R.id.rl_cbo_wenxin_selected:
                switchSelected(true, false);
                break;
            case R.id.rl_cbo_pay_selected:
                switchSelected(false, true);
                break;
            default:
                break;
        }
    }

    public void switchSelected(boolean wxChecked, boolean qqChecked) {
        cbo_wenxin_selected.setChecked(wxChecked);
        cbo_qq_selected.setChecked(qqChecked);
    }

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    //确认充值
    @OnClick(R.id.tv_recharge_ok)
    public void rechargeOk() {

    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        money = extras.getString("money");
        edit_withraw_money.setText("￥" + extras.getString("money"));
        tv_recharge_ok.setText("确认充值 ￥" + extras.getString("money"));
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
        tv_header_title.setText("充值订单");
    }
}
