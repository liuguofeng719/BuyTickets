package com.ticket.ui.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.CommonUtils;
import com.ticket.utils.TLog;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by liuguofeng719 on 2016/4/3.
 */
public class CharteredBusActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.ly_route_total)
    LinearLayout ly_route_total;
    @InjectView(R.id.iv_plus)
    ImageView iv_plus;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @OnClick(R.id.iv_plus)
    public void plus() {
        View inflate = getLayoutInflater().inflate(R.layout.chartered_bus_scheduling, null);
        ly_route_total.addView(inflate);
        traversalView((ViewGroup) inflate.getParent());
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.chartered_bus;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText("包车出行");
        View inflate = getLayoutInflater().inflate(R.layout.chartered_bus_scheduling, null);
        ly_route_total.addView(inflate);
        traversalView((ViewGroup) inflate.getParent());
    }

    /**
     * 遍历所有view
     *
     * @param viewGroup
     */
    public void traversalView(ViewGroup viewGroup) {
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                traversalView((ViewGroup) view);
            } else {
                doView(view);
            }
        }
    }

    /**
     * 处理view
     *
     * @param view
     */
    private void doView(View view) {
        if (view instanceof TextView) {
            if (view.getTag() == null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommonUtils.make(CharteredBusActivity.this, ((TextView) v).getText().toString());
                    }
                });
            } else {
                TLog.i("TextView", view.toString());
            }
        } else if (view instanceof ImageView) {
            if (view.getTag() != null) {
                if (view.getTag().toString().equals("minus")) {
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CommonUtils.make(CharteredBusActivity.this, "ImageView");
                        }
                    });
                }
            }
        }
    }
}
