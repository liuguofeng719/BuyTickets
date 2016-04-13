package com.ticket.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.BaseInfoVo;
import com.ticket.bean.UserVo;
import com.ticket.common.Constants;
import com.ticket.ui.adpater.base.ListViewDataAdapter;
import com.ticket.ui.adpater.base.ViewHolderBase;
import com.ticket.ui.adpater.base.ViewHolderCreator;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.AppPreferences;
import com.ticket.utils.CommonUtils;
import com.ticket.utils.TLog;

import java.io.Serializable;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by liuguofeng719 on 2016/4/3.
 */
public class CharteredBusActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.edit_car_number)
    EditText edit_car_number;
    @InjectView(R.id.tv_tips_scheduling)
    TextView tv_tips_scheduling;
    @InjectView(R.id.go_time)
    TextView go_time;
    @InjectView(R.id.tv_enquiry)
    TextView tv_enquiry;
    @InjectView(R.id.lv_scheduling)
    ListView lv_scheduling;

    private String date_time;
    private Dialog mDialog;
    private ListViewDataAdapter viewDataAdapter;
    private ArrayList dataList;
    private SortedMap<String, List<TaskPlans>> taskMap = new TreeMap<>();

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @OnClick(R.id.iv_plus)
    public void plus() {
        mDialog = CommonUtils.createDialog(this);
        mDialog.setContentView(R.layout.chartered_bus_dialog);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        final EditText startCity = (EditText) mDialog.findViewById(R.id.start_city);
        final EditText endCity = (EditText) mDialog.findViewById(R.id.end_city);
        final Spinner spinner_days = (Spinner) mDialog.findViewById(R.id.spinner_days);
        String[] mItems = getResources().getStringArray(R.array.spinedays);
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        spinner_days.setAdapter(adapter);
        mDialog.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskMap.get(spinner_days.getSelectedItem()) != null) {
                    List<TaskPlans> taskPlanses = taskMap.get(spinner_days.getSelectedItem().toString().trim());
                    TaskPlans taskPlans = new TaskPlans();
                    taskPlans.setStartCity(startCity.getText().toString());
                    taskPlans.setEndCity(endCity.getText().toString());
                    taskPlanses.add(taskPlans);
                } else {
                    List<TaskPlans> taskPlanses = new ArrayList<TaskPlans>();
                    TaskPlans taskPlans = new TaskPlans();
                    taskPlans.setStartCity(startCity.getText().toString());
                    taskPlans.setEndCity(endCity.getText().toString());
                    taskPlanses.add(taskPlans);
                    taskMap.put(spinner_days.getSelectedItem().toString().trim(), taskPlanses);
                }

                Set<Map.Entry<String, List<TaskPlans>>> entries = taskMap.entrySet();
                viewDataAdapter.getDataList().clear();
                for (Map.Entry<String, List<TaskPlans>> entry : entries) {
                    ListTaskPlans listTaskPlans = new ListTaskPlans();
                    String key = entry.getKey();
                    List<TaskPlans> value = entry.getValue();
                    listTaskPlans.setDays(key);
                    listTaskPlans.setTaskPlansList(value);
                    viewDataAdapter.getDataList().add(listTaskPlans);
                }
                viewDataAdapter.notifyDataSetChanged();
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    @OnClick(R.id.tv_enquiry)
    public void enquiry() {

        if (TextUtils.isEmpty(edit_car_number.getText().toString())) {
            CommonUtils.make(this, "请填写出发人数");
            return;
        }

        if (TextUtils.isEmpty(go_time.getText().toString())) {
            CommonUtils.make(this, "请输入出发日期");
            return;
        }

        StringBuilder tripSb = new StringBuilder();
        Set<Map.Entry<String, List<TaskPlans>>> entries = taskMap.entrySet();
        for (Map.Entry<String, List<TaskPlans>> entry : entries) {
            String key = entry.getKey();
            List<TaskPlans> value = entry.getValue();
            for (TaskPlans taskPlans : value) {
                tripSb.append(getDays(key) + ",").append(taskPlans.startCity).append("-").append(taskPlans.endCity).append("|");
            }
        }
        int lastIndex = tripSb.lastIndexOf("|");
        String trip = null;
        try {
            trip = URLEncoder.encode(tripSb.substring(0, lastIndex),"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Call<BaseInfoVo> userVoCall = getApis().CreateLeasedVehicleOrder(
                AppPreferences.getString("userId"),
                date_time,
                edit_car_number.getText().toString(),
                trip).clone();

        userVoCall.enqueue(new Callback<BaseInfoVo>() {
            @Override
            public void onResponse(Response<BaseInfoVo> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    CommonUtils.make(CharteredBusActivity.this, "发布包车成功");
                } else {
                    CommonUtils.make(CharteredBusActivity.this, "发布包车失败");
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public String getDays(String type) {
        if ("第1天".equals(type)) {
            return "1";
        } else if ("第2天".equals(type)) {
            return "2";
        } else if ("第3天".equals(type)) {
            return "3";
        } else if ("第4天".equals(type)) {
            return "4";
        } else {
            return "5";
        }
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

        this.viewDataAdapter = new ListViewDataAdapter<ListTaskPlans>(new ViewHolderCreator<ListTaskPlans>() {

            @Override
            public ViewHolderBase createViewHolder(int position) {
                return new ViewHolderBase<ListTaskPlans>() {
                    TextView tv_day_number;
                    ListView lv_scheduling_items;
                    ListViewDataAdapter itemListViewData;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = getLayoutInflater().inflate(R.layout.chartered_bus_scheduling, null);
                        tv_day_number = ButterKnife.findById(view, R.id.tv_day_number);
                        lv_scheduling_items = ButterKnife.findById(view, R.id.lv_scheduling_items);
                        return view;
                    }

                    @Override
                    public void showData(final int ps, final ListTaskPlans taskPlans) {
                        tv_day_number.setText(taskPlans.getDays());
                        itemListViewData = new ListViewDataAdapter<TaskPlans>(new ViewHolderCreator<TaskPlans>() {
                            @Override
                            public ViewHolderBase<TaskPlans> createViewHolder(int position) {
                                return new ViewHolderBase<TaskPlans>() {
                                    TextView start_city;
                                    TextView end_city;
                                    ImageView iv_minus;

                                    @Override
                                    public View createView(LayoutInflater layoutInflater) {
                                        View view = getLayoutInflater().inflate(R.layout.chartered_bus_item, null);
                                        start_city = ButterKnife.findById(view, R.id.start_city);
                                        end_city = ButterKnife.findById(view, R.id.end_city);
                                        iv_minus = ButterKnife.findById(view, R.id.iv_minus);
                                        return view;
                                    }

                                    @Override
                                    public void showData(final int position, TaskPlans itemData) {
                                        start_city.setText(itemData.getStartCity());
                                        end_city.setText(itemData.getEndCity());
                                        iv_minus.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                taskMap.get(taskPlans.getDays()).remove(position);
                                                itemListViewData.getDataList().remove(position);
                                                if (itemListViewData.getDataList().isEmpty()) {
                                                    viewDataAdapter.getDataList().remove(ps);
                                                    taskMap.remove(taskPlans.getDays());
                                                    itemListViewData.notifyDataSetChanged();
                                                    viewDataAdapter.notifyDataSetChanged();
                                                } else {
                                                    itemListViewData.notifyDataSetChanged();
                                                }
                                            }
                                        });
                                    }
                                };
                            }
                        });
                        itemListViewData.getDataList().addAll(taskPlans.getTaskPlansList());
                        lv_scheduling_items.setAdapter(itemListViewData);
                    }
                };
            }
        });
        lv_scheduling.setAdapter(this.viewDataAdapter);
    }

    class ListTaskPlans {

        private String days;

        private List<TaskPlans> taskPlansList;

        public void setTaskPlansList(List<TaskPlans> taskPlansList) {
            this.taskPlansList = taskPlansList;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public List<TaskPlans> getTaskPlansList() {
            return taskPlansList;
        }
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
