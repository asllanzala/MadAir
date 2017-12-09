package com.honeywell.hch.airtouch.ui.control.ui.device.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModelSharedPreference;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceStatus;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.MadAirDeviceObject;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.control.ui.device.controller.DeviceControlActivity;
import com.honeywell.hch.airtouch.ui.help.DeviceControlHelpActivity;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.response.GetReportResponse;

import static com.honeywell.hch.airtouch.ui.control.ui.device.view.FilterItemView.MAD_AIR_HIDE_FILTER;

/**
 * Created by Vincent on 25/7/16.
 */
public class MadAirDetailInfoFragment extends DeviceControlBaseFragment {
    private String TAG = "MadAirDetailInfoFragment";
    private ImageView mMadAirImg;
    private BroadcastReceiver mRefreshDataReceiver;
    protected TextView mGroupNameTextView;
    protected EditText mGroupNameEditText;
    private InputMethodManager mInputMethodManager;
    private Boolean mIsEditGroupNameMode = false;
    private LinearLayout mMadAirLl;

    public static MadAirDetailInfoFragment newInstance(HomeDevice homeDevice, Activity activity, int fromType) {
        MadAirDetailInfoFragment fragment = new MadAirDetailInfoFragment();
        fragment.initActivity(activity);
        fragment.setCurrentDevice(homeDevice, fromType);
        return fragment;
    }

