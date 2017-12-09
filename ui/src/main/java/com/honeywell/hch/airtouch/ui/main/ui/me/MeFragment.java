package com.honeywell.hch.airtouch.ui.main.ui.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.permission.Permission;
import com.honeywell.hch.airtouch.plateform.storage.TryDemoSharePreference;
import com.honeywell.hch.airtouch.plateform.storage.UpdateVersionPreference;
import com.honeywell.hch.airtouch.plateform.update.UpdateManager;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.authorize.ui.controller.list.AuthorizeListActivity;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseRequestFragment;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.debug.SwitchEnvActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.afterplay.EnrollSelectedLocationActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.beforeplay.EnrollScanActivity;
import com.honeywell.hch.airtouch.ui.homemanage.ui.controller.HomeManagementActivity;
import com.honeywell.hch.airtouch.ui.main.ui.me.feedback.FeedBackActivity;
import com.honeywell.hch.airtouch.ui.trydemo.ui.TryDemoMainActivity;


/**
 * Created by nan.liu on 2/13/15.
 */
public class MeFragment extends BaseRequestFragment implements View.OnClickListener {
    private View mTopView;

    private FrameLayout mMeLeftLy;
    private TextView mMeTitleTv;
    private TextView mCustomerTv;
    private LinearLayout mProfileLl;
    private LinearLayout mHomeManagerLl;
    private LinearLayout mShareDeviceLl;
    private LinearLayout mAddDeviceLl;
    private LinearLayout mCustomerLl;
    private LinearLayout mAboutLl;
    private LinearLayout mTestLl;
    private LinearLayout mTryDemoLl;
    private LinearLayout mFeedBackLl;
    private LinearLayout mEnvLl;
    private RelativeLayout mVersionRl;
    private TextView mUpdateVersionTv;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeCellFragment.
     */
    public static MeFragment newInstance(Activity activity) {
        MeFragment fragment = new MeFragment();
        fragment.initActivity(activity);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mTopView == null) {
            mTopView = inflater.inflate(R.layout.fragment_me, container, false);
            initStatusBar(mTopView);
            init();
            initData();
            setVersionRl();
            initListener();
        }

