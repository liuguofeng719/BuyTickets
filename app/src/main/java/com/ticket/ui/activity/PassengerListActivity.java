package com.ticket.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ticket.R;
import com.ticket.bean.PassengerListResp;
import com.ticket.bean.PassengerVo;
import com.ticket.common.Constants;
import com.ticket.ui.adpater.PassengerAdapter;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.AppPreferences;
import com.ticket.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PassengerListActivity extends BaseActivity {

    @InjectView(R.id.lv_passenger_list)
    ListView lv_passenger_list;
    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.btn_ok)
    TextView btn_ok;
    @InjectView(R.id.btn_add_passenger)
    Button btn_add_passenger;

    PassengerAdapter passengerAdapter;
    List<PassengerVo> passengerVoList;
    Bundle extras;

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.btn_add_passenger)
    public void addPassenger() {
        readyGo(AddPassengerActivity.class);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.passenger_list;
    }

    @Override
    protected View getLoadingTargetView() {
        return lv_passenger_list;
    }

    @Override
    protected void initViewsAndEvents() {

        lv_passenger_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.cbo_selected);
                if (checkBox.isChecked()) {
                    PassengerAdapter.checkItems.put(position, false);
                } else {
                    PassengerAdapter.checkItems.put(position, true);
                }
                passengerAdapter.notifyDataSetChanged();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Integer> selectedIds = new HashMap<String, Integer>();
                List<PassengerVo> selectedVo = new ArrayList<PassengerVo>();
                for (int i = 0; i < PassengerAdapter.checkItems.size(); i++) {
                    if (PassengerAdapter.checkItems.get(i)) {
                        PassengerVo pvo = passengerVoList.get(i);
                        selectedIds.put(pvo.getPassengerId(), i);
                        selectedVo.add(passengerVoList.get(i));
                    }
                }
                if (selectedIds.size() <= 0) {
                    Toast.makeText(v.getContext(), "请选择乘客", Toast.LENGTH_SHORT).show();
                    return;
                }
                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                Intent data = new Intent();
                data.putExtra("passengerVoList", new Gson().toJson(selectedVo));
                data.putExtra("selectedIds", gson.toJson(selectedIds));
                data.putExtras(data);
                setResult(Constants.comm.PASSENGER_SUCCESS, data);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getPassengers();
    }

    private void getPassengers() {
        showLoading(getString(R.string.common_loading_message));
        Call<PassengerListResp<List<PassengerVo>>> callPassenger = getApis().getPassengers(AppPreferences.getString("userId")).clone();
        callPassenger.enqueue(new Callback<PassengerListResp<List<PassengerVo>>>() {
            @Override
            public void onResponse(Response<PassengerListResp<List<PassengerVo>>> response, Retrofit retrofit) {
                hideLoading();
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    passengerVoList = response.body().getPassengerList();
                    passengerAdapter = new PassengerAdapter(response.body().getPassengerList());
                    lv_passenger_list.setAdapter(passengerAdapter);
                    String selectedIds = extras.getString("selectedIds");
                    if (!TextUtils.isEmpty(selectedIds)) {
                        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                        Map<String, Integer> mapIds = gson.fromJson(selectedIds, new TypeToken<Map<String, Integer>>() {
                        }.getType());
                        Set<String> keys = mapIds.keySet();
                        Iterator<String> it = keys.iterator();
                        while (it.hasNext()) {
                            String key = it.next();
                            int index = mapIds.get(key);
                            PassengerAdapter.checkItems.put(index, true);
                        }
                    }
                    passengerAdapter.notifyDataSetChanged();
                } else {
                    if (response.body() != null) {
                        PassengerListResp<List<PassengerVo>> body = response.body();
                        CommonUtils.make(PassengerListActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                    } else {
                        CommonUtils.make(PassengerListActivity.this, CommonUtils.getCodeToStr(response.code()));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
            }
        });
    }


}
