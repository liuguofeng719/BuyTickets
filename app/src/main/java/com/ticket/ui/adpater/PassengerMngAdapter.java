package com.ticket.ui.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.PassengerVo;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PassengerMngAdapter extends BaseAdapter {

    private List<PassengerVo> voList;

    public interface DelItemClickListener {
        public void DelItemClickListener(View v);
    }

    private DelItemClickListener delItemClickListener;

    public DelItemClickListener getDelItemClickListener() {
        return delItemClickListener;
    }

    public void setDelItemClickListener(DelItemClickListener delItemClickListener) {
        this.delItemClickListener = delItemClickListener;
    }

    public PassengerMngAdapter(List<PassengerVo> voList) {
        this.voList = voList;
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
        HolderDel holder;
        if (convertView == null) {
            holder = new HolderDel();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_passenger_item_del, null);
            ButterKnife.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (HolderDel) convertView.getTag();
        }
        PassengerVo passengerVo = (PassengerVo) getItem(position);
        holder.tv_pass_name.setText(passengerVo.getFullName());
        holder.tv_id_card.setText("身份证 " + passengerVo.getIdCard());
        holder.tv_del.setTag(passengerVo.getPassengerId());
        holder.tv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delItemClickListener.DelItemClickListener(v);
            }
        });
        return convertView;
    }

    public class HolderDel {
        @InjectView(R.id.tv_pass_name)
        TextView tv_pass_name;
        @InjectView(R.id.tv_id_card)
        TextView tv_id_card;
        @InjectView(R.id.tv_del)
        TextView tv_del;
    }
}
