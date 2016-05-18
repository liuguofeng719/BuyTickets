package com.ticket.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.ticket.R;
import com.ticket.bean.VersionVo;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.CommonUtils;
import com.ticket.utils.TLog;
import com.ticket.utils.UpdateManager;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SplashActivity extends BaseActivity {

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

    @Override
    protected void initViewsAndEvents() {
        final ImageView iv_bg = (ImageView) findViewById(R.id.splash_image);

        Call<VersionVo> versionVoCall = getApis().getVersion();
        versionVoCall.enqueue(new Callback<VersionVo>() {
            @Override
            public void onResponse(Response<VersionVo> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    TLog.d("version", "" + getVersionCode()+" =serverCode= "+response.body().getVersionCode());
                    if (response.body().getVersionCode() > getVersionCode()) {
                        new UpdateManager(SplashActivity.this,
                                response.body().getAppUrl(),
                                new IncomingHandler()).showDownloadDialog();
                    } else {
                        iv_bg.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SplashActivity.this, IndexActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 3000);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    class IncomingHandler extends Handler {

        public void handleMessage(android.os.Message msg) {
            if (msg.what == 12) {
                CommonUtils.make(SplashActivity.this, "不能打开升级地址");
            } else if (msg.what == 13) {
                CommonUtils.make(SplashActivity.this, "找不到升级地址");
            } else if (msg.what == 14) {
                CommonUtils.make(SplashActivity.this, "下载升级包失败，请检查磁盘空间或者是否关闭了权限");
            }
        }
    }


    private int getVersionCode() {
        PackageManager packageManager = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
