package com.honeywell.hch.airtouch.ui.control.ui.device.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.ViewHolderUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.devices.common.Filter;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.control.ui.device.adapter.DeviceFilterAdapter;

/**
 * Created by Vincent on 26/7/16.
 */
public class FilterItemView extends RelativeLayout {
    private final String TAG = "FilterItemView";
    private Context mContext;
    private final int FILTER_INITIAL = -1;
    private final int AIRTOUCH_FILTER_FLASH = 20;
    private final int AQUATOUCH_FILTER_FLASH = 15;
    public final static int MAD_AIR_HIDE_FILTER = -2;
    private DeviceFilterAdapter.BuyFilterCallBack mBuyFilterCallBack;

    public FilterItemView(Context context) {
        super(context);
        mContext = context;
        initView();

    }

    public FilterItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.filter_listview_item, this);
    }

    public void initViewHolder(View view, final Filter filter, int position) {
        ImageView filterIc = ViewHolderUtil.get(view, R.id.filter_icon);
        TextView filterPosition = ViewHolderUtil.get(view, R.id.filter_position);
        TextView filterNameTv = ViewHolderUtil.get(view, R.id.filter_name);
        TextView filterRemainTv = ViewHolderUtil.get(view, R.id.filter_remain_value_tv);
        ImageView shoppingCartIc = ViewHolderUtil.get(view, R.id.filter_shopping_cart);
        RelativeLayout filterValueRl = ViewHolderUtil.get(view, R.id.filter_value_rl);
        RelativeLayout filterRootRl = ViewHolderUtil.get(view, R.id.filter_item_root);
        filterRootRl.setBackground(getResources().getDrawable(R.drawable.all_device_item_back));

        shoppingCartIc.setImageResource(R.drawable.ic_shopping);
        filterValueRl.setVisibility(VISIBLE);
        Animation alphaOffAnimation = AnimationUtils.loadAnimation(mContext, R.anim.control_alpha);
        filterRemainTv.setTextColor(getResources().getColor(R.color.text_common));
        filterRemainTv.clearAnimation();
        filterNameTv.setTextColor(getResources().getColor(R.color.text_common));
        filterPosition.setTextColor(getResources().getColor(R.color.text_common));
        filterPosition.setText(String.valueOf(position + 1));
        filterNameTv.setText(filter.getName());
        filterIc.setImageResource(filter.getmFilterImage());
        int runTimeLife = FILTER_INITIAL;
        boolean isUnAuthFilter = filter.isUnAuthenticFilter();
        //水的runtime
        if (DeviceType.isWaterSeries(filter.getmDeviceType())) {
            runTimeLife = filter.getmWaterRunTimeLife();
            //空进的runtime
        } else if (DeviceType.isAirTouchSeries(filter.getmDeviceType())) {
            runTimeLife = filter.getLifePercentage();
        } else if (DeviceType.isMadAir(filter.getmDeviceType())) {
            runTimeLife = (int) filter.getRuntimeLife();
        }
        //初始化状态
        if (runTimeLife == FILTER_INITIAL || runTimeLife == HPlusConstants.ERROR_MAX_VALUE
                || runTimeLife == HPlusConstants.ERROR_SENSOR) {
            filterRemainTv.setText(HPlusConstants.DATA_LOADING_FAILED_STATUS);
            filterRemainTv.setTextColor(getResources().getColor(R.color.ds_clean_now));
        }//滤网不够了
        else if (runTimeLife <= AIRTOUCH_FILTER_FLASH && runTimeLife > 0) {
            filterRemainTv.setText(runTimeLife + "%");
            if (DeviceType.isWaterSeries(filter.getmDeviceType())) {
                if (runTimeLife <= AQUATOUCH_FILTER_FLASH) {
                    filterRemainTv.setTextColor(getResources().getColor(R.color.pm_25_worst));
                    filterRemainTv.startAnimation(alphaOffAnimation);
                }
            } else if (DeviceType.isAirTouchSeries(filter.getmDeviceType()) ||
                    DeviceType.isMadAir(filter.getmDeviceType())) {
                filterRemainTv.setTextColor(getResources().getColor(R.color.pm_25_worst));
                filterRemainTv.startAnimation(alphaOffAnimation);
            }
            //非认证或者需要购买
        } else if (runTimeLife == 0 || isUnAuthFilter) {
            filterValueRl.setVisibility(GONE);
            filterRootRl.setBackgroundResource(R.color.filter_empty);
            filterNameTv.setTextColor(getResources().getColor(R.color.white));
            filterPosition.setTextColor(getResources().getColor(R.color.white));
            shoppingCartIc.setImageResource(R.drawable.shopping_white);
            if (isUnAuthFilter) {
                filterNameTv.setText(getResources().getString(R.string.device_control_unauth_filter));
            } else {
                if (getResources().getString(R.string.pre_filter).equals(filterNameTv.getText())) {
                    filterNameTv.setText(getResources().getString(R.string.device_control_prefilter_empty));
                } else {
                    filterNameTv.setText(getResources().getString(R.string.device_control_filter_empty));
                }
            }
            //口罩连接和using显示滤网
        } else if (runTimeLife == MAD_AIR_HIDE_FILTER) {
            filterRemainTv.setText(HPlusConstants.DATA_LOADING_FAILED_STATUS);
            filterRemainTv.setTextColor(getResources().getColor(R.color.ds_clean_now));
            //正常情况
        } else {
            filterRemainTv.setText(runTimeLife + "%");
        }

        //印度账号不显示滤网
        if (!AppManager.getInstance().getLocalProtocol().canShowFilterPurchase()) {
            shoppingCartIc.setVisibility(INVISIBLE);
        }

        shoppingCartIc.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mBuyFilterCallBack != null) {
                    mBuyFilterCallBack.callback(filter);
                }
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

    public void setmBuyFilterCallBack(DeviceFilterAdapter.BuyFilterCallBack mBuyFilterCallBack) {
        this.mBuyFilterCallBack = mBuyFilterCallBack;
    }
}
