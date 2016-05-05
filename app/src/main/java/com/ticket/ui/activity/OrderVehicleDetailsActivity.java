package com.ticket.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ticket.R;
import com.ticket.bean.BaseInfoVo;
import com.ticket.bean.QuoteVo;
import com.ticket.bean.TripVo;
import com.ticket.bean.VehicleOrdersDetailsVoResp;
import com.ticket.bean.VehicleOrdersVo;
import com.ticket.ui.adpater.base.ListViewDataAdapter;
import com.ticket.ui.adpater.base.ViewHolderBase;
import com.ticket.ui.adpater.base.ViewHolderCreator;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.CommonUtils;
import com.ticket.widgets.ListViewForScrollView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class OrderVehicleDetailsActivity extends BaseActivity {

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
    @InjectView(R.id.tv_company_name)
    TextView tv_company_name;
    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.lv_scheduling)
    ListViewForScrollView lv_scheduling;
    @InjectView(R.id.lv_price_list)
    ListViewForScrollView lv_price_list;

    private Dialog dialogDataInit;
    private ListViewDataAdapter companyQuoteAdapter;
    private ListViewDataAdapter tripDataAdapter;
    private volatile SortedMap<String, List<TaskPlans>> taskMap = new TreeMap<>();

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
        return R.layout.order_vehicle_details;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        this.tv_header_title.setText("包车出行订单详情");

        this.tripDataAdapter = new ListViewDataAdapter<ListTaskPlans>(new ViewHolderCreator<ListTaskPlans>() {
            @Override
            public ViewHolderBase<ListTaskPlans> createViewHolder(int position) {
                return new ViewHolderBase<ListTaskPlans>() {
                    TextView tv_day_number;
                    ListView lv_scheduling_items;
                    ListViewDataAdapter itemListViewData;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = getLayoutInflater().inflate(R.layout.chartered_bus_scheduling, null);
                        tv_day_number = ButterKnife.findById(view, R.id.tv_day_number);
                        lv_scheduling_items = ButterKnife.findById(view, R.id.lv_scheduling_items);
                        return view;
                    }

                    @Override
                    public void showData(final int ps, final ListTaskPlans taskPlans) {
                        tv_day_number.setText("第" + taskPlans.getDays() + "天");
                        itemListViewData = new ListViewDataAdapter<TaskPlans>(new ViewHolderCreator<TaskPlans>() {
                            @Override
                            public ViewHolderBase<TaskPlans> createViewHolder(int position) {
                                return new ViewHolderBase<TaskPlans>() {
                                    TextView start_city;
                                    TextView end_city;
                                    ImageView iv_minus;

                                    @Override
                                    public View createView(LayoutInflater layoutInflater) {
                                        View view = getLayoutInflater().inflate(R.layout.chartered_bus_item, null);
                                        start_city = ButterKnife.findById(view, R.id.start_city);
                                        start_city.setGravity(Gravity.LEFT);
                                        end_city = ButterKnife.findById(view, R.id.end_city);
                                        end_city.setGravity(Gravity.RIGHT);
                                        iv_minus = ButterKnife.findById(view, R.id.iv_minus);
                                        iv_minus.setVisibility(View.GONE);
                                        return view;
                                    }

                                    @Override
                                    public void showData(final int position, TaskPlans itemData) {
                                        start_city.setText(itemData.getStartCity());
                                        end_city.setText(itemData.getEndCity());
                                    }
                                };
                            }
                        });
                        itemListViewData.getDataList().addAll(taskPlans.getTaskPlansList());
                        lv_scheduling_items.setAdapter(itemListViewData);
                    }
                };
            }
        });
        lv_scheduling.setAdapter(tripDataAdapter);
        this.companyQuoteAdapter = new ListViewDataAdapter<QuoteVo>(new ViewHolderCreator<QuoteVo>() {
            @Override
            public ViewHolderBase<QuoteVo> createViewHolder(int position) {
                return new ViewHolderBase<QuoteVo>() {
                    ImageView iv_car;
                    TextView tv_subordinate;
                    TextView tv_car_type;
                    TextView tv_car_price;
                    TextView tv_choose_car;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.order_vehicle_car_item, null);
                        iv_car = ButterKnife.findById(view, R.id.iv_car);
                        tv_subordinate = ButterKnife.findById(view, R.id.tv_subordinate);
                        tv_car_type = ButterKnife.findById(view, R.id.tv_car_type);
                        tv_car_price = ButterKnife.findById(view, R.id.tv_car_price);
                        tv_choose_car = ButterKnife.findById(view, R.id.tv_choose_car);
                        return view;
                    }

                    @Override
                    public void showData(final int position, final QuoteVo itemData) {
//                        DisplayImageOptions options = new DisplayImageOptions
//                                .Builder()
//                                .showImageForEmptyUri(R.drawable.face)
//                                .showImageOnFail(R.drawable.face)
//                                .showImageOnLoading(R.drawable.face)
//                                .cacheInMemory(true)
//                                .cacheOnDisk(true)
//                                .bitmapConfig(Bitmap.Config.RGB_565)
//                                .build();
                        ImageLoader.getInstance().displayImage(itemData.getCarPicture(), iv_car);
                        tv_subordinate.setText(itemData.getCompanyName());
                        tv_car_type.setText("车型：" + itemData.getCarTypeName());
                        tv_car_price.setText("承运价格：" + itemData.getQuotePrice());
                        tv_choose_car.setTag(itemData.getQuoteId());
                        tv_choose_car.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!itemData.isChoosed()) {
                                    Call<BaseInfoVo> companyQuote = getApis().chooseCompanyQuote(v.getTag().toString()).clone();
                                    companyQuote.enqueue(new Callback<BaseInfoVo>() {
                                        @Override
                                        public void onResponse(Response<BaseInfoVo> response, Retrofit retrofit) {
                                            if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                                                QuoteVo quoteVo = (QuoteVo) companyQuoteAdapter.getDataList().get(position);
                                                quoteVo.setIsChoosed(true);
                                                companyQuoteAdapter.notifyDataSetChanged();
                                                CommonUtils.make(OrderVehicleDetailsActivity.this, "选择包车成功");
                                            } else {
                                                CommonUtils.make(OrderVehicleDetailsActivity.this, response.body().getErrorMessage());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {

                                        }
                                    });
                                } else {
                                    CommonUtils.make(OrderVehicleDetailsActivity.this, "你已经选择车辆!");
                                }
                            }
                        });
                    }
                };
            }
        });
        lv_price_list.setAdapter(companyQuoteAdapter);
        getOrderDetails();
    }


    private void getOrderDetails() {
        dialogDataInit = CommonUtils.showDialog(OrderVehicleDetailsActivity.this);
        dialogDataInit.show();
        Call<VehicleOrdersDetailsVoResp> callOrder = getApis().getLeasedVehicleOrdersDetails(extras.getString("orderId")).clone();
        callOrder.enqueue(new Callback<VehicleOrdersDetailsVoResp>() {
            @Override
            public void onResponse(Response<VehicleOrdersDetailsVoResp> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    VehicleOrdersDetailsVoResp detailsVoResp = response.body();
                    VehicleOrdersVo vehicleOrdersVo = detailsVoResp.getVehicleOrdersDetails();
                    tv_order_code.setText(vehicleOrdersVo.getOrderNumber());
                    tv_order_status_desc.setText(vehicleOrdersVo.getOrderStatusString());
                    tv_pay_time.setText(vehicleOrdersVo.getPaymentDateTime());
                    tv_pay_mode.setText(vehicleOrdersVo.getPaymentWay());
                    tv_car_type.setText(vehicleOrdersVo.getCarTypeName());
                    tv_company_name.setText(vehicleOrdersVo.getCompanyName());
                    List<TripVo> tripList = detailsVoResp.getLeasedVehicleOrderTripList();
                    initTrip(tripList);
                    List<QuoteVo> companyQuote = detailsVoResp.getCompanyQuote();
                    companyQuoteAdapter.getDataList().addAll(companyQuote);
                    companyQuoteAdapter.notifyDataSetChanged();
                } else {
                    CommonUtils.make(OrderVehicleDetailsActivity.this, response.body().getErrorMessage().equals("") ? CommonUtils.getCodeToStr(response.code()) : response.body().getErrorMessage());
                }
                CommonUtils.dismiss(dialogDataInit);
            }

            @Override
            public void onFailure(Throwable t) {
                CommonUtils.dismiss(dialogDataInit);
            }
        });
    }

    /**
     * 行程
     *
     * @param tripList
     */
    private void initTrip(List<TripVo> tripList) {
        for (TripVo tripVo : tripList) {
            if (taskMap.get(tripVo.getDay()) != null) {
                List<TaskPlans> taskPlanses = taskMap.get(tripVo.getDay());
                TaskPlans taskPlans = new TaskPlans();
                taskPlans.setStartCity(tripVo.getStartCity());
                taskPlans.setEndCity(tripVo.getEndCity());
                taskPlanses.add(taskPlans);
            } else {
                List<TaskPlans> taskPlanses = new ArrayList<TaskPlans>();
                TaskPlans taskPlans = new TaskPlans();
                taskPlans.setStartCity(tripVo.getStartCity());
                taskPlans.setEndCity(tripVo.getEndCity());
                taskPlanses.add(taskPlans);
                taskMap.put(tripVo.getDay(), taskPlanses);
            }
        }
        Set<Map.Entry<String, List<TaskPlans>>> entries = taskMap.entrySet();
        tripDataAdapter.getDataList().clear();
        for (Map.Entry<String, List<TaskPlans>> entry : entries) {
            ListTaskPlans listTaskPlans = new ListTaskPlans();
            String key = entry.getKey();
            List<TaskPlans> value = entry.getValue();
            listTaskPlans.setDays(key);
            listTaskPlans.setTaskPlansList(value);
            tripDataAdapter.getDataList().add(listTaskPlans);
        }
        tripDataAdapter.notifyDataSetChanged();
    }

    class ListTaskPlans {

        private String days;

        private List<TaskPlans> taskPlansList;

        public void setTaskPlansList(List<TaskPlans> taskPlansList) {
            this.taskPlansList = taskPlansList;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public List<TaskPlans> getTaskPlansList() {
            return taskPlansList;
        }
    }

    class TaskPlans implements Serializable {

        private String startCity;
        private String endCity;

        public String getStartCity() {
            return startCity;
        }

        public void setStartCity(String startCity) {
            this.startCity = startCity;
        }

        public String getEndCity() {
            return endCity;
        }

        public void setEndCity(String endCity) {
            this.endCity = endCity;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogDataInit != null) {
            CommonUtils.dismiss(dialogDataInit);
        }
    }
}
