package com.ticket.ui.fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ticket.R;
import com.ticket.ui.activity.LoginActivity;
import com.ticket.ui.base.BaseFragment;
import com.ticket.utils.AppPreferences;
import com.ticket.widgets.SegmentedGroup;

import butterknife.InjectView;

public class OrderFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    @InjectView(R.id.order_group)
    SegmentedGroup order_group;
    @InjectView(R.id.waiting_order)
    RadioButton waiting_order;
    @InjectView(R.id.fl_myorder)
    FrameLayout fl_myorder;

    FragmentManager fragmentManager;

    @Override
    protected void onFirstUserVisible() {
        waiting_order.setChecked(true);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_myorder, new UnpaidFragment());
        transaction.commit();
    }

    @Override
    protected void onUserVisible() {
        if (TextUtils.isEmpty(AppPreferences.getString("userId"))) {
            readyGo(LoginActivity.class);
            return;
        }
        waiting_order.setChecked(true);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_myorder, new UnpaidFragment());
        transaction.commit();
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
        order_group.setOnCheckedChangeListener(this);
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.order;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.waiting_order://待支付
                FragmentTransaction unpaidTransaction = fragmentManager.beginTransaction();
                unpaidTransaction.replace(R.id.fl_myorder, new UnpaidFragment(), "Unpaid");
                unpaidTransaction.commit();
                break;
            case R.id.pay_order://已支付
                FragmentTransaction paidTransaction = fragmentManager.beginTransaction();
                paidTransaction.replace(R.id.fl_myorder, new PaidFragment(), "Paid");
                paidTransaction.commit();
                break;
        }
    }
}
