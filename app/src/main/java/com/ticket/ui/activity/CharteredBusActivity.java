package com.ticket.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.common.Constants;
import com.ticket.ui.adpater.base.ListViewDataAdapter;
import com.ticket.ui.adpater.base.ViewHolderBase;
import com.ticket.ui.adpater.base.ViewHolderCreator;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.CommonUtils;
import com.ticket.utils.TLog;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
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
    @InjectView(R.id.iv_plus)
    ImageView iv_plus;
    @InjectView(R.id.go_time)
    TextView go_time;
    @InjectView(R.id.lv_scheduling)
    ListView lv_scheduling;

    private String date_time;
    private Dialog mDialog;
    private ListViewDataAdapter viewDataAdapter;
    private ArrayList dataList;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @OnClick(R.id.iv_plus)
    public void plus() {
        dataList = viewDataAdapter.getDataList();
        mDialog = CommonUtils.createDialog(this);
        mDialog.setContentView(R.layout.chartered_bus_dialog);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        final EditText startCity = (EditText) mDialog.findViewById(R.id.start_city);
        final TextView tv_day_number = (TextView) mDialog.findViewById(R.id.tv_day_number);
        tv_day_number.setText(dataList.size() == 0 ? "第1天" : "第" + dataList.size() + "天");
        final EditText endCity = (EditText) mDialog.findViewById(R.id.end_city);
        mDialog.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskPlans taskPlans = new TaskPlans();
                taskPlans.setStartCity(startCity.getText().toString());
                taskPlans.setEndCity(endCity.getText().toString());
                dataList.add(taskPlans);
                viewDataAdapter.notifyDataSetChanged();
                mDialog.dismiss();
            }
        });
        mDialog.show();
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
        this.go_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGoForResult(PickerActivity.class, 1);
            }
        });

        this.viewDataAdapter = new ListViewDataAdapter<TaskPlans>(new ViewHolderCreator<TaskPlans>() {

            @Override
            public ViewHolderBase createViewHolder(int position) {
                return new ViewHolderBase<TaskPlans>() {
                    TextView tv_day_number;
                    TextView start_city;
                    TextView end_city;
                    ImageView iv_minus;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = getLayoutInflater().inflate(R.layout.chartered_bus_scheduling, null);
                        tv_day_number = ButterKnife.findById(view, R.id.tv_day_number);
                        start_city = ButterKnife.findById(view, R.id.start_city);
                        end_city = ButterKnife.findById(view, R.id.end_city);
                        iv_minus = ButterKnife.findById(view, R.id.iv_minus);
                        return view;
                    }

                    @Override
                    public void showData(final int position, TaskPlans itemData) {
                        int days = position + 1;
                        tv_day_number.setText("第" + days + "天");
                        start_city.setText(itemData.getStartCity());
                        end_city.setText(itemData.getEndCity());
                        iv_minus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewDataAdapter.getDataList().remove(position);
                                viewDataAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                };
            }
        });
        lv_scheduling.setAdapter(this.viewDataAdapter);
    }

    class TaskPlans implements Serializable {

        private String startCity;
        private String endCity;

        public String getStartCity() {
            return startCity;
        }

        public void setStartCity(String startCity) {
            this.startCity = startCity;
        }

        public String getEndCity() {
            return endCity;
        }

        public void setEndCity(String endCity) {
            this.endCity = endCity;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.comm.PICKER_SUCCESS) {
            Date dt = (Date) data.getSerializableExtra("date");
            setCurrentTime(dt);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setCurrentTime(Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String str[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};//字符串数组
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        int day = rightNow.get(Calendar.DAY_OF_WEEK);//获取时间
        go_time.setText(sdf.format(dt) + " " + str[day - 1]);
        SimpleDateFormat sdfView = new SimpleDateFormat("yyyy-MM-dd");
        date_time = sdfView.format(dt);
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

    }
}
