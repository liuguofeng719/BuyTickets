package com.ticket.ui.base;

import android.os.Bundle;

import com.ticket.TicketsApplication;
import com.ticket.api.Apis;
import com.ticket.netstatus.NetUtils;
import com.ticket.utils.RetrofitUtils;
import com.ticket.view.base.BaseView;

import retrofit.Retrofit;

public abstract class BaseActivity extends BaseFragmentActivity implements BaseView {

    protected Apis getApis(){
        Retrofit retrofit = RetrofitUtils.getRfHttpsInstance(Apis.TROYCD);
        return retrofit.create(Apis.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected TicketsApplication getBaseApplication() {
        return (TicketsApplication) getApplication();
    }

    @Override
    public void showError(String msg) {
        toggleShowError(true, msg, null);
    }

    @Override
    public void showException(String msg) {
        toggleShowError(true, msg, null);
    }

    @Override
    public void showNetError() {
        toggleNetworkError(true, null);
    }

    @Override
    public void showLoading(String msg) {
        toggleShowLoading(true, msg);
    }

    @Override
    public void hideLoading() {
        toggleShowLoading(false, null);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }
}
