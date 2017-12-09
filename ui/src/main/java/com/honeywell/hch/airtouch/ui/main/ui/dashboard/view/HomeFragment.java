package com.honeywell.hch.airtouch.ui.main.ui.dashboard.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseRequestFragment;
import com.honeywell.hch.airtouch.ui.control.manager.device.ControlBaseManager;
import com.honeywell.hch.airtouch.ui.emotion.ui.controller.EmotionActivity;
import com.honeywell.hch.airtouch.ui.emotion.ui.controller.EmotionActivity_new;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.beforeplay.EnrollScanActivity;
import com.honeywell.hch.airtouch.ui.main.manager.common.HomeControlManager;
import com.honeywell.hch.airtouch.ui.main.ui.common.MainActivity;
import com.honeywell.hch.airtouch.ui.main.ui.common.MainBaseActivity;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.presenter.HomeFramentPresenter;
import com.honeywell.hch.airtouch.ui.quickaction.manager.QuickActionManager;
import com.honeywell.hch.airtouch.ui.quickaction.ui.QuickActionActivity;
import com.honeywell.hch.airtouch.ui.schedule.controller.ArriveHomeActivity;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoConstant;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoHomeControlManager;
import com.honeywell.hch.airtouch.ui.trydemo.ui.TryDemoMainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nan.liu on 2/13/15.
 */
public class HomeFragment extends BaseRequestFragment implements IHomeFragmentView {

    private static final int INTERVAL = 1000;
    private long mClickTime = 0;
    private static final int AT_HOME_INDEX = 0;
    private static final int SLEEP_INDEX = 1;
    private static final int AWAKE_INDEX = 2;
    private static final int AWAY_INDEX = 3;


    private View mHomeCellView = null;
    private HomeFramentPresenter mHomePresenter;
    private RelativeLayout mHasDeviceView;
    private RelativeLayout mNoDeviceView;

    private ImageView mAddDeviceIcon;
    private TextView mNoDeviceTextView;

    //-------top view -----------------
    private ImageView mTopBackground;
    private TextView mTopBottomMsgTipTextView;
    private ImageView mTopStatusIcon;


    private TextView mNoDeviceTopTextView;

    //----------second view-------
    private HomeImageTextView mDeviceClassifyView;
    private List<HomeItemTextItemView> mHomeItemTextItemViewList = new ArrayList<>();
    private RelativeLayout mEmotionIconLayout;
    private TextView mUnsupportDeviceTextView;

    //---------control view -----------
    private HomeImageTextView mHomeControlView;
    private RelativeLayout mScheduleIconLayout;
    private TextView mAuthorizationCtrlTextview;

    private ImageView mArriveHomeImage;

    private boolean isHasInit = false;

    private ImageView mEmotionIv;
    private ControlStausReceiver mBroadcastReceiver;

    private ObjectAnimator mControlAnimation;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param locationData
     * @return A new instance of fragment HomeCellFragment.
     */
    public static HomeFragment newInstance(UserLocationData locationData, int index, Activity activity) {
        HomeFragment fragment = new HomeFragment();
        fragment.initPresenter(locationData, index, activity);
        return fragment;
    }

    private void initPresenter(UserLocationData locationData, int index, Activity activity) {
        mParentActivity = activity;
        //不能每次刷新的时候都new HomeControlManager 已经callback，否则会有对象不一致，导致获取到的数据都是错误的
        if (mHomePresenter == null) {
            ControlBaseManager controlBaseManager = mParentActivity instanceof MainActivity ? new HomeControlManager() : new TryDemoHomeControlManager();
            mHomePresenter = new HomeFramentPresenter(this, locationData, index, controlBaseManager);
        } else {
            mHomePresenter.resetHomeFragmentPresenter(this, locationData, index);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadCast();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mHomeCellView == null) {
            mHomeCellView = inflater.inflate(R.layout.fragment_homecell_new, container, false);

            initView();
            initHomeControlViewCount();
        }
        initDragDownManager(((MainBaseActivity) mParentActivity).getTopView(), ((MainBaseActivity) mParentActivity).getTopViewId());
        isHasInit = true;
        mHomePresenter.initHomeFragment();
        return mHomeCellView;
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isHasInit = false;
        unRegisterControlReceiver();

    }

