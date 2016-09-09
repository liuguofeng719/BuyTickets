package com.ticket.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.ui.adpater.ViewPageAdpater;
import com.ticket.ui.base.BaseActivity;
import com.ticket.ui.fragment.HomeFragment;
import com.ticket.ui.fragment.MyFragment;
import com.ticket.ui.fragment.NewOrderFragment;
import com.ticket.utils.AppPreferences;
import com.ticket.utils.ExitDoubleClick;
import com.ticket.utils.TLog;
import com.ticket.widgets.XViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class IndexActivity extends BaseActivity {

    @InjectView(R.id.viewPager)
    XViewPager viewPager;

    @InjectView(R.id.rdo_menu_group)
    RadioGroup radioGroup;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;

    private Bundle extras;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (extras != null) {
            int current = extras.getInt("current", 0);
            if (current != 0) {
                extras.remove("current");
                viewPager.setCurrentItem(current, false);
                ((RadioButton) this.radioGroup.getChildAt(current)).setChecked(true);
            }
        }
    }

    @Override
    protected void initViewsAndEvents() {
        viewPager.setEnableScroll(false);
        this.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdo_ticket:
                        tv_header_title.setText(getString(R.string.home_header_title));
                        viewPager.setCurrentItem(0, false);
                        break;
                    case R.id.rdo_order:
                        if (!TextUtils.isEmpty(AppPreferences.getString("userId"))) {
                            tv_header_title.setText(getString(R.string.home_header_order));
                            viewPager.setCurrentItem(1, false);
                        } else {
                            viewPager.setCurrentItem(0);
                            readyGo(LoginActivity.class);
                        }
                        break;
                    case R.id.rdo_my:
                        tv_header_title.setText(getString(R.string.home_user_info));
                        viewPager.setCurrentItem(2, false);
                        break;
                }
            }
        });
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new NewOrderFragment());
        fragmentList.add(new MyFragment());
        ViewPageAdpater viewPageAdpater = new ViewPageAdpater(getSupportFragmentManager(), fragmentList);
        this.viewPager.setAdapter(viewPageAdpater);
        if (extras != null) {
            TLog.d(TAG_LOG, extras.getInt("current") + "");
            this.viewPager.setCurrentItem(extras.getInt("current"));
        } else {
            this.viewPager.setCurrentItem(0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ExitDoubleClick.getInstance(this).doDoubleClick(2000, null);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
