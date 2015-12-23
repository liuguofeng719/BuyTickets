package com.ticket.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.ui.adpater.ViewPageAdpater;
import com.ticket.ui.base.BaseActivity;
import com.ticket.ui.fragment.MyFragment;
import com.ticket.ui.fragment.OrderFragment;
import com.ticket.ui.fragment.TicketFragment;
import com.ticket.utils.ExitDoubleClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class HomeActivity extends BaseActivity {

    @InjectView(R.id.viewPager)
    ViewPager viewPager;

    @InjectView(R.id.rdo_menu_group)
    RadioGroup radioGroup;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tv_header_title.setText(getString(R.string.home_header_title));
                        radioGroup.check(R.id.rdo_ticket);
                        break;
                    case 1:
                        tv_header_title.setText(getString(R.string.home_header_order));
                        radioGroup.check(R.id.rdo_order);
                        break;
                    case 2:
                        tv_header_title.setText(getString(R.string.home_user_info));
                        radioGroup.check(R.id.rdo_my);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        this.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdo_ticket:
                        tv_header_title.setText(getString(R.string.home_header_title));
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.rdo_order:
                        tv_header_title.setText(getString(R.string.home_header_order));
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.rdo_my:
                        tv_header_title.setText(getString(R.string.home_user_info));
                        viewPager.setCurrentItem(2);
                        break;
                }
            }
        });
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new TicketFragment());
        fragmentList.add(new OrderFragment());
        fragmentList.add(new MyFragment());
        ViewPageAdpater viewPageAdpater = new ViewPageAdpater(getSupportFragmentManager(), fragmentList);
        this.viewPager.setAdapter(viewPageAdpater);
        this.viewPager.setCurrentItem(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ExitDoubleClick.getInstance(this).doDoubleClick(2000, null);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
