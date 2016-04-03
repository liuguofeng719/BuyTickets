package com.ticket.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.common.Constants;
import com.ticket.ui.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by liuguofeng719 on 2016/4/2.
 */
public class CrowdFundingActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.edit_person_number)
    EditText edit_person_number;

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

    private String date_time;
    private String startCityName;
    private String startCityId;
    private String endCityName;
    private String endCityId;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
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

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.crowdfunding;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText("众筹拼车发布");
        setCurrentTime(new Date());
    }
}
