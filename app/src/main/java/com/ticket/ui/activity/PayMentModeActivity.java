package com.ticket.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ticket.utils.CommonUtils;
import com.ticket.utils.TLog;
import com.ticket.utils.alipay.PayResult;
import com.ticket.utils.wxpay.MD5;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;

import butterknife.InjectView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PayMentModeActivity extends BaseActivity {

    @InjectView(R.id.ly_weixin_pay)
    LinearLayout ly_weixin_pay;
    @InjectView(R.id.ly_bao_pay)
    LinearLayout ly_bao_pay;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.btn_back)
    ImageView btn_back;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    private String partner;

    private Bundle extras;


    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.pay_ment_mode;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
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
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            bundle.putString("status", "8000");
                            bundle.putString("msg", "支付结果确认中");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            bundle.putString("status", "0");
                            bundle.putString("msg", "支付失败");
                        }
                    }
                    readyGo(PaySuccessActivity.class, bundle);
                    break;
                case SDK_CHECK_FLAG:
                    Toast.makeText(PayMentModeActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    IWXAPI api;
    Dialog mDialog;
    @InjectView(R.id.tv_total_price)
    TextView tv_total_price;

    @Override
    protected void initViewsAndEvents() {
        tv_total_price.setText("￥" + extras.getString("money") + "元");
        Call<AlipayVo> siginCall = getApis().signAlipay(extras.getString("orderId")).clone();
        siginCall.enqueue(new Callback<AlipayVo>() {
            @Override
            public void onResponse(Response<AlipayVo> response, Retrofit retrofit) {

                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    partner = response.body().getSignedContent();
                } else {
                    if (response.body() != null) {
                        AlipayVo body = response.body();
                        CommonUtils.make(PayMentModeActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                    } else {
                        CommonUtils.make(PayMentModeActivity.this, CommonUtils.getCodeToStr(response.code()));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_header_title.setText(getString(R.string.pay_mode_title));
        ly_weixin_pay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog = CommonUtils.showDialog(PayMentModeActivity.this, "正在加载微信支付");
                mDialog.show();
                api = WXAPIFactory.createWXAPI(PayMentModeActivity.this, Constants.wxpay.APPID);
                api.registerApp(Constants.wxpay.APPID);

                Call<WXPayVo> callWX = getApis().payOrderByWeiChat(extras.getString("orderId")).clone();
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
                                CommonUtils.make(PayMentModeActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                            } else {
                                CommonUtils.make(PayMentModeActivity.this, CommonUtils.getCodeToStr(response.code()));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });

            }
        });

        ly_bao_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(PayMentModeActivity.this);
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
        });

    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    CommonUtils.dismiss(mDialog);
                    break;
                case 0:
                    CommonUtils.dismiss(mDialog);
                    CommonUtils.make(PayMentModeActivity.this, "错误订单，请重新下订单");
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
}
