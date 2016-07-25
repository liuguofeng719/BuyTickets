package com.ticket.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.ticket.R;
import com.ticket.bean.SuggestionInfoVo;
import com.ticket.common.Constants;
import com.ticket.ui.adpater.base.ListViewDataAdapter;
import com.ticket.ui.adpater.base.ViewHolderBase;
import com.ticket.ui.adpater.base.ViewHolderCreator;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.TLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by liuguofeng719 on 2016/7/24.
 */
public class SuggestionActivity extends BaseActivity implements OnGetSuggestionResultListener {

    @InjectView(R.id.tv_select_city)
    TextView tvSelectCity;
    @InjectView(R.id.tv_suggestion)
    EditText tvSuggestion;
    @InjectView(R.id.tv_cancel)
    TextView tvCancel;
    @InjectView(R.id.tv_select_city)
    TextView tv_select_city;
    @InjectView(R.id.lv_listView)
    ListView lvListView;

    private SuggestionSearch mSuggestionSearch;
    private ListViewDataAdapter<SuggestionInfoVo> listViewDataAdapter;
    private List<SuggestionInfoVo> infoVoList = new ArrayList<>();
    private Bundle extras;


    @OnClick(R.id.tv_select_city)
    public void tvSelectCity() {
        readyGoForResult(SuggestionCityActivity.class, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.comm.START_CITY_SUCCESS) {
            String startCityName = data.getStringExtra("cityName");
            tv_select_city.setText(startCityName);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.tv_cancel)
    public void tvCancel() {
        finish();
    }

    @OnTextChanged(value = R.id.tv_suggestion, callback = OnTextChanged.Callback.BEFORE_TEXT_CHANGED)
    public void tvSuggestion(Editable s) {
        if (s.length() > 0) {
            mSuggestionSearch.requestSuggestion(new SuggestionSearchOption()
                    .city(tv_select_city.getText().toString())
                    .keyword(s.toString().trim()));
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.suggestion_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected void initViewsAndEvents() {
        // 初始化建议搜索模块，注册建议搜索事件监听
        tv_select_city.setText(extras.getString("cityName"));
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        listViewDataAdapter = new ListViewDataAdapter<SuggestionInfoVo>(new ViewHolderCreator<SuggestionInfoVo>() {
            @Override
            public ViewHolderBase<SuggestionInfoVo> createViewHolder(int position) {
                return new ViewHolderBase<SuggestionInfoVo>() {
                    TextView tv_district;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.suggestion_item_activity, null);
                        tv_district = ButterKnife.findById(view, R.id.tv_district);
                        return view;
                    }

                    @Override
                    public void showData(int position, SuggestionInfoVo itemData) {
                        tv_district.setText(itemData.getDistrict() + itemData.getKey());
                        tv_district.setTag(itemData);
                    }
                };
            }
        });

        lvListView.setAdapter(listViewDataAdapter);
        lvListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_district = (TextView) view.findViewById(R.id.tv_district);
                SuggestionInfoVo sInfo = (SuggestionInfoVo) tv_district.getTag();
                Intent intent = new Intent();
                intent.putExtra("suggestionInfo", sInfo);
                if ("start".equals(extras.getString("city"))) {
                    setResult(Constants.comm.START_CITY_SUCCESS, intent);
                } else {
                    setResult(Constants.comm.END_CITY_SUCCESS, intent);
                }
                finish();
            }
        });
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        infoVoList.clear();
        ArrayList dataList = listViewDataAdapter.getDataList();
        dataList.clear();
        List<SuggestionResult.SuggestionInfo> allSuggestions = suggestionResult.getAllSuggestions();
        for (SuggestionResult.SuggestionInfo allSuggestion : allSuggestions) {
            SuggestionInfoVo suggestionInfoVo = new SuggestionInfoVo();
            suggestionInfoVo.setCity(allSuggestion.city);
            suggestionInfoVo.setDistrict(allSuggestion.district);
            suggestionInfoVo.setKey(allSuggestion.key);
            com.ticket.bean.LatLng latLng = new com.ticket.bean.LatLng();
            latLng.setLatitude(allSuggestion.pt.latitude);
            latLng.setLongitude(allSuggestion.pt.longitude);
            suggestionInfoVo.setPt(latLng);
            suggestionInfoVo.setUid(allSuggestion.uid);
            infoVoList.add(suggestionInfoVo);
            LatLng pt = allSuggestion.pt;
            if (pt != null) {
                TLog.d(TAG_LOG, allSuggestion.district + "==" +
                        allSuggestion.key + "==" +
                        pt.longitude + "," + pt.latitude);
            }
        }
        dataList.addAll(infoVoList);
        listViewDataAdapter.notifyDataSetChanged();
    }
}
