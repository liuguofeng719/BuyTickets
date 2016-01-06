package com.ticket.ui.activity;

import android.app.Dialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ticket.R;
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

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.iv_q)
    public void insuranceInfo() {
        final Dialog mDialogInfo = CommonUtils.createDialog(this);
        mDialogInfo.setContentView(R.layout.dialog_tips_info);
        ((TextView) mDialogInfo.findViewById(R.id.tv_tips_content)).setText(getString(R.string.tips_info_content_insurance));
        ((TextView) mDialogInfo.findViewById(R.id.tv_tips_title)).setText("选择服务");
        mDialogInfo.findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogInfo.dismiss();
            }
        });
        mDialogInfo.show();
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

    }
}
