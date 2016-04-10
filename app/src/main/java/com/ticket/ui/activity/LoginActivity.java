package com.ticket.ui.activity;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.UserVo;
import com.ticket.netstatus.NetUtils;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.AppPreferences;
import com.ticket.utils.CommonUtils;
import com.ticket.utils.TLog;

import java.util.HashMap;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends BaseActivity implements PlatformActionListener {

    @InjectView(R.id.ed_user_phone)
    EditText ed_user_phone;
    @InjectView(R.id.ed_user_pwd)
    EditText ed_user_pwd;
    @InjectView(R.id.fl_btn_submit)
    FrameLayout fl_btn_submit;
    @InjectView(R.id.tv_register)
    TextView tv_register;
    @InjectView(R.id.tv_forgot)
    TextView tv_forgot;

    @InjectView(R.id.iv_qq_login)
    ImageView iv_qq_login;
    @InjectView(R.id.iv_weixin_login)
    ImageView iv_weixin_login;

    @OnClick(R.id.iv_qq_login)
    public void qqLogin() {
        authorize(new QQ(this), "QQ");
    }

    @OnClick(R.id.iv_weixin_login)
    public void weixinLogin() {
        authorize(new Wechat(this), "WeiXin");
    }

    private void authorize(Platform plat, String type) {
        plat.isClientValid();
        if (plat.isValid()) {
            String userId = plat.getDb().getUserId();//关联唯一ID
            final String nickName = plat.getDb().getUserName();//nickname
            final String userIcon = plat.getDb().getUserIcon().replaceAll("/40","/100");
            Call<UserVo> userVoCall = getApis().externalSystemAuthentication(userIcon, type, userId, nickName).clone();
            userVoCall.enqueue(new Callback<UserVo>() {
                @Override
                public void onResponse(Response<UserVo> response, Retrofit retrofit) {
                    if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                        UserVo userVo = response.body();
                        AppPreferences.putString("userId", userVo.getUserId());
                        AppPreferences.putObject(userVo);
                        finish();
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }
        plat.setPlatformActionListener(this);
        plat.SSOSetting(true);
        plat.showUser(null);
    }

    @OnClick(R.id.tv_forgot)
    public void forgot() {
        readyGo(RestAndForgotActivity.class);
    }

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
        mDialog = CommonUtils.showDialog(this);
        this.fl_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
                v.setBackgroundResource(R.drawable.login_button);
                if (NetUtils.isNetworkAvailable(LoginActivity.this)) {
                    if (validate()) {
                        Call<UserVo> userCall = getApis().login(ed_user_phone.getText().toString(), ed_user_pwd.getText().toString()).clone();
                        userCall.enqueue(new Callback<UserVo>() {
                            @Override
                            public void onResponse(Response<UserVo> response, Retrofit retrofit) {
                                CommonUtils.dismiss(mDialog);
                                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                                    UserVo userVo = response.body();
                                    TLog.d(TAG_LOG, userVo.toString());
                                    AppPreferences.putString("userId", userVo.getUserId());
                                    AppPreferences.putObject(userVo);
                                    finish();
                                } else {
                                    if (response.body() != null) {
                                        UserVo body = response.body();
                                        CommonUtils.make(LoginActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                                    } else {
                                        CommonUtils.make(LoginActivity.this, CommonUtils.getCodeToStr(response.code()));
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                CommonUtils.dismiss(mDialog);
                            }
                        });
                    }
                } else {
                    CommonUtils.dismiss(mDialog);
                    CommonUtils.make(LoginActivity.this, getString(R.string.no_network));
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
            CommonUtils.make(LoginActivity.this, getString(R.string.login_phone_empty));
            return false;
        }
        if (!CommonUtils.isMobile(ed_user_phone.getText().toString())) {
            CommonUtils.make(LoginActivity.this, getString(R.string.login_phone_ok));
            return false;
        }
        if (TextUtils.isEmpty(ed_user_pwd.getText())) {
            CommonUtils.make(LoginActivity.this, getString(R.string.login_pwd_empty));
            return false;
        }
        if (ed_user_pwd.getText().length() < 6) {
            CommonUtils.make(LoginActivity.this, getString(R.string.login_pwd_length));
            return false;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            readyGo(IndexActivity.class);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }
}
