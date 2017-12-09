package com.honeywell.hch.airtouch.ui.emotion.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.honeywell.hch.airtouch.plateform.http.model.user.response.EmotionBottleResponse;
import com.honeywell.hch.airtouch.ui.emotion.ui.view.EmotionItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 15/8/16.
 */
public class EmotionItemAdapter extends BaseAdapter {
    private Context mContext;
    private List<EmotionBottleResponse> mEmotionBottleResponseList;

    public EmotionItemAdapter(Context context, List<EmotionBottleResponse> emotionBottleResponseList) {
        mContext = context;
        mEmotionBottleResponseList = emotionBottleResponseList;
    }

    public EmotionItemAdapter(Context context) {
        mContext = context;
        setEmotionBottleResponse(null);
    }


    @Override
    public int getCount() {
        return mEmotionBottleResponseList.size();
    }

    @Override
    public EmotionBottleResponse getItem(int position) {
        return mEmotionBottleResponseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void setEmotionBottleResponse(List<EmotionBottleResponse> emotionBottleResponseList) {
        if (emotionBottleResponseList != null) {
            this.mEmotionBottleResponseList = emotionBottleResponseList;
        } else {
            this.mEmotionBottleResponseList = new ArrayList<>();
        }
    }

    public void changeData(List<EmotionBottleResponse> emotionBottleResponseList) {
        this.setEmotionBottleResponse(emotionBottleResponseList);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            EmotionItemView emotionItemView = new EmotionItemView(mContext);
            convertView = emotionItemView;
        }
        ((EmotionItemView) convertView).initViewHolder(convertView, mEmotionBottleResponseList.get(position),position);
        //hide the split line
        if (getCount() == position + 1) {
            ((EmotionItemView) convertView).hideSplitLine(convertView, View.GONE);
        } else {
            ((EmotionItemView) convertView).hideSplitLine(convertView, View.VISIBLE);
        }
        return convertView;
    }
}