    @Override
    public void setTopViewBackground(boolean isWorse, boolean isUnSupport) {
        int resourceId = isWorse || isUnSupport ? R.drawable.home_top_yellow_bg : R.drawable.home_top_blue_bg;
        mTopBackground.setImageResource(resourceId);

        int statusIcon = R.drawable.normal_big_round_icon;
        if (isWorse) {
            statusIcon = isWorse ? R.drawable.abnormal_big_round_icon : R.drawable.normal_big_round_icon;
        } else if (isUnSupport) {
            statusIcon = R.drawable.unsupported_big_circle;
        }
        mTopStatusIcon.setImageResource(statusIcon);

        mNoDeviceView.setVisibility(View.GONE);
        mHasDeviceView.setVisibility(View.VISIBLE);
        mTopBottomMsgTipTextView.setVisibility(View.VISIBLE);
        mTopStatusIcon.setVisibility(View.VISIBLE);
        mNoDeviceTopTextView.setVisibility(View.GONE);
    }

    @Override
    public void setNetWorkErrorTopViewBackground() {
        mTopBackground.setImageResource(R.drawable.nerwork_error_bg);
        mTopStatusIcon.setImageResource(R.drawable.network_error_icon);

        mTopBottomMsgTipTextView.setVisibility(View.VISIBLE);
        mTopStatusIcon.setVisibility(View.VISIBLE);
        mNoDeviceTopTextView.setVisibility(View.GONE);
    }

    @Override
    public void setNetWorkErrorNoData() {
        mNoDeviceView.setVisibility(View.VISIBLE);
        mHasDeviceView.setVisibility(View.GONE);
        mAddDeviceIcon.setVisibility(View.GONE);
        mNoDeviceTextView.setText(AppManager.getInstance().getApplication().getString(R.string.all_device_loading_failed));
    }


    @Override
    public void setNoDeviceView() {
        mTopBackground.setImageResource(R.drawable.homepage_black_bg);
        mNoDeviceView.setVisibility(View.VISIBLE);
        mHasDeviceView.setVisibility(View.GONE);

        mAddDeviceIcon.setVisibility(View.VISIBLE);
        mNoDeviceTextView.setText(AppManager.getInstance().getApplication().getString(R.string.tap_add_device_msg));

        mTopBottomMsgTipTextView.setVisibility(View.GONE);
        mTopStatusIcon.setVisibility(View.GONE);
        mNoDeviceTopTextView.setVisibility(View.VISIBLE);

    }

    /**
     * 设置arrivehome icon的显示，只有在有空净设备的家里，才会显示arrive home的图标
     *
     * @param visible
     */
    public void setArriveHomeIconVisible(int visible) {
        mArriveHomeImage.setVisibility(visible);
    }


    @Override
    public void setTopViewTip(String tip) {
        mTopBottomMsgTipTextView.setText(tip);
        mTopBottomMsgTipTextView.invalidate();
    }


    @Override
    public void setDefaultHomeIcon(boolean isDefault, boolean isSelfHome) {

        int ctrlViewVisible = isSelfHome ? View.VISIBLE : View.GONE;
        mScheduleIconLayout.setVisibility(ctrlViewVisible);
        mHomeControlView.setVisibility(ctrlViewVisible);
        mAuthorizationCtrlTextview.setText(getString(R.string.authorization_home_ctrl_str));
        mAuthorizationCtrlTextview.setVisibility(isSelfHome ? View.GONE : View.VISIBLE);

    }

    @Override
    public void setNoControllableDevice() {
        mScheduleIconLayout.setVisibility(View.GONE);
        mHomeControlView.setVisibility(View.GONE);
        mAuthorizationCtrlTextview.setText(getString(R.string.home_page_no_control_device));
        mAuthorizationCtrlTextview.setVisibility(View.VISIBLE);
    }

