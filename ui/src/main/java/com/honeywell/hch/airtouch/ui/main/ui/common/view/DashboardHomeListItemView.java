package com.honeywell.hch.airtouch.ui.main.ui.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeAndCity;
import com.honeywell.hch.airtouch.ui.R;

/**
 * Created by h127856 on 7/22/16.
 */
public class DashboardHomeListItemView extends RelativeLayout {

    private ImageView mImageIconView;
    private TextView mTextView;
    private Context mContext;
    private ImageView mErrorFlagImageView;

    public DashboardHomeListItemView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public DashboardHomeListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public DashboardHomeListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.dashboard_homelist_item_view, this);

        mImageIconView = (ImageView) findViewById(R.id.home_icon_id);
        mTextView = (TextView) findViewById(R.id.home_name_txt_id);
        mErrorFlagImageView = (ImageView) findViewById(R.id.error_flag);
    }

    public void setViewValue(HomeAndCity homeAndCity) {
        mTextView.setText(homeAndCity.getHomeName());
        if (homeAndCity.isDefaultHome()) {
            mImageIconView.setVisibility(View.VISIBLE);
            mImageIconView.setImageResource(R.drawable.homelist_default_icon);
        } else if (!homeAndCity.isDefaultHome() && !homeAndCity.isOwnerHome()) {
            mImageIconView.setVisibility(View.VISIBLE);
            mImageIconView.setImageResource(R.drawable.shared_to_me);
        } else if (!homeAndCity.isRealHome()) {
            mImageIconView.setVisibility(View.VISIBLE);
            mImageIconView.setImageResource(R.drawable.homelist_madair_ic);
        } else {
            mImageIconView.setImageResource(R.drawable.normal_home_ic);
            mImageIconView.setVisibility(View.VISIBLE);
        }

        int visibleStatus = homeAndCity.isHasUnnormalDevice() ? View.VISIBLE : View.GONE;
        mErrorFlagImageView.setVisibility(visibleStatus);
    }

}
