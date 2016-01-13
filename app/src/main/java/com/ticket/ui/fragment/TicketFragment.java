package com.ticket.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.Pictures;
import com.ticket.common.Constants;
import com.ticket.ui.activity.CityActivity;
import com.ticket.ui.activity.FrequencyListActivity;
import com.ticket.ui.activity.PickerActivity;
import com.ticket.ui.base.BaseFragment;
import com.ticket.utils.CommonUtils;
import com.ticket.widgets.SlideShowView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TicketFragment extends BaseFragment implements SlideShowView.OnImageClickedListener {

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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @InjectView(R.id.btn_submit)
    Button btn_submit;

    @InjectView(R.id.iv_car)
    ImageView iv_car;

    @InjectView(R.id.slideShowView)
    SlideShowView mSlideShowView;

    private String date_time;
    private String startCityId;
    private String endCityId;
    private String startCityName;
    private String endCityName;

    @OnClick(R.id.iv_car)
    public void chooseCity() {
        if (TextUtils.isEmpty(start_city.getText())) {
            CommonUtils.make(getContext(), getString(R.string.start_tip_city));
            return;
        }
        if (TextUtils.isEmpty(end_city.getText())) {
            CommonUtils.make(getContext(), getString(R.string.end_tip_city));
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
            CommonUtils.make(getContext(), getString(R.string.start_tip_city));
            return;
        }
        if (TextUtils.isEmpty(end_city.getText())) {
            CommonUtils.make(getContext(), getString(R.string.end_tip_city));
            return;
        }
        if (TextUtils.isEmpty(go_time.getText())) {
            CommonUtils.make(getContext(), getString(R.string.tip_start_time));
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
    protected void onFirstUserVisible() {
        getPics();
    }

    private void getPics() {
        Call<Pictures> callPics = getApis().getAdvertisementPictures().clone();
        callPics.enqueue(new Callback<Pictures>() {
            @Override
            public void onResponse(Response<Pictures> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    if (mSlideShowView != null) {
                        mSlideShowView.clearImages();
                        mSlideShowView.setImageUrlList(Arrays.asList(response.body().getPictures()));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        this.mSlideShowView.setOnImageClickedListener(this);
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
                    CommonUtils.make(getContext(), getString(R.string.start_tip_city));
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
        return R.layout.ticket_fragment;
    }

    @Override
    public void onImageClicked(int position, String url) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSlideShowView != null) {
            mSlideShowView.onDestroy();
        }
    }
}
