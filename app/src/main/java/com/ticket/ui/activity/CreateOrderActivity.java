package com.ticket.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ticket.R;
import com.ticket.bean.FrequencyVo;
import com.ticket.bean.OrderCreateVoResp;
import com.ticket.bean.PassengerListResp;
import com.ticket.bean.PassengerVo;
import com.ticket.bean.UserVo;
import com.ticket.common.Constants;
import com.ticket.ui.adpater.PassengerAdapter;
import com.ticket.ui.adpater.base.ListViewDataAdapter;
import com.ticket.ui.adpater.base.ViewHolderBase;
import com.ticket.ui.adpater.base.ViewHolderCreator;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.AppPreferences;
import com.ticket.utils.CommonUtils;
import com.ticket.widgets.ListViewForScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CreateOrderActivity extends BaseActivity {

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
    @InjectView(R.id.lv_passenger)
    ListViewForScrollView lv_passenger;
    @InjectView(R.id.btn_add_passenger)
    Button btn_add_passenger;
    @InjectView(R.id.tv_price_total)
    TextView tv_price_total;
    @InjectView(R.id.iv_submit_order)
    Button iv_submit_order;

    @InjectView(R.id.iv_tips_info)
    ImageView iv_tips_info;

//    @InjectView(R.id.tv_insurance_info)
//    TextView tv_insurance_info;

    @InjectView(R.id.iv_notice)
    ImageView iv_notice;

    @InjectView(R.id.iv_q)
    ImageView iv_q;
    @InjectView(R.id.tv_insurance_price)
    CheckBox tv_insurance_price;

    @InjectView(R.id.tv_coupon)
    TextView tv_coupon;

    @InjectView(R.id.ly_insurance_price)
    LinearLayout ly_insurance_price;

    PassengerAdapter passengerAdapter;
    Dialog mDialog;
    ListView lv_passenger_list;
    List<PassengerVo> passengerVoList;
    View view_passenger;
    FrequencyVo frequencyVo;
    Bundle extras;
    //已选择的乘客
    ListViewDataAdapter listViewDataAdapter;
    List<PassengerVo> selectPassengers;
    Map<String, Integer> selectedIds = new HashMap<>();//已选择的乘客编码
    boolean insuranceCheck = true;//是否选择保险,默认选择保险费

    private double totalPrice;

    /**
     * 优惠券功能
     */
    @OnClick(R.id.tv_coupon)
    public void coupon() {
        readyGo(CouponActivity.class);
    }

    /**
     * 服务费提示信息
     */
    @OnClick(R.id.iv_tips_info)
    public void tipsInfo() {
        final Dialog mDialogInfo = CommonUtils.createDialog(this);
        mDialogInfo.setContentView(R.layout.dialog_tips_info);
        mDialogInfo.findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogInfo.dismiss();
            }
        });
        mDialogInfo.setCancelable(false);
        mDialogInfo.show();
    }

    /**
     * 是否选择保险费
     */
    @OnClick(R.id.ly_insurance_price)
    public void insurancePrice() {
        if (tv_insurance_price.isChecked()) {
            tv_insurance_price.setChecked(false);
            insuranceCheck = false;
        } else {
            tv_insurance_price.setChecked(true);
            insuranceCheck = true;
        }
        double total = getTotalTicketsAndService();
        if (selectedIds.size() > 0) {
            double insurancePrice = (Double.parseDouble(frequencyVo.getInsurancePrice()) * selectedIds.size());
            if (insuranceCheck) {
                total = total + insurancePrice;
            }
        }
        setTotal(total);
    }

    /**
     * 乘客须知
     */
    @OnClick(R.id.iv_notice)
    public void notice() {
        final Dialog mDialogInfo = CommonUtils.createDialog(this);
        mDialogInfo.setContentView(R.layout.dialog_tips_info);
        ((TextView) mDialogInfo.findViewById(R.id.tv_tips_title)).setText(R.string.notice_title);
        ((TextView) mDialogInfo.findViewById(R.id.tv_tips_content)).setText(R.string.notice);
        mDialogInfo.setCancelable(false);
        mDialogInfo.findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogInfo.dismiss();
            }
        });
        mDialogInfo.show();
    }

    @OnClick(R.id.iv_q)
    public void insuranceInfo() {
        final Dialog mDialogInfo = CommonUtils.createDialog(this);
        mDialogInfo.setContentView(R.layout.dialog_tips_info);
        ((TextView) mDialogInfo.findViewById(R.id.tv_tips_content)).setText(getString(R.string.tips_info_content_insurance));
        ((TextView) mDialogInfo.findViewById(R.id.tv_tips_title)).setText("乘车保险协议介绍");
        mDialogInfo.findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogInfo.dismiss();
            }
        });
        mDialogInfo.show();
    }

    /**
     * 乘客意外保险
     */
