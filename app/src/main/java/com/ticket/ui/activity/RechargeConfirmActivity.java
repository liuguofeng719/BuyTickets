package com.ticket.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.ticket.R;
import com.ticket.bean.AlipayVo;
import com.ticket.bean.WXPayVo;
import com.ticket.common.Constants;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.AppPreferences;
import com.ticket.utils.CommonUtils;
import com.ticket.utils.TLog;
import com.ticket.utils.alipay.PayResult;
import com.ticket.utils.wxpay.MD5;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    private String money;
    private IWXAPI api;
    private Dialog mDialog;
    private String partner;
    private String userId;

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
        if (cbo_wenxin_selected.isChecked()) {
            wenXinPay();
        }

        if (cbo_qq_selected.isChecked()) {
            alipay();
        }

    }

    private void wenXinPay() {
        mDialog = CommonUtils.showDialog(RechargeConfirmActivity.this, "正在加载微信支付");
        mDialog.show();
        api = WXAPIFactory.createWXAPI(RechargeConfirmActivity.this, Constants.wxpay.APPID);
        api.registerApp(Constants.wxpay.APPID);
        Call<WXPayVo> callWX = getApis().rechargeByWeiXin(userId, money).clone();
        callWX.enqueue(new Callback<WXPayVo>() {
            @Override
            public void onResponse(final Response<WXPayVo> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                WXPayVo wxPayVo = response.body();
                                TLog.d(TAG_LOG, wxPayVo.toString());

                                PayReq req = new PayReq();
                                req.appId = wxPayVo.getAppId();
                                req.partnerId = wxPayVo.getPartnerId();
                                req.prepayId = wxPayVo.getPrepayId();
                                req.nonceStr = wxPayVo.getNonceStr();
                                req.timeStamp = wxPayVo.getTimeStamp();
                                req.packageValue = "Sign=WXPay";

                                List<NameValuePair> signParams = new LinkedList<NameValuePair>();
                                signParams.add(new BasicNameValuePair("appid", req.appId));
                                signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
                                signParams.add(new BasicNameValuePair("package", req.packageValue));
                                signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
                                signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
                                signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
                                req.sign = genAppSign(signParams);
                                Message message = handler.obtainMessage();
                                if (api.sendReq(req)) {
                                    message.what = 1;
                                    handler.sendMessage(message);
                                } else {
                                    message.what = 0;
                                    handler.sendMessage(message);
                                }
                                TLog.d(TAG_LOG, api.sendReq(req) + "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    if (response.body() != null) {
                        WXPayVo body = response.body();
                        CommonUtils.make(RechargeConfirmActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                    } else {
                        CommonUtils.make(RechargeConfirmActivity.this, CommonUtils.getCodeToStr(response.code()));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            CommonUtils.dismiss(mDialog);
            switch (msg.what) {
                case 1:
                    Intent intent = new Intent(RechargeConfirmActivity.this,MyWalletActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    break;
                case 0:
                    Bundle bundle = new Bundle();
                    bundle.putString("status", "0");
                    bundle.putString("msg", "微信充值支付失败");
                    readyGo(PaySuccessActivity.class, bundle);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.wxpay.API_KEY);
        TLog.d(TAG_LOG, sb.toString());
        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        TLog.d(TAG_LOG, appSign);
        return appSign;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    Bundle bundle = new Bundle();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        bundle.putString("status", "9000");
                        bundle.putString("msg", "支付成功");
                        Intent intent = new Intent(RechargeConfirmActivity.this,MyWalletActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            bundle.putString("status", "8000");
                            bundle.putString("msg", "支付结果确认中");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            bundle.putString("status", "0");
                            bundle.putString("msg", "支付宝支付失败");
                        }
                        readyGo(PaySuccessActivity.class, bundle);
                    }
                    break;
                case SDK_CHECK_FLAG:
                    Toast.makeText(RechargeConfirmActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private void alipay() {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(RechargeConfirmActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(partner);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
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
        userId = AppPreferences.getString("userId");
        Call<AlipayVo> siginCall = getApis().rechargeByAlipay(userId, money).clone();
        siginCall.enqueue(new Callback<AlipayVo>() {
            @Override
            public void onResponse(Response<AlipayVo> response, Retrofit retrofit) {

                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    partner = response.body().getSignedContent();
                } else {
                    if (response.body() != null) {
                        AlipayVo body = response.body();
                        CommonUtils.make(RechargeConfirmActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                    } else {
                        CommonUtils.make(RechargeConfirmActivity.this, CommonUtils.getCodeToStr(response.code()));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
