package com.honeywell.hch.airtouch.ui.common.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.ViewHolderUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.plateform.devices.water.model.UnSupportDeviceObject;
import com.honeywell.hch.airtouch.plateform.devices.water.model.WaterDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceGroupItem;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.MadAirDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.SelectStatusDeviceItem;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.controller.HomeDeviceInfoBaseFragment.ButtomType;
import com.honeywell.hch.airtouch.ui.control.manager.model.DeviceMode;

/**
 * Created by Vincent on 3/6/16.
 */
public class AllDeviceItemView extends RelativeLayout {
    private final String TAG = "AllDeviceItemView";
    private Context mContext;

    private final static float ALPHA_DISABLE = 0.5f;
    private final static float ALPHA_ENABLE = 1.0f;

    /**
     * 不能进入下一级界面的时候，箭头要设置为透明度30%
     */
    private final static float ALPHA_ARROR_CANNOT_GO = 0.3f;

    public AllDeviceItemView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public AllDeviceItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.all_device_listview_item, this);
    }

    public void initViewHolder(View view, SelectStatusDeviceItem selectStatusDeviceItem, ButtomType buttomType) {

        setViewEanble(view, buttomType.mDeviceAlphaEnable);

        HomeDevice homeDevice = selectStatusDeviceItem.getDeviceItem();
        CheckBox checkBox = ViewHolderUtil.get(view, R.id.all_device_check_box);

        ImageView deviceImg = ViewHolderUtil.get(view, R.id.filter_icon);
        ImageView expandImg = ViewHolderUtil.get(view, R.id.all_device_item_expand_iv);
        setEnterpriseAccountArror(expandImg);

        TextView deviceNameTv = ViewHolderUtil.get(view, R.id.all_device_name_tv);
        TextView deviceStatusTv = ViewHolderUtil.get(view, R.id.all_device_status_tv);
        ImageView deviceStatusImg = ViewHolderUtil.get(view, R.id.all_device_status_iv);

        RelativeLayout firstRl = ViewHolderUtil.get(view, R.id.all_device_first_value_rl);
        CustomFontTextView firstValueTv = ViewHolderUtil.get(view, R.id.all_device_first_value_tv);
        TextView firstValueHintTv = ViewHolderUtil.get(view, R.id.all_device_first_hint_tv);

        RelativeLayout secondRl = ViewHolderUtil.get(view, R.id.all_device_second_value_rl);
        CustomFontTextView secondValueTv = ViewHolderUtil.get(view, R.id.all_device_second_value_tv);
        TextView secondValueHintTv = ViewHolderUtil.get(view, R.id.all_device_second_hint_tv);

        TextView unsupportDeviceText = ViewHolderUtil.get(view,R.id.unknow_device_str);
        RelativeLayout nameLayout = ViewHolderUtil.get(view,R.id.all_device_name_rl);
        LinearLayout valueLayout = ViewHolderUtil.get(view,R.id.all_device_value_rl);

//        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "name: " + homeDevice.getDeviceInfo().getName());
        checkBox.setChecked(selectStatusDeviceItem.isSelected());
        firstRl.setVisibility(VISIBLE);
        deviceStatusImg.setImageResource(homeDevice.getControlFeature().getAllDeviceStatusImage());
        deviceStatusTv.setText(homeDevice.getControlFeature().getScenerioModeAction());
        deviceImg.setImageResource(homeDevice.getControlFeature().getAllDeviceBigImg());
        if (buttomType != ButtomType.NORMAL) {
            checkBox.setVisibility(VISIBLE);
            expandImg.setVisibility(INVISIBLE);
        } else {
            checkBox.setVisibility(GONE);
            expandImg.setVisibility(VISIBLE);
        }

        if (homeDevice instanceof UnSupportDeviceObject){
            nameLayout.setVisibility(View.GONE);
            valueLayout.setVisibility(View.GONE);
            unsupportDeviceText.setVisibility(View.VISIBLE);
        }else if(homeDevice instanceof MadAirDeviceObject){
            deviceNameTv.setText(((MadAirDeviceObject) homeDevice).getmMadAirDeviceModel().getDeviceName());
            nameLayout.setVisibility(View.VISIBLE);
            valueLayout.setVisibility(View.GONE);
            unsupportDeviceText.setVisibility(View.GONE);
        } else {
            deviceNameTv.setText(homeDevice.getDeviceInfo().getName());
            nameLayout.setVisibility(View.VISIBLE);
            valueLayout.setVisibility(View.VISIBLE);
            unsupportDeviceText.setVisibility(View.GONE);
            if (homeDevice instanceof AirTouchDeviceObject) {
                if (((AirTouchDeviceObject) homeDevice).canShowTvoc()) {
                    secondRl.setVisibility(VISIBLE);
                    secondValueHintTv.setText(mContext.getString(R.string.all_device_tvoc));
                    secondValueTv.setCustomText(((AirTouchDeviceObject) homeDevice).getTvocFeature().getTVOC());
                    secondValueTv.setTextColor(((AirTouchDeviceObject) homeDevice).getTvocFeature().getTVOCColor());
                } else {
                    secondRl.setVisibility(INVISIBLE);
                }
                deviceImg.setImageResource(R.drawable.all_device_air_icon);
                firstValueHintTv.setText(mContext.getString(R.string.pm25_str));
                firstValueTv.setCustomText(((AirTouchDeviceObject) homeDevice).getPmSensorFeature().getPm25Value());
                firstValueTv.setTextColor(((AirTouchDeviceObject) homeDevice).getPmSensorFeature().getPm25Color());
            } else if (homeDevice instanceof WaterDeviceObject) {
                secondRl.setVisibility(INVISIBLE);
                firstValueHintTv.setText(mContext.getString(R.string.smart_ro_water));
                firstValueTv.setCustomText(((WaterDeviceObject) homeDevice).getWaterQualityFeature().showQualityLevel());
                firstValueTv.setTextColor(((WaterDeviceObject) homeDevice).getWaterQualityFeature().showQualityAllDeviceColor());
                if (homeDevice.getiDeviceStatusFeature().isNormal()) {
                    deviceImg.setImageResource(R.drawable.all_device_ro_icon);
                } else {
                    deviceImg.setImageResource(R.drawable.all_device_ro_icon_abnormal);
                }
            }

        }

    }

    public void initViewHolder(View view, DeviceGroupItem deviceGroupItem, ButtomType buttomType) {

        setViewEanble(view, buttomType.mGroupAlphaEnable);

        CheckBox checkBox = ViewHolderUtil.get(view, R.id.all_device_check_box);

        ImageView deviceImg = ViewHolderUtil.get(view, R.id.filter_icon);
        ImageView expandImg = ViewHolderUtil.get(view, R.id.all_device_item_expand_iv);

        TextView deviceNameTv = ViewHolderUtil.get(view, R.id.all_device_name_tv);
        TextView deviceStatusTv = ViewHolderUtil.get(view, R.id.all_device_status_tv);
        ImageView deviceStatusImg = ViewHolderUtil.get(view, R.id.all_device_status_iv);

        RelativeLayout firstRl = ViewHolderUtil.get(view, R.id.all_device_first_value_rl);
        RelativeLayout secondRl = ViewHolderUtil.get(view, R.id.all_device_second_value_rl);

        TextView unsupportDeviceText = ViewHolderUtil.get(view,R.id.unknow_device_str);
        RelativeLayout nameLayout = ViewHolderUtil.get(view,R.id.all_device_name_rl);
        LinearLayout valueLayout = ViewHolderUtil.get(view,R.id.all_device_value_rl);

        firstRl.setVisibility(GONE);
        secondRl.setVisibility(GONE);
        checkBox.setVisibility(GONE);
        expandImg.setVisibility(VISIBLE);
        expandImg.setAlpha(ALPHA_ENABLE);

        if (isGroupItemNormal(deviceGroupItem)) {
            deviceImg.setImageResource(R.drawable.all_device_group_icon);
        } else {
            deviceImg.setImageResource(R.drawable.group_abnormal);
        }

        deviceStatusTv.setText(DeviceMode.getModeName(deviceGroupItem.getScenarioOperation()));
        deviceStatusImg.setImageResource(DeviceMode.getAllDeviceModeImg(deviceGroupItem.getScenarioOperation()));
        deviceNameTv.setText(deviceGroupItem.getGroupName());

        nameLayout.setVisibility(View.VISIBLE);
        valueLayout.setVisibility(View.VISIBLE);
        unsupportDeviceText.setVisibility(View.GONE);
    }


    /**
     * 获取Item的check状态
     * @param view
     * @return
     */
    public boolean getItemCheckStatus(View view){
        CheckBox checkBox =  ViewHolderUtil.get(view, R.id.all_device_check_box);
        if (checkBox != null && checkBox.getVisibility() == View.VISIBLE){
            return checkBox.isChecked();
        }
        return false;
    }

    //check whether group own a abnormal device
    private boolean isGroupItemNormal(DeviceGroupItem deviceGroupItem) {
        for (SelectStatusDeviceItem selectStatusDeviceItem : deviceGroupItem.getGroupDeviceList()) {
            if (!selectStatusDeviceItem.getDeviceItem().getiDeviceStatusFeature().isNormal() || !DeviceType.isHplusSeries(selectStatusDeviceItem.getDeviceItem().getDeviceType())) {
                return false;
            }
        }
        return true;
    }

    private void setViewEanble(View view, boolean isEnable) {
        float alphaVlaue = isEnable ? ALPHA_ENABLE : ALPHA_DISABLE;
        view.setAlpha(alphaVlaue);
    }

    private void setEnterpriseAccountArror(ImageView expandImg) {
        if (!AppManager.getLocalProtocol().canGoToGroupPage()) {
            expandImg.setAlpha(ALPHA_ARROR_CANNOT_GO);
        }
    }

}
