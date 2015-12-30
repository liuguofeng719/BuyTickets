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
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import com.ticket.common.Constants;
import com.ticket.ui.adpater.PassengerAdapter;
import com.ticket.ui.adpater.base.ListViewDataAdapter;
import com.ticket.ui.adpater.base.ViewHolderBase;
import com.ticket.ui.adpater.base.ViewHolderCreator;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.AppPreferences;
import com.ticket.utils.CommonUtils;
import com.ticket.utils.TLog;

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
    @InjectView(R.id.tv_insurance_price)
    CheckBox tv_insurance_price;
    @InjectView(R.id.lv_passenger)
    ListView lv_passenger;
    @InjectView(R.id.btn_add_passenger)
    Button btn_add_passenger;
    @InjectView(R.id.tv_price_total)
    TextView tv_price_total;
    @InjectView(R.id.iv_submit_order)
    Button iv_submit_order;

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
    double total;//总金额

    @OnClick(R.id.btn_add_passenger)
    public void addPassenger() {
        if (!TextUtils.isEmpty(AppPreferences.getString("userId"))) {
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
            String jsonStr = gson.toJson(selectedIds);
            Bundle bundle = new Bundle();
            bundle.putString("selectedIds", jsonStr);
            readyGoForResult(PassengerListActivity.class, 1, bundle);
        } else {
            readyGo(LoginActivity.class);
        }
    }

    @OnClick(R.id.iv_submit_order)
    public void submitOrder() {
        if (!TextUtils.isEmpty(AppPreferences.getString("userId"))) {
            if (listViewDataAdapter.getDataList().size() <= 0) {
                Toast.makeText(this, "请选择乘客", Toast.LENGTH_SHORT).show();
                return;
            }
            mDialog = CommonUtils.showDialog(this, "正在提交订单...");
            mDialog.show();
            submitOrderHttp();
        } else {
            readyGo(LoginActivity.class);
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
        TLog.d(TAG_LOG, tv_insurance_price.isChecked() + "");
        if (!tv_insurance_price.isChecked()) {
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

            }
        });
    }

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
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

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
        setTotal(total * selectedIds.size());
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
        total = Double.parseDouble(frequencyVo.getTicketPrice()) + Double.parseDouble(frequencyVo.getServicePrice()) + Double.parseDouble(frequencyVo.getInsurancePrice());
        setTotal(total);
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
                        holder.tv_delete.setTag(itemData);
                        holder.tv_delete.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                PassengerVo passengerVo = (PassengerVo) v.getTag();
                                if (selectedIds.get(passengerVo.getPassengerId()) != null) {
                                    selectedIds.remove(passengerVo.getPassengerId());
                                    setTotal(total * selectedIds.size());
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
        tv_insurance_price.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    total = total - Double.parseDouble(frequencyVo.getInsurancePrice());
                    setTotal(total);
                } else {
                    total = total + Double.parseDouble(frequencyVo.getInsurancePrice());
                    setTotal(total);
                }
            }
        });
    }

    private void setTotal(double price) {
        tv_price_total.setText("￥" + price);
    }

    public class Holder {
        @InjectView(R.id.tv_pass_name)
        TextView tv_pass_name;
        @InjectView(R.id.tv_id_card)
        TextView tv_id_card;
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
