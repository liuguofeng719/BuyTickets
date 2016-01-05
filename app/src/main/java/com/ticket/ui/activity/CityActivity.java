package com.ticket.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.CityListResp;
import com.ticket.bean.CityVo;
import com.ticket.common.Constants;
import com.ticket.ui.adpater.CityListAdapter;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.CommonUtils;
import com.ticket.utils.TLog;
import com.ticket.widgets.PinnedHeaderListView;
import com.ticket.widgets.SiderBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.InjectView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CityActivity extends BaseActivity implements SiderBar.OnTouchingLetterChangedListener {

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.ed_search)
    EditText ed_search;
    @InjectView(R.id.lv_city)
    PinnedHeaderListView lv_city;
    @InjectView(R.id.siderBar)
    SiderBar siderBar;

    TextView messageView;
    private Bundle extras;
    private CityListAdapter mAdapter;
    private List<CityVo> cityVoList = new ArrayList<>();
    private WindowManager mWindowManager;

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.t_city;
    }

    @Override
    protected View getLoadingTargetView() {
        return lv_city;
    }

    @Override
    protected void initViewsAndEvents() {
        siderBar.setOnTouchingLetterChangedListener(this);
        View view = getLayoutInflater().inflate(R.layout.letter, null);
        this.messageView = (TextView) view.findViewById(R.id.tv_letter);
        this.messageView.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        lp.format = 1;
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(this.messageView, lp);

        this.btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if ("start".equals(extras.getString("city"))) {
            this.getStartCity();
        } else {
            this.getEndCity(extras.getString("startCityId"));
        }

        this.lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.city_list_item_name);
                TLog.d(TAG_LOG, textView.getTag().toString() + " = " + textView.getText());
                Intent intent = new Intent();
                intent.putExtra("cityId", textView.getTag().toString());
                intent.putExtra("cityName", textView.getText());
                if ("start".equals(extras.getString("city"))) {
                    setResult(Constants.comm.START_CITY_SUCCESS, intent);
                } else {
                    setResult(Constants.comm.END_CITY_SUCCESS, intent);
                }
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
                    if ("start".equals(extras.getString("city"))) {
                        getStartCity();
                    } else {
                        getEndCity(extras.getString("startCityId"));
                    }
                }
            }
        });
    }

    //获取开始城市
    private void getStartCity() {
        showLoading(getString(R.string.common_loading_message));
        Call<CityListResp<List<CityVo>>> callOrgCity = getApis().getOriginatingCity().clone();
        callOrgCity.enqueue(new Callback<CityListResp<List<CityVo>>>() {
            @Override
            public void onResponse(Response<CityListResp<List<CityVo>>> response, Retrofit retrofit) {

                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    final List<CityVo> cityList = response.body().getCityList();
                    //获取热门城市
                    Call<CityListResp<List<CityVo>>> callHotCity = getApis().getHotOriginatingCity().clone();
                    callHotCity.enqueue(new Callback<CityListResp<List<CityVo>>>() {

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
                                    CommonUtils.make(CityActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                                } else {
                                    CommonUtils.make(CityActivity.this, CommonUtils.getCodeToStr(response.code()));
                                }
                            }
                            hideLoading();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            hideLoading();
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

    //获取到达城市
    private void getEndCity(final String startId) {
        TLog.d(TAG_LOG, startId);
        showLoading(getString(R.string.common_loading_message));
        Call<CityListResp<List<CityVo>>> callOrgCity = getApis().getDestinationCities(startId).clone();
        callOrgCity.enqueue(new Callback<CityListResp<List<CityVo>>>() {

            @Override
            public void onResponse(Response<CityListResp<List<CityVo>>> response, Retrofit retrofit) {

                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    final List<CityVo> cityList = response.body().getCityList();
                    //获取热门城市
                    Call<CityListResp<List<CityVo>>> callHotCity = getApis().getHotDestinationCities(startId).clone();
                    callHotCity.enqueue(new Callback<CityListResp<List<CityVo>>>() {

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
                                    Collections.sort(cityList, new Comparator<CityVo>() {
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
                                    });
                                    setCityData(hotCity, cityList);
                                }
                            }
                            hideLoading();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            hideLoading();
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
        this.messageView.setText(s);
        this.messageView.setVisibility(View.VISIBLE);
        this.messageView.setText(s);
        this.messageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                messageView.setVisibility(View.INVISIBLE);
            }
        }, 2000);
        lv_city.setSelection(this.mAdapter.getPositionForSection(findIndex(s)));
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
        super.onPause();
        if (messageView.getParent() != null) {
            mWindowManager.removeView(messageView);
        }
    }
}
