package com.ticket.ui.activity;

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
import com.ticket.R;
import com.ticket.bean.AlipayVo;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.CommonUtils;
import com.ticket.utils.alipay.PayResult;

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
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("status", "9000");
                        bundle.putString("msg", "支付成功");
                        readyGo(PaySucessActivity.class);
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("status", "8000");
                            bundle.putString("msg", "支付结果确认中");
                            readyGo(PaySucessActivity.class);
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Bundle bundle = new Bundle();
                            bundle.putString("status", "0");
                            bundle.putString("msg", "支付失败");
                            readyGo(PaySucessActivity.class);
                        }
                    }
                    break;
                case SDK_CHECK_FLAG:
                    Toast.makeText(PayMentModeActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void initViewsAndEvents() {
        Call<AlipayVo> siginCall = getApis().signAlipay(extras.getString("orderId")).clone();
        siginCall.enqueue(new Callback<AlipayVo>() {
            @Override
            public void onResponse(Response<AlipayVo> response, Retrofit retrofit) {

                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    partner = response.body().getSignedContent();
                } else {
                    if (response.body() != null) {
                        AlipayVo body = response.body();
                        CommonUtils.make(PayMentModeActivity.this, body.getErrorMessage().equals("") ? response.message() : body.getErrorMessage());
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
}