//    @OnClick(R.id.tv_insurance_info)
//    public void addInsuranceInfo() {
//        Bundle bundle = new Bundle();
//        bundle.putString("insurancePrice", frequencyVo.getInsurancePrice());
//        bundle.putBoolean("insuranceCheck", insuranceCheck);
//        readyGoForResult(InsuranceActivity.class, 1, bundle);
//    }
    @OnClick(R.id.btn_add_passenger)
    public void addPassenger() {
        if (!TextUtils.isEmpty(AppPreferences.getString("userId"))) {
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
            String jsonStr = gson.toJson(selectedIds);
            Bundle bundle = new Bundle();
            bundle.putString("selectedIds", jsonStr);
            readyGoForResult(PassengerListActivity.class, 1, bundle);
        } else {
            readyGoThenKill(LoginActivity.class);
        }
    }

    @OnClick(R.id.iv_submit_order)
    public void submitOrder() {
        if (!TextUtils.isEmpty(AppPreferences.getString("userId"))) {
            if (!AppPreferences.getObject(UserVo.class).getIsBindingPhoneNumber()) {
                Toast.makeText(this, "请绑定手机号码", Toast.LENGTH_SHORT).show();
                readyGo(BindingUserMobileActivity.class);
                return;
            }
            if (listViewDataAdapter.getDataList().size() <= 0) {
                Toast.makeText(this, "请选择乘客", Toast.LENGTH_SHORT).show();
                return;
            }
            mDialog = CommonUtils.showDialog(this, "正在提交订单...");
            mDialog.show();
            submitOrderHttp();
        } else {
            readyGoThenKill(LoginActivity.class);
        }
    }

    private void submitOrderHttp() {
        //路由ID
        String routingId = frequencyVo.getRoutingId();
        //userID
        String userID = AppPreferences.getString("userId");
        //乘客编码
        String passengers = getPassengersIds();
        //isBuyInsurance 是否购买保险
        int isBuyInsurance = 0;//false
        if (insuranceCheck) { //勾选不买，否则买
            isBuyInsurance = 1;//true
        }

        Call<OrderCreateVoResp> callOrder = getApis().createOrder(routingId, userID, passengers, isBuyInsurance).clone();
        callOrder.enqueue(new Callback<OrderCreateVoResp>() {

            @Override
            public void onResponse(Response<OrderCreateVoResp> response, Retrofit retrofit) {
                mDialog.dismiss();
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("orderNumber", response.body().getOrderNumber());
                    bundle.putString("orderId", response.body().getOrderId());
                    bundle.putString("money", "" + totalPrice);
                    readyGo(PayMentModeActivity.class, bundle);
                } else {
                    if (response.body() != null) {
                        OrderCreateVoResp body = response.body();
                        CommonUtils.make(CreateOrderActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                    } else {
                        CommonUtils.make(CreateOrderActivity.this, CommonUtils.getCodeToStr(response.code()));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDialog.dismiss();
            }
        });
    }

    /**
     * 多个联系人用逗号分隔
     *
     * @return
     */
    public String getPassengersIds() {
        Set<String> keys = selectedIds.keySet();
        Iterator<String> it = keys.iterator();
        StringBuffer sb = new StringBuffer();
        int size = keys.size();
        while (it.hasNext()) {
            String key = it.next();
            if (size == 1) {
                sb.append(key);
            } else {
                sb.append(key).append(",");
            }
            size--;
        }
        return sb.toString();
    }

    @Override
    protected void onDestroy() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.comm.PASSENGER_SUCCESS) {
            loadSelectPass(data);
        } else if (resultCode == Constants.comm.INSURANCE_PRICE_SUCCESS) {
            insuranceCheck = data.getBooleanExtra("insuranceCheck", false);
            double total = getTotalTicketsAndService();
            if (selectedIds.size() > 0) {
                double insurancePrice = (Double.parseDouble(frequencyVo.getInsurancePrice()) * selectedIds.size());
                if (insuranceCheck) {
                    total = total + insurancePrice;
                }
            }
            setTotal(total);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获取(票价+服务费)x人数
     *
     * @return double
     */
    private double getTotalTicketsAndService() {
        return (Double.parseDouble(frequencyVo.getTicketPrice()) + Double.parseDouble(frequencyVo.getServicePrice())) * selectedIds.size();
    }

    /**
     * 获取选择的乘客
     *
     * @param data
     */

    private void loadSelectPass(Intent data) {
        String passengerVoList = data.getStringExtra("passengerVoList");
        String mapStr = data.getStringExtra("selectedIds");
        Gson gson = new GsonBuilder().create();
        selectedIds = gson.fromJson(mapStr, new TypeToken<Map<String, Integer>>() {
        }.getType());
        selectPassengers = new GsonBuilder().create().fromJson(passengerVoList, new TypeToken<List<PassengerVo>>() {
        }.getType());
        this.listViewDataAdapter.getDataList().clear();
        this.listViewDataAdapter.getDataList().addAll(selectPassengers);
        this.listViewDataAdapter.notifyDataSetChanged();
        double total = getTotalTicketsAndService();
        if (insuranceCheck) {
            total = total + (Double.parseDouble(frequencyVo.getInsurancePrice()) * selectedIds.size());
        }
        setTotal(total);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
        this.frequencyVo = (FrequencyVo) extras.getSerializable("frequencyVo");
    }

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.create_order;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        this.tv_header_title.setText(getString(R.string.write_order_title));
        tv_station_title.setText(frequencyVo.getGoDate() + "  " + frequencyVo.getGoTime() + "发车");
        tv_startPoint.setText(frequencyVo.getStartCityName());
        tv_destination.setText(frequencyVo.getStopCityName());
        tv_startStation.setText(frequencyVo.getStartStationName());
        tv_endStation.setText(frequencyVo.getStopStationName());
        tv_ticket_price.setText("￥" + frequencyVo.getTicketPrice());
        tv_service_price.setText("￥" + frequencyVo.getServicePrice());
        tv_insurance_price.setText("￥" + frequencyVo.getInsurancePrice());
        this.listViewDataAdapter = new ListViewDataAdapter<PassengerVo>(new ViewHolderCreator<PassengerVo>() {
            @Override
            public ViewHolderBase<PassengerVo> createViewHolder(int position) {

                return new ViewHolderBase<PassengerVo>() {
                    Holder holder = new Holder();

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View convertView = layoutInflater.inflate(R.layout.selected_passenger_item, null);
                        ButterKnife.inject(holder, convertView);
                        return convertView;
                    }

                    @Override
                    public void showData(int position, PassengerVo itemData) {
                        holder.tv_pass_name.setText(itemData.getFullName());
                        holder.tv_id_card.setText(itemData.getIdCard());
                        holder.tv_phone.setText(itemData.getMobileNumber());
                        holder.tv_delete.setTag(itemData);
                        holder.tv_delete.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                PassengerVo passengerVo = (PassengerVo) v.getTag();
                                if (selectedIds.get(passengerVo.getPassengerId()) != null) {
                                    selectedIds.remove(passengerVo.getPassengerId());
                                    double total = getTotalTicketsAndService();
                                    if (insuranceCheck) {
                                        total = total + (Double.parseDouble(frequencyVo.getInsurancePrice()) * selectedIds.size());
                                    }
                                    setTotal(total);
                                }
                                listViewDataAdapter.getDataList().remove(passengerVo);
                                listViewDataAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                };
            }
        });
        lv_passenger.setAdapter(listViewDataAdapter);
    }


    //设置总金额
    private void setTotal(double price) {
        totalPrice = price;
        tv_price_total.setText("￥" + price);
    }

    public class Holder {
        @InjectView(R.id.tv_pass_name)
        TextView tv_pass_name;
        @InjectView(R.id.tv_id_card)
        TextView tv_id_card;
        @InjectView(R.id.tv_phone)
        TextView tv_phone;
        @InjectView(R.id.tv_delete)
        TextView tv_delete;
    }

    private void createDialog(Context context) {
        mDialog = new Dialog(context, R.style.myDialog);
        Window window = mDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 不做实现
     */
    private void getDialog() {
        if (mDialog == null) {
            createDialog(this);
        }
        view_passenger = getLayoutInflater().inflate(R.layout.passenger_dialog, null);
        lv_passenger_list = ButterKnife.findById(view_passenger, R.id.lv_passenger_list);
        lv_passenger_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.cbo_selected);
                if (checkBox.isChecked()) {
                    PassengerAdapter.checkItems.put(position, false);
                } else {
                    PassengerAdapter.checkItems.put(position, true);
                }
                passengerAdapter.notifyDataSetChanged();
            }
        });
        Call<PassengerListResp<List<PassengerVo>>> callPassenger = getApis().getPassengers(AppPreferences.getString("userId")).clone();
        callPassenger.enqueue(new Callback<PassengerListResp<List<PassengerVo>>>() {
            @Override
            public void onResponse(Response<PassengerListResp<List<PassengerVo>>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    passengerVoList = response.body().getPassengerList();
                    passengerAdapter = new PassengerAdapter(response.body().getPassengerList());
                    lv_passenger_list.setAdapter(passengerAdapter);
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });

        TextView btn_ok = ButterKnife.findById(view_passenger, R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> selectedIds = new ArrayList<Integer>();
                for (int i = 0; i < PassengerAdapter.checkItems.size(); i++) {
                    if (PassengerAdapter.checkItems.get(i)) {
                        selectedIds.add(i);
                    }
                }
                if (selectedIds.size() <= 0) {
                    Toast.makeText(mDialog.getContext(), "请选择乘客", Toast.LENGTH_SHORT).show();
                    return;
                }
                closeDialog();
            }
        });

        TextView btn_cancel = ButterKnife.findById(view_passenger, R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });
        mDialog.setContentView(view_passenger);
        mDialog.show();
    }

    private void closeDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

}
