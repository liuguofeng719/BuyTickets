package com.ticket.ui.adpater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ticket.bean.TravelRoutingListVo;

import java.util.List;

/**
 * Created by liuguofeng719 on 2016/4/3.
 */
public class StudentTripAdapter extends BaseAdapter {

    List<TravelRoutingListVo> listVos;

    public static final int platform_type_ = 1;//平台发布
    public static final int crow_funding_type = 2;//众筹发布

    public StudentTripAdapter(List<TravelRoutingListVo> listVos) {
        this.listVos = listVos;
    }

    @Override
    public int getCount() {
        return listVos != null ? listVos.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return listVos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    static class PlatformHolder {

    }

    static class CrowFundingHolder {

    }
}
