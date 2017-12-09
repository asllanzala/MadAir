package com.honeywell.hch.airtouch.ui.emotion.ui.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StatusBarUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.permission.HPlusPermission;
import com.honeywell.hch.airtouch.plateform.permission.Permission;
import com.honeywell.hch.airtouch.plateform.permission.PermissionListener;
import com.honeywell.hch.airtouch.plateform.share.ShareUtility;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseFragmentActivity;
import com.honeywell.hch.airtouch.ui.common.ui.view.HplusNetworkErrorLayout;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.emotion.manager.EmotionPresenter;
import com.honeywell.hch.airtouch.ui.emotion.ui.adapter.EmotionFragmentAdapter;
import com.honeywell.hch.airtouch.ui.emotion.ui.view.EmotionFragment_new;
import com.honeywell.hch.airtouch.ui.emotion.ui.view.NoScrollViewPager;
import com.honeywell.hch.airtouch.ui.quickaction.manager.QuickActionManager;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 12/8/16.
 */
public class EmotionActivity_new extends BaseFragmentActivity implements PermissionListener {

    private final String TAG = "EmotionActivity";
    private NoScrollViewPager mPageVp;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private EmotionFragmentAdapter mEmotionFragmentAdapter;
    private FrameLayout mBackFl;
    private RelativeLayout mEmotionShareRl;
    private LinearLayout mTabOneLl, mTabTwoLl;
    //Tab显示内容TextView
    private TextView mOneTv, mTwoTv;
    //Tab的那个引导线
    private ImageView mTabLineIv;
    private final int LOCATIONIDDEFAULT = 0;
    private int mLocationId;
    private int PAGELIMIT = 0;
    private final int DEFAULTINDEX = 0;
    //ViewPager的当前选中页
    private int currentIndex;
    //屏幕的宽度
    private int screenWidth;
    private Bitmap mShareBitmap;
    private RelativeLayout mTitleRl;
    private final int ZERO = 0;
    private final int ONE = 1;
    private EmotionFragment_new mCurrentEmotionFragment;
    private RelativeLayout.LayoutParams mTabLineLp;
    private View mShareXmlView;
    private ImageView mShareCaptureView;
    private LinearLayout mShareContent;
    private ImageView mShareQrCodeIv;
    private String mCountry;

    private HplusNetworkErrorLayout mHplusNetworkErrorLayout;

    //emtoion的数据刷新时间是放在内存中
    private long mLastUpdateTime = HPlusConstants.DEFAULT_TIME;

