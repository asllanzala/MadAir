package com.honeywell.hch.airtouch.ui.main.ui.dashboard.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.library.util.StatusBarUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.storage.TryDemoSharePreference;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseRequestFragment;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.beforeplay.EnrollScanActivity;
import com.honeywell.hch.airtouch.ui.trydemo.ui.TryDemoAllDeviceActivity;
import com.honeywell.hch.airtouch.ui.trydemo.ui.TryDemoMainActivity;

/**
 * Created by nan.liu on 2/13/15.
 */
public class NoLocationHomeFragment extends BaseRequestFragment {


    private View mHomeCellView = null;

    private ImageView mTopImageView;

    private ImageView mHomeStatusIconImageView;


    private TextView mTopMsgTextView;

    private TextView mNoHomeTopTextView;

    private ImageView mAddDeviceImageIconView;

    private TextView mAddDeviceTip;

    private ImageView mLoadingImageView;
    private TextView mLoadingMsgTextView;
    private boolean isHasInit = false;
    private RelativeLayout mTryDemoLayout;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param
     * @return A new instance of fragment HomeCellFragment.
     */
    public static NoLocationHomeFragment newInstance(Activity activity) {
        NoLocationHomeFragment fragment = new NoLocationHomeFragment();
        fragment.initPresenter(activity);
        return fragment;
    }

    private void initPresenter(Activity activity) {
        mParentActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mHomeCellView == null) {
            mHomeCellView = inflater.inflate(R.layout.fragment_homecell_nolocation, container, false);
            StatusBarUtil.initMarginTopWithStatusBarHeight(mHomeCellView.findViewById(R.id.actionbar_tile_bg), mParentActivity);
            initView();
            initTryDemoLayout();
            isHasInit = true;
            initNoLocationView();
        }

