package com.ticket.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.LeasedVehicleListResp;
import com.ticket.bean.LeasedVehicleOrder;
import com.ticket.bean.ShareMessageVo;
import com.ticket.ui.activity.LoginActivity;
import com.ticket.ui.activity.OrderVehicleDetailsActivity;
import com.ticket.ui.activity.PayMentChartedBusActivity;
import com.ticket.ui.adpater.base.ListViewDataAdapter;
import com.ticket.ui.adpater.base.ViewHolderBase;
import com.ticket.ui.adpater.base.ViewHolderCreator;
import com.ticket.ui.base.BaseFragment;
import com.ticket.utils.AppPreferences;
import com.ticket.utils.CommonUtils;
import com.ticket.utils.ShareUtils;
import com.ticket.utils.ShareVo;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by liuguofeng719 on 2016/3/29.
 */
public class CharteredBusFragment extends BaseFragment {

    @InjectView(R.id.lv_order)
    ListView lv_order;

    ListViewDataAdapter listViewDataAdapter;

    @Override
    protected void onFirstUserVisible() {
    }

    @Override
    protected void onUserVisible() {
        if (TextUtils.isEmpty(AppPreferences.getString("userId"))) {
            readyGo(LoginActivity.class);
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getStatusOrder();
    }

    @Override
    protected void onUserInvisible() {
    }

    @Override
    protected View getLoadingTargetView() {
        return lv_order;
    }

    @Override
    protected void initViewsAndEvents() {

        this.listViewDataAdapter = new ListViewDataAdapter<LeasedVehicleOrder>(new ViewHolderCreator<LeasedVehicleOrder>() {
            @Override
            public ViewHolderBase<LeasedVehicleOrder> createViewHolder(int position) {
                return new ViewHolderBase<LeasedVehicleOrder>() {
                    LinearLayout rl_order_number;
                    TextView tv_order_code;
                    TextView tv_status;
                    TextView tv_station;//行程
                    TextView tv_goDateTime;//行程
                    TextView tv_person_count;//人数
                    TextView tv_total_price;//总金额
                    TextView btn_gopay;//去支付
                    TextView btn_text_share;//分享

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.my_order_item, null);
                        rl_order_number = ButterKnife.findById(view, R.id.rl_order_number);
                        tv_order_code = ButterKnife.findById(view, R.id.tv_order_code);
                        tv_status = ButterKnife.findById(view, R.id.tv_status);
                        tv_station = ButterKnife.findById(view, R.id.tv_station);
                        tv_person_count = ButterKnife.findById(view, R.id.tv_person_count);
                        tv_total_price = ButterKnife.findById(view, R.id.tv_total_price);
                        tv_goDateTime = ButterKnife.findById(view, R.id.tv_goDateTime);
                        btn_gopay = ButterKnife.findById(view, R.id.btn_gopay);
                        btn_text_share = ButterKnife.findById(view, R.id.btn_text_share);
                        btn_gopay.setVisibility(View.GONE);
                        return view;
                    }

                    @Override
                    public void showData(int position, final LeasedVehicleOrder itemData) {
                        if ("2".equals(itemData.getState())) {
                            if (!itemData.isPaid()) {
                                btn_gopay.setVisibility(View.VISIBLE);
                                btn_gopay.setTag(itemData.getOrderId());
                                btn_gopay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("money", itemData.getTotalPrice());
                                        bundle.putString("orderId", v.getTag().toString());
                                        readyGo(PayMentChartedBusActivity.class, bundle);
                                    }
                                });
                            }
                        }
                        tv_order_code.setText(itemData.getOrderNumber());
//                        String payStr = itemData.isPaid() ? "已支付" : "未支付";
                        tv_status.setText(getStatus(itemData.getState()));
                        tv_station.setText(itemData.getTrip());
                        tv_person_count.setText(itemData.getPassengerAmount() + "人");
                        tv_total_price.setText("￥" + itemData.getTotalPrice());
                        tv_goDateTime.setText(itemData.getGoDateTime());
                        rl_order_number.setTag(itemData.getOrderId());
                        rl_order_number.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putString("orderId", v.getTag().toString());
                                readyGo(OrderVehicleDetailsActivity.class, bundle);
                            }
                        });
                        btn_text_share.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Call<ShareMessageVo> messageVoCall = getApis().share(itemData.getOrderNumber()).clone();
                                messageVoCall.enqueue(new Callback<ShareMessageVo>() {
                                    @Override
                                    public void onResponse(Response<ShareMessageVo> response, Retrofit retrofit) {
                                        if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                                            ShareVo shareVo = new ShareVo();
                                            shareVo.setText(response.body().getShareMessage());
                                            shareVo.setTitle(response.body().getShareMessage());
                                            shareVo.setTitleUrl(response.body().getNavigateUrl());
                                            shareVo.setUrl(response.body().getNavigateUrl());
                                            shareVo.setComment("");
                                            shareVo.setSite("");
                                            shareVo.setSiteUrl("");
                                            ShareUtils.showShare(getActivity(), shareVo);
                                        } else {
                                            CommonUtils.make(getActivity(), "分享失败");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {

                                    }
                                });
                            }
                        });
                    }

                    public String getStatus(String type) {
                        if ("0".equals(type)) {
                            return "等待询价";
                        } else if ("1".equals(type)) {
                            return "询价完成";
                        } else if ("2".equals(type)) {
                            return "等待付款";
                        } else if ("3".equals(type)) {
                            return "交易成功";
                        }
                        return "";
                    }
                };
            }
        });
        this.lv_order.setAdapter(this.listViewDataAdapter);
    }

    private void getStatusOrder() {
        showLoading(getString(R.string.common_loading_message));
        Call<LeasedVehicleListResp<List<LeasedVehicleOrder>>> travelOrdersVoRespCall = getApis().getLeasedVehicleOrders(AppPreferences.getString("userId")).clone();
        travelOrdersVoRespCall.enqueue(new Callback<LeasedVehicleListResp<List<LeasedVehicleOrder>>>() {
            @Override
            public void onResponse(Response<LeasedVehicleListResp<List<LeasedVehicleOrder>>> response, Retrofit retrofit) {
                hideLoading();
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    List<LeasedVehicleOrder> travelOrders = response.body().getLeasedVehicleOrderList();
                    if (travelOrders != null) {
                        listViewDataAdapter.getDataList().clear();
                        listViewDataAdapter.getDataList().addAll(travelOrders);
                    }
                    listViewDataAdapter.notifyDataSetChanged();
                } else {
                    if (response.body() != null) {
                        LeasedVehicleListResp<List<LeasedVehicleOrder>> body = response.body();
                        CommonUtils.make(getParentFragment().getActivity(), body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                    } else {
                        CommonUtils.make(getParentFragment().getActivity(), CommonUtils.getCodeToStr(response.code()));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
            }
        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.my_order;
    }
}
