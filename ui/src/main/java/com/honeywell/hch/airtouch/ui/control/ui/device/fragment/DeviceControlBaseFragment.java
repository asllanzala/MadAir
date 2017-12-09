package com.honeywell.hch.airtouch.ui.control.ui.device.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;
import com.honeywell.hch.airtouch.plateform.devices.common.Filter;
import com.honeywell.hch.airtouch.plateform.devices.water.model.WaterDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.permission.Permission;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseRequestFragment;
import com.honeywell.hch.airtouch.ui.common.ui.view.GroupContolListView;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.control.manager.device.ControlUIBaseManager;
import com.honeywell.hch.airtouch.ui.control.manager.device.IControlManager;
import com.honeywell.hch.airtouch.ui.control.manager.model.ControlConstant;
import com.honeywell.hch.airtouch.ui.control.ui.device.adapter.DeviceFilterAdapter;
import com.honeywell.hch.airtouch.ui.control.ui.device.controller.DeviceControlActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.beforeplay.EnrollScanActivity;
import com.honeywell.hch.airtouch.ui.help.DeviceControlHelpActivity;
import com.honeywell.hch.airtouch.ui.trydemo.ui.IRefreshOpr;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vincent on 25/7/16.
 */
public class DeviceControlBaseFragment extends BaseRequestFragment implements View.OnClickListener, IRefreshOpr {
    private BroadcastReceiver mRunStatusChangedReceiver;
    protected Animation alphaOffAnimation;
    protected ImageView mTitleIv;
    protected FrameLayout mBackLayout;
    protected TextView mDeviceNameTv;
    protected ColorStateList mModeOnTextColor, mModeOffTextColor;
    protected View headView;
    protected DeviceFilterAdapter mDeviceFilterAdapter;
    protected List<Filter> mFilters = new ArrayList<>();
    protected ControlUIBaseManager mControlUIManager;
    private final int HEADHEIGHT = 75;
    protected HomeDevice mCurrentDevice;

    protected TextView mErrortextTV;
    protected TextView mErrorTitleTv;
    protected ImageView mErrorIv;
    protected GroupContolListView mFilterListView;
    protected LinearLayout mDeviceErrorLl;
    protected RelativeLayout mDeviceControlRl;
    protected ScrollView mScrollView;
    protected ImageView mErrorLine;
    protected LinearLayout mOfflineLl;
    protected LinearLayout mOfflineErrorLl;
    protected LinearLayout mPowerOffLl;
    protected TextView mOfflineErrorTv;
    protected TextView mOfflineResolve;
    protected RelativeLayout mfilterLayoutForUnAuth;
    protected TextView mPowerOffTv;
    protected Drawable mPowerGreyDb;
    protected Drawable mPowerLightDb;
    protected final int TEXT_CLICKABLE_SIZE = 15;
    protected int mDeviceId;

    //umeng
    protected String mDeviceProductName = "";
    protected boolean isControlling = false;
    protected int ENLENGTH = 21;
    protected int CNLENGTH = 4;
    protected int DEFAULTINDEX = -1;
    protected ImageView mErrortextIv;

    protected int mFromType = ControlConstant.FROM_NORMAL_CONTROL;


