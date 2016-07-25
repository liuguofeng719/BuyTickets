package com.ticket.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.ticket.R;
import com.ticket.TicketsApplication;
import com.ticket.bean.CityListResp;
import com.ticket.bean.CityVo;
import com.ticket.bean.ProvincesResp;
import com.ticket.bean.ProvincesVo;
import com.ticket.common.Constants;
import com.ticket.ui.adpater.CityListAdapter;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.CommonUtils;
import com.ticket.utils.LocationService;
import com.ticket.utils.TLog;
import com.ticket.widgets.PinnedHeaderListView;
import com.ticket.widgets.SiderBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SuggestionCityActivity extends BaseActivity implements SiderBar.OnTouchingLetterChangedListener {

    @InjectView(R.id.ed_search)
    EditText ed_search;
    @InjectView(R.id.lv_city)
    PinnedHeaderListView lv_city;
    @InjectView(R.id.siderBar)
    SiderBar siderBar;
    @InjectView(R.id.tv_location)
    TextView tvLocation;

    private String provinceName;
    private String cityName;
    private Bundle extras;
    private CityListAdapter mAdapter;
    private LocationService locationService;
    private List<CityVo> cityVoList = new ArrayList<>();

    @OnClick(R.id.tv_cancel)
    public void tvCancel() {
        finish();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.suggestion_city_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lv_city;
    }

    @Override
    protected void initViewsAndEvents() {
        showLoading(getString(R.string.common_loading_message));

        siderBar.setOnTouchingLetterChangedListener(this);
        this.lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.city_list_item_name);
                TLog.d(TAG_LOG, textView.getTag().toString() + " = " + textView.getText());
                Intent intent = new Intent();
                intent.putExtra("cityId", textView.getTag().toString());
                intent.putExtra("cityName", textView.getText());
                setResult(Constants.comm.START_CITY_SUCCESS, intent);
                finish();
            }
        });
        //处理搜索功能
        this.ed_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TLog.d("onTextChanged", s.toString());
                List<CityVo> newCitys = new ArrayList<CityVo>();
                if (cityVoList != null) {
                    for (CityVo cityVo : cityVoList) {
                        if (cityVo.getCityName().contains(s)) {
                            newCitys.add(cityVo);
                        }
                    }
                    cityVoList.clear();
                    Collections.sort(newCitys, new CityComparator());
                    cityVoList.addAll(newCitys);
                    mAdapter.updateView(cityVoList);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                TLog.d("afterTextChanged", s.toString());
                if (s.length() == 0) {
                    getStartProviceCity("");
                }
            }
        });
        getStartProviceCity("");//默认获取四川
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                // -----------location config ------------
                locationService = ((TicketsApplication) (mContext.getApplicationContext())).locationService;
                locationService.setLocationOption(locationService.getDefaultLocationClientOption());
                locationService.registerListener(mListener);
                locationService.start();
            }
        });
    }

    Call<CityListResp<List<CityVo>>> callStartHotCity = null;

    private void commCall(Call<CityListResp<List<CityVo>>> callOrgCity) {
        callOrgCity.enqueue(new Callback<CityListResp<List<CityVo>>>() {
            @Override
            public void onResponse(Response<CityListResp<List<CityVo>>> response, Retrofit retrofit) {
                hideLoading();
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    final List<CityVo> cityList = response.body().getCityList();
                    //获取热门城市
                    callStartHotCity = getApis().getHotOriginatingCity().clone();
                    callStartHotCity.enqueue(new Callback<CityListResp<List<CityVo>>>() {
                        @Override
                        public void onResponse(Response<CityListResp<List<CityVo>>> response, Retrofit retrofit) {
                            if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                                List<CityVo> hotCity = response.body().getCityList();
                                if (hotCity != null) {
                                    TLog.d(TAG_LOG, "" + hotCity.size());
                                    for (CityVo cityVo : hotCity) {
                                        cityVo.setFirstLatter("热门");
                                    }
                                    TLog.d(TAG_LOG, hotCity.toString());
                                    setCityData(hotCity, cityList);
                                }
                            } else {
                                if (response.body() != null) {
                                    CityListResp<List<CityVo>> body = response.body();
                                    CommonUtils.make(SuggestionCityActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                                } else {
                                    CommonUtils.make(SuggestionCityActivity.this, CommonUtils.getCodeToStr(response.code()));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
            }
        });
    }

    /**
     * 通过省市获取地市
     *
     * @param tag
     */
    private void getStartProviceCity(String tag) {
        String findName = "";
        Set<String> keySet = provinceMap.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
            String key = it.next();
            if (provinceMap.get(key).equalsIgnoreCase(tag)) {
                findName = key;
                break;
            }
        }
        String provinceID = "6019C2D3-6ABE-435B-AA55-028EB90D10AB";
        if (!TextUtils.isEmpty(provinceID)) {
            Call<CityListResp<List<CityVo>>> callOrgCity = getApis().getOriginatingCity(provinceID).clone();
            this.commCall(callOrgCity);
        } else {
            hideLoading();
        }
    }

    List<ProvincesVo> provinceList;
    Map<String, String> provinceMap = new ConcurrentHashMap<>();
    Call<ProvincesResp<List<ProvincesVo>>> callStartCity = null;

    //获取省级
    private void getProvinces() {
        callStartCity = getApis().getOriginatingProvinces().clone();
        callStartCity.enqueue(new Callback<ProvincesResp<List<ProvincesVo>>>() {
            @Override
            public void onResponse(Response<ProvincesResp<List<ProvincesVo>>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    provinceList = response.body().getProvinceList();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
            }
        });
    }


    class CityComparator implements Comparator<CityVo> {
        //如果o1小于o2,返回一个负数;如果o1大于o2，返回一个正数;如果他们相等，则返回0;
        @Override
        public int compare(CityVo lhs, CityVo rhs) {
            if (lhs.getFirstLatter().equals("热门")) {
                return -1;
            } else if (rhs.getFirstLatter().equals("热门")) {
                return 1;
            } else {
                return lhs.getFirstLatter().compareTo(rhs.getFirstLatter());
            }
        }
    }

    private void setCityData(List<CityVo> hotCity, List<CityVo> cityList) {
        if (lv_city != null) {
            Collections.sort(cityList, new CityComparator());
            hotCity.addAll(cityList);
            this.cityVoList = hotCity;
            mAdapter = new CityListAdapter(this.cityVoList);
            lv_city.setAdapter(mAdapter);
            lv_city.setOnScrollListener(mAdapter);
            lv_city.setPinnedHeaderView(getLayoutInflater().inflate(R.layout.header_item, lv_city, false));
        }
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        if (!TextUtils.isEmpty(s)) {
            lv_city.setSelection(this.mAdapter.getPositionForSection(findIndex(s)));
        }
    }

    //根据s找到对应的s的位置
    public int findIndex(String s) {
        for (int i = 0; i < SiderBar.sideBar.length; i++) {
            //根据city中的sortKey进行比较
            if (s.equals(SiderBar.sideBar[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void onPause() {
        TLog.d("onPause", "onPause");
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        if (callStartCity != null) {
            callStartCity.cancel();
            if (callStartHotCity != null) {
                callStartHotCity.cancel();
            }
        }
    }


    /*****
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
                sb.append("\nProvince : ");
                sb.append(location.getProvince());
                provinceName = location.getProvince();
                sb.append("\ncitycode : ");
                sb.append(location.getCityCode());
                sb.append("\ncity : ");
                sb.append(location.getCity());
                cityName = location.getCity();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvLocation.setText(cityName);
                    }
                });
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
                TLog.d("baidu", sb.toString());
                locationService.stop();
            }
        }
    };
}
