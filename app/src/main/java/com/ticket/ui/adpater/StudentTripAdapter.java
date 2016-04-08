package com.ticket.ui.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.TravelRoutingListVo;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by liuguofeng719 on 2016/4/3.
 */
public class StudentTripAdapter extends BaseAdapter {

    public interface OnShareJoinListener {
        public void onShareListener(View view);
        public void onJoinListener(View view);
    }
    public OnShareJoinListener onShareJoinListener;

    public void setOnShareJoinListener(OnShareJoinListener onShareJoinListener) {
        this.onShareJoinListener = onShareJoinListener;
    }

    List<TravelRoutingListVo> listVos = new ArrayList<>();

    public List<TravelRoutingListVo> getDataList() {
        return listVos;
    }

    @Override
    public int getCount() {
        return listVos != null ? listVos.size() : 0;
    }

    @Override
    public TravelRoutingListVo getItem(int position) {
        return listVos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        TravelRoutingListVo routingListVo = getItem(position);
        if (viewType == 1) {
            PlatformHolder platformHolder = null;
            if (convertView == null) {
                platformHolder = new PlatformHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stu_platform_list_item, null);
                ButterKnife.inject(platformHolder, convertView);
                convertView.setTag(platformHolder);
            } else {
                platformHolder = ((PlatformHolder) convertView.getTag());
            }
            platformHolder.tv_goDateTime.setText(routingListVo.getGoDate());
            platformHolder.tv_type_text.setText(routingListVo.getPublishedType());
            platformHolder.tv_person_count.setText("已定" + routingListVo.getReachSeatAmount() + "人");
            platformHolder.tv_status.setText(getStatus(routingListVo.getState()));
            platformHolder.tv_startStation.setText(routingListVo.getStartPlaceName());
            platformHolder.tv_endStation.setText(routingListVo.getStopPlaceName());
            platformHolder.tv_student_ticket.setText("学生票：" + routingListVo.getStudentPrice() + "元");
            platformHolder.tv_adult_ticket.setText("成人票：" + routingListVo.getNormalPrice() + "元");
            platformHolder.tv_seat_amount.setText(routingListVo.getCarName());
            platformHolder.tv_reachSeat_amount.setText("(达成出行满" + routingListVo.getReachSeatAmount() + "人)");
            platformHolder.btn_text_share.setTag(routingListVo);
            platformHolder.btn_text_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShareJoinListener.onShareListener(v);
                }
            });
            platformHolder.btn_text_join.setTag(routingListVo);
            platformHolder.btn_text_join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShareJoinListener.onJoinListener(v);
                }
            });
        } else {
            CrowFundingHolder crowFundingHolder = null;
            if (convertView == null) {
                crowFundingHolder = new CrowFundingHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stu_crowfunding_list_item, null);
                ButterKnife.inject(crowFundingHolder, convertView);
                convertView.setTag(crowFundingHolder);
            } else {
                crowFundingHolder = ((CrowFundingHolder) convertView.getTag());
            }
            crowFundingHolder.tv_goDateTime.setText(routingListVo.getGoDate());
            crowFundingHolder.tv_type_text.setText(routingListVo.getPublishedType());
            crowFundingHolder.tv_status.setText(getStatus(routingListVo.getState()));
            crowFundingHolder.tv_startStation.setText(routingListVo.getStartPlaceName());
            crowFundingHolder.tv_endStation.setText(routingListVo.getStopPlaceName());
            crowFundingHolder.tv_creator.setText(routingListVo.getCreateUser());
            crowFundingHolder.btn_text_share.setTag(routingListVo);
            crowFundingHolder.btn_text_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShareJoinListener.onShareListener(v);
                }
            });
            crowFundingHolder.btn_text_join.setTag(routingListVo);
            crowFundingHolder.btn_text_join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShareJoinListener.onJoinListener(v);
                }
            });
        }
        return convertView;
    }

    public String getStatus(String type) {
        if ("0".equals(type)) {
            return "未询价";
        } else if ("1".equals(type)) {
            return "已询价等待达成出行";
        } else if ("2".equals(type)) {
            return "达成出行";
        }
        return "";
    }

    @Override
    public int getItemViewType(int position) {
        TravelRoutingListVo routingListVo = listVos.get(position);
        if ("平台发布".equals(routingListVo.getPublishedType())) {
            return 1;
        } else if ("众筹发布".equals(routingListVo.getPublishedType())) {
            return 1;
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    static class PlatformHolder {
        @InjectView(R.id.tv_goDateTime)
        TextView tv_goDateTime;
        @InjectView(R.id.tv_type_text)
        TextView tv_type_text;
        @InjectView(R.id.tv_startStation)
        TextView tv_startStation;
        @InjectView(R.id.tv_endStation)
        TextView tv_endStation;
        @InjectView(R.id.tv_person_count)
        TextView tv_person_count;
        @InjectView(R.id.tv_status)
        TextView tv_status;
        @InjectView(R.id.tv_student_ticket)
        TextView tv_student_ticket;
        @InjectView(R.id.tv_adult_ticket)
        TextView tv_adult_ticket;
        @InjectView(R.id.tv_seat_amount)
        TextView tv_seat_amount;
        @InjectView(R.id.tv_reachSeat_amount)
        TextView tv_reachSeat_amount;
        @InjectView(R.id.btn_text_share)
        TextView btn_text_share;
        @InjectView(R.id.btn_text_join)
        TextView btn_text_join;
    }

    static class CrowFundingHolder {
        @InjectView(R.id.tv_goDateTime)
        TextView tv_goDateTime;
        @InjectView(R.id.tv_type_text)
        TextView tv_type_text;
        @InjectView(R.id.tv_startStation)
        TextView tv_startStation;
        @InjectView(R.id.tv_endStation)
        TextView tv_endStation;
        @InjectView(R.id.tv_status)
        TextView tv_status;
        @InjectView(R.id.tv_creator)
        TextView tv_creator;
        @InjectView(R.id.btn_text_share)
        TextView btn_text_share;
        @InjectView(R.id.btn_text_join)
        TextView btn_text_join;
    }
}