    protected void initView(View view, int stringId) {
        initStatusBar(view);
        initListHeadView(stringId);
        mDeviceNameTv = (TextView) view.findViewById(R.id.title_textview_id);
        mTitleIv = (ImageView) view.findViewById(R.id.all_device_select_iv);
        mTitleIv.setImageResource(R.drawable.ic_help);
        mTitleIv.setVisibility(View.VISIBLE);
        mDeviceControlRl = (RelativeLayout) view.findViewById(R.id.device_control_mode_layout);
        mBackLayout = (FrameLayout) view.findViewById(R.id.enroll_back_layout);
        mErrortextTV = (TextView) view.findViewById(R.id.device_alert_aralm_tv);
        mErrortextIv = (ImageView) view.findViewById(R.id.device_error_alarm_iv1);
        mErrorTitleTv = (TextView) view.findViewById(R.id.device_alert_tv);
        mErrorLine = (ImageView) view.findViewById(R.id.error_line);
        mErrorIv = (ImageView) view.findViewById(R.id.device_alert_iv);
        mOfflineLl = (LinearLayout) view.findViewById(R.id.device_offline_ll);
        mOfflineErrorLl = (LinearLayout) view.findViewById(R.id.device_offline_error_ll);
        mPowerOffLl = (LinearLayout) view.findViewById(R.id.device_power_off_layout);
        mOfflineErrorTv = (TextView) view.findViewById(R.id.device_offline_error);
        mOfflineResolve = (TextView) view.findViewById(R.id.offline_resolve);
        mPowerOffTv = (TextView) view.findViewById(R.id.device_control_power_off_tv);
        mfilterLayoutForUnAuth = (RelativeLayout) view.findViewById(R.id.filter_title_layout_for_unAuth);
        mDeviceErrorLl = (LinearLayout) view.findViewById(R.id.device_error_layout);
        mScrollView = (ScrollView) view.findViewById(R.id.aqua_scrollview);
        disableAutoScrollToBottom();
        mFilterListView = (GroupContolListView) view.findViewById(R.id.device_filter_lv);
        mFilterListView.addHeaderView(headView, null, false);

        mModeOnTextColor = getFragmentActivity().getResources().getColorStateList(R.color.auth_cancel_text_color);
        mModeOffTextColor = getFragmentActivity().getResources().getColorStateList(R.color.text_common);
        mPowerGreyDb = getResources().getDrawable(R.drawable.device_poweroff_btn);
        mPowerLightDb = getResources().getDrawable(R.drawable.device_poweron_btn);
        mPowerGreyDb.setBounds(0, 0, mPowerGreyDb.getMinimumWidth(), mPowerGreyDb.getMinimumHeight());
        mPowerLightDb.setBounds(0, 0, mPowerLightDb.getMinimumWidth(), mPowerLightDb.getMinimumHeight());
        alphaOffAnimation = AnimationUtils.loadAnimation(getFragmentActivity(), R.anim.control_alpha);
        initDragDownManager(view, R.id.layout_root_rl);
    }

    protected void initListHeadView(int stringId) {
        headView = LayoutInflater.from(mParentActivity).inflate(R.layout.filter_listview_item_header, null);
        ((TextView) headView.findViewById(R.id.filter_header_name_tv)).setText(stringId);
        RelativeLayout listViewHeadRl = (RelativeLayout) headView.findViewById(R.id.layout_header);
        ListView.LayoutParams params = new ListView.LayoutParams
                (ListView.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(HEADHEIGHT));

        listViewHeadRl.setLayoutParams(params);
    }

    protected void initListener() {
        mBackLayout.setOnClickListener(this);
        mPowerOffTv.setOnClickListener(this);
        mTitleIv.setOnClickListener(this);

    }

    public void setControlType(int controlType) {
        mFromType = controlType;
    }


    protected void initControlUIManager(HomeDevice homeDevice, int controlType) {
        mFromType = controlType;
        mControlUIManager = ((DeviceControlActivity) mParentActivity).getControlUIManager();
//        mControlUIManager.createFilters(homeDevice, mFilters);
        initManagerCallBack(mControlUIManager);
    }

    protected void initManagerCallBack(ControlUIBaseManager controlUIManager) {
        controlUIManager.setErrorCallback(mErrorCallBack);
        controlUIManager.setSuccessCallback(mSuccessCallback);
    }

    protected IControlManager.SuccessCallback mSuccessCallback = new IControlManager.SuccessCallback() {
        @Override
        public void onSuccess(ResponseResult responseResult) {
            dealSuccessCallBack(responseResult);
        }
    };

    protected IControlManager.ErrorCallback mErrorCallBack = new IControlManager.ErrorCallback() {
        @Override
        public void onError(ResponseResult responseResult, int id) {
            dealErrorCallBack(responseResult, id);
        }
    };

    /*
        group control update group name need overide
     */
    protected void dealErrorCallBack(ResponseResult responseResult, int id) {
        super.dealErrorCallBack(responseResult, id);
        errorHandle(responseResult, getString(id));
    }

