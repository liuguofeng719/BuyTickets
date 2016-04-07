package com.ticket.ui.adpater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.PlaceVo;
import com.ticket.widgets.PinnedHeaderListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CityPlaceListAdapter extends BaseAdapter implements PinnedHeaderListView.PinnedHeaderAdapter, SectionIndexer, OnScrollListener {
    private List<PlaceVo> mList;
    private int mLocationPosition = -1;

    private List<Integer> mPositions;
    private List<String> mSections;

    public CityPlaceListAdapter(List<PlaceVo> mList) {
        this.mList = mList;
        initDateHead();
    }

    /* 获取头部head标签数据 */
    private void initDateHead() {
        mSections = new ArrayList<String>();
        mPositions = new ArrayList<Integer>();
        for (int i = 0; i < mList.size(); i++) {
            if (i == 0) {
                mSections.add(String.valueOf(mList.get(i).getFirstLatter().toUpperCase()));
                mPositions.add(i);
                continue;
            }
            if (i != mList.size()) {
                if (!mList.get(i).getFirstLatter().toUpperCase().equals(mList.get(i - 1).getFirstLatter().toUpperCase())) {
                    mSections.add(String.valueOf(mList.get(i).getFirstLatter().toUpperCase()));
                    mPositions.add(i);
                }
            }
        }
    }

    public void updateView(List<PlaceVo> cityVoList){
        this.mList = cityVoList;
        this.initDateHead();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_list_item, null);
            ButterKnife.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        PlaceVo city = mList.get(position);
        int section = getSectionForPosition(position);
        if (getPositionForSection(section) == position) {
            holder.keySort.setVisibility(View.VISIBLE);
            holder.keySort.setText(city.getFirstLatter().toUpperCase());
        } else {
            holder.keySort.setVisibility(View.GONE);
        }
        holder.cityName.setText(city.getPlaceName());
        holder.cityName.setTag(city.getPlaceId());
        return convertView;
    }

    public class Holder {
        @InjectView(R.id.city_list_item_sort)
        public TextView keySort;
        @InjectView(R.id.city_list_item_name)
        public TextView cityName;
    }

    @Override
    public int getPinnedHeaderState(int position) {
        int realPosition = position;
        if (realPosition < 0
                || (mLocationPosition != -1 && mLocationPosition == realPosition)) {
            return PINNED_HEADER_GONE;
        }
        mLocationPosition = -1;
        int section = getSectionForPosition(realPosition);
        int nextSectionPosition = getPositionForSection(section + 1);
        if (nextSectionPosition != -1
                && realPosition == nextSectionPosition - 1) {
            return PINNED_HEADER_PUSHED_UP;
        }
        return PINNED_HEADER_VISIBLE;
    }

    @Override
    public Object[] getSections() {
        return mSections.toArray();
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if (sectionIndex < 0 || sectionIndex >= mPositions.size()) {
            return -1;
        }
        return mPositions.get(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {
        if (position < 0 || position >= getCount()) {
            return -1;
        }
        int index = Arrays.binarySearch(mPositions.toArray(), position);
        return index >= 0 ? index : -index - 2;
    }

    @Override
    public void configurePinnedHeader(View header, int position, int alpha) {
        int realPosition = position;
        int section = getSectionForPosition(realPosition);
        if (section != -1) {
            String title = (String) getSections()[section];
            ((TextView) header.findViewById(R.id.city_header_item_sort)).setText(title);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (view instanceof PinnedHeaderListView) {
            ((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
        }
    }
}
