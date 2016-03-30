package com.ticket.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.ticket.R;
import com.ticket.ui.activity.LoginActivity;
import com.ticket.ui.base.BaseFragment;
import com.ticket.utils.AppPreferences;
import com.ticket.widgets.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class NewOrderFragment extends BaseFragment {

    @InjectView(R.id.tab_page_indicator)
    TabPageIndicator tab_page_indicator;
    @InjectView(R.id.viewPager)
    ViewPager viewPager;

    List<Fragment> fragmentList = new ArrayList<>();
    private static String[] TITLES;

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {
        if (TextUtils.isEmpty(AppPreferences.getString("userId"))) {
            readyGo(LoginActivity.class);
            return;
        }
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
        TITLES = getResources().getStringArray(R.array.order_nav);
        fragmentList.add(new CarTicketFragment());
        fragmentList.add(new StudentTraelFragment());
        fragmentList.add(new CharteredBusFragment());
        fragmentList.add(new LongBusFragment());
        viewPager.setAdapter(new OrderAdapter(getChildFragmentManager()));
        tab_page_indicator.setViewPager(viewPager);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.my_order_news;
    }

    class OrderAdapter extends FragmentPagerAdapter {

        public OrderAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position % TITLES.length].toUpperCase();
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }
}