    public void registerRunStatusChangedReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(HPlusConstants.SHORTTIME_REFRESH_END_ACTION);
        intentFilter.addAction(HPlusConstants.DEVICE_CONTROL_BROADCAST_ACTION_STOP_FLASHINGTASK);
        mRunStatusChangedReceiver = new RunStatusChangedReceiver();
        mParentActivity.registerReceiver(mRunStatusChangedReceiver, intentFilter);
    }

    public void unRegisterRunStatusChangedReceiver() {
        if (mRunStatusChangedReceiver != null) {
            mParentActivity.unregisterReceiver(mRunStatusChangedReceiver);
        }
    }


    private class RunStatusChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //列表不处于编辑状态并且当前不处于操作状态（由是否LoadingProgressDialog显示来判断）才自动刷新
            if ((mDialog == null || !mDialog.isShowing())) {
                String action = intent.getAction();
                if (HPlusConstants.SHORTTIME_REFRESH_END_ACTION.equals(action) || HPlusConstants.TRY_DEMO_VALUE_CHANGE.equals(action)) {
                    LogUtil.log(LogUtil.LogLevel.INFO, "AquaTouchFragment", "SHORTTIME_REFRESH_END_ACTION----");
                    dealWithBroadCast();
                } else if (HPlusConstants.DEVICE_CONTROL_BROADCAST_ACTION_STOP_FLASHINGTASK.equals(action)) {
                    LogUtil.log(LogUtil.LogLevel.INFO, "AquaTouchFragment", "DEVICE_CONTROL_BROADCAST_ACTION_STOP_FLASHINGTASK----");
                    isControlling = false;
                    dealWithBroadCast();
                }
            }
        }
    }

    protected void initAdapter() {
        mDeviceFilterAdapter = new DeviceFilterAdapter(mParentActivity, mFilters);
        mFilterListView.setAdapter(mDeviceFilterAdapter);
        initItemCallBack();
    }

    protected void initItemCallBack() {

        mFilterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.log(LogUtil.LogLevel.INFO, "FilterItemView", "Position: " + position);
            }
        });
        // buy filter
        mDeviceFilterAdapter.setmBuyFilterCallBack(new DeviceFilterAdapter.BuyFilterCallBack() {

            @Override
            public void callback(Filter filter) {
                Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(filter.getPurchaseUrl()));
                startActivity(it);
            }
        });
    }

    /**
     * 需要子类进行重写
     */
    public Bundle getmLatestRunStatus() {
        return null;
    }

    /**
     * 需要子类进行重写
     */
    protected void dealWithBroadCast() {

    }

    @Override
    public void doRefreshUIOpr() {
        doRefreshUI();
    }

    protected void doRefreshUI() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterRunStatusChangedReceiver();
        resetManagerCallBack();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.enroll_back_layout) {
            ((DeviceControlActivity) mParentActivity).setQuickActionResult();
        } else if (v.getId() == R.id.all_device_select_iv) {
            Intent intent = new Intent(mParentActivity, DeviceControlHelpActivity.class);
            intent.putExtra(DeviceControlActivity.ARG_FROM_TYPE, mFromType);
            intent.putExtra(DeviceControlHelpActivity.HELP_PARAMETER, mCurrentDevice.getDeviceInfo());
            startActivity(intent);
            mParentActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }
    }

    protected void canShowFilter() {
        if (AppManager.getLocalProtocol().getRole().canShowFilter(mCurrentDevice.getDeviceInfo())) {
            mFilterListView.setVisibility(View.VISIBLE);
            mfilterLayoutForUnAuth.setVisibility(View.GONE);
        } else {
            //授权，但如果是offlien 也不显示提示语
            if (mCurrentDevice.getiDeviceStatusFeature().isOffline()) {
                mfilterLayoutForUnAuth.setVisibility(View.GONE);
            } else {
                mfilterLayoutForUnAuth.setVisibility(View.VISIBLE);
            }
            mFilterListView.setVisibility(View.GONE);
        }
    }

    protected void showNormalOrErrorLayout(boolean isNormal) {
        if (isNormal) {
            canShowFilter();
            mDeviceErrorLl.setVisibility(View.GONE);

            int visible = mCurrentDevice.getControlFeature().isCanControlable() ? View.VISIBLE : View.GONE;
            mDeviceControlRl.setVisibility(visible);

            mOfflineErrorLl.setVisibility(View.GONE);
            mPowerOffLl.setVisibility(View.GONE);
            mErrorLine.setVisibility(View.GONE);
            mErrortextTV.setTextColor(mParentActivity.getResources().getColor(R.color.text_common));
            //offline error
            if (mCurrentDevice.getiDeviceStatusFeature().isOffline()) {
                mDeviceErrorLl.setVisibility(View.VISIBLE);
                mDeviceControlRl.setVisibility(View.GONE);
                mFilterListView.setVisibility(View.GONE);
                mErrortextTV.setVisibility(View.GONE);
                mErrortextIv.setVisibility(View.GONE);
                mOfflineLl.setVisibility(View.VISIBLE);
                mErrorIv.setImageResource(R.drawable.device_control_offline);
                mErrorTitleTv.setText(R.string.device_control_offline);
                setUpWifi();
            }
            // remote control is disabled
            else if (mCurrentDevice.getControlFeature().isCanControlable() && !mCurrentDevice.getControlFeature().canRemoteControl()) {
                mDeviceErrorLl.setVisibility(View.VISIBLE);
                mDeviceControlRl.setVisibility(View.GONE);
                mErrorLine.setVisibility(View.VISIBLE);
                mOfflineLl.setVisibility(View.GONE);
                mErrorIv.setImageResource(R.drawable.device_control_remote);
                mErrorTitleTv.setText(R.string.device_control_not_control);
                mErrortextTV.setText(R.string.device_control_not_control_remind);
                mErrortextIv.setVisibility(View.GONE);
                //power off
            } else if (mCurrentDevice instanceof AirTouchDeviceObject) {
                AirtouchRunStatus airtouchRunStatus = ((AirTouchDeviceObject) mCurrentDevice).getAirtouchDeviceRunStatus();
                if (airtouchRunStatus != null && airtouchRunStatus.getScenarioMode() == HPlusConstants.MODE_OFF_INT) {
                    mDeviceErrorLl.setVisibility(View.VISIBLE);
                    mDeviceControlRl.setVisibility(View.GONE);
                    mOfflineLl.setVisibility(View.GONE);
                    mPowerOffLl.setVisibility(View.VISIBLE);
                    mErrorLine.setVisibility(View.VISIBLE);
                    mErrorIv.setImageResource(R.drawable.device_control_power_off);
                    mErrorTitleTv.setText(R.string.device_control_power_off);
                    mErrortextTV.setText(R.string.device_control_power_off_remind);
                    mErrortextIv.setVisibility(View.GONE);
                    mPowerOffTv.setClickable(true);
                    mPowerOffTv.setTextColor(mModeOffTextColor);
                    mPowerOffTv.setCompoundDrawables(null, mPowerGreyDb, null, null);
                }
            }
            // offline and error
        } else if (mCurrentDevice.getiDeviceStatusFeature().isOffline()) {
            mOfflineErrorLl.setVisibility(View.VISIBLE);
            mDeviceErrorLl.setVisibility(View.VISIBLE);
            mDeviceControlRl.setVisibility(View.GONE);
            mFilterListView.setVisibility(View.GONE);
            mErrortextTV.setVisibility(View.GONE);
            mErrortextIv.setVisibility(View.GONE);
            mOfflineLl.setVisibility(View.VISIBLE);
            mPowerOffLl.setVisibility(View.GONE);
            mErrorLine.setVisibility(View.GONE);
            mErrorIv.setImageResource(R.drawable.device_control_offline);
            mErrorTitleTv.setText(R.string.device_control_offline);
            setUpWifi();
            setmErrortextTV(mOfflineErrorTv);
            //error
        } else {
            mDeviceErrorLl.setVisibility(View.VISIBLE);
            mDeviceControlRl.setVisibility(View.GONE);
            mFilterListView.setVisibility(View.GONE);
            mPowerOffLl.setVisibility(View.GONE);
            mErrorLine.setVisibility(View.GONE);
            mErrorIv.setImageResource(R.drawable.device_control_error);
            mErrorTitleTv.setText(R.string.device_control_device_error);
            setmErrortextTV(mErrortextTV);
        }
    }


    private void setmErrortextTV(TextView errorTv) {
        errorTv.setTextColor(getResources().getColor(R.color.text_common));
        String errorAlarm = ((WaterDeviceObject) mCurrentDevice).getErrorAlarm();
        int index = errorAlarm.indexOf(getString(R.string.contact_customer_care));
        if (index != DEFAULTINDEX) {
            initClickView(errorTv, errorAlarm, index, index + CNLENGTH, index, index + ENLENGTH, new ClickOperator() {
                @Override
                public void dealClick() {
                    //call customer service
                    mHPlusPermission.checkAndRequestPermission(Permission.PermissionCodes.CALL_PHONE_REQUEST_CODE, mParentActivity);
                }
            });
        } else {
            errorTv.setText(((WaterDeviceObject) mCurrentDevice).getErrorAlarm());
        }
    }

    private void setUpWifi() {
        initClickView(mOfflineResolve, getString(R.string.device_control_offline_remind_step4), 30, 39, 79, 96, new ClickOperator() {
            @Override
            public void dealClick() {
                if (((DeviceControlActivity) mParentActivity).getmUserLocation().isIsLocationOwner()) {
                    intentStartActivity(EnrollScanActivity.class);
                } else {
                    MessageBox.createSimpleDialog(mParentActivity, null,
                            getString(R.string.offline_device_already_registered_authorize), getString(R.string.ok),
                            null);

                }

            }
        });
    }

    private void initClickView(TextView view, String str, int chineseStart, int chineseEnd, int englishStart, int englishEnd, final ClickOperator clickOperator) {
        SpannableString ssTitle = new SpannableString(str);
        view.setMovementMethod(LinkMovementMethod.getInstance());

        ClickableSpan clickableSpan = new ClickableSpan() {

            @Override
            public void updateDrawState(TextPaint ds) {
            }

            @Override
            public void onClick(View widget) {
                clickOperator.dealClick();
            }
        };

        if (AppConfig.shareInstance().getLanguage().equals(AppConfig.LANGUAGE_ZH)) {
            ssTitle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.enroll_blue2)), chineseStart, chineseEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
            ssTitle.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), chineseStart, chineseEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体
            ssTitle.setSpan(clickableSpan, chineseStart, chineseEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssTitle.setSpan(new RelativeSizeSpan(1.2f), chineseStart, chineseEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //2.0f表示默认字体大小的两倍
        } else {
            ssTitle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.enroll_blue2)), englishStart, englishEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
            ssTitle.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), englishStart, englishEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体
            ssTitle.setSpan(new RelativeSizeSpan(1.2f), englishStart, englishEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //2.0f表示默认字体大小的两倍
            ssTitle.setSpan(clickableSpan, englishStart, englishEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        view.setText(ssTitle);
    }

    /**
     * 当界面销毁的时候
     */
    protected void resetManagerCallBack() {
        mControlUIManager.setErrorCallback(null);
        mControlUIManager.setSuccessCallback(null);
    }

    interface ClickOperator {
        public void dealClick();
    }

    protected void startFlick(TextView view) {
        if (null == view) {
            return;
        }
        Animation alphaAnimation = new AlphaAnimation(1, 0.2f);

        alphaAnimation.setDuration(800);

        alphaAnimation.setInterpolator(new LinearInterpolator());

        alphaAnimation.setRepeatCount(Animation.INFINITE);

        alphaAnimation.setRepeatMode(Animation.REVERSE);

        view.startAnimation(alphaAnimation);
        view.setClickable(false);
        view.setTextColor(mModeOnTextColor);
        view.setCompoundDrawables(null, mPowerLightDb, null, null);
    }

    /**
     * 取消View闪烁效果
     */

    protected void stopFlick(View view) {
        if (null == view) {
            return;
        }
        view.clearAnimation();
        view.setClickable(true);
    }

    /**
     * 让scrollview获取到焦点，这样子view就不会获取到焦点，就不会自动滑动到底部
     */
    protected void disableAutoScrollToBottom() {
        mScrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        mScrollView.setFocusable(true);
        mScrollView.setFocusableInTouchMode(true);
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
    }

}
