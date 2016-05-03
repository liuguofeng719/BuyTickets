package com.ticket.ui.activity;

import android.app.Dialog;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.BaseInfoVo;
import com.ticket.bean.MessageVo;
import com.ticket.bean.UserVo;
import com.ticket.common.Constants;
import com.ticket.netstatus.NetUtils;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.AppPreferences;
import com.ticket.utils.CommonUtils;

import butterknife.InjectView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class BindingUserMobileActivity extends BaseActivity {

    @InjectView(R.id.ed_user_phone)
    EditText ed_user_phone;
    @InjectView(R.id.ed_verifyCode)
    EditText ed_verifyCode;
    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.btn_submit)
    Button btn_submit;
    @InjectView(R.id.btn_verify_code)
    Button btn_verify_code;
    CountTimer countTimer;
    String verifyCode;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.bind_number;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        countTimer = new CountTimer(Constants.reg.millisInFuture, Constants.reg.countDownInterval);
        this.tv_header_title.setText("绑定手机");
        this.btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTimer.cancel();
                finish();
            }
        });
        this.btn_verify_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtils.isNetworkAvailable(BindingUserMobileActivity.this)) {
                    if (validateUserInfo()) return;
                    countTimer.start();//开启验证码
                    Call<MessageVo> callMsg = getApis().getVerifyCode(ed_user_phone.getText().toString()).clone();
                    callMsg.enqueue(new Callback<MessageVo>() {

                        @Override
                        public void onResponse(Response<MessageVo> response, Retrofit retrofit) {
                            verifyCode = response.body().getVerifyCode();
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
                } else {
                    CommonUtils.make(BindingUserMobileActivity.this, getString(R.string.no_network));
                }
            }
        });

        this.btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtils.isNetworkAvailable(BindingUserMobileActivity.this)) {
                    if (validateSubmit()) {
                        final Dialog mDialog = CommonUtils.showDialog(BindingUserMobileActivity.this, "正在绑定手机号");
                        mDialog.show();
                        Call<UserVo> bindingMobile = getApis().bindingUserMobileNumber(AppPreferences.getString("userId"), ed_user_phone.getText().toString()).clone();
                        bindingMobile.enqueue(new Callback<UserVo>() {
                            @Override
                            public void onResponse(Response<UserVo> response, Retrofit retrofit) {
                                countTimer.cancel();
                                if (response.isSuccess() && response.body().isSuccessfully()) {
                                    CommonUtils.make(BindingUserMobileActivity.this, "手机号绑定成功");
                                    UserVo userVo = response.body();
                                    AppPreferences.putString("userId", userVo.getUserId());
                                    AppPreferences.putObject(userVo);
                                    finish();
                                } else {
                                    CommonUtils.dismiss(mDialog);
                                    countTimer.onFinish();
                                    if (response.body() != null) {
                                        BaseInfoVo body = response.body();
                                        CommonUtils.make(BindingUserMobileActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                                    } else {
                                        CommonUtils.make(BindingUserMobileActivity.this, CommonUtils.getCodeToStr(response.code()));
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                countTimer.cancel();
                                CommonUtils.dismiss(mDialog);
                            }
                        });
                    }
                } else {
                    CommonUtils.make(BindingUserMobileActivity.this, getString(R.string.no_network));
                }
            }
        });
    }

    class CountTimer extends CountDownTimer {
        /**
         * @param millisInFuture    时间间隔是多长的时间
         * @param countDownInterval 回调onTick方法，没隔多久执行一次
         */
        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        // 间隔时间结束的时候调用的方法
        @Override
        public void onTick(long millisUntilFinished) {
            // 更新页面的组件
            btn_verify_code.setText(millisUntilFinished / 1000 + "秒后发送");
            btn_verify_code.setClickable(false);
        }

        // 间隔时间内执行的操作
        @Override
        public void onFinish() {
            btn_verify_code.setText(getString(R.string.login_reg_code));
            btn_verify_code.setClickable(true);
        }
    }


    private boolean validateUserInfo() {
        if (TextUtils.isEmpty(ed_user_phone.getText())) {
            CommonUtils.make(BindingUserMobileActivity.this, getString(R.string.login_phone_empty));
            return true;
        }
        if (!CommonUtils.isMobile(ed_user_phone.getText().toString())) {
            CommonUtils.make(BindingUserMobileActivity.this, getString(R.string.login_phone_ok));
            return true;
        }
        return false;
    }

    private boolean validateSubmit() {
        if (validateUserInfo()) return false;
        if (TextUtils.isEmpty(ed_verifyCode.getText())) {
            CommonUtils.make(BindingUserMobileActivity.this, getString(R.string.login_reg_code_empty));
            return false;
        }
        return true;
    }
}