    private UserLocationData mUserLocationData;
    private LinearLayout mEmotionTapLl;
    private HPlusPermission mHPlusPermission;
    private AlertDialog mPermissionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_new);
        initStatusBar();
        initView();
        initListener();
        initData();
        initTabLineWidth();
    }

    private void initView() {
        mHPlusPermission = new HPlusPermission(this);
        mTabOneLl = (LinearLayout) findViewById(R.id.emotion_tab_one_ll);
        mTabTwoLl = (LinearLayout) findViewById(R.id.emotion_tab_two_ll);
        mOneTv = (TextView) this.findViewById(R.id.emotion_one_tv);
        mTwoTv = (TextView) this.findViewById(R.id.emotion_two_tv);
        mTabLineIv = (ImageView) this.findViewById(R.id.id_tab_line_iv);
        mPageVp = (NoScrollViewPager) this.findViewById(R.id.emotion_view_pager);
        mBackFl = (FrameLayout) findViewById(R.id.enroll_back_layout);
        mEmotionShareRl = (RelativeLayout) findViewById(R.id.emotion_share_right_rl);
        mTitleRl = (RelativeLayout) findViewById(R.id.total_tile_id);
        mTabLineLp = (RelativeLayout.LayoutParams) mTabLineIv.getLayoutParams();
        mEmotionTapLl = (LinearLayout) findViewById(R.id.switch_tab_root_ll);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mShareXmlView = inflater.inflate(R.layout.emotional_share, null);
        mShareCaptureView = (ImageView) mShareXmlView.findViewById(R.id.share_img);
        mShareQrCodeIv = (ImageView) mShareXmlView.findViewById(R.id.emotion_share_qrcode_iv);
        mShareContent = (LinearLayout) findViewById(R.id.emotion_share_content);

        mCountry = UserInfoSharePreference.getCountryCode();
        if (StringUtil.isEmpty(mCountry) || mCountry.equals(HPlusConstants.CHINA_CODE)) {
            mShareQrCodeIv.setImageResource(R.drawable.hplus_qrcode_china);
        } else if (mCountry.equals(HPlusConstants.INDIA_CODE)) {
            mShareQrCodeIv.setImageResource(R.drawable.hplus_qrcode_india);
        }

        mHplusNetworkErrorLayout = (HplusNetworkErrorLayout) findViewById(R.id.network_error_layout);

    }

    public void setViewPagerCanScroll(boolean canScroll) {
        mPageVp.setCanScroll(canScroll);
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        StatusBarUtil.initMarginTopWithStatusBarHeight(findViewById(R.id.actionbar_tile_bg), this);
    }

    @Override
    public void dealWithNoNetWork() {
        mHplusNetworkErrorLayout.setVisibility(View.VISIBLE);
        mHplusNetworkErrorLayout.setErrorMsg(HplusNetworkErrorLayout.NETWORK_OFF);
    }

    @Override
    public void dealWithNetworkError() {
        mHplusNetworkErrorLayout.setVisibility(View.VISIBLE);
        mHplusNetworkErrorLayout.setErrorMsg(HplusNetworkErrorLayout.CONNECT_SERVER_ERROR);
    }

    @Override
    public void dealNetworkConnected() {
        mHplusNetworkErrorLayout.setVisibility(View.GONE);
    }

    private void initListener() {
        mBackFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backIntent();
            }
        });
        mEmotionShareRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHPlusPermission.checkAndRequestPermission(Permission.PermissionCodes.STORAGE_REQUEST_CODE, EmotionActivity_new.this);
            }
        });
    }

    private void initData() {
        mLocationId = getIntent().getIntExtra(QuickActionManager.LOCATIONIDPARAMETER, LOCATIONIDDEFAULT);
        boolean isFromTryDemo = getIntent().getBooleanExtra(TryDemoConstant.IS_FROM_TRY_DEMO, false);
        mUserLocationData = UserDataOperator.getUserLocationByID(mLocationId, UserAllDataContainer.shareInstance().getUserLocationDataList());
        if (isFromTryDemo) {
            mFragmentList.add(EmotionFragment_new.newInstance(mUserLocationData, this, 0, EmotionPresenter.AIR_TOUCH_TYPE, isFromTryDemo));
            mTabOneLl.setVisibility(View.VISIBLE);
            enableClickTabTwo();
            PAGELIMIT++;
            if (!AppConfig.shareInstance().isIndiaAccount()) {
                mFragmentList.add(EmotionFragment_new.newInstance(mUserLocationData, this, 1, EmotionPresenter.AQUA_TOUCH_TYPE, isFromTryDemo));
                mTabTwoLl.setVisibility(View.VISIBLE);
                PAGELIMIT++;
            }
        } else {
            if (mUserLocationData.isOwnAirTouchSeries()) {
                PAGELIMIT++;
                mFragmentList.add(EmotionFragment_new.newInstance(mUserLocationData, this, 0, EmotionPresenter.AIR_TOUCH_TYPE, isFromTryDemo));
                mTabOneLl.setVisibility(View.VISIBLE);
                enableClickTabTwo();
            }
            if (mUserLocationData.isOwnWaterSeries()) {
                PAGELIMIT++;
                mFragmentList.add(EmotionFragment_new.newInstance(mUserLocationData, this, 1, EmotionPresenter.AQUA_TOUCH_TYPE, isFromTryDemo));
                mTabTwoLl.setVisibility(View.VISIBLE);
            }
        }

        if (PAGELIMIT == 1) {
            mEmotionTapLl.setVisibility(View.INVISIBLE);
        }
        mEmotionFragmentAdapter = new EmotionFragmentAdapter(
                this.getSupportFragmentManager(), mFragmentList);
        mPageVp.setAdapter(mEmotionFragmentAdapter);
        mPageVp.setCurrentItem(DEFAULTINDEX);
        mPageVp.setOffscreenPageLimit(PAGELIMIT);
        mCurrentEmotionFragment = (EmotionFragment_new) mEmotionFragmentAdapter.getItem(DEFAULTINDEX);
        mPageVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }

            /**
             * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
             * offsetPixels:当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float offset,
                                       int offsetPixels) {

                setPageScrolled(mTabLineLp, position, offset);
            }

            @Override
            public void onPageSelected(int position) {
                resetTextView();
                setPageSelected(position);
            }
        });

    }

    private void setPageSelected(int position) {
        switch (position) {
            case 0:
                mOneTv.setTextColor(Color.WHITE);
                enableClickTabTwo();
                break;
            case 1:
                mTwoTv.setTextColor(Color.WHITE);
                enableClickTabOne();
                break;
        }
        currentIndex = position;
        mCurrentEmotionFragment = (EmotionFragment_new) mEmotionFragmentAdapter.getItem(currentIndex);
        isShowShareRl();
    }

    /**
     * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
     * 设置mTabLineIv的左边距 滑动场景：
     * 记3个页面,
     * 从左到右分别为0,1,2
     * 0->1; 1->2; 2->1; 1->0
     */
    private void setPageScrolled(RelativeLayout.LayoutParams lp, int position, float offset) {
        if (currentIndex == 0 && position == 0) {// 0->1
            lp.leftMargin = (int) (offset * (screenWidth * 1.0 / PAGELIMIT) + currentIndex
                    * (screenWidth / PAGELIMIT));
        } else if (currentIndex == 1 && position == 1) {// 1->2
            lp.leftMargin = (int) (offset * (screenWidth * 1.0 / PAGELIMIT) + currentIndex
                    * (screenWidth / PAGELIMIT));
        } else if (currentIndex == 2 && position == 2) {// 2->3
            lp.leftMargin = (int) (offset * (screenWidth * 1.0 / PAGELIMIT) + currentIndex
                    * (screenWidth / PAGELIMIT));
        } else if (currentIndex == 3 && position == 2) {// 3->2
            lp.leftMargin = (int) (-(1 - offset)
                    * (screenWidth * 1.0 / PAGELIMIT) + currentIndex
                    * (screenWidth / PAGELIMIT));
        } else if (currentIndex == 2 && position == 1) {// 2->1
            lp.leftMargin = (int) (-(1 - offset)
                    * (screenWidth * 1.0 / PAGELIMIT) + currentIndex
                    * (screenWidth / PAGELIMIT));
        } else if (currentIndex == 1 && position == 0) {// 1->0
            lp.leftMargin = (int) (-(1 - offset)
                    * (screenWidth * 1.0 / PAGELIMIT) + currentIndex
                    * (screenWidth / PAGELIMIT));
        }
        mTabLineIv.setLayoutParams(lp);
    }

    /**
     * 设置滑动条的宽度为屏幕的1/4(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mTabLineIv
                .getLayoutParams();
        lp.width = screenWidth / PAGELIMIT;
        mTabLineIv.setLayoutParams(lp);
    }

    /**
     * 重置颜色
     */
    private void resetTextView() {
        mOneTv.setTextColor(getResources().getColor(R.color.emotion_text_normal));
        mTwoTv.setTextColor(getResources().getColor(R.color.emotion_text_normal));
    }

    private Bitmap takeScreenShot() {
        mPageVp.setDrawingCacheEnabled(true);
        mPageVp.buildDrawingCache();
        Bitmap bitmap = mPageVp.getDrawingCache();

//        // 获取屏幕长和高
        int shareContentwidth = DensityUtil.getScreenWidth();
        int shareContentheight = mPageVp.getHeight();

        int screenwidth = DensityUtil.getScreenWidth();
        int screenheight = ShareUtility.getShareSceenHeight(this);
        if (ShareUtility.isNavigatorBarVisible(this)) {
            screenheight = screenheight + ShareUtility.getNavigateHeight(this);
        }
        Bitmap captureBitmap = Bitmap.createBitmap(bitmap, ZERO, ZERO, shareContentwidth, shareContentheight);
        mPageVp.destroyDrawingCache();


        RelativeLayout.LayoutParams params
                = new RelativeLayout.LayoutParams(mShareCaptureView.getLayoutParams());
        params.height = shareContentheight;
        params.width = shareContentwidth;
        mShareCaptureView.setLayoutParams(params);
        mShareCaptureView.setImageBitmap(captureBitmap);
//        //调用下面这个方法非常重要，如果没有调用这个方法，得到的bitmap为null
        mShareXmlView.measure(View.MeasureSpec.makeMeasureSpec(screenwidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(screenheight, View.MeasureSpec.EXACTLY));
//        //这个方法也非常重要，设置布局的尺寸和位置
        mShareXmlView.layout(0, 0, mShareXmlView.getMeasuredWidth(), mShareXmlView.getMeasuredHeight());

//
//        //获得绘图缓存中的Bitmap
        mShareXmlView.setDrawingCacheEnabled(true);
        mShareXmlView.buildDrawingCache();
        Bitmap mSocialShareBitmap = mShareXmlView.getDrawingCache();

        if (bitmap != null) {
            bitmap.recycle();
        }
        if (captureBitmap != null) {
            captureBitmap.recycle();
        }

        return mSocialShareBitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recycleShareBitmap();

    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "onResume:-----");
        recycleShareBitmap();
    }

    public void recycleShareBitmap() {
        mEmotionShareRl.setClickable(true);
        if (mShareBitmap != null && !mShareBitmap.isRecycled()) {
            mShareBitmap.recycle();
            mShareBitmap = null;
        }
        if (mShareXmlView != null) {
            mShareXmlView.destroyDrawingCache();
        }
    }

    public void doClick(View v) {
        if (v.getId() == R.id.emotion_tab_one_ll) {
            currentIndex = ZERO;
            enableClickTabTwo();
        } else if (v.getId() == R.id.emotion_tab_two_ll) {
            if (PAGELIMIT == ONE) {
                currentIndex = ZERO;
            } else {
                currentIndex = ONE;
            }
            enableClickTabOne();
        }
        mPageVp.setCurrentItem(currentIndex);
        mTabLineLp.leftMargin = (int) ((screenWidth * 1.0 / PAGELIMIT) + currentIndex
                * (screenWidth / PAGELIMIT));
        mTabLineIv.setLayoutParams(mTabLineLp);
    }

    public void isShowShareRl() {
        if (mCurrentEmotionFragment.isGetDataSuccess()) {
            mEmotionShareRl.setVisibility(View.VISIBLE);
        } else {
            mEmotionShareRl.setVisibility(View.GONE);
        }
    }

    private void enableClickTabOne() {
        mTabTwoLl.setClickable(false);
        mTabOneLl.setClickable(true);
    }

    private void enableClickTabTwo() {
        mTabTwoLl.setClickable(true);
        mTabOneLl.setClickable(false);
    }

    public long getLastUpdateTime() {
        return mLastUpdateTime;
    }

    public void setLastUpdateTime(long mLastUpdateTime) {
        this.mLastUpdateTime = mLastUpdateTime;
    }

    @Override
    public void onPermissionGranted(int permissionCode) {
        if (permissionCode == Permission.PermissionCodes.STORAGE_REQUEST_CODE) {
            doTheShareAction();
        }

    }

    @Override
    public void onPermissionNotGranted(String[] permission, int permissionCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(permission, permissionCode);
        }
    }

    @Override
    public void onPermissionDenied(int permissionCode) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case Permission.PermissionCodes.STORAGE_REQUEST_CODE:
                if (mHPlusPermission.verifyPermissions(grantResults)) {
                    doTheShareAction();
                } else {
                    noStoragePermission();
                }
                break;
        }
    }

    private void noStoragePermission() {
        mPermissionDialog = MessageBox.createTwoButtonDialog((Activity) mContext, null, getString(R.string.storage_permission),
                getString(R.string.cancel), leftButton, getString(R.string.go_to_setting), goToSetting);

    }

    private MessageBox.MyOnClick goToSetting = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            goToPermissionSetting();
        }
    };

    private void goToPermissionSetting() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(localIntent);
    }

    MessageBox.MyOnClick leftButton = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            if (mPermissionDialog != null) {
                mPermissionDialog.dismiss();
            }
        }
    };

    private void doTheShareAction() {
        mEmotionShareRl.setClickable(false);
        mShareBitmap = takeScreenShot();
        ShareUtility.getInstance(mContext).shareImage(mShareBitmap);
    }

}
