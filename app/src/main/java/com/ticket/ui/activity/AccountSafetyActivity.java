package com.ticket.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.UserVo;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.AppPreferences;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by liuguofeng719 on 2016/3/29.
 */
public class AccountSafetyActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;

    @InjectView(R.id.tv_qq_account)
    TextView tv_qq_account;
    @InjectView(R.id.tv_wx_account)
    TextView tv_wx_account;
    @InjectView(R.id.tv_phone_account)
    TextView tv_phone_account;
    @InjectView(R.id.tv_restpwd)
    TextView tv_restpwd;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @OnClick(R.id.tv_restpwd)
    public void restpwd() {
        readyGo(RestAndForgotActivity.class);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.account_safety;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText("账号与安全");
    }

    @Override
    protected void onResume() {
        super.onResume();
        initBindUser();
    }

    private void initBindUser() {
        UserVo userVo = AppPreferences.getObject(UserVo.class);

        if (userVo.getIsBindingWeiXin()) {
            tv_wx_account.setText("已绑定");
        } else {
            tv_wx_account.setText("未绑定");
            tv_wx_account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    authorize(new Wechat(AccountSafetyActivity.this), "WeiXin");
                }
            });
        }
        if (userVo.getIsBindingQQ()) {
            tv_qq_account.setText("已绑定");
        } else {
            tv_qq_account.setText("未绑定");
            tv_qq_account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    authorize(new QQ(AccountSafetyActivity.this), "QQ");
                }
            });
        }
        if (userVo.getIsBindingPhoneNumber()) {
            tv_phone_account.setText(userVo.getPhoneNumber());
        } else {
            tv_phone_account.setText("未绑定");
            tv_phone_account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    readyGo(RegisterActivity.class);
                }
            });
        }
    }

    private void authorize(Platform plat, final String type) {
        if (plat.isValid()) {
            String userId = plat.getDb().getUserId();//关联唯一ID
            final String nickName = plat.getDb().getUserName();//nickname
            final String userIcon = plat.getDb().getUserIcon().replaceAll("/40", "/100");
            Call<UserVo> userVoCall = getApis().externalSystemAuthentication(AppPreferences.getString("userId"), userIcon, type, userId, nickName);
            userVoCall.enqueue(new Callback<UserVo>() {
                @Override
                public void onResponse(Response<UserVo> response, Retrofit retrofit) {
                    if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                        UserVo userVo = response.body();
                        AppPreferences.putString("userId", userVo.getUserId());
                        AppPreferences.putObject(userVo);
                        if ("QQ".equals(type)) {
                            tv_qq_account.setText("已绑定");
                        }
                        if ("WeiXin".equals(type)) {
                            tv_qq_account.setText("已绑定");
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }
        plat.SSOSetting(true);
        plat.showUser(null);
    }
}
