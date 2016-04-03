package com.ticket.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.TravelRoutingVo;
import com.ticket.bean.TravelRoutingVoResp;
import com.ticket.common.Constants;
import com.ticket.ui.adpater.base.ListViewDataAdapter;
import com.ticket.ui.adpater.base.ViewHolderBase;
import com.ticket.ui.adpater.base.ViewHolderCreator;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.CommonUtils;

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

/**
 * 学生出行
 */
public class StudentTripActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.tv_text_info)
    TextView tv_text_info;

    @InjectView(R.id.ly_start_city)
    LinearLayout ly_start_city;
    @InjectView(R.id.ly_end_city)
    LinearLayout ly_end_city;
    @InjectView(R.id.start_city)
    TextView start_city;
    @InjectView(R.id.end_city)
    TextView end_city;
    @InjectView(R.id.go_time)
    TextView go_time;

    @InjectView(R.id.btn_submit)
    Button btn_submit;
    @InjectView(R.id.iv_car)
    ImageView iv_car;
    @InjectView(R.id.lv_recommend)
    ListView lv_recommend;

    Call<TravelRoutingVoResp<List<TravelRoutingVo>>> travelRoutingPreview;

    private String date_time;
    private String startCityId;
    private String endCityId;
    private String startCityName;
    private String endCityName;

    private String provinceName;
    private String cityName;
    private ListViewDataAdapter listViewDataAdapter;

    @OnClick(R.id.iv_car)
    public void chooseCity() {
        if (TextUtils.isEmpty(start_city.getText())) {
            CommonUtils.make(this, getString(R.string.start_tip_city));
            return;
        }
        if (TextUtils.isEmpty(end_city.getText())) {
            CommonUtils.make(this, getString(R.string.end_tip_city));
            return;
        }
        swapCity();
    }

    /**
     * 切换城市
     */
    private void swapCity() {

        String tempCity = startCityName;
        startCityName = endCityName;
        endCityName = tempCity;

        String tempCityId = startCityId;
        startCityId = endCityId;
        endCityId = tempCityId;

        start_city.setText(startCityName);
        end_city.setText(endCityName);
    }

    @OnClick(R.id.btn_submit)
    public void submit() {
        if (TextUtils.isEmpty(start_city.getText())) {
            CommonUtils.make(this, getString(R.string.start_tip_city));
            return;
        }
        if (TextUtils.isEmpty(end_city.getText())) {
            CommonUtils.make(this, getString(R.string.end_tip_city));
            return;
        }
        if (TextUtils.isEmpty(go_time.getText())) {
            CommonUtils.make(this, getString(R.string.tip_start_time));
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("station", startCityName + " - " + endCityName);
        bundle.putString("startCityID", startCityId);
        bundle.putString("stopCityID", endCityId);
        bundle.putString("goDate", date_time);
        readyGo(FrequencyListActivity.class, bundle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.comm.PICKER_SUCCESS) {
            Date dt = (Date) data.getSerializableExtra("date");
            setCurrentTime(dt);
        } else if (resultCode == Constants.comm.START_CITY_SUCCESS) {
            startCityName = data.getStringExtra("cityName");
            start_city.setText(startCityName);
            startCityId = data.getStringExtra("cityId");
        } else if (resultCode == Constants.comm.END_CITY_SUCCESS) {
            endCityName = data.getStringExtra("cityName");
            end_city.setText(endCityName);
            endCityId = data.getStringExtra("cityId");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setCurrentTime(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String str[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};//字符串数组
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        int day = rightNow.get(Calendar.DAY_OF_WEEK);//获取时间
        go_time.setText(sdf.format(dt) + " " + str[day - 1]);
        SimpleDateFormat sdfView = new SimpleDateFormat("yyyy-MM-dd");
        date_time = sdfView.format(dt);
    }

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.student_trip;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText("学生出行");
        SpannableString sbs = new SpannableString("没有您想去的地方？发布一条众筹路线试试");
        String s = sbs.toString();
        int start = s.indexOf("众");
        int end = s.indexOf("线");
        sbs.setSpan(new NoLineClickSpan(), start, end + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_text_info.setMovementMethod(LinkMovementMethod.getInstance());
        tv_text_info.setHighlightColor(getResources().getColor(R.color.transparent));
        tv_text_info.setLinkTextColor(getResources().getColor(R.color.common_text_color));
        tv_text_info.setText(sbs);
        //获取推荐路线预览
        this.getPreview();

        this.ly_start_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("city", "start");
                readyGoForResult(CityActivity.class, 1, bundle);
            }
        });
        this.ly_end_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(start_city.getText())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("city", "end");
                    bundle.putString("startCityId", startCityId);
                    readyGoForResult(CityActivity.class, 1, bundle);
                } else {
                    CommonUtils.make(StudentTripActivity.this, getString(R.string.start_tip_city));
                }
            }
        });
        this.go_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGoForResult(PickerActivity.class, 1);
            }
        });
        setCurrentTime(new Date());
    }

    private void getPreview() {
        listViewDataAdapter = new ListViewDataAdapter<TravelRoutingVo>(new ViewHolderCreator<TravelRoutingVo>() {
            @Override
            public ViewHolderBase<TravelRoutingVo> createViewHolder(int position) {
                return new ViewHolderBase<TravelRoutingVo>() {
                    TextView tv_startCity;
                    TextView tv_endCity;
                    TextView tv_goDateTime;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.student_trip_item, null);
                        tv_startCity = ButterKnife.findById(view, R.id.tv_startCity);
                        tv_endCity = ButterKnife.findById(view, R.id.tv_endCity);
                        tv_goDateTime = ButterKnife.findById(view, R.id.tv_goDateTime);
                        return view;
                    }

                    @Override
                    public void showData(int position, TravelRoutingVo itemData) {
                        tv_startCity.setText(itemData.getStartCityName());
                        tv_endCity.setText(itemData.getStopCityName());
                        tv_startCity.setText(itemData.getStartCityName());
                        tv_goDateTime.setText("出发日期：" + itemData.getGoDate());
                    }
                };
            }
        });

        travelRoutingPreview = getApis().getTravelRoutingPreview().clone();
        travelRoutingPreview.enqueue(new Callback<TravelRoutingVoResp<List<TravelRoutingVo>>>() {
            @Override
            public void onResponse(Response<TravelRoutingVoResp<List<TravelRoutingVo>>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.isSuccess()) {
                    List<TravelRoutingVo> travelPreviewList = response.body().getTravelPreviewList();
                    if (travelPreviewList != null && travelPreviewList.size() > 0) {
                        listViewDataAdapter.getDataList().addAll(response.body().getTravelPreviewList());
                    }
                    listViewDataAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    class NoLineClickSpan extends ClickableSpan {

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
           readyGo(CrowdFundingActivity.class);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (travelRoutingPreview != null) {
            travelRoutingPreview.cancel();
        }
    }
}
