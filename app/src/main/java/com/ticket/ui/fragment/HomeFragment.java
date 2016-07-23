package com.ticket.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.ticket.R;
import com.ticket.bean.PicturesVo;
import com.ticket.bean.PicturesVoResp;
import com.ticket.ui.activity.CharteredBusActivity;
import com.ticket.ui.activity.LoginActivity;
import com.ticket.ui.activity.StudentTripActivity;
import com.ticket.ui.activity.TicketActivity;
import com.ticket.ui.activity.TravelCharteredActivity;
import com.ticket.ui.activity.WebViewActivity;
import com.ticket.ui.base.BaseFragment;
import com.ticket.utils.AppPreferences;
import com.ticket.widgets.SlideShowView;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
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

//    @InjectView(R.id.btn_text_share)
//    Button btn_text_share;
//    @InjectView(R.id.btn_text_login)
//    Button btn_text_login;
//    @InjectView(R.id.btn_qq_login)
//    Button btn_qq_login;
//    @InjectView(R.id.btn_qq_share)
//    Button btn_qq_share;

    @OnClick(R.id.ly_student)
    public void studentTips() {
        readyGo(StudentTripActivity.class);
    }

//    @OnClick(R.id.btn_text_login)
//    public void btn_text_login() {
//        authorize(new Wechat(getActivity()));
//    }
//    @OnClick(R.id.btn_qq_login)
//    public void btn_qq_login() {
//        authorize(new QQ(getActivity()));
//    }
//    @OnClick(R.id.btn_qq_share)
//    public void btn_qq_share() {
//        ShareVo shareVo = new ShareVo();
//        shareVo.setType(ShareVo.platform.QQ.name());
//        shareVo.setText("测试测试测试微信QQ分享");
//        shareVo.setTitle("我是来自Android客户端分享实例Demo的数据");
//        shareVo.setTitleUrl("http://www.baidu.com");
//        shareVo.setComment("测试QQ分享");
//        shareVo.setSite("百度");
//        shareVo.setSiteUrl("http://www.baidu.com");
//        ShareUtils.showShare(getActivity(), shareVo);
//    }

    @OnClick(R.id.ly_travel)
    public void travel() {
        readyGo(TravelCharteredActivity.class);
    }

    /**
     * 老的包车页面
     */
    private void oldTravel() {
        if (!TextUtils.isEmpty(AppPreferences.getString("userId"))) {
            readyGo(CharteredBusActivity.class);
        } else {
            readyGo(LoginActivity.class);
        }
    }

//    private void authorize(Platform plat) {
//        if (plat.isValid()) {
//            String userId = plat.getDb().getUserId();
//            if (!TextUtils.isEmpty(userId)) {
//                TLog.d(TAG_LOG, userId + " plat= " + plat.getName());
////                UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
////                login(plat.getName(), userId, null);
//                return;
//            }
//        }
//        plat.SSOSetting(true);
//        plat.showUser(null);
//    }
//
//    @OnClick(R.id.btn_text_share)
//    public void btnTextShare() {
//        ShareVo shareVo = new ShareVo();
//        shareVo.setType(ShareVo.platform.WECHAT.name());
////        shareVo.setText("测试测试测试微信分享");
//        shareVo.setUrl("http://user.qzone.qq.com/1039163285/infocenter?ptsig=*Sb6sER-9smBstwyL28cYg2h0D99pYGcPvoySuzkGyc_");
//        shareVo.setTitle("我是来自Android客户端分享实例Demo的数据");
//        ShareUtils.showShare(getActivity(), shareVo);
//    }

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
}
