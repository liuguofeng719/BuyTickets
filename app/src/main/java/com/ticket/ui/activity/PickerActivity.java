package com.ticket.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.timessquare.CalendarPickerView;
import com.ticket.R;
import com.ticket.common.Constants;
import com.ticket.ui.base.BaseActivity;

import java.util.Calendar;
import java.util.Date;

import butterknife.InjectView;

/**
 * Created by liuguofeng719 on 2015/12/15.
 */
public class PickerActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.calendar_view)
    CalendarPickerView calendar_view;
    @InjectView(R.id.tv_done)
    TextView tv_done;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.select_date;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        this.btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        calendar_view.init(new Date(), nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.SINGLE);
        this.tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date dt = calendar_view.getSelectedDate();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("date", dt == null ? new Date() : dt);
                intent.putExtras(bundle);
                setResult(Constants.comm.PICKER_SUCCESS, intent);
                finish();
            }
        });
    }
}
