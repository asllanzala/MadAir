package com.honeywell.hch.airtouch.ui.main.ui.devices.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.library.util.StatusBarUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseRequestFragment;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.beforeplay.EnrollScanActivity;

/**
 * Created by Vincent on 20/7/16.
 */
public class NoLocationAllDeviceFragment  extends BaseRequestFragment {

    private View mParentView = null;

    private ImageView mLoadingImageView;
    private TextView mLoadingTextView;
    private LinearLayout mLoadingLinearLayout;
    private RelativeLayout mAddDeviceLayout;
    private ImageView mAddDeviceIcon;
    private TextView mDeviceTitleView;
    private RelativeLayout mNoContentView;

    private boolean isHasInit = false;

    public static NoLocationAllDeviceFragment newInstance( Activity activity) {
        NoLocationAllDeviceFragment fragment = new NoLocationAllDeviceFragment();
        fragment.initActivity(activity);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mParentView == null) {
            mParentView = inflater.inflate(R.layout.activity_alldevice_no_location, container, false);
            initView();
            isHasInit = true;
            initNoLocationView();

        }
        return mParentView;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isHasInit = false;
    }

    public void initNoLocationView(){
        if (NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication()) && !UserAllDataContainer.shareInstance().isHasNetWorkError() && isHasInit){
            setTitle();
        }else if (isHasInit && !NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication())){
            setNoNetWorkView();
        }else if (isHasInit && UserAllDataContainer.shareInstance().isHasNetWorkError()){
            setNetWorkErrorView();
        }
        setSecondLayout();
    }

    private void initView(){
        initStatusBar(mParentView);
        StatusBarUtil.changeStatusBarTextColor(mParentView.findViewById(R.id.root_view_id), View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mLoadingImageView = (ImageView)mParentView.findViewById(R.id.loading_img);
        mLoadingTextView = (TextView)mParentView.findViewById(R.id.cache_loading_msg_id);

        mLoadingLinearLayout = (LinearLayout)mParentView.findViewById(R.id.loading_cache_id);

        mAddDeviceLayout = (RelativeLayout)mParentView.findViewById(R.id.add_device_layout);
        mAddDeviceIcon = (ImageView)mParentView.findViewById(R.id.add_gray_icon);
        mAddDeviceIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentStartActivity(EnrollScanActivity.class);
            }
        });

        mDeviceTitleView = (TextView)mParentView.findViewById(R.id.device_title);
        mNoContentView = (RelativeLayout)mParentView.findViewById(R.id.no_content_id);
    }


    private void setTitle(){

        if (UserAllDataContainer.shareInstance().isLoadingNetworkSuccess()){
            mDeviceTitleView.setVisibility(View.VISIBLE);
            mLoadingLinearLayout.setVisibility(View.GONE);
        }else{
            mLoadingLinearLayout.setVisibility(View.VISIBLE);
            mDeviceTitleView.setVisibility(View.GONE);
            if (UserAllDataContainer.shareInstance().isLoadCacheSuccessButRefreshFailed() ||
                    UserAllDataContainer.shareInstance().isLoadDataFailed()) {
                mLoadingImageView.clearAnimation();
                mLoadingImageView.setImageResource(R.drawable.alert_yellow);
                mLoadingTextView.setText(AppManager.getInstance().getApplication().getString(R.string.cache_loading_failed));
            } else {
                Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                        mParentActivity, R.anim.loading_animation);
                LinearInterpolator lir = new LinearInterpolator();
                hyperspaceJumpAnimation.setInterpolator(lir);
                mLoadingImageView.startAnimation(hyperspaceJumpAnimation);
                mLoadingTextView.setText(AppManager.getInstance().getApplication().getString(R.string.footview_loading));
            }
        }
    }

    private void setSecondLayout(){
        if (isHasInit){
            int visible = UserAllDataContainer.shareInstance().isLoadingNetworkSuccess() ? View.VISIBLE : View.GONE;
            mAddDeviceLayout.setVisibility(visible);
            mNoContentView.setVisibility(visible ==  View.VISIBLE ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public void setNoNetWorkView() {
        super.setNoNetWorkView();
        hideLoadingView();
    }


    @Override
    public void setNetWorkErrorView() {
        super.setNetWorkErrorView();
        hideLoadingView();
    }

    private void hideLoadingView(){
        if(mLoadingLinearLayout != null){
            mDeviceTitleView.setVisibility(View.VISIBLE);
            mLoadingLinearLayout.setVisibility(View.GONE);
        }
    }

}
