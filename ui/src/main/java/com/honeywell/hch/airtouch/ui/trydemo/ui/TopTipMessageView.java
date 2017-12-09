package com.honeywell.hch.airtouch.ui.trydemo.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.honeywell.hch.airtouch.library.util.StatusBarUtil;
import com.honeywell.hch.airtouch.ui.R;

/**
 * Created by honeywell on 27/12/2016.
 */

public class TopTipMessageView extends RelativeLayout {

    private Context mContext;
    public TopTipMessageView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView(){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.try_demo_top_msg, this);
        RelativeLayout relativeLayout = (RelativeLayout)view.findViewById(R.id.trydemo_top_id);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, StatusBarUtil.getStatusBarHeight(mContext));
        relativeLayout.setLayoutParams(lp);
    }
}
