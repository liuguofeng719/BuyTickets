package com.ticket.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.PassengerListResp;
import com.ticket.bean.PassengerVo;
import com.ticket.ui.adpater.PassengerMngAdapter;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.AppPreferences;
import com.ticket.utils.CommonUtils;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class PassengerManagerActivity extends BaseActivity implements PassengerMngAdapter.DelItemClickListener {

    @InjectView(R.id.lv_passenger_list)
    ListView lv_passenger_list;
    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.ly_buttom)
    LinearLayout ly_buttom;
    @InjectView(R.id.btn_add_passenger)
    TextView btn_add_passenger;

    PassengerMngAdapter passengerAdapter;
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
    protected void onResume() {
        super.onResume();
        getPassengers();
    }

    @Override
    protected void initViewsAndEvents() {
        ly_buttom.setVisibility(View.GONE);
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
                    passengerAdapter = new PassengerMngAdapter(response.body().getPassengerList());
                    passengerAdapter.setDelItemClickListener(PassengerManagerActivity.this);
                    lv_passenger_list.setAdapter(passengerAdapter);
                } else {
                    if (response.body() != null) {
                        PassengerListResp<List<PassengerVo>> body = response.body();
                        CommonUtils.make(PassengerManagerActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                    } else {
                        CommonUtils.make(PassengerManagerActivity.this, CommonUtils.getCodeToStr(response.code()));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
            }
        });
    }

    @Override
    public void DelItemClickListener(final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PassengerManagerActivity.this);
        builder.setMessage("确认要删除联系人？");
        builder.setTitle("温馨提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String pId = v.getTag().toString();
                Call<PassengerListResp> callDelPass = getApis().deletePassenger(pId).clone();
                callDelPass.enqueue(new Callback<PassengerListResp>() {
                    @Override
                    public void onResponse(Response<PassengerListResp> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                            getPassengers();
                        } else {
                            if (response.body() != null) {
                                PassengerListResp<List<PassengerVo>> body = response.body();
                                CommonUtils.make(PassengerManagerActivity.this, body.getErrorMessage() == null ? response.message() : body.getErrorMessage());
                            } else {
                                CommonUtils.make(PassengerManagerActivity.this, CommonUtils.getCodeToStr(response.code()));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
