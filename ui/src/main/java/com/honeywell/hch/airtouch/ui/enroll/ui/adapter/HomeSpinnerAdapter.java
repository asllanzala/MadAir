package com.honeywell.hch.airtouch.ui.enroll.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.manager.model.DropTextModel;

import java.util.List;


/**
 * SpinnerArrayAdapter for home selection
 */
public class HomeSpinnerAdapter<T> extends ArrayAdapter<T> {
    private Context mContext;
    private DropTextModel[] mHomeStrings;
    private DropTextModel mSelectedHome;

    public HomeSpinnerAdapter(Context context, List<T> objects) {
        super(context, 0, objects);
        mContext = context;
    }

    public HomeSpinnerAdapter(Context context, T[] objects) {
        super(context, 0, objects);
        mContext = context;
        mHomeStrings = (DropTextModel[]) objects;
    }

    public DropTextModel getSelectedHome() {
        return mSelectedHome;
    }

    public DropTextModel getItemValue(int position) {
        return (DropTextModel) getItem(position);
    }

    @Override
    public int getCount() {
        if (mHomeStrings == null) {
            return 0;
        }
        return mHomeStrings.length;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout
                    .list_item_home_spinner, parent, false);
        }

        if (position == 0) {
            if (position == (getCount() - 1)) {
                //only one item
                view.setBackgroundResource(R.drawable.list_four_corner_round);
            } else {
                //the first item
                view.setBackgroundResource(R.drawable.list_top_corner_round);
            }
        } else if (position == (getCount() - 1))
            //last item
            view.setBackgroundResource(R.drawable.list_bottom_corner_round);
        else {
            //middle item
            view.setBackgroundResource(R.drawable.list_no_corner_round);
        }

        TextView tv = (TextView) view.findViewById(R.id.list_item_home_title);
        tv.setWidth(parent.getWidth());
        mSelectedHome = getItemValue(position);
        tv.setText(mSelectedHome.getTextViewString());

        // 有图片资源
        if (mSelectedHome.getLeftImageId() != 0) {
            ImageView imageView = (ImageView) view.findViewById(R.id.left_image_id);
            imageView.setVisibility(View.VISIBLE);
            imageView.setBackgroundResource(mSelectedHome.getLeftImageId());
        }

        if (!StringUtil.isEmpty(mSelectedHome.getRightTextViewString())){
            TextView rightTv = (TextView)view.findViewById(R.id.right_textview_id);
            rightTv.setVisibility(View.VISIBLE);
            rightTv.setText(mSelectedHome.getRightTextViewString());
        }

        return view;
    }


}

