package com.ticket.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ticket.R;
import com.ticket.bean.TravelVehicleOrdersDetailsVoResp;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.CommonUtils;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TravelOrderDetailsActivity extends BaseActivity {

    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.btn_back)
    ImageView btnBack;
    @InjectView(R.id.tv_order_code)
    TextView tvOrderCode;
    @InjectView(R.id.tv_order_status_desc)
    TextView tvOrderStatusDesc;
    @InjectView(R.id.tv_pay_time)
    TextView tvPayTime;
    @InjectView(R.id.tv_pay_mode)
    TextView tvPayMode;
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
    private Dialog dialogDataInit;

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    private Bundle extras;

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.order_confirm_details_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        this.tv_header_title.setText("包车出行订单详情");
        getOrderDetails();
    }

    private void getOrderDetails() {
        dialogDataInit = CommonUtils.showDialog(TravelOrderDetailsActivity.this);
        dialogDataInit.show();
        Call<TravelVehicleOrdersDetailsVoResp> callOrder = getApis().getLeasedVehicleOrdersNewsDetails(extras.getString("orderId")).clone();
        callOrder.enqueue(new Callback<TravelVehicleOrdersDetailsVoResp>() {
            @Override
            public void onResponse(Response<TravelVehicleOrdersDetailsVoResp> response, Retrofit retrofit) {
                CommonUtils.dismiss(dialogDataInit);
                TravelVehicleOrdersDetailsVoResp body = response.body();
                if (response.isSuccess() && body != null && body.isSuccessfully()) {

                    TravelVehicleOrdersDetailsVoResp.VehicleOrdersDetails vehicleOrdersDetails = body.getVehicleOrdersDetails();
                    tvOrderCode.setText(vehicleOrdersDetails.getOrderNumber());
                    tvOrderStatusDesc.setText(vehicleOrdersDetails.getOrderStatusString());
                    tvPayTime.setText(vehicleOrdersDetails.getPaymentDateTime());
                    tvPayMode.setText(vehicleOrdersDetails.getPaymentWay());
                    tvOrderTotal.setText("总金额:" + vehicleOrdersDetails.getTotalPrice()+"元");
                    TravelVehicleOrdersDetailsVoResp.AssignedCarDetail assignedCarDetail = body.getAssignedCarDetail();
                    ImageLoader.getInstance().displayImage(assignedCarDetail.getCarTypePicture(), ivCarImage);
                    tvCarType.setText(assignedCarDetail.getCarTypeName());
                    tvDriverName.setText(assignedCarDetail.getFullName());
                    tvDriverAge.setText(assignedCarDetail.getDrivingExperience() + "年驾龄 " + assignedCarDetail.getTravelKilometers() + "公里");
                    tvPhone.setText("" + assignedCarDetail.getPhoneNumber());
                    tvCartNumber.setText("" + assignedCarDetail.getRegistrationNumber());
                } else {
                    CommonUtils.make(TravelOrderDetailsActivity.this, body.getErrorMessage());
                }

            }

            @Override
            public void onFailure(Throwable t) {
                CommonUtils.dismiss(dialogDataInit);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogDataInit != null) {
            CommonUtils.dismiss(dialogDataInit);
        }
    }
}