    @Override
    public void initDeviceClassifyView(Map<Integer, String> deviceIconAndStr, boolean isunSupportDevice) {
        mHomeItemTextItemViewList.clear();

        if (isunSupportDevice) {
            mDeviceClassifyView.setVisibility(View.GONE);
            mEmotionIconLayout.setVisibility(View.GONE);
            mUnsupportDeviceTextView.setVisibility(View.VISIBLE);
        } else {
            mDeviceClassifyView.setVisibility(View.VISIBLE);
            mEmotionIconLayout.setVisibility(View.VISIBLE);
            mUnsupportDeviceTextView.setVisibility(View.GONE);
            for (final Integer resouceId : deviceIconAndStr.keySet()) {
                mHomeItemTextItemViewList.add(mDeviceClassifyView.newHomeImageTextViewItem(resouceId, deviceIconAndStr.get(resouceId), false, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mParentActivity, QuickActionActivity.class);
                        if (resouceId == R.drawable.air) {
                            intent.putExtra(QuickActionManager.QUICKTYPE, QuickActionManager.QuickActionType.PM25);
                        } else if (resouceId == R.drawable.tvoc_icon) {
                            intent.putExtra(QuickActionManager.QUICKTYPE, QuickActionManager.QuickActionType.TVOC);
                        } else if (resouceId == R.drawable.water) {
                            intent.putExtra(QuickActionManager.QUICKTYPE, QuickActionManager.QuickActionType.WATERQUALITY);
                        }
                        intent.putExtra(TryDemoConstant.IS_FROM_TRY_DEMO, mParentActivity instanceof TryDemoMainActivity);

                        intent.putExtra(QuickActionManager.LOCATIONIDPARAMETER, mHomePresenter.getmUserLocationData().getLocationID());
                        startActivity(intent);
                        mParentActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                    }
                }));
            }
            mDeviceClassifyView.addHomeImageTextViewLayout(mHomeItemTextItemViewList);
        }

    }

    @Override
    public void setAirtouchPMValue(String pmValue, int textColor, boolean isNeedShowCleanNow) {

        //根据设计，第一个为空净的指标
        for (int i = 0; i < mHomeItemTextItemViewList.size(); i++) {
            if (mHomeItemTextItemViewList.get(i).getResourceId() == R.drawable.air) {
                setDeviceClassifyView(i, pmValue, textColor, isNeedShowCleanNow);
                break;
            }
        }
    }

    @Override
    public void setAirtouchTVOCValue(String tvocValue, int textColor, boolean isNeedShowCleanNow) {
        for (int i = 0; i < mHomeItemTextItemViewList.size(); i++) {
            if (mHomeItemTextItemViewList.get(i).getResourceId() == R.drawable.tvoc_icon) {
                setDeviceClassifyView(i, tvocValue, textColor, isNeedShowCleanNow);
                break;
            }
        }
    }

    @Override
    public void setWaterQuality(String level, int textColor, boolean isNeedShowDetail) {

        for (int i = 0; i < mHomeItemTextItemViewList.size(); i++) {
            if (mHomeItemTextItemViewList.get(i).getResourceId() == R.drawable.water) {
                setDeviceClassifyView(i, level, textColor, isNeedShowDetail);
                break;
            }
        }
    }

    @Override
    public void setControlFaile(int resultCode) {
        if (!NetWorkUtil.isNetworkAvailable(mParentActivity) || UserAllDataContainer.shareInstance().isHasNetWorkError()) {
            dealWithNetWorkResponse();
        } else {
            mDropDownAnimationManager.showDragDownLayout(mParentActivity.getString(R.string.home_control_timeout), true);
        }
        initScenarioModelView(mHomePresenter.getLocationScenarioIndexView());
        return;
    }

    @Override
    public void initScenarioModelView(int indexView) {
        stopAllViewFlick();
        if (indexView != DashBoadConstant.DEAFAULT_SCENARIO_MODEL) {
            setScenarioActvie(indexView);
        }
    }

    @Override
    public void setScenarioModelViewFlashing(int indexView) {
        startControlModelFlick(indexView);
    }


    public void refreshFragment(UserLocationData locationData, int index) {
        initPresenter(locationData, index, mParentActivity);
        if (isHasInit) {
            mHomePresenter.initHomeFragment();
        }
    }

    private void initView() {
        mHasDeviceView = (RelativeLayout) mHomeCellView.findViewById(R.id.ds_has_device_secondlay_view);
        mNoDeviceView = (RelativeLayout) mHomeCellView.findViewById(R.id.ds_no_device_secondlay_view);

        mDeviceClassifyView = (HomeImageTextView) mHomeCellView.findViewById(R.id.home_device_info);
        mHomeControlView = (HomeImageTextView) mHomeCellView.findViewById(R.id.home_control_view);

        mTopBackground = (ImageView) mHomeCellView.findViewById(R.id.top_bg_id);
        mTopBottomMsgTipTextView = (TextView) mHomeCellView.findViewById(R.id.home_status_id);
        mTopStatusIcon = (ImageView) mHomeCellView.findViewById(R.id.home_status_icon);
        mNoDeviceTopTextView = (TextView) mHomeCellView.findViewById(R.id.no_device_top_text);

        mScheduleIconLayout = (RelativeLayout) mHomeCellView.findViewById(R.id.schedule_icon_layout);
        mArriveHomeImage = (ImageView) mHomeCellView.findViewById(R.id.ctrl_icon);
        mArriveHomeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ArriveHomeActivity.class);
                intent.putExtra(TryDemoConstant.IS_FROM_TRY_DEMO, mParentActivity instanceof TryDemoMainActivity);
                intent.putExtra(HPlusConstants.LOCATION_ID, mHomePresenter.getLocationId());
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });
        mAuthorizationCtrlTextview = (TextView) mHomeCellView.findViewById(R.id.auth_ctrl_view_id);

        mNoDeviceTextView = (TextView) mHomeCellView.findViewById(R.id.loading_text_id);
        mAddDeviceIcon = (ImageView) mHomeCellView.findViewById(R.id.add_gray_icon);
        mAddDeviceIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentStartActivity(EnrollScanActivity.class);
            }
        });
        mEmotionIv = (ImageView) mHomeCellView.findViewById(R.id.emotion_icon);
        mEmotionIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EmotionActivity_new.class);
                intent.putExtra(TryDemoConstant.IS_FROM_TRY_DEMO, mParentActivity instanceof TryDemoMainActivity);

                intent.putExtra(QuickActionManager.LOCATIONIDPARAMETER, mHomePresenter.getmUserLocationData().getLocationID());
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

            }
        });

        mEmotionIconLayout = (RelativeLayout) mHomeCellView.findViewById(R.id.emotion_icon_layout);
        mUnsupportDeviceTextView = (TextView) mHomeCellView.findViewById(R.id.unsupport_text_id);
    }


    private void initHomeControlViewCount() {
        List<HomeItemTextItemView> homeItemTextItemViewList = new ArrayList<>();
        homeItemTextItemViewList.add(mHomeControlView.newHomeImageTextViewItem(R.drawable.gohome_inactive, mParentActivity.getString(R.string.group_control_home), true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canClcikable()) {
                    mHomePresenter.controlAtHomeModel();
                    startControlModelFlick(AT_HOME_INDEX);
                }
            }
        }));

        homeItemTextItemViewList.add(mHomeControlView.newHomeImageTextViewItem(R.drawable.home_sleep_inactive, mParentActivity.getString(R.string.group_control_sleep), true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canClcikable()) {
                    mHomePresenter.controlSleepModel();
                    startControlModelFlick(SLEEP_INDEX);
                }
            }
        }));

        homeItemTextItemViewList.add(mHomeControlView.newHomeImageTextViewItem(R.drawable.home_awake_inactive, mParentActivity.getString(R.string.home_ctrl_awake), true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canClcikable()) {
                    mHomePresenter.controlAwakeModel();
                    startControlModelFlick(AWAKE_INDEX);
                }
            }
        }));

        homeItemTextItemViewList.add(mHomeControlView.newHomeImageTextViewItem(R.drawable.away_inactive, mParentActivity.getString(R.string.group_control_away), true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canClcikable()) {
                    mHomePresenter.controlAwayModel();
                    startControlModelFlick(AWAY_INDEX);
                }
            }
        }));
        mHomeControlView.addHomeImageTextViewLayout(homeItemTextItemViewList);
    }


    private void setDeviceClassifyView(int index, String value, int textColor, boolean isNeedCleanBtn) {
        mHomeItemTextItemViewList.get(index).setValueTextString(String.valueOf(value));
        mHomeItemTextItemViewList.get(index).setValueTextColor(textColor);
        mHomeItemTextItemViewList.get(index).setDealBadTextViewVisible(isNeedCleanBtn);
    }

    private boolean canClcikable() {
        if (System.currentTimeMillis() - mClickTime > INTERVAL) {
            mClickTime = System.currentTimeMillis();
            return true;
        }
        mClickTime = System.currentTimeMillis();
        return false;
    }


    private void startControlModelFlick(int index) {
        stopAllViewFlick();
        setScenarioActvie(index);
        if (mHomeControlView != null && index < mHomeControlView.getTopViewChildViewCount()) {
            mControlAnimation = ObjectAnimator.ofFloat(mHomeControlView.getHomeItemTextView(index), "alpha", 1, 0.2f);
            mControlAnimation.setInterpolator(new LinearInterpolator());
            mControlAnimation.setRepeatCount(Animation.INFINITE);
            mControlAnimation.setRepeatMode(ValueAnimator.REVERSE);
            mControlAnimation.setDuration(800);
            mControlAnimation.start();
            mHomeControlView.getHomeItemTextView(index).setClickable(false);
        }
    }

    private void setScenarioActvie(int index) {
        (mHomeControlView.getHomeItemTextView(index)).setImageSrc(DashBoadConstant.HOME_SCENARIO_ACTVIE_SRC[index], mParentActivity.getResources().getColor(DashBoadConstant.HOME_SCENARIO_ACTVIE_TEXTCOLOR[index]));
    }

    private void setScenarioInActvie(int index) {
        (mHomeControlView.getHomeItemTextView(index)).setImageSrc(DashBoadConstant.HOME_SCENARIO_INACTVIE_SRC[index], mParentActivity.getResources().getColor(R.color.group_edit_text));
    }


    /**
     * 取消View闪烁效果
     */

    private void stopAllViewFlick() {

        if (mHomeControlView != null && mHomeControlView.getTopViewChildViewCount() > 0) {
            for (int i = 0; i < mHomeControlView.getTopViewChildViewCount(); i++) {
                setScenarioInActvie(i);
                if (mControlAnimation != null) {
                    mControlAnimation.removeAllListeners();
                    mControlAnimation.cancel();
                    mControlAnimation.end();
                }
                mHomeControlView.getHomeItemTextView(i).clearAnimation();
                ObjectAnimator test = ObjectAnimator.ofFloat(mHomeControlView.getHomeItemTextView(i), "alpha", 1.0f, 1.0f);
                test.setDuration(10);
                test.start();
                mHomeControlView.getHomeItemTextView(i).setClickable(true);
            }
        }
    }

    private void registerBroadCast() {
        mBroadcastReceiver = new ControlStausReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(HPlusConstants.HOME_CONTROL_STOP_FLASHINGTASK);
        mParentActivity.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void unRegisterControlReceiver() {
        if (mBroadcastReceiver != null) {
            mParentActivity.unregisterReceiver(mBroadcastReceiver);
        }
    }

    private class ControlStausReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int locationId = intent.getIntExtra(HPlusConstants.LOCAL_LOCATION_ID, 0);
            if (isHasInit && locationId != 0 && mHomePresenter != null && locationId == mHomePresenter.getLocationId()) {
                int flag = intent.getIntExtra(DashBoadConstant.ARG_RESULT_CODE, 0);
                if (flag == HPlusConstants.COMM_TASK_ALL_FAILED) {
                    mDropDownAnimationManager.showDragDownLayout(mParentActivity.getString(R.string.home_control_all_failed), true);
                } else if (flag == HPlusConstants.COMM_TASK_PART_FAILED) {
                    mDropDownAnimationManager.showDragDownLayout(mParentActivity.getString(R.string.home_control_part_failed), true);
                }
                initScenarioModelView(mHomePresenter.getLocationScenarioIndexView());
            }

        }

    }

    protected void dealWithNetWorkResponse() {
        if (mTopStatusIcon != null) {
            startFlick(mTopStatusIcon);
        }
    }


    public ControlBaseManager getHomeControlManager() {
        if (mHomePresenter != null) {
            return mHomePresenter.getGroupManager();
        }
        return null;
    }


}