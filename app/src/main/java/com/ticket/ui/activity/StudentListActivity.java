package com.ticket.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.TravelRoutingListVo;
import com.ticket.bean.TravelRoutingListVoResp;
import com.ticket.common.Constants;
import com.ticket.ui.adpater.StudentTripAdapter;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.CommonUtils;
import com.ticket.utils.TLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class StudentListActivity extends BaseActivity {

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

    private Call<TravelRoutingListVoResp<List<TravelRoutingListVo>>> routingListVoRespCall = null;
    StudentTripAdapter studentTripAdapter;
    private Date date = null;
    private Bundle extras;

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_time_prev)
    public void prevDay() {
        date = plusDay(-1);
        String goDate = dateFormat(date);
        getRoutingList(goDate);
        isCurrDate();
    }

    /**
     * 判断是否是今天
     */
    private void isCurrDate() {
        if (DateUtils.isToday(date.getTime())) {
            tv_time_prev.setClickable(false);
            tv_time_prev.setTextColor(Color.parseColor("#cccccc"));
        } else {
            tv_time_prev.setClickable(true);
            tv_time_prev.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    @OnClick(R.id.tv_time_next)
    public void nextDay() {
        tv_time_prev.setClickable(true);
        date = plusDay(1);
        isCurrDate();
        String goDate = dateFormat(date);
        getRoutingList(goDate);
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
            getRoutingList(dateFormat(date));
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
        isCurrDate();
        this.tv_time_content.setText(new SimpleDateFormat("MM月dd日").format(date));
        studentTripAdapter = new StudentTripAdapter();
        this.lv_frquency.setAdapter(studentTripAdapter);
        getRoutingList(extras.getString("goDate"));
    }



    private void getRoutingList(String goDate) {
        tv_empty.setVisibility(View.GONE);
        lv_frquency.setVisibility(View.VISIBLE);
        showLoading(getString(R.string.common_loading_message));
        routingListVoRespCall = getApis().getTravelRoutingList(
                extras.getString("startCityID"),
                extras.getString("stopCityID"),
                goDate);
        routingListVoRespCall.enqueue(new Callback<TravelRoutingListVoResp<List<TravelRoutingListVo>>>() {
            @Override
            public void onResponse(Response<TravelRoutingListVoResp<List<TravelRoutingListVo>>> response, Retrofit retrofit) {
                hideLoading();
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    List<TravelRoutingListVo> routingListVos = response.body().getTravelRoutingList();
                    if (routingListVos != null && routingListVos.size() == 0) {
                        if (tv_empty != null) {
                            tv_empty.setVisibility(View.VISIBLE);
                            lv_frquency.setVisibility(View.GONE);
                        }
                    }
                    studentTripAdapter.getDataList().clear();
                    studentTripAdapter.getDataList().addAll(routingListVos);
                    studentTripAdapter.notifyDataSetChanged();
                } else {
                    if (response.body() != null) {
                        TravelRoutingListVoResp<List<TravelRoutingListVo>> body = response.body();
                        CommonUtils.make(StudentListActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                    } else {
                        CommonUtils.make(StudentListActivity.this, CommonUtils.getCodeToStr(response.code()));
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
    protected void onStop() {
        super.onStop();
        if (routingListVoRespCall != null) {
            routingListVoRespCall.cancel();
        }
    }
}
