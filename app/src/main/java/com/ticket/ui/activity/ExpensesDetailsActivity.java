package com.ticket.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.ui.base.BaseActivity;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by liuguofeng719 on 2016/3/28.
 */
public class ExpensesDetailsActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.lv_expenses_details)
    ListView lv_expenses_details;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.expenses_details;
    }

    @Override
    protected View getLoadingTargetView() {
        return lv_expenses_details;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText("收支明细");
    }
}
