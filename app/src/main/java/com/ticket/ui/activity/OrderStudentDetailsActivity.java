package com.ticket.ui.activity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ticket.R;
import com.ticket.bean.BaseInfoVo;
import com.ticket.bean.MessagesVo;
import com.ticket.bean.PassengerDetailVo;
import com.ticket.bean.PassengerVo;
import com.ticket.bean.StudentPassengerVo;
import com.ticket.bean.TravelOrderDetailVo;
import com.ticket.bean.TravelOrderDetailVoResp;
import com.ticket.ui.adpater.base.ListViewDataAdapter;
import com.ticket.ui.adpater.base.ViewHolderBase;
import com.ticket.ui.adpater.base.ViewHolderCreator;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.CommonUtils;
import com.ticket.utils.TLog;
import com.ticket.widgets.CircleImageView;
import com.ticket.widgets.ListViewForScrollView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class OrderStudentDetailsActivity extends BaseActivity {

    @InjectView(R.id.tv_order_code)
    TextView tv_order_code;
    @InjectView(R.id.tv_pay_time)
    TextView tv_pay_time;
    @InjectView(R.id.tv_pay_mode)
    TextView tv_pay_mode;
    @InjectView(R.id.tv_order_status_desc)
    TextView tv_order_status_desc;
    @InjectView(R.id.tv_car_type)
    TextView tv_car_type;
    @InjectView(R.id.tv_passenger_paid_amount)
    TextView tv_passenger_paid_amount;
    @InjectView(R.id.tv_deal_seat_amount)
    TextView tv_deal_seat_amount;
    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.tv_station_title)
    TextView tv_station_title;
    @InjectView(R.id.tv_startPoint)
    TextView tv_startPoint;
    @InjectView(R.id.tv_destination)
    TextView tv_destination;
    @InjectView(R.id.tv_startStation)
    TextView tv_startStation;
    @InjectView(R.id.tv_endStation)
    TextView tv_endStation;
    @InjectView(R.id.lv_passenger)
    ListViewForScrollView lv_passenger;
    @InjectView(R.id.lv_message)
    ListViewForScrollView lv_message;
    @InjectView(R.id.tv_msg_more)
    TextView tv_msg_more;

    private ListViewDataAdapter passengerAdpater;
    private Dialog dialogDataInit;
    private ListViewDataAdapter<MessagesVo> messageDataAdapter;

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_msg_more)
    public void msgMore() {

    }

    private Bundle extras;

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.order_student_details;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {

        this.tv_header_title.setText("学生出行订单详情");

        passengerAdpater = new ListViewDataAdapter<StudentPassengerVo>(new ViewHolderCreator<StudentPassengerVo>() {
            @Override
            public ViewHolderBase<StudentPassengerVo> createViewHolder(int position) {
                return new ViewHolderBase<StudentPassengerVo>() {
                    TextView tv_pass_name;
                    TextView tv_id_card;
                    TextView tv_phone;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.selected_passenger_item, null);
                        tv_pass_name = ButterKnife.findById(view, R.id.tv_pass_name);
                        tv_id_card = ButterKnife.findById(view, R.id.tv_id_card);
                        tv_phone = ButterKnife.findById(view, R.id.tv_phone);
                        return view;
                    }

                    @Override
                    public void showData(int position, StudentPassengerVo itemData) {
                        TLog.d(TAG_LOG, itemData.toString());
                        tv_pass_name.setText(itemData.getPassengerName());
                        tv_id_card.setText(itemData.getIdCard());
                        tv_phone.setText(itemData.getPhoneNumber());
//                        if (itemData.isCanbeRefund()) {
//                            tv_delete.setVisibility(View.VISIBLE);
//                            tv_delete.setTag(itemData.getOrderDetailID());
//                            tv_delete.setText(getString(R.string.refund_ticket));
//                            tv_delete.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    final Dialog dialog = CommonUtils.showDialog(OrderStudentDetailsActivity.this);
//                                    dialog.show();
//                                    Call<BaseInfoVo> refundCall = getApis().refundTicket(v.getTag().toString()).clone();
//                                    refundCall.enqueue(new Callback<BaseInfoVo>() {
//                                        @Override
//                                        public void onResponse(Response<BaseInfoVo> response, Retrofit retrofit) {
//                                            CommonUtils.dismiss(dialog);
//                                            if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
//                                                CommonUtils.make(OrderStudentDetailsActivity.this, "退票成功");
//                                                getOrderDetails();
//                                            } else {
//                                                if (response.body() != null) {
//                                                    BaseInfoVo body = response.body();
//                                                    CommonUtils.make(OrderStudentDetailsActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
//                                                } else {
//                                                    CommonUtils.make(OrderStudentDetailsActivity.this, CommonUtils.getCodeToStr(response.code()));
//                                                }
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFailure(Throwable t) {
//                                            CommonUtils.dismiss(dialog);
//                                        }
//                                    });
//                                }
//                            });
//                        }
                    }
                };
            }
        });
        lv_passenger.setAdapter(passengerAdpater);
        this.messageDataAdapter = new ListViewDataAdapter<MessagesVo>(new ViewHolderCreator<MessagesVo>() {
            @Override
            public ViewHolderBase<MessagesVo> createViewHolder(int position) {
                return new ViewHolderBase<MessagesVo>() {
                    CircleImageView iv_face;
                    TextView tv_nickname;
                    TextView tv_send_date;
                    TextView tv_msg;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.order_student_msg_item, null);
                        iv_face = ButterKnife.findById(view, R.id.iv_face);
                        tv_nickname = ButterKnife.findById(view, R.id.tv_nickname);
                        tv_send_date = ButterKnife.findById(view, R.id.tv_send_date);
                        tv_msg = ButterKnife.findById(view, R.id.tv_msg);
                        return view;
                    }

                    @Override
                    public void showData(int position, MessagesVo itemData) {
                        TLog.d(TAG_LOG, itemData.toString());

                        DisplayImageOptions options = new DisplayImageOptions
                                .Builder()
                                .showImageForEmptyUri(R.drawable.face)
                                .showImageOnFail(R.drawable.face)
                                .showImageOnLoading(R.drawable.face)
                                .cacheInMemory(true)
                                .cacheOnDisk(true)
                                .bitmapConfig(Bitmap.Config.RGB_565)
                                .build();

                        ImageLoader.getInstance().displayImage(itemData.getHeadPicture(), iv_face,options);
                        tv_nickname.setText(itemData.getNickName());
                        tv_send_date.setText(itemData.getPublishDateTime());
                        tv_msg.setText(itemData.getContent());
                    }
                };
            }
        });
        lv_message.setAdapter(messageDataAdapter);
        getOrderDetails();
    }

    private void getOrderDetails() {
        dialogDataInit = CommonUtils.showDialog(OrderStudentDetailsActivity.this);
        dialogDataInit.show();
        Call<TravelOrderDetailVoResp> callOrder = getApis().getTravelOrderDetails(extras.getString("orderId")).clone();
        callOrder.enqueue(new Callback<TravelOrderDetailVoResp>() {
            @Override
            public void onResponse(Response<TravelOrderDetailVoResp> response, Retrofit retrofit) {
                CommonUtils.dismiss(dialogDataInit);
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    TravelOrderDetailVoResp detailVoResp = response.body();
                    TravelOrderDetailVo orderDetails = detailVoResp.getTravelOrderDetail();
                    tv_order_code.setText(orderDetails.getOrderNumber());
                    tv_order_status_desc.setText(orderDetails.getOrderStatusString());
                    tv_pay_time.setText(orderDetails.getPaymentDateTime());
                    tv_pay_mode.setText(orderDetails.getPaymentWay());
                    tv_car_type.setText(orderDetails.getCarModel());
                    tv_passenger_paid_amount.setText(orderDetails.getPassengerPaidAmount()+"人");
                    tv_deal_seat_amount.setText(orderDetails.getDealSeatAmont()+"人");
                    tv_station_title.setText(orderDetails.getGoDateTime() + "发车");
                    tv_startPoint.setText(orderDetails.getStartCity());
                    tv_destination.setText(orderDetails.getStopCity());
                    tv_startStation.setText(orderDetails.getStartCityPlace());
                    tv_endStation.setText(orderDetails.getStopCityPlace());

                    List<StudentPassengerVo> passengerVos = detailVoResp.getPassengers();
                    passengerAdpater.getDataList().addAll(passengerVos);
                    passengerAdpater.notifyDataSetChanged();
                    List<MessagesVo> messagesVos = detailVoResp.getMessages();
                    messageDataAdapter.getDataList().addAll(messagesVos);
                    messageDataAdapter.notifyDataSetChanged();
                } else {
                    CommonUtils.make(OrderStudentDetailsActivity.this, response.body().getErrorMessage().equals("") ? response.message() : response.body().getErrorMessage());
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
