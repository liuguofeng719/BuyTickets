package com.ticket.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.ticket.R;
import com.ticket.bean.PicturesVo;
import com.ticket.bean.PicturesVoResp;
import com.ticket.ui.activity.TicketActivity;
import com.ticket.ui.activity.WebViewActivity;
import com.ticket.ui.base.BaseFragment;
import com.ticket.widgets.SlideShowView;

import java.util.List;

import butterknife.InjectView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class HomeFragment extends BaseFragment implements SlideShowView.OnImageClickedListener {

    @InjectView(R.id.ly_bus)
    LinearLayout ly_bus;
    @InjectView(R.id.ly_bus_long)
    LinearLayout ly_bus_long;
    @InjectView(R.id.ly_student)
    LinearLayout ly_student;
    @InjectView(R.id.ly_travel)
    LinearLayout ly_travel;

    @InjectView(R.id.slideShowView)
    SlideShowView mSlideShowView;

    @Override
    protected void onFirstUserVisible() {
        getPics();
    }

    private void getPics() {
        Call<PicturesVoResp<List<PicturesVo>>> callPics = getApis().getAdvertisementPictures().clone();
        callPics.enqueue(new Callback<PicturesVoResp<List<PicturesVo>>>() {
            @Override
            public void onResponse(Response<PicturesVoResp<List<PicturesVo>>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    if (mSlideShowView != null) {
                        mSlideShowView.clearImages();
                        List<PicturesVo> pics = response.body().getPictures();
                        if (pics.size() > 0) {
                            mSlideShowView.setImageUrlList(pics);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

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
        this.mSlideShowView.setOnImageClickedListener(this);
        ly_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGo(TicketActivity.class);
            }
        });
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.index_main;
    }

    @Override
    public void onImageClicked(int position, SlideShowView.ImageViewTag url) {
        Bundle bundle = new Bundle();
        bundle.putString("navUrl", url.getNavUrl());
        readyGo(WebViewActivity.class, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSlideShowView != null) {
            mSlideShowView.onDestroy();
        }
    }
}
