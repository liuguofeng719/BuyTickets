package com.ticket.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ticket.R;
import com.ticket.bean.OrderCreateVoResp;
import com.ticket.bean.PassengerVo;
import com.ticket.bean.TravelRoutingListVo;
import com.ticket.bean.UserVo;
import com.ticket.common.Constants;
import com.ticket.ui.adpater.base.ListViewDataAdapter;
import com.ticket.ui.adpater.base.ViewHolderBase;
import com.ticket.ui.adpater.base.ViewHolderCreator;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.AppPreferences;
import com.ticket.utils.CommonUtils;
import com.ticket.widgets.ListViewForScrollView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CreateStudentOrderActivity extends BaseActivity {

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
    CheckBox tv_ticket_price;
    @InjectView(R.id.tv_ticket_student)
    CheckBox tv_ticket_student;
    @InjectView(R.id.lv_passenger)
    ListViewForScrollView lv_passenger;
    @InjectView(R.id.btn_add_passenger)
    Button btn_add_passenger;
    @InjectView(R.id.tv_price_total)
    TextView tv_price_total;
    @InjectView(R.id.iv_submit_order)
    Button iv_submit_order;

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
    @InjectView(R.id.ly_student_price)
    LinearLayout ly_student_price;
    @InjectView(R.id.ly_adult_price)
    LinearLayout ly_adult_price;

    Dialog mDialog;
    TravelRoutingListVo frequencyVo;
    Bundle extras;

    //已选择的乘客
    ListViewDataAdapter listViewDataAdapter;
    List<PassengerVo> selectPassengers;
    Map<String, Integer> selectedIds = new HashMap<>();//已选择的乘客编码
    private volatile SparseBooleanArray checkItems = new SparseBooleanArray();
    private double totalPrice;

    /**
     * 优惠券功能
     */
    @OnClick(R.id.tv_coupon)
    public void coupon() {
        readyGo(CouponActivity.class);
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
        String routingId = frequencyVo.getTravelId();
        //userID
        String userID = AppPreferences.getString("userId");
        //乘客编码
        String passengers = null;
        try {
            passengers = URLEncoder.encode(getPassengersIds(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Call<OrderCreateVoResp> callOrder = getApis().CreateTravelOrder(userID, routingId, passengers).clone();
        callOrder.enqueue(new Callback<OrderCreateVoResp>() {

            @Override
            public void onResponse(Response<OrderCreateVoResp> response, Retrofit retrofit) {
                mDialog.dismiss();
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("orderNumber", response.body().getOrderNumber());
                    bundle.putString("orderId", response.body().getOrderId());
                    bundle.putString("money", "" + totalPrice);
                    readyGo(PayMentStudentActivity.class, bundle);
                } else {
                    if (response.body() != null) {
                        OrderCreateVoResp body = response.body();
                        CommonUtils.make(CreateStudentOrderActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                    } else {
                        CommonUtils.make(CreateStudentOrderActivity.this, CommonUtils.getCodeToStr(response.code()));
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
        StringBuffer sbStr = new StringBuffer();
        ArrayList<PassengerVo> dataList = this.listViewDataAdapter.getDataList();
        for (int i = 0; i < dataList.size(); i++) {
            PassengerVo passengerVo = dataList.get(i);
            if (checkItems.get(i)) {
                sbStr.append(passengerVo.getPassengerId()).append(",1").append("|");
            } else {
                sbStr.append(passengerVo.getPassengerId()).append(",0").append("|");
            }
        }
        String substring = sbStr.substring(0, sbStr.lastIndexOf("|"));
        return substring.toString();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.comm.PASSENGER_SUCCESS) {
            loadSelectPass(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获取(票价+服务费)x人数
     *
     * @return double
     */
    private double getTotalTicketsAndService() {
        Map<String, Integer> mapType = getPersonTypeMap();
        double studentPrice = (Double.parseDouble(tv_ticket_student.getTag().toString()) + Double.parseDouble(frequencyVo.getInsurcePrice())) * mapType.get("student");
        double adultPrice = (Double.parseDouble(tv_ticket_price.getTag().toString()) + Double.parseDouble(frequencyVo.getInsurcePrice())) * mapType.get("adult");
        return studentPrice + adultPrice;
    }

    /**
     * @return
     */
    public Map<String, Integer> getPersonTypeMap() {
        Map<String, Integer> integerMap = new Hashtable<>();
        int size = checkItems.size();
        int student = 0;
        int adult = 0;
        for (int i = 0; i < size; i++) {
            if (checkItems.get(i)) {
                student++;
            } else {
                adult++;
            }
        }
        integerMap.put("student", student);
        integerMap.put("adult", adult);
        return integerMap;
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
        this.init();
        double total = getTotalTicketsAndService();
        setTotal(total);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
        this.frequencyVo = (TravelRoutingListVo) extras.getSerializable("routingListVo");
    }

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.create_student_order;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        this.tv_header_title.setText(getString(R.string.write_order_title));
        tv_station_title.setText(frequencyVo.getGoDate() + "  " + frequencyVo.getGoTime() + "发车");
        tv_startPoint.setText(frequencyVo.getStartCity());
        tv_destination.setText(frequencyVo.getStopCity());
        tv_startStation.setText(frequencyVo.getStartPlaceName());
        tv_endStation.setText(frequencyVo.getStopPlaceName());
        tv_ticket_price.setText("￥" + frequencyVo.getNormalPrice());
        tv_ticket_price.setTag(frequencyVo.getNormalPrice());
        tv_ticket_student.setText("￥" + frequencyVo.getStudentPrice());
        tv_ticket_student.setTag(frequencyVo.getStudentPrice());
        tv_insurance_price.setText("￥" + frequencyVo.getInsurcePrice());
        this.listViewDataAdapter = new ListViewDataAdapter<PassengerVo>(new ViewHolderCreator<PassengerVo>() {
            @Override
            public ViewHolderBase<PassengerVo> createViewHolder(int position) {

                return new ViewHolderBase<PassengerVo>() {
                    Holder holder = new Holder();

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View convertView = layoutInflater.inflate(R.layout.student_passenger_item, null);
                        ButterKnife.inject(holder, convertView);
                        return convertView;
                    }

                    @Override
                    public void showData(final int position, PassengerVo itemData) {
                        holder.tv_pass_name.setText(itemData.getFullName());
                        holder.tv_id_card.setText(itemData.getIdCard());
                        holder.tv_delete.setTag(itemData);
                        holder.tv_delete.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                PassengerVo passengerVo = (PassengerVo) v.getTag();
                                if (selectedIds.get(passengerVo.getPassengerId()) != null) {
                                    selectedIds.remove(passengerVo.getPassengerId());
                                    delCheckItemStatus(position);
                                    double total = getTotalTicketsAndService();
                                    setTotal(total);
                                }
                                listViewDataAdapter.getDataList().remove(passengerVo);
                                listViewDataAdapter.notifyDataSetChanged();
                            }
                        });
                        holder.cbo_student_sel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    setCheckItemStatus(position, true);
                                } else {
                                    setCheckItemStatus(position, false);
                                }
                                double total = getTotalTicketsAndService();
                                setTotal(total);
                            }
                        });
                    }
                };
            }
        });
        lv_passenger.setAdapter(listViewDataAdapter);
    }

    public void init() {
        for (int i = 0; i < selectedIds.size(); i++) {
            checkItems.put(i, false);
        }
    }

    public void delCheckItemStatus(int index) {
        checkItems.delete(index);
    }

    public void setCheckItemStatus(Integer index, Boolean flag) {
        checkItems.put(index, flag);
    }

    public void setCheckItemsStatus(Boolean flag) {
        for (int i = 0; i < checkItems.size(); i++) {
            checkItems.put(i, flag);
        }
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
        @InjectView(R.id.tv_delete)
        TextView tv_delete;
        @InjectView(R.id.cbo_student_sel)
        CheckBox cbo_student_sel;
    }

    @Override
    protected void onDestroy() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        super.onDestroy();
    }
}
