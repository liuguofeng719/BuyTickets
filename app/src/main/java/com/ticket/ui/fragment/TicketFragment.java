package com.ticket.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.ticket.R;
import com.ticket.TicketsApplication;
import com.ticket.common.Constants;
import com.ticket.ui.activity.CityActivity;
import com.ticket.ui.activity.FrequencyListActivity;
import com.ticket.ui.activity.PickerActivity;
import com.ticket.ui.base.BaseFragment;
import com.ticket.utils.CommonUtils;
import com.ticket.utils.LocationService;
import com.ticket.utils.TLog;

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

    private LocationService locationService;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

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
        // -----------location config ------------
        locationService = ((TicketsApplication) (mContext.getApplicationContext())).locationService;
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.registerListener(mListener);
        locationService.start();
    }

    @Override
    public void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    /*****
     * @see copy funtion to you project
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");
                sb.append(location.getCityCode());
                sb.append("\ncity : ");
                sb.append(location.getCity());
                sb.append("\nDistrict : ");
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");
                sb.append(location.getStreet());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\nDescribe: ");
                sb.append(location.getLocationDescribe());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());
                sb.append("\nPoi: ");
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                TLog.d("baidu",sb.toString());
                locationService.stop();
            }
        }

    };


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
