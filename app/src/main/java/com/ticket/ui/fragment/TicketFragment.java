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
import com.ticket.common.Constants;
import com.ticket.ui.activity.CityActivity;
import com.ticket.ui.activity.FrequencyListActivity;
import com.ticket.ui.activity.PickerActivity;
import com.ticket.ui.base.BaseFragment;
import com.ticket.utils.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.InjectView;
import butterknife.OnClick;

public class TicketFragment extends BaseFragment {

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

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    Bundle savedState;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private Bundle saveState() {
        Bundle outState = new Bundle();
        outState.putString("startCityName", startCityName);
        outState.putString("startCityId", startCityId);
        outState.putString("endCityId", endCityId);
        outState.putString("endCityName", endCityName);
        outState.putString("date_time", date_time);
        outState.putString("go_time", go_time.getText().toString());
        return outState;
    }

    private void saveStateToArguments() {
        savedState = saveState();
        if (savedState != null) {
            Bundle b = getArguments();
            b.putBundle("internalSavedViewState8954201239547", savedState);
        }
    }

    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        savedState = b.getBundle("internalSavedViewState8954201239547");
        if (savedState != null) {
            restoreState();
            return true;
        }
        return false;
    }

    private void restoreState() {
        if (savedState != null) {
            start_city.setText(savedState.getString("startCityName"));
            end_city.setText(savedState.getString("endCityName"));
            go_time.setText(savedState.getString("go_time"));
            startCityId = savedState.getString("startCityId");
            endCityId = savedState.getString("endCityId");
            date_time = savedState.getString("date_time");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
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
}