        return mTopView;
    }

    private void init() {
        mMeLeftLy = (FrameLayout) mTopView.findViewById(R.id.enroll_back_layout);
        mMeTitleTv = (TextView) mTopView.findViewById(R.id.title_textview_id);
        mCustomerTv = (TextView) mTopView.findViewById(R.id.me_customer_care);
        mProfileLl = (LinearLayout) mTopView.findViewById(R.id.me_profile_ll);
        mHomeManagerLl = (LinearLayout) mTopView.findViewById(R.id.me_home_manager_ll);
        mShareDeviceLl = (LinearLayout) mTopView.findViewById(R.id.me_share_device_ll);
        mAddDeviceLl = (LinearLayout) mTopView.findViewById(R.id.me_add_device_ll);
        mCustomerLl = (LinearLayout) mTopView.findViewById(R.id.me_customer_care_ll);
        mAboutLl = (LinearLayout) mTopView.findViewById(R.id.me_about_ll);
        mTestLl = (LinearLayout) mTopView.findViewById(R.id.me_test_ll);
        mVersionRl = (RelativeLayout) mTopView.findViewById(R.id.me_about_version_rl);
        mUpdateVersionTv = (TextView) mTopView.findViewById(R.id.me_about_version_tv);
        mTryDemoLl = (LinearLayout) mTopView.findViewById(R.id.me_try_demo_ll);
        int visibleStatus = AppManager.getInstance().getLocalProtocol().isCanShowTryDemo() ? View.VISIBLE : View.GONE;
        mTryDemoLl.setVisibility(visibleStatus);
        mFeedBackLl = (LinearLayout) mTopView.findViewById(R.id.me_feedback_ll);
        mEnvLl = (LinearLayout) mTopView.findViewById(R.id.me_environment_switch);
        initDragDownManager(mTopView, R.id.me_root);

    }

    private void initData() {
        mMeLeftLy.setVisibility(View.GONE);
        mMeTitleTv.setText(getString(R.string.me_title));
        if (AppConfig.shareInstance().isIndiaAccount()) {
            mCustomerTv.setText(HPlusConstants.INDIA_CONTACT_PHONE_NUMBER);
        } else {
            mCustomerTv.setText(HPlusConstants.CONTACT_PHONE_NUMBER);
        }
        if (AppManager.isEnterPriseVersion()) {
            mHomeManagerLl.setVisibility(View.GONE);
            mShareDeviceLl.setVisibility(View.GONE);
            mFeedBackLl.setVisibility(View.GONE);
        }
        if (AppConfig.isDebugMode) {
            //设置网络切换
            mEnvLl.setVisibility(View.VISIBLE);
        }
    }

    public void setVersionRl() {
        if (UpdateVersionPreference.getOwnUpdate()) {
            mVersionRl.setVisibility(View.VISIBLE);
            mUpdateVersionTv.setText(UpdateManager.getInstance().getVersionCollector().getVersionName());
        } else {
            mVersionRl.setVisibility(View.GONE);
        }
    }

    private void initListener() {
        mProfileLl.setOnClickListener(this);
        mHomeManagerLl.setOnClickListener(this);
        mShareDeviceLl.setOnClickListener(this);
        mAddDeviceLl.setOnClickListener(this);
        mCustomerLl.setOnClickListener(this);
        mAboutLl.setOnClickListener(this);
        mTestLl.setOnClickListener(this);
        mTryDemoLl.setOnClickListener(this);
        mFeedBackLl.setOnClickListener(this);
        mEnvLl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.me_profile_ll) {
            intentStartActivity(ProfileActivity.class);
        } else if (v.getId() == R.id.me_home_manager_ll) {
            intentStartActivity(HomeManagementActivity.class);
        } else if (v.getId() == R.id.me_share_device_ll) {
            intentStartActivity(AuthorizeListActivity.class);
        } else if (v.getId() == R.id.me_add_device_ll) {
            intentStartActivity(EnrollScanActivity.class);
        } else if (v.getId() == R.id.me_customer_care_ll) {
            mHPlusPermission.checkAndRequestPermission(Permission.PermissionCodes.CALL_PHONE_REQUEST_CODE, mParentActivity);
        } else if (v.getId() == R.id.me_about_ll) {
            intentStartActivity(AboutActivity.class);
        } else if (v.getId() == R.id.me_try_demo_ll) {
            if (TryDemoSharePreference.isShownTryDemoEntrance()) {
                mAlertDialog = MessageBox.createSimpleDialog(mParentActivity, null, getString(R.string.try_demo_entrance),
                        getString(R.string.ok), rightButton);
            } else {
                intentStartActivity(TryDemoMainActivity.class);
            }
        } else if (v.getId() == R.id.me_feedback_ll) {
            Intent intent = new Intent(mParentActivity, FeedBackActivity.class);
            mParentActivity.startActivityForResult(intent, FeedBackActivity.SUBMIT_FEEDBACK_REQUEST_CODE);
            mParentActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        } else if (v.getId() == R.id.me_test_ll) {
            if (AppConfig.isDebugMode)
                intentStartActivity(EnrollSelectedLocationActivity.class);
        } else if (v.getId() == R.id.me_environment_switch) {
            intentStartActivity(SwitchEnvActivity.class);
        }
    }

    MessageBox.MyOnClick rightButton = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            TryDemoSharePreference.saveTryDemoEntrance(false);
            intentStartActivity(TryDemoMainActivity.class);
        }
    };

    public void dealWithAcitivityResult() {
        mDropDownAnimationManager.showDragDownLayout(getString(R.string.try_demo_feedback_submited), false);
    }

}