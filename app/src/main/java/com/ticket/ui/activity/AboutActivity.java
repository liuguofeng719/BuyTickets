package com.ticket.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.ui.base.BaseActivity;

import butterknife.InjectView;

public class AboutActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btn_back;

    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.about;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText("关于我们");
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
