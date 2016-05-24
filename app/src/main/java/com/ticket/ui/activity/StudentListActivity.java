package com.ticket.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.BaseInfoVo;
import com.ticket.bean.TravelRoutingListVo;
import com.ticket.bean.TravelRoutingListVoResp;
import com.ticket.common.Constants;
import com.ticket.ui.adpater.base.ListViewDataAdapter;
import com.ticket.ui.adpater.base.ViewHolderBase;
import com.ticket.ui.adpater.base.ViewHolderCreator;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.AppPreferences;
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
    ListViewDataAdapter listViewDataAdapter;

    private Date date = null;
    private Bundle extras;
    private Dialog dialog;

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
        this.listViewDataAdapter = new ListViewDataAdapter<TravelRoutingListVo>(new ViewHolderCreator<TravelRoutingListVo>() {
            @Override
            public ViewHolderBase<TravelRoutingListVo> createViewHolder(int position) {
                return new ViewHolderBase<TravelRoutingListVo>() {
                    PlatformHolder holder = new PlatformHolder();

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View convertView = layoutInflater.inflate(R.layout.stu_platform_list_item, null);
                        ButterKnife.inject(holder, convertView);
                        return convertView;
                    }

                    @Override
                    public void showData(int position, final TravelRoutingListVo routingListVo) {

                        if ("0".equals(routingListVo.getState())) {
                            holder.btn_text_join.setVisibility(View.VISIBLE);
                            holder.btn_text_pay.setVisibility(View.GONE);
                        }

                        if ("1".equals(routingListVo.getState())) {//询价成功 显示价格和已定的人数
                            holder.btn_text_join.setVisibility(View.GONE);
                            holder.btn_text_pay.setVisibility(View.VISIBLE);
                            holder.tv_student_ticket.setText("学生票：" + routingListVo.getStudentPrice() + "元");
                            holder.tv_adult_ticket.setText("成人票：" + routingListVo.getNormalPrice() + "元");
                            holder.tv_person_count.setText("剩余" + routingListVo.getReachSeatAmount() + "座 ");
                        }

                        holder.tv_status.setText(getStatus(routingListVo.getState()));//平台类型
                        holder.tv_goDateTime.setText(routingListVo.getGoTime());//出发日期
                        holder.tv_type_text.setText(routingListVo.getPublishedType());//平台类型
                        holder.tv_startStation.setText(routingListVo.getStartPlaceName());
                        holder.tv_endStation.setText(routingListVo.getStopPlaceName());

//                        if ("平台发布".equals(routingListVo.getPublishedType())) {
                            holder.tv_seat_amount.setText(routingListVo.getCarName());
                            holder.tv_reachSeat_amount.setText("(达成出行还差" + routingListVo.getReachSeatAmount() + "人)");
//                        } else if ("用户发布".equals(routingListVo.getPublishedType())) {
//                            holder.tv_seat_amount.setText("发起人:" + routingListVo.getCreateUser());
//                        }
                        holder.btn_text_share.setTag(routingListVo);
                        holder.btn_text_share.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        holder.btn_text_join.setTag(routingListVo);
                        holder.btn_text_join.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shareButton(routingListVo);
                            }
                        });
                        holder.btn_text_pay.setTag(routingListVo);
                        holder.btn_text_pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TravelRoutingListVo routingListVo = (TravelRoutingListVo) v.getTag();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("routingListVo", routingListVo);
                                readyGo(CreateStudentOrderActivity.class,bundle);
                            }
                        });
                        holder.ly_fre_item.setTag(routingListVo);
                        holder.ly_fre_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }

                    public String getStatus(String type) {
                        if ("0".equals(type)) {
                            return "未询价";
                        } else if ("1".equals(type)) {
                            return "等待达成出行";
                        } else if ("2".equals(type)) {
                            return "达成出行";
                        }
                        return "";
                    }
                };
            }
        });
        this.lv_frquency.setAdapter(listViewDataAdapter);
        getRoutingList(extras.getString("goDate"));
    }

    private void shareButton(TravelRoutingListVo routingListVo) {
        dialog = CommonUtils.showDialog(this, "正在加入");
        dialog.show();
        Call<BaseInfoVo> infoVoCall = getApis().joinTravel(routingListVo.getTravelId(), AppPreferences.getString("userId")).clone();
        infoVoCall.enqueue(new Callback<BaseInfoVo>() {
            @Override
            public void onResponse(Response<BaseInfoVo> response, Retrofit retrofit) {
                CommonUtils.dismiss(dialog);
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    CommonUtils.make(StudentListActivity.this, "加入成功");
                } else {
                    CommonUtils.make(StudentListActivity.this, response.body().getErrorMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                CommonUtils.dismiss(dialog);
            }
        });
    }

    static class PlatformHolder {
        @InjectView(R.id.ly_fre_item)
        LinearLayout ly_fre_item;
        @InjectView(R.id.tv_goDateTime)
        TextView tv_goDateTime;
        @InjectView(R.id.tv_type_text)
        TextView tv_type_text;
        @InjectView(R.id.tv_startStation)
        TextView tv_startStation;
        @InjectView(R.id.tv_endStation)
        TextView tv_endStation;
        @InjectView(R.id.tv_person_count)
        TextView tv_person_count;
        @InjectView(R.id.tv_status)
        TextView tv_status;
        @InjectView(R.id.tv_student_ticket)
        TextView tv_student_ticket;
        @InjectView(R.id.tv_adult_ticket)
        TextView tv_adult_ticket;
        @InjectView(R.id.tv_seat_amount)
        TextView tv_seat_amount;
        @InjectView(R.id.tv_reachSeat_amount)
        TextView tv_reachSeat_amount;
        @InjectView(R.id.btn_text_share)
        TextView btn_text_share;
        @InjectView(R.id.btn_text_join)
        TextView btn_text_join;
        @InjectView(R.id.btn_text_pay)
        TextView btn_text_pay;
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
                    listViewDataAdapter.getDataList().clear();
                    listViewDataAdapter.getDataList().addAll(routingListVos);
                    listViewDataAdapter.notifyDataSetChanged();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            CommonUtils.dismiss(dialog);
        }
    }
}
