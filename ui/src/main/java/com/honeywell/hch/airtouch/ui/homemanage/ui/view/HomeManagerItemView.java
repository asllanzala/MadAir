package com.honeywell.hch.airtouch.ui.homemanage.ui.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.ViewHolderUtil;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeAndCity;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.honeywell.hch.airtouch.ui.homemanage.ui.adapter.HomeManageAdapter;

/**
 * Created by Vincent on 14/7/16.
 */
public class HomeManagerItemView extends RelativeLayout {
    private final String TAG = "AllDeviceItemView";
    private Context mContext;
    private final static int DEFAULT_INT_VALUE = -1;
    private HomeManageAdapter.DeleteHomeCallback mDeleteHomeCallback;
    private HomeManageAdapter.RenameHomeCallback mRenameHomeCallback;
    private HomeManageAdapter.SetDefaultHomeCallback mSetDefaultHomeCallback;

    public HomeManagerItemView(Context context) {
        super(context);
        mContext = context;
        initView();

    }

    public HomeManagerItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.home_manager_item, this);
    }

    public void initViewHolder(View view, final HomeAndCity homeAndCity, final Context mContext) {
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "initViewHolder---- ");
        final ImageView homeStatusIv = ViewHolderUtil.get(view, R.id.honme_manager_icon_iv);

        TextView homeNameTv = ViewHolderUtil.get(view, R.id.home_manager_home_tv);
        TextView homeLoactionTv = ViewHolderUtil.get(view, R.id.home_manager_location_tv);

        ImageView homeExpandIv = ViewHolderUtil.get(view, R.id.home_manager_expand_iv);

        LinearLayout expandLl = ViewHolderUtil.get(view, R.id.home_manager_expand_ll);

        LinearLayout defaultLl = ViewHolderUtil.get(view, R.id.default_ll);
        LinearLayout renameLl = ViewHolderUtil.get(view, R.id.rename_ll);
        LinearLayout deleteLl = ViewHolderUtil.get(view, R.id.delete_ll);

        homeExpandIv.setImageResource(R.drawable.home_manager_expand_icon);
        homeNameTv.setText(homeAndCity.getHomeName());
        homeLoactionTv.setText(homeAndCity.getHomeCity());
        if (homeAndCity.isExpand()) {
            expandLl.setVisibility(VISIBLE);
            homeExpandIv.setImageResource(R.drawable.home_manager_expand_pull_up);
        } else {
            expandLl.setVisibility(GONE);
            homeExpandIv.setImageResource(R.drawable.home_manager_expand_icon);
        }
        if (homeAndCity.isOwnerHome()) {
            homeStatusIv.setVisibility(INVISIBLE);
            defaultLl.setVisibility(VISIBLE);
            renameLl.setVisibility(VISIBLE);
            deleteLl.setVisibility(VISIBLE);
        } else {
            homeStatusIv.setVisibility(VISIBLE);
            defaultLl.setVisibility(VISIBLE);
            renameLl.setVisibility(GONE);
            deleteLl.setVisibility(GONE);
            homeStatusIv.setImageResource(R.drawable.home_manager_shared_to_me);
        }

        final int locationId = getDefaultHomeId();
        //默认第一次
        if (locationId == DEFAULT_INT_VALUE) {
            processSetDefaultHome(homeAndCity.getLocationId(), mContext);
            homeStatusIv.setVisibility(VISIBLE);
            homeStatusIv.setImageResource(R.drawable.defual_home_indicator);
        }

        if (homeAndCity.getLocationId() == locationId) {
            homeStatusIv.setVisibility(VISIBLE);
            homeStatusIv.setImageResource(R.drawable.defual_home_indicator);
        }

        defaultLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "defaultLl: ");
                homeAndCity.setIsDefaultHome(true);
                homeStatusIv.setVisibility(VISIBLE);
                homeStatusIv.setImageResource(R.drawable.defual_home_indicator);
                if (mSetDefaultHomeCallback != null) {
                    mSetDefaultHomeCallback.callback(locationId);
                }
                processSetDefaultHome(homeAndCity.getLocationId(), mContext);

            }
        });

        renameLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "renameLl: ");
                mRenameHomeCallback.callback(homeAndCity);
            }
        });

        deleteLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "deleteLl: ");
                mDeleteHomeCallback.callback(homeAndCity);
            }
        });

    }

    private void processSetDefaultHome(int locationId, Context mContext) {
        UserInfoSharePreference.saveDefaultHomeId(locationId);
        Intent intent = new Intent(HPlusConstants.SET_DEFALUT_HOME);
        mContext.sendBroadcast(intent);
    }

    private int getDefaultHomeId() {
        return UserInfoSharePreference.getDefaultHomeId();
    }

    public void setmDeleteHomeCallback(HomeManageAdapter.DeleteHomeCallback mDeleteHomeCallback) {
        this.mDeleteHomeCallback = mDeleteHomeCallback;
    }

    public void setmRenameHomeCallback(HomeManageAdapter.RenameHomeCallback mRenameHomeCallback) {
        this.mRenameHomeCallback = mRenameHomeCallback;
    }

    public void setmSetDefaultHomeCallback(HomeManageAdapter.SetDefaultHomeCallback mSetDefaultHomeCallback) {
        this.mSetDefaultHomeCallback = mSetDefaultHomeCallback;
    }

}
