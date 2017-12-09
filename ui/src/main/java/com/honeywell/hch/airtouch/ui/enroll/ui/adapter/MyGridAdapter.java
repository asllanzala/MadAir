package com.honeywell.hch.airtouch.ui.enroll.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollChoiceModel;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.ViewHolderUtil;

import java.util.List;

/**
 * @author http://blog.csdn.net/finddreams
 * @Description:gridview的Adapter
 */
public class MyGridAdapter extends BaseAdapter {
    private final String TAG = "MyGridAdapter";
    private Context mContext;
    private List<EnrollChoiceModel> mChoiceList;

    public MyGridAdapter(Context mContext, List<EnrollChoiceModel> choiceList) {
        super();
        this.mContext = mContext;
        mChoiceList = choiceList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mChoiceList.size();
    }

    @Override
    public EnrollChoiceModel getItem(int position) {
        // TODO Auto-generated method stub
        return mChoiceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.activity_enroll_grid_item, null);
        }
        RelativeLayout relativeLayout = ViewHolderUtil.get(convertView, R.id.choice_rl);
        ImageView deviceImage = ViewHolderUtil.get(convertView, R.id.iv_item);
        TextView deviceText = ViewHolderUtil.get(convertView, R.id.tv_item);
        deviceImage.setImageResource(mChoiceList.get(position).getDeviceImage());
        deviceText.setText(mContext.getString(mChoiceList.get(position).getDeviceName()));
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "position: " + position);
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "image: " + mChoiceList.get(position).getDeviceImage());
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "name: " + mChoiceList.get(position).getDeviceName());
        relativeLayout.setAlpha(mChoiceList.get(position).getAlpha());
//        if (position % 2 == 0) {
//            relativeLayout.setAlpha(1.0f);
//        } else {
//            relativeLayout.setAlpha(0.5f);
//        }

        return convertView;
    }

}
