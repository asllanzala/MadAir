package com.honeywell.hch.airtouch.ui.quickaction.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.devices.water.model.WaterDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceInfo;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.SelectStatusDeviceItem;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.manager.model.Category;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.common.ui.adapter.CategoryAdapter;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseActivity;
import com.honeywell.hch.airtouch.ui.common.ui.controller.HomeDeviceInfoBaseFragment;
import com.honeywell.hch.airtouch.ui.common.ui.view.AllDeviceItemView;
import com.honeywell.hch.airtouch.ui.common.ui.view.CustomFontTextView;
import com.honeywell.hch.airtouch.ui.common.ui.view.GroupContolListView;
import com.honeywell.hch.airtouch.ui.control.manager.model.ControlConstant;
import com.honeywell.hch.airtouch.ui.control.ui.device.controller.DeviceControlActivity;
import com.honeywell.hch.airtouch.ui.quickaction.manager.QuickActionManager;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoConstant;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoHomeListContructor;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoIndicatorValueManager;
import com.honeywell.hch.airtouch.ui.trydemo.ui.IRefreshOpr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 9/8/16.
 */
public class QuickActionActivity extends BaseActivity implements IRefreshOpr {
    private TextView mTitleTv;
    private Object mQuickType;
    private QuickActionManager mQuickActionManager;
    private UserLocationData mUserLocation = null;
    private final int LOCATIONIDDEFAULT = 0;
    private List<HomeDevice> mQuickHomeDeviceList;
    private HomeDevice mWorstHomeDevce;
    private TextView mIndicatorTv;
    private CustomFontTextView mWorstValueTv;
    private TextView mWorstDescTv;
    private TextView mNeedAttentionTv;
    private TextView mDeviceName;
    private ScrollView mScrollView;
    private CategoryAdapter mCustomBaseAdapter;
    private ArrayList<Category> mListData;
    private GroupContolListView mDeviceListview;
    private RefreshDeviceStatusReceiver refreshDeviceStatusReceiver;
    private View mView;
    private SelectStatusDeviceItem selectStatusDeviceItem;
    private boolean isFromTryDemo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_action);
        initStatusBar();

        isFromTryDemo = getIntent().getBooleanExtra(TryDemoConstant.IS_FROM_TRY_DEMO, false);
        mQuickType = getIntent().getSerializableExtra(QuickActionManager.QUICKTYPE);

        init();
        initData();
        initItemSelectedListener();
        registerUserAliveChangedReceiver();
    }

    private void init() {
        mTitleTv = (TextView) findViewById(R.id.title_textview_id);
        mIndicatorTv = (TextView) findViewById(R.id.quick_action_indicator_tv);
        mWorstValueTv = (CustomFontTextView) findViewById(R.id.quick_action_value_tv);
        mWorstDescTv = (TextView) findViewById(R.id.quick_action_desc_tv);
        mNeedAttentionTv = (TextView) findViewById(R.id.quick_action_no_attention);
        mDeviceName = (TextView) findViewById(R.id.quick_action_device_name);
        mDeviceListview = (GroupContolListView) findViewById(R.id.device_listView);
        mScrollView = (ScrollView) findViewById(R.id.quick_scrollview);
        mScrollView.smoothScrollTo(0, 0);
        mQuickActionManager = new QuickActionManager();
    }

    private void initData() {
        if (isFromTryDemo) {
            TryDemoIndicatorValueManager.addRefreshListener(this);
            mUserLocation = UserDataOperator.getLocationWithId(getIntent().getIntExtra(QuickActionManager.LOCATIONIDPARAMETER, LOCATIONIDDEFAULT), TryDemoHomeListContructor.getInstance().getUserLocationDataList()
                    , TryDemoHomeListContructor.getInstance().getVirtualUserLocationDataList());
        } else {
            mUserLocation = UserDataOperator.getLocationWithId(getIntent().getIntExtra(QuickActionManager.LOCATIONIDPARAMETER, LOCATIONIDDEFAULT), UserAllDataContainer.shareInstance().getUserLocationDataList()
                    , UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList());
        }

        if (mUserLocation != null) {
            intQuickActionValue();
            initAdapter();
        }
    }


    private void intQuickActionValue() {
        mQuickHomeDeviceList = mQuickActionManager.getQuickHomeDevice(mUserLocation, mQuickType);
        mWorstHomeDevce = mQuickActionManager.getWorstDevice(mUserLocation, mQuickType);

        if (mQuickType == QuickActionManager.QuickActionType.PM25) {
            mTitleTv.setText(R.string.quick_action_pm25_title);
            mIndicatorTv.setText(getString(R.string.quick_action_pm25_title));
            String pm25 = ((AirTouchDeviceObject) mWorstHomeDevce).getPmSensorFeature().getPm25Value();
            mWorstValueTv.setCustomText(pm25);
            mWorstValueTv.setTextColor(((AirTouchDeviceObject) mWorstHomeDevce).getPmSensorFeature().getPm25Color());
            mWorstDescTv.setText(mQuickActionManager.getPm25Status((AirTouchDeviceObject) mWorstHomeDevce));
            mWorstDescTv.setTextColor(((AirTouchDeviceObject) mWorstHomeDevce).getPmSensorFeature().getPm25Color());
            if (HPlusConstants.DATA_LOADING_STATUS.equals(pm25) || HPlusConstants.DATA_LOADING_FAILED_STATUS.equals(pm25)) {
                mWorstDescTv.setVisibility(View.GONE);
            } else {
                mWorstDescTv.setVisibility(View.VISIBLE);
            }
        } else if (mQuickType == QuickActionManager.QuickActionType.TVOC) {
            mTitleTv.setText(R.string.quick_action_tvoc_title);
            mIndicatorTv.setText(getString(R.string.all_device_tvoc));
            String tvoc = ((AirTouchDeviceObject) mWorstHomeDevce).getTvocFeature().getTVOC();
            mWorstValueTv.setCustomText(tvoc);
            mWorstValueTv.setTextColor(((AirTouchDeviceObject) mWorstHomeDevce).getTvocFeature().getTVOCColor());
            if (DeviceType.isAirTouch450(mWorstHomeDevce.getDeviceType()) || DeviceType.isAirTouch450Update(mWorstHomeDevce.getDeviceType()) || DeviceType.isAirTouchXCompactSeries(mWorstHomeDevce.getDeviceType()) ||
                    HPlusConstants.DATA_LOADING_STATUS.equals(tvoc) || HPlusConstants.DATA_LOADING_FAILED_STATUS.equals(tvoc)) {
                mWorstDescTv.setVisibility(View.GONE);
            } else {
                mWorstDescTv.setVisibility(View.VISIBLE);
                mWorstDescTv.setText(mQuickActionManager.getTVOCStatus((AirTouchDeviceObject) mWorstHomeDevce));
                mWorstDescTv.setTextColor(((AirTouchDeviceObject) mWorstHomeDevce).getTvocFeature().getTVOCColor());
            }
        } else if (mQuickType == QuickActionManager.QuickActionType.WATERQUALITY) {
            mIndicatorTv.setText(getString(R.string.dashboard_water_quality));
            mTitleTv.setText(R.string.dashboard_water_quality);
            mWorstValueTv.setCustomText(((WaterDeviceObject) mWorstHomeDevce).getWaterQualityFeature().showQualityLevel());
            mWorstValueTv.setTextColor(((WaterDeviceObject) mWorstHomeDevce).getWaterQualityFeature().showQualityColor());
            mWorstDescTv.setVisibility(View.GONE);
        }
        if (mQuickHomeDeviceList.size() == 0) {
            mNeedAttentionTv.setVisibility(View.VISIBLE);
        }
        //TODO:  Don't show device name if data of category is reading or cannot be read, or user will be confusing.
        if (HPlusConstants.DATA_LOADING_STATUS.equals(mWorstValueTv.getText().toString()) ||
                HPlusConstants.DATA_LOADING_FAILED_STATUS.equals(mWorstValueTv.getText().toString())) {
            mDeviceName.setVisibility(View.GONE);
        } else {
            mDeviceName.setText(getString(R.string.quick_action_attention_device, mWorstHomeDevce.getDeviceInfo().getName()));
            mDeviceName.setVisibility(View.VISIBLE);
        }

    }

    private void initAdapter() {
        mListData = mQuickActionManager.getCategoryData(mQuickHomeDeviceList);

        //解决bug AIRQA-1650,在某些时候，刚好用户点了title的select按钮，进入选择状态时候，这个时候自动刷新成功
        //就会丢失列表的选择状态，所以这里如果上一次mCustomBaseAdapter不为空，就把当前的状态继续给新的。
        HomeDeviceInfoBaseFragment.ButtomType buttomType = HomeDeviceInfoBaseFragment.ButtomType.NORMAL;
        mCustomBaseAdapter = new CategoryAdapter(mContext, mListData, buttomType);
        mDeviceListview.setAdapter(mCustomBaseAdapter);
    }

    private void initItemSelectedListener() {
        mDeviceListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = parent.getItemAtPosition(position);
                if (object instanceof SelectStatusDeviceItem) {
                    selectStatusDeviceItem = ((SelectStatusDeviceItem) object);
                    mView = view;
                    //点击设备列表
                    if (isFromTryDemo) {
                        goToTryDemoDeviceControlActivity(parent, position);
                    } else {
                        goToDeviceControlActivity();
                    }
                }
            }
        });
    }


    private void goToTryDemoDeviceControlActivity(AdapterView<?> parent, int position) {
        Object object = parent.getItemAtPosition(position);
        SelectStatusDeviceItem selectStatusDeviceItem = ((SelectStatusDeviceItem) object);
        //点击设备列表
        Intent intent = new Intent(mContext, DeviceControlActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(DeviceControlActivity.ARG_LOCATION, mUserLocation.getLocationID());
        bundle.putInt(DeviceControlActivity.ARG_FROM_TYPE, ControlConstant.FROM_DEMO_CONTROL);
        bundle.putInt(DeviceControlActivity.ARG_DEVICE_ID, selectStatusDeviceItem.getDeviceItem().getDeviceId());
        intent.putExtras(bundle);
        startActivityForResult(intent, DashBoadConstant.DEVICE_CONTROL_REQUEST_CODE);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    private void goToDeviceControlActivity() {
        if (AppManager.getLocalProtocol().getRole().canShowDeviceDetail()) {
            Intent intent = new Intent(mContext, DeviceControlActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(DeviceControlActivity.ARG_LOCATION, mUserLocation.getLocationID());
            bundle.putInt(DeviceControlActivity.ARG_FROM_TYPE, ControlConstant.FROM_NORMAL_CONTROL);

            bundle.putInt(DeviceControlActivity.ARG_DEVICE_ID, selectStatusDeviceItem.getDeviceItem().getDeviceId());
            intent.putExtras(bundle);
            startActivityForResult(intent, DashBoadConstant.QUICK_ACTION_RESULT_CODE);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }
    }

    private void registerUserAliveChangedReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(HPlusConstants.SHORTTIME_REFRESH_END_ACTION);
        refreshDeviceStatusReceiver = new RefreshDeviceStatusReceiver();
        registerReceiver(refreshDeviceStatusReceiver, intentFilter);
    }

    private class RefreshDeviceStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (HPlusConstants.SHORTTIME_REFRESH_END_ACTION.equals(action)) {
                //location 变化
                initData();
            }
        }
    }

    private void unRefreshDeviceStatusReceiver() {
        if (refreshDeviceStatusReceiver != null) {
            unregisterReceiver(refreshDeviceStatusReceiver);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (resultCode) {
            case DashBoadConstant.QUICK_ACTION_RESULT_CODE:
                DeviceInfo deviceInfo = (DeviceInfo) intent.getSerializableExtra(DashBoadConstant.ARG_QUICK_ACTION_DEVICE);
                if (deviceInfo != null) {
                    selectStatusDeviceItem.getDeviceItem().setDeviceInfo(deviceInfo);
                    ((AllDeviceItemView) mView).initViewHolder(mView, selectStatusDeviceItem, mCustomBaseAdapter.getButtomType());
                }

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRefreshDeviceStatusReceiver();
        TryDemoIndicatorValueManager.removeRefreshListener(this);
    }

    public void doClick(View v) {
        if (v.getId() == R.id.enroll_back_layout) {
            backIntent();
        }
    }

    @Override
    public void doRefreshUIOpr() {
        if (mUserLocation != null) {
            intQuickActionValue();
            initAdapter();
        }
    }
}
