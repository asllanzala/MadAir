package com.honeywell.hch.airtouch.ui.homemanage.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.manager.model.DropTextModel;

import java.util.List;

/**
 * Created by Vincent on 19/7/16.
 */
public class AddHomeAdapter<T> extends ArrayAdapter<T> {
    private Context mContext;
    private DropTextModel[] mHomeStrings;
    private DropTextModel mSelectedHome;

    public AddHomeAdapter(Context context, List<T> objects) {
        super(context, 0, objects);
        mContext = context;
    }

    public AddHomeAdapter(Context context, T[] objects) {
        super(context, 0, objects);
        mContext = context;
        mHomeStrings = (DropTextModel[]) objects;
    }

    public void clearHomeData() {
        mHomeStrings = null;
        notifyDataSetChanged();
    }

    public String getItemValue(int position) {
        return ((DropTextModel) getItem(position)).getTextViewString();
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
                    .list_item_add_home_spinner, parent, false);
        }

        TextView tv = (TextView) view.findViewById(R.id.list_item_home_title);
        tv.setWidth(parent.getWidth());
        tv.setText(getItemValue(position));

        return view;
    }


}
