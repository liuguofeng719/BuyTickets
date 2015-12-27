package com.ticket.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.OrderVo;
import com.ticket.bean.OrderVoResp;
import com.ticket.ui.activity.LoginActivity;
import com.ticket.ui.activity.OrderDetailsActivity;
import com.ticket.ui.activity.PayMentModeActivity;
import com.ticket.ui.adpater.base.ListViewDataAdapter;
import com.ticket.ui.adpater.base.ViewHolderBase;
import com.ticket.ui.adpater.base.ViewHolderCreator;
import com.ticket.ui.base.BaseFragment;
import com.ticket.utils.AppPreferences;
import com.ticket.utils.CommonUtils;
import com.ticket.widgets.SegmentedGroup;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class OrderFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    @InjectView(R.id.order_group)
    SegmentedGroup order_group;
    @InjectView(R.id.waiting_order)
    RadioButton waiting_order;
    @InjectView(R.id.lv_order)
    ListView lv_order;
    ListViewDataAdapter listViewDataAdapter;

    @Override
    protected void onFirstUserVisible() {
        waiting_order.setChecked(true);
        getStatusOrder(0);//未支付
    }

    private void getStatusOrder(int isPaid) {
        showLoading(getString(R.string.common_loading_message));
        Call<OrderVoResp<List<OrderVo>>> orderCall = getApis().getOrders(AppPreferences.getString("userId"), isPaid).clone();
        orderCall.enqueue(new Callback<OrderVoResp<List<OrderVo>>>() {
            @Override
            public void onResponse(Response<OrderVoResp<List<OrderVo>>> response, Retrofit retrofit) {
                hideLoading();
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    List<OrderVo> orderList = response.body().getOrders();
                    listViewDataAdapter.getDataList().clear();
                    listViewDataAdapter.getDataList().addAll(orderList);
                    listViewDataAdapter.notifyDataSetChanged();
                } else {
                    CommonUtils.make(getActivity(), response.body().getErrorMessage().equals("") ? response.message() : response.body().getErrorMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
            }
        });
    }

    @Override
    protected void onUserVisible() {
        if (TextUtils.isEmpty(AppPreferences.getString("userId"))) {
            readyGo(LoginActivity.class);
            return ;
        }
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
        order_group.setOnCheckedChangeListener(this);

        this.listViewDataAdapter = new ListViewDataAdapter<OrderVo>(new ViewHolderCreator<OrderVo>() {
            @Override
            public ViewHolderBase<OrderVo> createViewHolder(int position) {
                return new ViewHolderBase<OrderVo>() {
                    RelativeLayout rl_order_number;
                    TextView tv_order_code;
                    TextView tv_status;
                    TextView tv_station;//行程
                    TextView tv_goDateTime;//行程
                    TextView tv_person_count;//人数
                    TextView tv_total_price;//总金额
                    TextView btn_gopay;//去支付

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
                        return view;
                    }

                    @Override
                    public void showData(int position, OrderVo itemData) {
                        tv_order_code.setText(itemData.getOrderNumber());
                        tv_status.setText(itemData.getOrderStatusDescription());
                        tv_station.setText(itemData.getTrip());
                        tv_person_count.setText(itemData.getPassengerAmount() + "人");
                        tv_total_price.setText("￥" + itemData.getTotalPrice());
                        btn_gopay.setTag(itemData.getOrderNumber());
                        tv_goDateTime.setText(itemData.getGoDate());
                        if (!itemData.getIsPaid()) {//未支付
                            btn_gopay.setVisibility(View.VISIBLE);
                            btn_gopay.setText("去支付");
                            btn_gopay.setTag(itemData.getOrderId());
                            btn_gopay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("orderNumber", v.getTag().toString());
                                    bundle.putString("orderId", v.getTag().toString());
                                    readyGo(PayMentModeActivity.class, bundle);
                                }
                            });
                        } else {
                            btn_gopay.setVisibility(View.INVISIBLE);
                        }
                        rl_order_number.setTag(itemData.getOrderId());
                        rl_order_number.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putString("orderId", v.getTag().toString());
                                readyGo(OrderDetailsActivity.class, bundle);
                            }
                        });
                    }
                };
            }
        });
        this.lv_order.setAdapter(this.listViewDataAdapter);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.my_order;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.waiting_order://待支付
                getStatusOrder(0);
                break;
            case R.id.pay_order://已支付
                getStatusOrder(1);
                break;
        }
    }
}
