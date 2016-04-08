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
import com.ticket.bean.PlaceListResp;
import com.ticket.bean.PlaceVo;
import com.ticket.ui.adpater.CityPlaceListAdapter;
import com.ticket.ui.base.BaseActivity;
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

/**
 * Created by liuguofeng719 on 2016/4/7.
 */
public class PlaceActivity extends BaseActivity implements SiderBar.OnTouchingLetterChangedListener {

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

    TextView messageView;
    private Bundle extras;
    private CityPlaceListAdapter mAdapter;
    private List<PlaceVo> placeVoArrayList = new ArrayList<>();
    private WindowManager mWindowManager;
    private Call<PlaceListResp<List<PlaceVo>>> placeListRespCall;

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.t_city_place;
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
            tv_header_title.setText("开始地点");
        }else{
            tv_header_title.setText("结束地点");
        }
        this.getStartCity();
        this.lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.city_list_item_name);
                TLog.d(TAG_LOG, textView.getTag().toString() + " = " + textView.getText());
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setClass(PlaceActivity.this, CrowdFundingActivity.class);
                intent.putExtra("cityId", textView.getTag().toString());
                intent.putExtra("cityName", textView.getText());
                intent.putExtra("city", extras.getString("city"));
                startActivity(intent);
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
                List<PlaceVo> newCitys = new ArrayList<>();
                if (placeVoArrayList != null) {
                    for (PlaceVo cityVo : placeVoArrayList) {
                        if (cityVo.getPlaceName().contains(s)) {
                            newCitys.add(cityVo);
                        }
                    }
                    placeVoArrayList.clear();
                    Collections.sort(newCitys, new CityComparator());
                    placeVoArrayList.addAll(newCitys);
                    mAdapter.updateView(placeVoArrayList);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                TLog.d("afterTextChanged", s.toString());
                if (s.length() == 0) {
                    if ("start".equals(extras.getString("city"))) {
                        getStartCity();
                    }
                }
            }
        });
    }

    //获取开始城市
    private void getStartCity() {
        placeListRespCall = getApis().getPlaceList(extras.getString("cityId")).clone();
        placeListRespCall.enqueue(new Callback<PlaceListResp<List<PlaceVo>>>() {

            @Override
            public void onResponse(Response<PlaceListResp<List<PlaceVo>>> response, Retrofit retrofit) {
                hideLoading();
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    List<PlaceVo> placeList = response.body().getPlaceList();
                    setCityData(placeList);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
            }
        });
    }

    class CityComparator implements Comparator<PlaceVo> {
        //如果o1小于o2,返回一个负数;如果o1大于o2，返回一个正数;如果他们相等，则返回0;
        @Override
        public int compare(PlaceVo lhs, PlaceVo rhs) {
            if (lhs.getFirstLatter().equals("热门")) {
                return -1;
            } else if (rhs.getFirstLatter().equals("热门")) {
                return 1;
            } else {
                return lhs.getFirstLatter().compareTo(rhs.getFirstLatter());
            }
        }
    }

    private void setCityData(List<PlaceVo> placeVoList) {
        if (lv_city != null) {
            Collections.sort(placeVoList, new CityComparator());
            this.placeVoArrayList = placeVoList;
            mAdapter = new CityPlaceListAdapter(this.placeVoArrayList);
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

    @Override
    protected void onStop() {
        super.onStop();
        if (placeListRespCall != null) {
            placeListRespCall.cancel();
        }
    }
}
