package com.ticket.ui.adpater;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.PassengerVo;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PassengerAdapter extends BaseAdapter {

    private List<PassengerVo> voList;
    public static SparseBooleanArray checkItems;

    public PassengerAdapter(List<PassengerVo> voList) {
        this.voList = voList;
        checkItems = new SparseBooleanArray();
        init();
    }

    private void init() {
        for (int i = 0; i < voList.size(); i++) {
            checkItems.put(i, false);
        }
    }

    public static void setCheckItemStatus(Integer index, Boolean flag) {
        checkItems.put(index, flag);
    }

    public static void setCheckItemsStatus(Boolean flag) {
        for (int i = 0; i < checkItems.size(); i++) {
            checkItems.put(i, flag);
        }
    }

    @Override
    public int getCount() {
        return voList.size();
    }

    @Override
    public Object getItem(int position) {
        return voList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_passenger_item, null);
            ButterKnife.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        PassengerVo passengerVo = (PassengerVo) getItem(position);
        holder.tv_pass_name.setText(passengerVo.getFullName());
        holder.tv_phone.setText(passengerVo.getMobileNumber());
        holder.tv_id_card.setText("身份证 " + passengerVo.getIdCard());
        holder.cbo_selected.setChecked(checkItems.get(position));
        return convertView;
    }

    public class Holder {
        @InjectView(R.id.tv_pass_name)
        TextView tv_pass_name;
        @InjectView(R.id.tv_id_card)
        TextView tv_id_card;
        @InjectView(R.id.tv_phone)
        TextView tv_phone;
        @InjectView(R.id.cbo_selected)
        CheckBox cbo_selected;
    }
}
