package com.ticket.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.common.Constants;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.CommonUtils;

import butterknife.InjectView;
import butterknife.OnClick;

public class InsuranceActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.iv_q)
    ImageView iv_q;
    @InjectView(R.id.rl_cbo_selected)
    RelativeLayout rl_cbo_selected;
    @InjectView(R.id.rl_cbo_no_selected)
    RelativeLayout rl_cbo_no_selected;
    @InjectView(R.id.cbo_selected)
    CheckBox cbo_selected;
    @InjectView(R.id.cbo_no_selected)
    CheckBox cbo_no_selected;

    @InjectView(R.id.tv_service_price)
    TextView tv_service_price;

    @OnClick(R.id.btn_back)
    public void back() {
        Intent intent = new Intent();
        if (cbo_no_selected.isChecked()) {
            intent.putExtra("insuranceCheck", false);
        }
        if (cbo_selected.isChecked()) {
            intent.putExtra("insuranceCheck", true);
        }
        setResult(Constants.comm.INSURANCE_PRICE_SUCCESS, intent);
        finish();
    }

    @OnClick(R.id.rl_cbo_selected)
    public void cboSelected() {
        cbo_no_selected.setChecked(false);
        cbo_selected.setChecked(true);
    }

    @OnClick(R.id.rl_cbo_no_selected)
    public void cboNoSelected() {
        cbo_selected.setChecked(false);
        cbo_no_selected.setChecked(true);
    }

    @OnClick(R.id.iv_q)
    public void insuranceInfo() {
        final Dialog mDialogInfo = CommonUtils.createDialog(this);
        mDialogInfo.setContentView(R.layout.dialog_tips_info);
        ((TextView) mDialogInfo.findViewById(R.id.tv_tips_content)).setText(getString(R.string.tips_info_content_insurance));
        ((TextView) mDialogInfo.findViewById(R.id.tv_tips_title)).setText("乘车保险协议介绍");
        mDialogInfo.findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogInfo.dismiss();
            }
        });
        mDialogInfo.show();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        tv_service_price.setText(extras.getString("insurancePrice")+"元（份）");
        boolean insunranceCheck = extras.getBoolean("insuranceCheck", true);
        if (insunranceCheck) {
            cbo_selected.setChecked(true);
            cbo_no_selected.setChecked(false);
        } else {
            cbo_selected.setChecked(false);
            cbo_no_selected.setChecked(true);
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.insurance_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText("选择服务");
    }
}
