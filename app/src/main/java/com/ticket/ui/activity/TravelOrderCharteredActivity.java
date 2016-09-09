package com.ticket.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ticket.R;
import com.ticket.bean.AssignedCarDetailResp;
import com.ticket.bean.AssignedCarVo;
import com.ticket.bean.TravelOrderVo;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.AppPreferences;
import com.ticket.utils.CommonUtils;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by liuguofeng719 on 2016/7/24.
 */
public class TravelOrderCharteredActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btnBack;
    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.iv_car_image)
    ImageView ivCarImage;
    @InjectView(R.id.tv_car_type)
    TextView tvCarType;
    @InjectView(R.id.tv_driver_name)
    TextView tvDriverName;
    @InjectView(R.id.tv_driver_age)
    TextView tvDriverAge;
    @InjectView(R.id.tv_phone)
    TextView tvPhone;
    @InjectView(R.id.tv_cart_number)
    TextView tvCartNumber;
    @InjectView(R.id.tv_order_total)
    TextView tvOrderTotal;
    @InjectView(R.id.ly_content)
    LinearLayout ly_content;
    private String carID;
    private Dialog dialogDataInit;

    @OnClick(R.id.btn_pay)
    public void btnPay() {
        if (!TextUtils.isEmpty(AppPreferences.getString("userId"))) {
            dialogDataInit = CommonUtils.showDialog(TravelOrderCharteredActivity.this);
            dialogDataInit.show();
            Call<TravelOrderVo> baseInfoVoCall = getApis().createLeasedVehicleOrder(
                    AppPreferences.getString("userId"),
                    carID,
                    extras.getString("days"),
                    extras.getString("date"),
                    extras.getString("totalDistance"),
                    extras.getString("trip"),
                    extras.getString("totalPrice")
            ).clone();

            baseInfoVoCall.enqueue(new Callback<TravelOrderVo>() {
                @Override
                public void onResponse(Response<TravelOrderVo> response, Retrofit retrofit) {
                    CommonUtils.dismiss(dialogDataInit);
                    TravelOrderVo body = response.body();
                    if (response.isSuccess() && body != null && body.isSuccessfully()) {
                        Bundle bundle = new Bundle();
                        bundle.putString("money", extras.getString("totalPrice"));
                        bundle.putString("orderId", body.getOrderId());
                        readyGo(PayMentChartedBusActivity.class, bundle);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    CommonUtils.dismiss(dialogDataInit);
                }
            });
        } else {
            readyGo(LoginActivity.class);
        }
    }

    private Bundle extras;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.order_confirm_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return ly_content;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText("确认订单");
        showLoading(getString(R.string.common_loading_message));
        orderDetail(extras.getString("carType"));
    }

    private void orderDetail(String carType) {
        Call<AssignedCarDetailResp<AssignedCarVo>> assignedCarDetailRespCall = getApis().assignedCar(carType).clone();
        assignedCarDetailRespCall.enqueue(new Callback<AssignedCarDetailResp<AssignedCarVo>>() {
            @Override
            public void onResponse(Response<AssignedCarDetailResp<AssignedCarVo>> response, Retrofit retrofit) {
                hideLoading();
                if (response.isSuccess() && response.body() != null) {
                    AssignedCarDetailResp<AssignedCarVo> assignedCarDetailResp = response.body();
                    AssignedCarVo assignedCarDetail = assignedCarDetailResp.getAssignedCarDetail();
                    carID = assignedCarDetail.getCarID();
                    ImageLoader.getInstance().displayImage(assignedCarDetail.getCarTypePicture(), ivCarImage);
                    tvCarType.setText(assignedCarDetail.getCarTypeName());
                    tvDriverName.setText(assignedCarDetail.getFullName());
                    tvDriverAge.setText(assignedCarDetail.getDrivingExperience() + "年驾龄  " + assignedCarDetail.getTravelKilometers() + "万公里安全行驶记录");
                    tvPhone.setText(assignedCarDetail.getPhoneNumber());
                    tvCartNumber.setText(assignedCarDetail.getRegistrationNumber());
                    tvOrderTotal.setText("订单总金额:" + extras.getString("totalPrice") + "元");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
            }
        });
    }
}
