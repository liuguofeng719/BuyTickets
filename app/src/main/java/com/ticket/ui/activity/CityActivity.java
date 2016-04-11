package com.ticket.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import com.ticket.bean.ProvincesResp;
import com.ticket.bean.ProvincesVo;
import com.ticket.common.Constants;
import com.ticket.ui.adpater.CityListAdapter;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.CommonUtils;
import com.ticket.utils.TLog;
import com.ticket.widgets.PinnedHeaderListView;
import com.ticket.widgets.SiderBar;
import com.ticket.widgets.TagGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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

    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;

    @InjectView(R.id.tag_group)
    TagGroup tag_group;

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
        showLoading(getString(R.string.common_loading_message));
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
            tv_header_title.setText(getString(R.string.start_city));
            this.getStartCity();
        } else {
            tv_header_title.setText(getString(R.string.end_city));
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

        tag_group.setOnTagClickListener(new TagGroup.OnTagClickListener() {

            @Override
            public void onTagClick(String tag) {
                if ("start".equals(extras.getString("city"))) {
                    getStartProviceCity(tag);
                } else {
                    getEndProviceCity(tag);
                }
            }
        });
    }

    /**
     * 通过省获取结束城市
     *
     * @param tag
     */
    private void getEndProviceCity(String tag) {
        String findName = "";
        Set<String> keySet = provinceMap.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
            String key = it.next();
            if (provinceMap.get(key).equalsIgnoreCase(tag)) {
                findName = key;
                break;
            }
        }
        if (!TextUtils.isEmpty(findName)) {
            Call<CityListResp<List<CityVo>>> callOrgCity = getApis().GetDestinationCitiesByProvinceID(extras.getString("startCityId"), findName).clone();
            callOrgCity.enqueue(new Callback<CityListResp<List<CityVo>>>() {
                @Override
                public void onResponse(Response<CityListResp<List<CityVo>>> response, Retrofit retrofit) {
                    hideLoading();
                    if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                        final List<CityVo> cityList = response.body().getCityList();
                        //获取热门城市
                        Call<CityListResp<List<CityVo>>> callHotCity = getApis().getHotDestinationCities(extras.getString("startCityId")).clone();
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
        } else {
            hideLoading();
        }
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
                                    CommonUtils.make(CityActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                                } else {
                                    CommonUtils.make(CityActivity.this, CommonUtils.getCodeToStr(response.code()));
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
        if (!TextUtils.isEmpty(findName)) {
            Call<CityListResp<List<CityVo>>> callOrgCity = getApis().getOriginatingCity(findName).clone();
            this.commCall(callOrgCity);
        } else {
            hideLoading();
        }
    }

    List<ProvincesVo> provinceList;
    Map<String, String> provinceMap = new ConcurrentHashMap<>();

    Call<ProvincesResp<List<ProvincesVo>>> callStartCity = null;

    //获取开始城市
    private void getStartCity() {
        //showLoading(getString(R.string.common_loading_message));
        callStartCity = getApis().getOriginatingProvinces().clone();

        callStartCity.enqueue(new Callback<ProvincesResp<List<ProvincesVo>>>() {

            @Override
            public void onResponse(Response<ProvincesResp<List<ProvincesVo>>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    provinceList = response.body().getProvinceList();
                    List<String> tagList = new ArrayList<String>();
                    for (ProvincesVo pvo : provinceList) {
                        tagList.add(pvo.getProvinceName());
                        provinceMap.put(pvo.getProvinceId(), pvo.getProvinceName());
                    }
                    tag_group.setTags(tagList, 0);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
            }
        });

    }

    Call<CityListResp<List<CityVo>>> callEndCity = null;
    Call<CityListResp<List<CityVo>>> callEndHotCity = null;

    //获取到达城市
    private void getEndCity(final String startId) {
        TLog.d(TAG_LOG, startId);
        callEndCity = getApis().getDestinationCities(startId).clone();
        callEndCity.enqueue(new Callback<CityListResp<List<CityVo>>>() {

            @Override
            public void onResponse(Response<CityListResp<List<CityVo>>> response, Retrofit retrofit) {

                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    final List<CityVo> cityList = response.body().getCityList();
                    //获取省市
                    List<ProvincesVo> provinceList = response.body().getProvinceList();
                    List<String> tagList = new ArrayList<String>();
                    for (ProvincesVo pvo : provinceList) {
                        tagList.add(pvo.getProvinceName());
                        provinceMap.put(pvo.getProvinceId(), pvo.getProvinceName());
                    }
                    tag_group.setTags(tagList, 0);

                    //获取热门城市
                    callEndHotCity = getApis().getHotDestinationCities(startId).clone();
                    callEndHotCity.enqueue(new Callback<CityListResp<List<CityVo>>>() {

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
        if(!TextUtils.isEmpty(s)){
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
        super.onPause();
        if (messageView.getParent() != null) {
            mWindowManager.removeView(messageView);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (extras != null) {
            if ("start".equals(extras.getString("city"))) {
                if (callStartCity != null) {
                    callStartCity.cancel();
                    if (callStartHotCity != null) {
                        callStartHotCity.cancel();
                    }
                }
            } else {
                if (callEndCity != null) {
                    callEndCity.cancel();
                    if (callEndHotCity != null) {
                        callEndHotCity.cancel();
                    }
                }
            }
        }
    }
}
