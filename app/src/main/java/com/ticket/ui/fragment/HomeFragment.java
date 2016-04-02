package com.ticket.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ticket.R;
import com.ticket.bean.PicturesVo;
import com.ticket.bean.PicturesVoResp;
import com.ticket.ui.activity.CrowdFundingActivity;
import com.ticket.ui.activity.StudentTripActivity;
import com.ticket.ui.activity.TicketActivity;
import com.ticket.ui.activity.WebViewActivity;
import com.ticket.ui.base.BaseFragment;
import com.ticket.utils.ShareUtils;
import com.ticket.utils.ShareVo;
import com.ticket.utils.TLog;
import com.ticket.widgets.SlideShowView;

import java.util.HashMap;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class HomeFragment extends BaseFragment implements SlideShowView.OnImageClickedListener, PlatformActionListener {

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

    @InjectView(R.id.btn_text_share)
    Button btn_text_share;
    @InjectView(R.id.btn_text_login)
    Button btn_text_login;

    @OnClick(R.id.ly_student)
    public void studentTips(){
        readyGo(StudentTripActivity.class);
    }

    @OnClick(R.id.btn_text_login)
    public void btn_text_login() {
        authorize(new Wechat(getActivity()));
    }

    @OnClick(R.id.ly_travel)
    public void travel(){
        readyGo(CrowdFundingActivity.class);
    }

    private void authorize(Platform plat) {
        if (plat.isValid()) {
            String userId = plat.getDb().getUserId();
            if (!TextUtils.isEmpty(userId)) {
                TLog.d(TAG_LOG, userId + " plat= " + plat.getName());
//                UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
//                login(plat.getName(), userId, null);
                return;
            }
        }
        plat.setPlatformActionListener(this);
        plat.SSOSetting(true);
        plat.showUser(null);
    }

    @OnClick(R.id.btn_text_share)
    public void btnTextShare() {
        ShareVo shareVo = new ShareVo();
        shareVo.setType(ShareVo.platform.WECHAT.name());
//        shareVo.setText("测试测试测试微信分享");
        shareVo.setUrl("http://user.qzone.qq.com/1039163285/infocenter?ptsig=*Sb6sER-9smBstwyL28cYg2h0D99pYGcPvoySuzkGyc_");
        shareVo.setTitle("我是来自Android客户端分享实例Demo的数据");
        ShareUtils.showShare(getActivity(), shareVo);
    }

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
        ShareSDK.initSDK(getActivity());
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

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
        if (action == Platform.ACTION_USER_INFOR) {
        }
        System.out.println(hashMap);
        System.out.println("------User Name ---------" + platform.getDb().getUserName());
        System.out.println("------User ID ---------" + platform.getDb().getUserId());
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }
}
