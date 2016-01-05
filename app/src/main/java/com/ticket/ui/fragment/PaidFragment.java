package com.ticket.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.OrderVo;
import com.ticket.bean.OrderVoResp;
import com.ticket.ui.activity.LoginActivity;
import com.ticket.ui.activity.OrderDetailsActivity;
import com.ticket.ui.adpater.base.ListViewDataAdapter;
import com.ticket.ui.adpater.base.ViewHolderBase;
import com.ticket.ui.adpater.base.ViewHolderCreator;
import com.ticket.ui.base.BaseFragment;
import com.ticket.utils.AppPreferences;
import com.ticket.utils.CommonUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 已支付
 */
public class PaidFragment extends BaseFragment {

    @InjectView(R.id.lv_order)
    ListView lv_order;
    ListViewDataAdapter listViewDataAdapter;

    @Override
    protected void onFirstUserVisible() {
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
                    if (response.body() != null) {
                        OrderVoResp<List<OrderVo>> body = response.body();
                        CommonUtils.make(getParentFragment().getActivity(), body.getErrorMessage()==null ? response.message() : body.getErrorMessage());
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
    protected void onUserVisible() {
        if (TextUtils.isEmpty(AppPreferences.getString("userId"))) {
            readyGo(LoginActivity.class);
            return;
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
        getStatusOrder(1);//已支付
        this.listViewDataAdapter = new ListViewDataAdapter<OrderVo>(new ViewHolderCreator<OrderVo>() {
            @Override
            public ViewHolderBase<OrderVo> createViewHolder(int position) {
                return new ViewHolderBase<OrderVo>() {
                    LinearLayout rl_order_number;
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
                        btn_gopay.setVisibility(View.GONE);
                        return view;
                    }

                    @Override
                    public void showData(int position, OrderVo itemData) {
                        tv_order_code.setText(itemData.getOrderNumber());
                        tv_status.setText(itemData.getOrderStatusDescription());
                        tv_station.setText(itemData.getTrip());
                        tv_person_count.setText(itemData.getPassengerAmount() + "人");
                        tv_total_price.setText("￥" + itemData.getTotalPrice());
                        tv_goDateTime.setText(itemData.getGoDate());
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
}
