package com.ticket.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.ticket.R;
import com.ticket.ui.base.BaseActivity;

import butterknife.InjectView;

public class WebViewActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btn_back;

    @InjectView(R.id.iv_preview)
    WebView iv_preview;
    Bundle bundle;

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.bundle = extras;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.web_view;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_preview.loadUrl(bundle.getString("navUrl"));
        iv_preview.getSettings().setJavaScriptEnabled(true);
        iv_preview.getSettings().setSupportZoom(false);
        iv_preview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        iv_preview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && iv_preview.canGoBack()) {
            iv_preview.goBack();
        }
        return super.onKeyDown(keyCode, event);
    }
}
