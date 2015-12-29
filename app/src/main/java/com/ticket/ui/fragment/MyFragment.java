package com.ticket.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.ui.activity.AboutActivity;
import com.ticket.ui.activity.LoginActivity;
import com.ticket.ui.activity.PassengerManagerActivity;
import com.ticket.ui.activity.RestAndForgotActivity;
import com.ticket.ui.base.BaseFragment;
import com.ticket.utils.AppPreferences;
import com.ticket.utils.CommonUtils;

import butterknife.InjectView;
import butterknife.OnClick;

public class MyFragment extends BaseFragment {

    @InjectView(R.id.tv_pass_info)
    TextView tv_pass_info;
    @InjectView(R.id.btn_login)
    Button btn_login;
    @InjectView(R.id.tv_quit)
    TextView tv_quit;
    @InjectView(R.id.tv_restpwd)
    TextView tv_restpwd;
    @InjectView(R.id.tv_login_title)
    TextView tv_login_title;
    @InjectView(R.id.tv_about)
    TextView tv_about;

    @OnClick(R.id.tv_quit)
    public void quitLogin() {
        AppPreferences.putString("userId", "");
        AppPreferences.putString("userPhone", "");
        AppPreferences.putString("userPwd", "");
        CommonUtils.make(getActivity(), "系统退出成功");
        tv_login_title.setText("");
        tv_login_title.setVisibility(View.GONE);
        btn_login.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_restpwd)
    public void restpwd() {
        readyGo(RestAndForgotActivity.class);
    }

    @OnClick(R.id.btn_login)
    public void login() {
        readyGo(LoginActivity.class);
    }

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
        if (!TextUtils.isEmpty(AppPreferences.getString("userId"))) {
            tv_login_title.setText(AppPreferences.getString("userPhone"));
            btn_login.setVisibility(View.GONE);
            tv_login_title.setVisibility(View.VISIBLE);
        } else {
            tv_login_title.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);
        }
        tv_pass_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(AppPreferences.getString("userId"))) {
                    readyGo(PassengerManagerActivity.class);
                } else {
                    readyGo(LoginActivity.class);
                }
            }
        });
        tv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGo(AboutActivity.class);
            }
        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.user_info;
    }
}
