package com.ticket.ui.base;

import com.ticket.api.Apis;
import com.ticket.utils.RetrofitUtils;
import com.ticket.view.base.BaseView;

import retrofit.Retrofit;

public abstract class BaseFragment extends BaseLazyFragment implements BaseView {
    protected Apis getApis(){
        Retrofit retrofit = RetrofitUtils.getRfHttpsInstance(Apis.TROYCD);
        return retrofit.create(Apis.class);
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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
        toggleShowLoading(true, null);
    }

    @Override
    public void hideLoading() {
        toggleShowLoading(false, null);
    }
}