        return mHomeCellView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isHasInit = false;
    }

    public void initNoLocationView() {
        if (NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication()) && !UserAllDataContainer.shareInstance().isHasNetWorkError() && isHasInit) {
            setTopView();
        } else if (isHasInit && !NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication())) {
            setNoNetWorkView();
        } else if (isHasInit && UserAllDataContainer.shareInstance().isHasNetWorkError()) {
            setNetWorkErrorView();
        }
        setSecondPartView();
    }

    private void initView() {
        mTopImageView = (ImageView) mHomeCellView.findViewById(R.id.top_bg_id);
        mHomeStatusIconImageView = (ImageView) mHomeCellView.findViewById(R.id.home_status_icon);
        mTopMsgTextView = (TextView) mHomeCellView.findViewById(R.id.home_status_id);
        mNoHomeTopTextView = (TextView) mHomeCellView.findViewById(R.id.no_device_top_text);
        mAddDeviceImageIconView = (ImageView) mHomeCellView.findViewById(R.id.add_gray_icon);
        mAddDeviceTip = (TextView) mHomeCellView.findViewById(R.id.loading_text_id);

        mLoadingImageView = (ImageView) mHomeCellView.findViewById(R.id.loading_img);
        mLoadingMsgTextView = (TextView) mHomeCellView.findViewById(R.id.cache_loading_msg_id);

        mAddDeviceImageIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentStartActivity(EnrollScanActivity.class);
            }
        });

        mTryDemoLayout = (RelativeLayout) mHomeCellView.findViewById(R.id.try_demo_layout);
        mTryDemoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TryDemoSharePreference.isShownTryDemoEntrance()) {
                    mAlertDialog = MessageBox.createSimpleDialog(mParentActivity, null, getString(R.string.try_demo_entrance),
                            getString(R.string.ok), rightButton);
                } else {
                    intentStartActivity(TryDemoMainActivity.class);
                }
            }
        });
    }

    //无网络或者无法链接服务器时要隐藏
    private void initTryDemoLayout() {
        if (AppManager.getInstance().getLocalProtocol().isCanShowTryDemo()) {
            mTryDemoLayout.setVisibility(View.VISIBLE);
        } else {
            mTryDemoLayout.setVisibility(View.GONE);
        }
    }

    MessageBox.MyOnClick rightButton = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            TryDemoSharePreference.saveTryDemoEntrance(false);
            intentStartActivity(TryDemoMainActivity.class);
        }
    };

    private void setTopView() {
        initTryDemoLayout();
        mTopImageView.setVisibility(View.VISIBLE);
        mNoHomeTopTextView.setVisibility(View.VISIBLE);
        mHomeStatusIconImageView.setVisibility(View.GONE);
        mTopMsgTextView.setVisibility(View.GONE);

        mTopImageView.setImageResource(R.drawable.homepage_black_bg);
        if (UserAllDataContainer.shareInstance().isLoadingNetworkSuccess()) {
            mNoHomeTopTextView.setText(AppManager.getInstance().getApplication().getString(R.string.authorize_no_device));
            mLoadingImageView.setVisibility(View.GONE);
            mLoadingMsgTextView.setVisibility(View.GONE);
        } else {
            mNoHomeTopTextView.setText(AppManager.getInstance().getApplication().getString(R.string.no_content_yet));
            setLoadingView();
        }
    }

    private void setLoadingView() {
        mLoadingImageView.setVisibility(View.VISIBLE);
        mLoadingMsgTextView.setVisibility(View.VISIBLE);
        if (UserAllDataContainer.shareInstance().isLoadCacheSuccessButRefreshFailed() ||
                UserAllDataContainer.shareInstance().isLoadDataFailed()) {
            mLoadingImageView.clearAnimation();
            mLoadingImageView.setImageResource(R.drawable.alert_yellow);
            mLoadingMsgTextView.setText(AppManager.getInstance().getApplication().getString(R.string.cache_loading_failed));
        } else {
            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                    mParentActivity, R.anim.loading_animation);
            LinearInterpolator lir = new LinearInterpolator();
            hyperspaceJumpAnimation.setInterpolator(lir);
            mLoadingImageView.startAnimation(hyperspaceJumpAnimation);
            mLoadingMsgTextView.setText(AppManager.getInstance().getApplication().getString(R.string.footview_loading));

        }
    }


    private void setSecondPartView() {
        if (isHasInit) {
            int visible = UserAllDataContainer.shareInstance().isLoadingNetworkSuccess() ? View.VISIBLE : View.GONE;
            mAddDeviceImageIconView.setVisibility(visible);
            mAddDeviceTip.setVisibility(visible);
        }

    }

    @Override
    public void setNoNetWorkView() {
        if (isHasInit) {
            initTopViewWithNetworkError();
            mTryDemoLayout.setVisibility(View.GONE);
            setTopViewTip(AppManager.getInstance().getApplication().getString(R.string.network_off_msg));
        }

    }

    @Override
    public void setNetWorkErrorView() {
        if (isHasInit) {
            initTopViewWithNetworkError();
            mTryDemoLayout.setVisibility(View.GONE);
            setTopViewTip(AppManager.getInstance().getApplication().getString(R.string.network_error_msg));
        }
    }

    private void initTopViewWithNetworkError() {
        mTopImageView.setVisibility(View.VISIBLE);
        mTopMsgTextView.setVisibility(View.VISIBLE);
        mTopImageView.setImageResource(R.drawable.nerwork_error_bg);

        mHomeStatusIconImageView.setVisibility(View.VISIBLE);
        mNoHomeTopTextView.setVisibility(View.GONE);

        mLoadingImageView.setVisibility(View.GONE);
        mLoadingMsgTextView.setVisibility(View.GONE);
    }

    private void setTopViewTip(String tip) {
        if (UserAllDataContainer.shareInstance().isLoadingNetworkSuccess()) {
            mTopMsgTextView.setText(tip);
        } else {
            mTopMsgTextView.setText(tip + "\n" + AppManager.getInstance().getApplication().getString(R.string.no_content_yet));

        }
    }


}