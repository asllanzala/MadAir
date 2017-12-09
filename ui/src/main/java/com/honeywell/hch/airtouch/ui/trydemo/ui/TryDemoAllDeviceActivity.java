package com.honeywell.hch.airtouch.ui.trydemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.honeywell.hch.airtouch.plateform.http.manager.model.RealUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.SelectStatusDeviceItem;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.manager.model.Category;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.common.ui.adapter.CategoryAdapter;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseActivity;
import com.honeywell.hch.airtouch.ui.common.ui.controller.HomeDeviceInfoBaseFragment;
import com.honeywell.hch.airtouch.ui.common.ui.view.GroupContolListView;
import com.honeywell.hch.airtouch.ui.control.manager.model.ControlConstant;
import com.honeywell.hch.airtouch.ui.control.ui.device.controller.DeviceControlActivity;
import com.honeywell.hch.airtouch.ui.splash.StartActivity;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoAllDeviceUiManager;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoDataConstructor;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoIndicatorValueManager;

import java.util.ArrayList;

/**
 * Created by Vincent on 2/11/16.
 */
public class TryDemoAllDeviceActivity extends BaseActivity implements IRefreshOpr {
    private String TAG = "TryDemoAllDeviceActivity";

    private ScrollView mScrollView;
    private int mLocationId = 0;
    private TextView mTitleTextview;
    private CategoryAdapter mCustomBaseAdapter;
    private GroupContolListView mDeviceListView;
    private TryDemoAllDeviceUiManager mTryOutAllDeviceUiManager;
    private ArrayList<Category> mListData;

    private TryDemoIndicatorValueManager mTryDemoIndicatorValueManager = null;
    private boolean isFromStartActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alldevice_tryout);
        initStatusBar();
        initView();
        initTryOutAllDeviceUiManager();
        initData();
        initItemSelectedListener();

        mTryDemoIndicatorValueManager = new TryDemoIndicatorValueManager();
        mTryDemoIndicatorValueManager.addRefreshListener(this);

        startChangeIndicatorValue();

        isFromStartActivity = getIntent().getBooleanExtra(StartActivity.FROM_START_ACTIVITY, false);

    }

    @Override
    public  void finish(){
        mTryDemoIndicatorValueManager.exitTryDemoProcess();
        TryDemoDataConstructor.exitTryDemo();
        if (isFromStartActivity) {
            Intent i = new Intent();
            i.setClass(this, StartActivity.class);
            i.putExtra(StartActivity.FROM_ANOTHER_ACTIVITY,true);
            startActivity(i);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }

        super.finish();
    }

    private void initView() {
        mDeviceListView = (GroupContolListView) findViewById(R.id.all_device_listView);
        mScrollView = (ScrollView) findViewById(R.id.aqua_scrollview);
        mTitleTextview = (TextView) findViewById(R.id.title_textview_id);
        mScrollView.smoothScrollTo(0, 0);
    }

    private void initData() {
        mTitleTextview.setText(getString(R.string.try_demo_alldevice_title));
        initAdapter();
    }

    private void initAdapter() {
        mListData = mTryOutAllDeviceUiManager.getCategoryData();
        HomeDeviceInfoBaseFragment.ButtomType buttomType = HomeDeviceInfoBaseFragment.ButtomType.NORMAL;
        mCustomBaseAdapter = new CategoryAdapter(mContext, mListData, buttomType);
        mDeviceListView.setAdapter(mCustomBaseAdapter);
        mCustomBaseAdapter.notifyDataSetChanged();
    }

    private void initTryOutAllDeviceUiManager() {
        UserLocationData userLocationData = new RealUserLocationData();
        mTryOutAllDeviceUiManager = new TryDemoAllDeviceUiManager(userLocationData);
    }

    private void initItemSelectedListener() {
        mDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = parent.getItemAtPosition(position);
                SelectStatusDeviceItem selectStatusDeviceItem = ((SelectStatusDeviceItem) object);
                //点击设备列表
                Intent intent = new Intent(mContext, DeviceControlActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(DeviceControlActivity.ARG_LOCATION, mLocationId);
                bundle.putInt(DeviceControlActivity.ARG_FROM_TYPE, ControlConstant.FROM_DEMO_CONTROL);
                bundle.putInt(DeviceControlActivity.ARG_DEVICE_ID, selectStatusDeviceItem.getDeviceItem().getDeviceId());
                intent.putExtras(bundle);
                startActivityForResult(intent, DashBoadConstant.DEVICE_CONTROL_REQUEST_CODE);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });
    }

    public void doClick(View v) {
        if (v.getId() == R.id.enroll_back_layout) {
            backIntent();
        }
    }

    @Override
    public void doRefreshUIOpr() {
        mCustomBaseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case DashBoadConstant.DEVICE_CONTROL_REQUEST_CODE:
                doRefreshUIOpr();
            default:
                break;
        }
    }
    private void startChangeIndicatorValue(){
        mTryDemoIndicatorValueManager.startChangePMValue();
    }
}
