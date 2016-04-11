package com.ticket.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.BaseInfoVo;
import com.ticket.bean.OrderDeatilResp;
import com.ticket.bean.OrderDetailVo;
import com.ticket.bean.PassengerDetailVo;
import com.ticket.bean.TravelOrderDetailVo;
import com.ticket.bean.TravelOrderDetailVoResp;
import com.ticket.ui.adpater.base.ListViewDataAdapter;
import com.ticket.ui.adpater.base.ViewHolderBase;
import com.ticket.ui.adpater.base.ViewHolderCreator;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.CommonUtils;
import com.ticket.utils.TLog;

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
    @InjectView(R.id.tv_order_status)
    TextView tv_order_status;
    @InjectView(R.id.tv_order_price)
    TextView tv_order_price;
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
    @InjectView(R.id.tv_ticket_price)
    TextView tv_ticket_price;
    @InjectView(R.id.tv_service_price)
    TextView tv_service_price;
    @InjectView(R.id.tv_insurance_price)
    CheckBox tv_insurance_price;
    @InjectView(R.id.lv_passenger)
    ListView lv_passenger;

    ListViewDataAdapter listViewDataAdapter;
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
        return R.layout.order_student_details;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {

        this.tv_header_title.setText("学生出行订单详情");

        listViewDataAdapter = new ListViewDataAdapter<PassengerDetailVo>(new ViewHolderCreator<PassengerDetailVo>() {
            @Override
            public ViewHolderBase<PassengerDetailVo> createViewHolder(int position) {
                return new ViewHolderBase<PassengerDetailVo>() {
                    TextView tv_pass_name;
                    TextView tv_id_card;
                    TextView tv_delete;
                    TextView tv_phone;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.selected_passenger_item, null);
                        tv_pass_name = ButterKnife.findById(view, R.id.tv_pass_name);
                        tv_id_card = ButterKnife.findById(view, R.id.tv_id_card);
                        tv_delete = ButterKnife.findById(view, R.id.tv_delete);
                        tv_phone = ButterKnife.findById(view, R.id.tv_phone);
                        tv_delete.setVisibility(View.INVISIBLE);
                        return view;
                    }

                    @Override
                    public void showData(int position, PassengerDetailVo itemData) {
                        TLog.d(TAG_LOG, itemData.toString());
                        tv_pass_name.setText(itemData.getPassengerName());
                        tv_id_card.setText(itemData.getIdCard());
                        tv_phone.setText(itemData.getMobileNumber());
                        if (itemData.isCanbeRefund()) {
                            tv_delete.setVisibility(View.VISIBLE);
                            tv_delete.setTag(itemData.getOrderDetailID());
                            tv_delete.setText(getString(R.string.refund_ticket));
                            tv_delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Dialog dialog = CommonUtils.showDialog(OrderStudentDetailsActivity.this);
                                    dialog.show();
                                    Call<BaseInfoVo> refundCall = getApis().refundTicket(v.getTag().toString()).clone();
                                    refundCall.enqueue(new Callback<BaseInfoVo>() {
                                        @Override
                                        public void onResponse(Response<BaseInfoVo> response, Retrofit retrofit) {
                                            CommonUtils.dismiss(dialog);
                                            if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                                                CommonUtils.make(OrderStudentDetailsActivity.this, "退票成功");
                                                getOrderDetails();
                                            } else {
                                                if (response.body() != null) {
                                                    BaseInfoVo body = response.body();
                                                    CommonUtils.make(OrderStudentDetailsActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                                                } else {
                                                    CommonUtils.make(OrderStudentDetailsActivity.this, CommonUtils.getCodeToStr(response.code()));
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            CommonUtils.dismiss(dialog);
                                        }
                                    });
                                }
                            });
                        }
                    }
                };
            }
        });
        lv_passenger.setAdapter(listViewDataAdapter);
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
//                    TravelOrderDetailVo orderDetails = detailVoResp.getTravelOrderDetail();
//                    tv_order_code.setText(orderDetails.getOrderNumber());
//                    tv_pay_time.setText(orderDetails.getPayDateTime());
//                    tv_pay_mode.setText(orderDetails.getPayFuncation());
//                    tv_order_status.setText(orderDetails.getOrderStatusDescription());
//                    tv_order_price.setText("￥" + orderDetails.getOrderTotalPrice());
//                    tv_station_title.setText(orderDetails.getGoDate() + "  " + orderDetails.getGoTime() + "发车");
//                    tv_startPoint.setText(orderDetails.getStartStationCityName());
//                    tv_destination.setText(orderDetails.getStopStationCityName());
//                    tv_startStation.setText(orderDetails.getStartStationName());
//                    tv_endStation.setText(orderDetails.getStopStationName());
//                    tv_ticket_price.setText("￥" + orderDetails.getTotalTicketPrice());
//                    tv_service_price.setText("￥" + orderDetails.getTotalServicePrice());
//                    tv_insurance_price.setText("￥" + orderDetails.getTotalInsurancePrice());
//                    List<PassengerDetailVo> passengerVos = detailVoResp.getPassengers();
//                    listViewDataAdapter.getDataList().addAll(passengerVos);
//                    listViewDataAdapter.notifyDataSetChanged();
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