package com.ticket.ui.activity;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.UserVo;
import com.ticket.netstatus.NetUtils;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.AppPreferences;
import com.ticket.utils.CommonUtils;
import com.ticket.utils.TLog;

import butterknife.InjectView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends BaseActivity {

    @InjectView(R.id.ed_user_phone)
    EditText ed_user_phone;
    @InjectView(R.id.ed_user_pwd)
    EditText ed_user_pwd;
    @InjectView(R.id.fl_btn_submit)
    FrameLayout fl_btn_submit;
    @InjectView(R.id.tv_register)
    TextView tv_register;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.t_login;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    Dialog mDialog = null;

    @Override
    protected void initViewsAndEvents() {
        mDialog = CommonUtils.createDialog(this);
        mDialog.setContentView(R.layout.loading);
        this.fl_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
                v.setBackgroundResource(R.drawable.login_button);
                if (NetUtils.isNetworkAvailable(LoginActivity.this)) {
                    if (validate()) {
                        fl_btn_submit.setEnabled(false);
                        Call<UserVo> userCall = getApis().login(ed_user_phone.getText().toString(), ed_user_pwd.getText().toString()).clone();
                        userCall.enqueue(new Callback<UserVo>() {
                            @Override
                            public void onResponse(Response<UserVo> response, Retrofit retrofit) {
                                CommonUtils.dismiss(mDialog);
                                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                                    UserVo user = response.body();
                                    TLog.d(TAG_LOG, user.toString());
                                    AppPreferences.putString("userId", user.getUserId());
                                    AppPreferences.putString("userPhone", ed_user_phone.getText().toString());
                                    AppPreferences.putString("userPwd", ed_user_pwd.getText().toString());
                                    readyGoThenKill(HomeActivity.class);
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                fl_btn_submit.setEnabled(true);
                                CommonUtils.dismiss(mDialog);
                            }
                        });
                    }
                } else {
                    CommonUtils.dismiss(mDialog);
                    CommonUtils.make(getWindow().getDecorView(), getString(R.string.no_network));
                }
            }
        });
        this.tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGo(RegisterActivity.class);
            }
        });
    }

    private boolean validate() {
        if (TextUtils.isEmpty(ed_user_phone.getText())) {
            CommonUtils.make(getWindow().getDecorView(), getString(R.string.login_phone_empty));
            return false;
        }
        if (!CommonUtils.isMobile(ed_user_phone.getText().toString())) {
            CommonUtils.make(getWindow().getDecorView(), getString(R.string.login_phone_ok));
            return false;
        }
        if (TextUtils.isEmpty(ed_user_pwd.getText())) {
            CommonUtils.make(getWindow().getDecorView(), getString(R.string.login_pwd_empty));
            return false;
        }
        if (ed_user_pwd.getText().length() < 6) {
            CommonUtils.make(getWindow().getDecorView(), getString(R.string.login_pwd_length));
            return false;
        }
        return true;
    }
}