    protected void setCurrentDevice(HomeDevice device, int fromType) {
        mCurrentDevice = device;
        mDeviceId = mCurrentDevice.getDeviceId();
        mControlUIManager = ((DeviceControlActivity) mParentActivity).getControlUIManager();
        mControlUIManager.createFilters(mCurrentDevice, mFilters);

        registerRunStatusChangedReceiver();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_madair_detail_info, container, false);
        initView(view);
        initAdapter();
        initListener();
        updateFilterRemain();
        registerChangedReceiver();
        initMadAirTitle(view);
        setupGroupNameEditText();
        return view;
    }

    protected void initView(View view) {
        super.initView(view, R.string.mad_air_filter_status);
        mInputMethodManager = (InputMethodManager) this.mParentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        mMadAirImg = (ImageView) view.findViewById(R.id.mad_air_device_iv);
        mMadAirImg.setImageResource(mControlUIManager.getMadAirDeviceImg(((MadAirDeviceObject) mCurrentDevice).getmMadAirDeviceModel()));
        mMadAirLl = (LinearLayout) view.findViewById(R.id.fragment_mad_air_ll);
        mMadAirLl.setOnClickListener(this);
    }

    private void initMadAirTitle(View view) {
        mGroupNameEditText = (EditText) view.findViewById(R.id.group_title_edit_id);
        mGroupNameTextView = (TextView) view.findViewById(R.id.message_title_text_id);
        mGroupNameTextView.setText(((MadAirDeviceObject) mCurrentDevice).getmMadAirDeviceModel().getDeviceName());
        mTitleIv = (ImageView) view.findViewById(R.id.all_device_select_iv);
        mTitleIv.setImageResource(R.drawable.ic_help);
        mTitleIv.setVisibility(View.VISIBLE);
    }

    private void updateFilterRemain() {
        MadAirDeviceStatus madAirDeviceStatus = ((MadAirDeviceObject) mCurrentDevice).getmMadAirDeviceModel().getDeviceStatus();
        if (madAirDeviceStatus.equals(MadAirDeviceStatus.CONNECT) ||
                madAirDeviceStatus.equals(MadAirDeviceStatus.USING)
                ) {
            int filterPercent = ((MadAirDeviceObject) mCurrentDevice).getmMadAirDeviceModel().getFilterUsageDuration();
            if (!GetReportResponse.isFilterNotInstalled((byte) ((MadAirDeviceObject) mCurrentDevice).getmMadAirDeviceModel().getAlarmInfo())) {
                mFilters.get(0).setRuntimeLife(MadAirDeviceModel.calculateUsagePercent(filterPercent));
            } else {
                mFilters.get(0).setRuntimeLife(MAD_AIR_HIDE_FILTER);
            }
        } else {
            mFilters.get(0).setRuntimeLife(MAD_AIR_HIDE_FILTER);
        }
        mDeviceFilterAdapter.changeData(mFilters);
        mDeviceFilterAdapter.notifyDataSetChanged();
    }

    private void registerChangedReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(HPlusConstants.REFRESH_MADAIR_DATA);
        mRefreshDataReceiver = new RefreshDataReceiver();
        mParentActivity.registerReceiver(mRefreshDataReceiver, intentFilter);
    }

    private class RefreshDataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mCurrentDevice = mControlUIManager.getHomeDeviceByDeviceId(mDeviceId);
            if (mCurrentDevice != null) {
                updateFilterRemain();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.enroll_back_layout) {
            ((DeviceControlActivity) mParentActivity).setQuickActionResult();
            mInputMethodManager.hideSoftInputFromWindow(mGroupNameTextView.getWindowToken(), 0);
        } else if (v.getId() == R.id.all_device_select_iv) {
            Intent intent = new Intent(mParentActivity, DeviceControlHelpActivity.class);
            intent.putExtra(DeviceControlActivity.ARG_FROM_TYPE, mFromType);
            intent.putExtra(DeviceControlHelpActivity.MAD_AIR_HELP_PARAMETER, ((MadAirDeviceObject) mCurrentDevice).getmMadAirDeviceModel());
            startActivity(intent);
            mParentActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        } else if (v.getId() == R.id.fragment_mad_air_ll) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Linear view");
            if (mIsEditGroupNameMode) {
                updateNameGroupProcess();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mParentActivity.unregisterReceiver(mRefreshDataReceiver);
    }

    private void setupGroupNameEditText() {
        mGroupNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkClickStatus(mGroupNameTextView);

                mGroupNameEditText.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER
                                && mInputMethodManager.isActive()) {
                            updateNameGroupProcess();
                            return true;
                        }
                        return false;
                    }
                });
                mInputMethodManager
                        .showSoftInput(mGroupNameEditText, InputMethodManager.SHOW_FORCED);
            }
        });

        mGroupNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                StringUtil.maxCharacterFilter(mGroupNameEditText);
                StringUtil.addOrEditHomeFilter(mGroupNameEditText);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void updateNameGroupProcess() {
        mInputMethodManager.hideSoftInputFromWindow(mGroupNameEditText.getWindowToken(), 0);
        String deviceName = mGroupNameEditText.getText().toString();
        if (StringUtil.isEmpty(deviceName.trim())) {
            quitEditGroupNameMode();
            return;
        }
        if (deviceName.equals(mGroupNameTextView.getText().toString())) {
            quitEditGroupNameMode();
            return;
        }
        if (mControlUIManager.isDuplicateMadAirAddress(deviceName)) {
            mDropDownAnimationManager.showDragDownLayout(getString(R.string.mad_air_enroll_same_name), true);
            quitEditGroupNameMode();
            return;
        }
        mControlUIManager.saveDeviceName(((MadAirDeviceObject) mCurrentDevice).getmMadAirDeviceModel().getMacAddress(), deviceName);
        mGroupNameTextView.setText(deviceName);
        quitEditGroupNameMode();
    }

    private void quitEditGroupNameMode() {
        if (mDialog != null) {
            mDialog.cancel();
        }
        mIsEditGroupNameMode = false;
        mGroupNameEditText.setVisibility(View.GONE);
        mGroupNameTextView.setVisibility(View.VISIBLE);
    }

    private void checkClickStatus(View v) {
        if (v.getId() == R.id.message_title_text_id) {
            mIsEditGroupNameMode = true;
            mGroupNameEditText.setVisibility(View.VISIBLE);
            mGroupNameTextView.setVisibility(View.GONE);
            mGroupNameEditText.setText(mGroupNameTextView.getText().toString());
            mGroupNameEditText.setFocusable(true);
            mGroupNameEditText.setFocusableInTouchMode(true);
            mGroupNameEditText.requestFocus();
        }
    }
}
