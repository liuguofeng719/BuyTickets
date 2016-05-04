package com.ticket.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.ticket.R;
import com.ticket.ui.base.BaseActivity;

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
        ImageView iv_bg = (ImageView) findViewById(R.id.splash_image);
        iv_bg.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,
                        IndexActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
