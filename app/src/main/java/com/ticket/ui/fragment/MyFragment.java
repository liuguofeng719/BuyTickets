package com.ticket.ui.fragment;

import android.view.View;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.ui.activity.PassengerManagerActivity;
import com.ticket.ui.base.BaseFragment;

import butterknife.InjectView;

public class MyFragment extends BaseFragment {

    @InjectView(R.id.tv_pass_info)
    TextView tv_pass_info;

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_pass_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGo(PassengerManagerActivity.class);
            }
        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.user_info;
    }
}
