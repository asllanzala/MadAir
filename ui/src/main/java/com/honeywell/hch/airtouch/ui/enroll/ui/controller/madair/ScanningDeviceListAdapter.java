package com.honeywell.hch.airtouch.ui.enroll.ui.controller.madair;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.ViewHolderUtil;
import com.honeywell.hch.airtouch.ui.R;

import java.util.HashMap;
import java.util.List;


public class ScanningDeviceListAdapter extends BaseAdapter {

    private Context mContext = null;
    private List<String> mModelNameList;
    private HashMap<Integer, ImageView> mSelectViewMap = new HashMap<>();

    public ScanningDeviceListAdapter(Context ctx, List<String> modelNames) {
        mContext = ctx;
        mModelNameList = modelNames;
    }

    @Override
    public int getCount() {
        return mModelNameList == null ? 0 : mModelNameList.size();
    }

    @Override
    public String getItem(int position) {
        return mModelNameList == null ? null : mModelNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clearAllSelectImageView() {
        for (Integer position : mSelectViewMap.keySet()) {
            mSelectViewMap.get(position).setVisibility(View.INVISIBLE);
        }
    }

    public void setSelectImageViewVisible(int position) {
        ImageView selectView = mSelectViewMap.get(position);
        if (selectView != null)
            selectView.setVisibility(View.VISIBLE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.list_item_mad_air_manual_select, null);
        }
        TextView mModelNameTextView = ViewHolderUtil.get(convertView, R.id.list_item_model_name_tv);
        ImageView mSelectImageView = ViewHolderUtil.get(convertView,R.id.list_item_select_image);
        mModelNameTextView.setText(mModelNameList.get(position));
        mSelectViewMap.put(position, mSelectImageView);
        return convertView;
    }
}
