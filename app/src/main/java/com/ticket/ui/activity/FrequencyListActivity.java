package com.ticket.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.FrequencyListResp;
import com.ticket.bean.FrequencyVo;
import com.ticket.common.Constants;
import com.ticket.ui.adpater.base.ListViewDataAdapter;
import com.ticket.ui.adpater.base.ViewHolderBase;
import com.ticket.ui.adpater.base.ViewHolderCreator;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.CommonUtils;
import com.ticket.utils.TLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class FrequencyListActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.tv_time_prev)
    TextView tv_time_prev;
    @InjectView(R.id.tv_time_content)
    TextView tv_time_content;
    @InjectView(R.id.tv_time_next)
    TextView tv_time_next;
    @InjectView(R.id.lv_frquency)
    ListView lv_frquency;
    @InjectView(R.id.tv_empty)
    TextView tv_empty;

    ListViewDataAdapter listViewDataAdapter;
    Date date = null;
    private Bundle extras;

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_time_prev)
    public void prevDay() {
        date = plusDay(-1);
        String goDate = dateFormat(date);
        getFrequecyList(goDate);
    }

    @OnClick(R.id.tv_time_next)
    public void nextDay() {
        date = plusDay(1);
        String goDate = dateFormat(date);
        getFrequecyList(goDate);
    }

    private String dateFormat(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfView = new SimpleDateFormat("MM月dd日");
        this.tv_time_content.setText(sdfView.format(dt));
        return sdf.format(dt);
    }

    @OnClick(R.id.tv_time_content)
    public void chooseDate() {
        readyGoForResult(PickerActivity.class, 1);
    }

    private Date plusDay(int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_WEEK, day);
        return c.getTime();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.comm.PICKER_SUCCESS) {
            date = (Date) data.getSerializableExtra("date");
            getFrequecyList(dateFormat(date));
            this.tv_time_content.setText(new SimpleDateFormat("MM月dd日").format(date));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.frequency_list;
    }

    @Override
    protected View getLoadingTargetView() {
        return lv_frquency;
    }

    @Override
    protected void initViewsAndEvents() {
        TLog.d("initViewsAndEvents",
                "startCityID" + extras.getString("startCityID") +
                        "stopCityID=" + extras.getString("stopCityID") + "goDate=" + extras.getString("goDate"));
        this.tv_header_title.setText(extras.getString("station"));
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(extras.getString("goDate").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.tv_time_content.setText(new SimpleDateFormat("MM月dd日").format(date));
        this.listViewDataAdapter = new ListViewDataAdapter<FrequencyVo>(new ViewHolderCreator<FrequencyVo>() {

            @Override
            public ViewHolderBase<FrequencyVo> createViewHolder(int position) {
                return new ViewHolderBase<FrequencyVo>() {

                    TextView tv_goDateTime;
                    TextView tv_startStation;
                    TextView tv_endStation;
                    TextView tv_salePrice;
                    LinearLayout ly_fre_item;
                    TextView tv_tickets_amount;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.frequency_list_item, null);
                        tv_goDateTime = ButterKnife.findById(view, R.id.tv_goDateTime);
                        tv_startStation = ButterKnife.findById(view, R.id.tv_startStation);
                        tv_endStation = ButterKnife.findById(view, R.id.tv_endStation);
                        tv_salePrice = ButterKnife.findById(view, R.id.tv_salePrice);
                        ly_fre_item = ButterKnife.findById(view, R.id.ly_fre_item);
                        tv_tickets_amount = ButterKnife.findById(view, R.id.tv_tickets_amount);
                        return view;
                    }

                    @Override
                    public void showData(int position, FrequencyVo itemData) {
                        tv_goDateTime.setText(itemData.getGoTime());
                        tv_startStation.setText(itemData.getStartStationName());
                        tv_endStation.setText(itemData.getStopStationName());
                        tv_salePrice.setText(itemData.getTicketPrice());
                        tv_tickets_amount.setText("剩票(" + itemData.getRemainingTicketsAmount() + ")张");
                        ly_fre_item.setTag(itemData);
                        ly_fre_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FrequencyVo frequencyVo = (FrequencyVo) v.getTag();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("frequencyVo", frequencyVo);
                                readyGo(CreateOrderActivity.class, bundle);
                            }
                        });
                    }
                };
            }
        });

        this.lv_frquency.setAdapter(this.listViewDataAdapter);
        getFrequecyList(extras.getString("goDate"));
    }

    private void getFrequecyList(String goDate) {
        tv_empty.setVisibility(View.GONE);
        lv_frquency.setVisibility(View.VISIBLE);
        showLoading(getString(R.string.common_loading_message));
        Call<FrequencyListResp<List<FrequencyVo>>> callFrequency = getApis().getFrequencyList(
                extras.getString("startCityID"),
                extras.getString("stopCityID"),
                goDate);
        callFrequency.enqueue(new Callback<FrequencyListResp<List<FrequencyVo>>>() {
            @Override
            public void onResponse(Response<FrequencyListResp<List<FrequencyVo>>> response, Retrofit retrofit) {
                hideLoading();
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    List<FrequencyVo> frquecyList = response.body().getRoutings();
                    if (frquecyList.size() == 0) {
                        tv_empty.setVisibility(View.VISIBLE);
                        lv_frquency.setVisibility(View.GONE);
                    }
                    listViewDataAdapter.getDataList().clear();
                    listViewDataAdapter.getDataList().addAll(frquecyList);
                    listViewDataAdapter.notifyDataSetChanged();
                } else {
                    if (response.body() != null) {
                        FrequencyListResp<List<FrequencyVo>> body = response.body();
                        CommonUtils.make(FrequencyListActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                    } else {
                        CommonUtils.make(FrequencyListActivity.this, CommonUtils.getCodeToStr(response.code()));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
            }
        });
    }
}
