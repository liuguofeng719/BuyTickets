package com.ticket.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.ticket.R;
import com.ticket.bean.BaseInfoVo;
import com.ticket.common.Constants;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.AppPreferences;
import com.ticket.utils.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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
    @InjectView(R.id.go_date)
    TextView go_date;
    @InjectView(R.id.btn_submit)
    Button btn_submit;
    @InjectView(R.id.iv_car)
    ImageView iv_car;

    private String date_time;
    private String startCityName;
    private String startCityId;
    private String endCityName;
    private String endCityId;
    private Call<BaseInfoVo> infoVoCall;
    private Dialog dialog;

    TimePickerView pvTime;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.comm.PICKER_SUCCESS) {
            Date dt = (Date) data.getSerializableExtra("date");
            setCurrentTime(dt);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if ("start".equals(intent.getStringExtra("city"))) {
            startCityName = intent.getStringExtra("cityName");
            start_city.setText(startCityName);
            startCityId = intent.getStringExtra("cityId");
        } else {
            endCityName = intent.getStringExtra("cityName");
            end_city.setText(endCityName);
            endCityId = intent.getStringExtra("cityId");
        }
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
        if (TextUtils.isEmpty(edit_person_number.getText())) {
            CommonUtils.make(this, "出行人数不能为空");
            return;
        }
        if (Integer.parseInt(edit_person_number.getText().toString()) == 0) {
            CommonUtils.make(this, "出行人数不能为0");
            return;
        }
        if (TextUtils.isEmpty(go_date.getText())) {
            CommonUtils.make(this, "出发日期不能为空");
            return;
        }
        if (TextUtils.isEmpty(go_time.getText())) {
            CommonUtils.make(this, "出发时间不能为空");
            return;
        }
        dialog.show();
        infoVoCall = getApis().createTravel(startCityId, endCityId, AppPreferences.getString("userId"),date_time, go_time.getText().toString()).clone();
        infoVoCall.enqueue(new Callback<BaseInfoVo>() {
            @Override
            public void onResponse(Response<BaseInfoVo> response, Retrofit retrofit) {
                CommonUtils.dismiss(dialog);
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    CommonUtils.make(CrowdFundingActivity.this, "发布众筹成功");
                }else{
                    CommonUtils.make(CrowdFundingActivity.this, "发布众筹失败，请稍后重试");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                CommonUtils.dismiss(dialog);
            }
        });
    }

    private void setCurrentTime(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String str[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};//字符串数组
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        int day = rightNow.get(Calendar.DAY_OF_WEEK);//获取时间
        go_date.setText(sdf.format(dt) + " " + str[day - 1]);
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
        dialog = CommonUtils.showDialog(this, "正在发布");
        tv_header_title.setText("众筹拼车发布");
        SpannableString sbs = new SpannableString("输入麻烦?028-61039462打电话给我，我帮您填");
        String s = sbs.toString();
        int start = s.indexOf("0");
        int end = s.indexOf("打");
        sbs.setSpan(new NoLineClickSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_text_info.setMovementMethod(LinkMovementMethod.getInstance());
        tv_text_info.setHighlightColor(getResources().getColor(R.color.transparent));
        tv_text_info.setLinkTextColor(getResources().getColor(R.color.common_text_color));
        tv_text_info.setText(sbs);
        this.ly_start_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("city", "start");
                readyGo(CityListActivity.class, bundle);
            }
        });
        this.ly_end_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(start_city.getText())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("city", "end");
                    bundle.putString("startCityId", startCityId);
                    readyGo(CityListActivity.class, bundle);
                } else {
                    CommonUtils.make(CrowdFundingActivity.this, getString(R.string.start_tip_city));
                }
            }
        });
        this.go_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGoForResult(PickerActivity.class, 1);
            }
        });
        this.go_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });
        setCurrentTime(new Date());
        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
                String format = sdf.format(date);
                go_time.setText(format);
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
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:028-61039462");
            intent.setData(data);
            startActivity(intent);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (infoVoCall != null) {
            infoVoCall.cancel();
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
