package com.honeywell.hch.airtouch.ui.control.ui.device.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.devices.common.Filter;
import com.honeywell.hch.airtouch.ui.control.ui.device.view.FilterItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 26/7/16.
 */
public class DeviceFilterAdapter extends BaseAdapter {
    private final String TAG = "DeviceFilterAdapter";
    private static final int TYPE_CATEGORY_ITEM = 0;
    private static final int TYPE_ITEM = 1;

    private List<Filter> mFilterListData;
    private LayoutInflater mInflater;
    private Context mContext;
    private final int EMPTY = 0;
    private BuyFilterCallBack mBuyFilterCallBack;


    public DeviceFilterAdapter(Context context, List<Filter> mData) {
        mContext = context;
        mFilterListData = mData;
        mInflater = LayoutInflater.from(context);
    }


    public void changeData(List<Filter> data) {
        this.setCategoryList(data);
        this.notifyDataSetChanged();
    }

    private void setCategoryList(List<Filter> data) {
        if (mFilterListData != null) {
            this.mFilterListData = data;
        } else {
            this.mFilterListData = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "getCount: " + mFilterListData.size());
        return mFilterListData.size();
    }

    @Override
    public Filter getItem(int position) {

        return mFilterListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "position222: " + position);
        if (null == convertView) {
            FilterItemView itemView = new FilterItemView(mContext);
            itemView.setmBuyFilterCallBack(mBuyFilterCallBack);
            convertView = itemView;
        }


        Filter filter = getItem(position);
        if (filter != null) {
            ((FilterItemView) convertView).initViewHolder(convertView, filter, position);
        }
//
// if (DeviceType.isAirTouchSeries(filter.getmHomeDevice().getDeviceType())) {
//
//        }
        return convertView;
    }

    public interface BuyFilterCallBack {
        void callback(Filter filter);
    }

    public void setmBuyFilterCallBack(BuyFilterCallBack mBuyFilterCallBack) {
        this.mBuyFilterCallBack = mBuyFilterCallBack;
    }
}
