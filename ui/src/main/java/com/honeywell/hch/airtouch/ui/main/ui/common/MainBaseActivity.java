package com.honeywell.hch.airtouch.ui.main.ui.common;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.library.util.StatusBarUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.http.manager.model.CategoryHomeCityDashBoard;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseFragmentActivity;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseRequestFragment;
import com.honeywell.hch.airtouch.ui.common.ui.view.BottomIconViewItem;
import com.honeywell.hch.airtouch.ui.main.manager.common.MainActivityUIManager;
import com.honeywell.hch.airtouch.ui.main.ui.common.view.BottomNavigationView;
import com.honeywell.hch.airtouch.ui.main.ui.common.view.CustomViewPager;
import com.honeywell.hch.airtouch.ui.main.ui.common.view.HomeListAdapter;
import com.honeywell.hch.airtouch.ui.main.ui.common.view.ViewPager;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.ui.MadAirFragment;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.view.HomePageAdapter_New;
import com.honeywell.hch.airtouch.ui.main.ui.devices.view.AllDeviceFragment;
import com.honeywell.hch.airtouch.ui.main.ui.devices.view.AllDevicePageAdapter_New;
import com.honeywell.hch.airtouch.ui.main.ui.title.view.AllDeviceTitlePageIndicator;
import com.honeywell.hch.airtouch.ui.main.ui.title.view.DashboardTitlePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by h127856 on 7/18/16.
 * 作为实际的MainActivity和TryDemoActivity的父类
 */
public class MainBaseActivity extends BaseFragmentActivity implements ViewPager.OnPageChangeListener {

    private static final int DEFAULT_STATUS_BAR_HEIGHT = 75;
    private static final int NAVIGATE_BAR_HEIGHT = DensityUtil.dip2px(50);
    private static final int MAX_ITEMSIZE = 10;
    private static final int ITEMHEIGHT = 320;
    protected static final int INIT_DEFAULT_HOME_INDEX = -1;
    protected static final int INIT_DEFAULT_LOCATION_ID = -1;

    protected static final int DASHBOARD = 0;
    protected static final int ALL_DEVICE = 1;
    protected static final int MESSAGE = 2;
    protected static final int ME = 3;
    protected int mCurrentTab = AppManager.isEnterPriseVersion() ? ALL_DEVICE : DASHBOARD;


    protected RelativeLayout mParentView;
    protected CustomViewPager mViewPager = null;
    protected FragmentStatePagerAdapter mHomePageAdapter = null;

    protected List<BaseRequestFragment> mHomeFragmentList = new CopyOnWriteArrayList<>();
    protected List<AllDeviceFragment> mAllDeviceFramentList = new CopyOnWriteArrayList<>();

    protected int mCurrentHomeIndex = INIT_DEFAULT_HOME_INDEX;
    protected int mCurrentAllDeviceIndex = INIT_DEFAULT_HOME_INDEX;
    protected int mCurrentLocationId = INIT_DEFAULT_LOCATION_ID;

    protected BottomNavigationView mBottomNavigationView;

    protected ListView mHomeListView;
    protected HomeListAdapter mHomeListAdapter;
    protected List<CategoryHomeCityDashBoard> mCategoryHomeCities;


    //用于显示家的名字，天气已经城市
    protected DashboardTitlePageIndicator mDashboardPageIndicator;
    protected AllDeviceTitlePageIndicator mAllDeviceTitlePageIndicator;

    // arrow layout
    protected RelativeLayout mArrowLayout;
    protected RelativeLayout mLeftUnnormalNumberLayout;
    protected RelativeLayout mRightUnnormalNumberLayout;
    protected ImageView mLeftArrowImageView;
    protected ImageView mRightArrowImageView;
    protected MainActivityUIManager mMainActivityUIManager;
    protected TextView mLeftUnnormalNumberTextView;
    protected TextView mRightUnnormalNumberTextView;

    protected boolean isShowHomeList = false;

    protected RelativeLayout mNavagateLayout;

    protected FrameLayout mMessagesFrameLayout;
    protected FrameLayout mMeFrameLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState = null;
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_new);

        mParentView = (RelativeLayout) findViewById(R.id.parent_view_id);

        mViewPager = (CustomViewPager) findViewById(R.id.main_page);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.ds_bottom_view);

        mHomeListView = (ListView) findViewById(R.id.home_city_listView);
        mArrowLayout = (RelativeLayout) findViewById(R.id.left_right_indicator_layout);

        mArrowLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int statusBarHeight = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 0 : StatusBarUtil.getStatusBarHeight(MainBaseActivity.this);
                int framentHeight = DensityUtil.getScreenHeight() - statusBarHeight - NAVIGATE_BAR_HEIGHT;
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mArrowLayout.getLayoutParams());
                layoutParams.height = (5 * framentHeight) / 11;
                mArrowLayout.setLayoutParams(layoutParams);
            }
        });

        mLeftUnnormalNumberLayout = (RelativeLayout) findViewById(R.id.left_unnormal_layout);
        mLeftUnnormalNumberLayout.setVisibility(View.GONE);
        mRightUnnormalNumberLayout = (RelativeLayout) findViewById(R.id.right_unnormal_layout);
        mRightUnnormalNumberLayout.setVisibility(View.GONE);
        mLeftUnnormalNumberTextView = (TextView) findViewById(R.id.left_unnormal_home_size_id);
        mRightUnnormalNumberTextView = (TextView) findViewById(R.id.right_unnormal_home_size_id);

        mNavagateLayout = (RelativeLayout) findViewById(R.id.navagate_layout);


        mLeftArrowImageView = (ImageView) findViewById(R.id.left_arrow_image_id);
        mRightArrowImageView = (ImageView) findViewById(R.id.right_arrow_image_id);
        setArrowClickListener();

        mDashboardPageIndicator = (DashboardTitlePageIndicator) findViewById(R.id.page_indicator);
        mAllDeviceTitlePageIndicator = (AllDeviceTitlePageIndicator) findViewById(R.id.alldevice_page_indicator);


        if (AppManager.isEnterPriseVersion()) {
            mHomePageAdapter = new AllDevicePageAdapter_New(getSupportFragmentManager(), mAllDeviceFramentList);
            mViewPager.setAdapter(mHomePageAdapter);
            mAllDeviceTitlePageIndicator.setViewPager(mViewPager, MainBaseActivity.this);
            mAllDeviceTitlePageIndicator.setOnPageChangeListener(MainBaseActivity.this);
            mDashboardPageIndicator.setVisibility(View.GONE);
        } else {
            mHomePageAdapter = new HomePageAdapter_New(getSupportFragmentManager(), mHomeFragmentList);
            mViewPager.setAdapter(mHomePageAdapter);
            mDashboardPageIndicator.setViewPager(mViewPager, MainBaseActivity.this);
            mDashboardPageIndicator.setOnPageChangeListener(MainBaseActivity.this);
            mAllDeviceTitlePageIndicator.setActivity(this);
            mAllDeviceTitlePageIndicator.setVisibility(View.GONE);
        }

        mMessagesFrameLayout = (FrameLayout) findViewById(R.id.message_contentFrame);
        mMeFrameLayout = (FrameLayout) findViewById(R.id.me_contentFrame);
    }


    private void setNetworkErrorActionOfFragment(BaseRequestFragment fragment) {
        if (fragment != null) {
            fragment.setNetWorkErrorView();
        }
    }

    private void setNoworkActionOfFragment(BaseRequestFragment fragment) {
        if (fragment != null) {
            fragment.setNoNetWorkView();
        }
    }

    private void setNetworkConnectActionOfFramgent(BaseRequestFragment fragment) {
        if (fragment != null) {
            fragment.setNetWorkViewGone();
        }
    }


    public int getTotalViewPagerSize() {
        if (AppManager.isEnterPriseVersion()) {
            return mAllDeviceFramentList.size();
        }
        return mHomeFragmentList.size();
    }

    public int getTotalViewPagerForAllDeviceSize() {
        return mAllDeviceFramentList.size();
    }


    /**
     * 显示dashboard家的viewpager内容
     */
    public void showDashboardViewPager(int homeSize) {
        mDashboardPageIndicator.setVisibility(View.VISIBLE);
        mAllDeviceTitlePageIndicator.setVisibility(View.GONE);
        mDashboardPageIndicator.initPresenter(mCurrentHomeIndex);
        initArrowViewAndErrorNumber(mCurrentHomeIndex, homeSize);
        mMessagesFrameLayout.setVisibility(View.GONE);
        mMeFrameLayout.setVisibility(View.GONE);
        mArrowLayout.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        StatusBarUtil.changeStatusBarTextColor(mParentView, View.SYSTEM_UI_FLAG_VISIBLE);

    }

    /**
     * 显示device 的viewpager内容
     */
    public void showAllDeviceViewPager() {
        mViewPager.setVisibility(View.VISIBLE);
        mDashboardPageIndicator.setVisibility(View.GONE);
        mAllDeviceTitlePageIndicator.setVisibility(View.VISIBLE);
        mArrowLayout.setVisibility(View.GONE);
        mAllDeviceTitlePageIndicator.initPresenter(mCurrentAllDeviceIndex);
        mMessagesFrameLayout.setVisibility(View.GONE);
        mMeFrameLayout.setVisibility(View.GONE);
        StatusBarUtil.changeStatusBarTextColor(mParentView, View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }


    /**
     * 获取AllDeviceFragment list
     *
     * @return
     */
    public List<AllDeviceFragment> getAllDeviceFramentList() {
        return mAllDeviceFramentList;
    }


    public void initHomeListView(boolean isFromRefresh) {
        if (isFromRefresh && isShowHomeList || !isFromRefresh) {
            List<CategoryHomeCityDashBoard> categoryHomeCities = new ArrayList<>();
            if (mCurrentTab == ALL_DEVICE) {
                categoryHomeCities = mMainActivityUIManager.getHomeNameListAllDevice();
            } else {
                categoryHomeCities = mMainActivityUIManager.getHomeNameList();
            }

            int size = mMainActivityUIManager.getHomeListSize();
            mHomeListView.setVisibility(View.VISIBLE);

            this.mCategoryHomeCities = categoryHomeCities;
            setHomeNameListViewHeight(size);
            isShowHomeList = true;
            if (mHomeListView.getAdapter() == null || !isFromRefresh) {
                mHomeListAdapter = new HomeListAdapter(this, mCategoryHomeCities);
                mHomeListView.setAdapter(mHomeListAdapter);
            } else {
                mHomeListAdapter.changeData(mCategoryHomeCities); //update your adapter's data
            }

            mHomeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mViewPager.setCurrentItem(mHomeListAdapter.getListPosition(position));

                    hideCityListAndSetDropIconStuas();
                }
            });
        }

    }

    public void addBottomNavigateView(List<BottomIconViewItem> bottomIconViewItemList) {
        if (!AppManager.isEnterPriseVersion()) {
            bottomIconViewItemList.add(mBottomNavigationView.addBottomViewItem(R.drawable.dashboard_active, R.drawable.dashboard_inactive, getString(R.string.dash_board_btn_text), new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mCurrentTab != DASHBOARD) {
                        mCurrentTab = DASHBOARD;
                        mBottomNavigationView.setItemClick(getString(R.string.dash_board_btn_text));

                        mDashboardPageIndicator.setViewPager(mViewPager, MainBaseActivity.this);
                        mDashboardPageIndicator.setOnPageChangeListener(MainBaseActivity.this);

                        setLayoutVisible();

                        mHomePageAdapter = new HomePageAdapter_New(getSupportFragmentManager(), mHomeFragmentList);
                        mViewPager.setAdapter(mHomePageAdapter);
                        mViewPager.setCurrentItem(mCurrentHomeIndex);
                    }

                }
            }));
        }


        bottomIconViewItemList.add(mBottomNavigationView.addBottomViewItem(R.drawable.devices_active, R.drawable.devices_inactive, getString(R.string.devices_btn_text), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentTab != ALL_DEVICE) {
                    mCurrentTab = ALL_DEVICE;


                    mBottomNavigationView.setItemClick(getString(R.string.devices_btn_text));
                    mAllDeviceTitlePageIndicator.setViewPager(mViewPager, MainBaseActivity.this);
                    mAllDeviceTitlePageIndicator.setOnPageChangeListener(MainBaseActivity.this);
                    setLayoutVisible();

                    mHomePageAdapter = new AllDevicePageAdapter_New(getSupportFragmentManager(), mAllDeviceFramentList);
                    mViewPager.setAdapter(mHomePageAdapter);
                    mViewPager.setCurrentItem(mCurrentAllDeviceIndex);
                }
            }
        }));
    }


    /**
     * 点击 navigation bottom时候，需要进行的显示处理,需要子类进行复写
     */
    protected void setLayoutVisible() {

    }


    private void setHomeNameListViewHeight(int size) {
        int height = ListView.LayoutParams.WRAP_CONTENT;
        if (size > MAX_ITEMSIZE) {
            height = DensityUtil.dip2px(ITEMHEIGHT);
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtil.dip2px(160), height);
        params.topMargin = DensityUtil.dip2px(40) + StatusBarUtil.getStatusBarForAndroidM(mContext);
        params.leftMargin = DensityUtil.dip2px(20);

        mHomeListView.setLayoutParams(params);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!isTouchInHomeList(event.getX(), event.getY())) {
            hideCityListAndSetDropIconStuas();
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void hideCityListAndSetDropIconStuas() {
        hideCityListView();
        mDashboardPageIndicator.setDropDownImageView(true);
        mAllDeviceTitlePageIndicator.setDropDownImageView(true);
    }

    public void hideCityListView() {
        if (mCategoryHomeCities != null && mCategoryHomeCities.size() > 0) {
            mCategoryHomeCities.clear();
            mHomeListAdapter.notifyDataSetChanged();
            setHomeNameListViewHeight(mCategoryHomeCities.size());
            mHomeListView.setVisibility(View.GONE);
        }
        isShowHomeList = false;
    }

    public boolean isTouchInHomeList(float x, float y) {

        //获取mDropImage在屏幕中的坐标
        int[] location = new int[2];
        mHomeListView.getLocationOnScreen(location);
        float topX = location[0];
        float topY = location[1];

        //判断触摸点是否在mDropImage内
        boolean isIn = x >= topX && x <= topX + mHomeListView.getWidth() && y >= topY && y <= topY + mHomeListView.getHeight();
        boolean isInTitle = x >= 0 && x <= mDashboardPageIndicator.homeLayoutWidth() && y >= 0 && y <= DensityUtil.dip2px(60);
        if (isIn || isInTitle) {
            return true;
        }
        return false;
    }


    private void setArrowClickListener() {
        mLeftArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentHomeIndex > 0) {
                    mViewPager.setCurrentItem(mCurrentHomeIndex - 1);
                }
            }
        });

        mRightArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentHomeIndex < mHomeFragmentList.size() - 1) {
                    mViewPager.setCurrentItem(mCurrentHomeIndex + 1);
                }
            }
        });
    }

    public void initArrowViewAndErrorNumber(int index, int homeSize) {
        if (homeSize == 1) {
            mLeftArrowImageView.setVisibility(View.GONE);
            mRightArrowImageView.setVisibility(View.GONE);
            return;
        } else if (index == 0) {
            mLeftArrowImageView.setVisibility(View.GONE);
            mRightArrowImageView.setVisibility(View.VISIBLE);
        } else if (index == mHomeFragmentList.size() - 1) {
            mLeftArrowImageView.setVisibility(View.VISIBLE);
            mRightArrowImageView.setVisibility(View.GONE);
        } else {
            mLeftArrowImageView.setVisibility(View.VISIBLE);
            mRightArrowImageView.setVisibility(View.VISIBLE);
        }

        int leftNumber = mMainActivityUIManager.getLeftErrorHomeNumber(index);
        if (leftNumber == 0) {
            mLeftUnnormalNumberLayout.setVisibility(View.GONE);
        } else {
            mLeftUnnormalNumberLayout.setVisibility(View.VISIBLE);
            mLeftUnnormalNumberTextView.setText(String.valueOf(leftNumber));
        }

        int rightNumber = mMainActivityUIManager.getRightErrorHomeNumber(index);
        if (rightNumber == 0) {
            mRightUnnormalNumberLayout.setVisibility(View.GONE);
        } else {
            mRightUnnormalNumberLayout.setVisibility(View.VISIBLE);
            mRightUnnormalNumberTextView.setText(String.valueOf(rightNumber));
        }
    }


    public View getTopView() {
        return mParentView;
    }

    public int getTopViewId() {
        return R.id.parent_view_id;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentHomeIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DashBoadConstant.GROUP_CONTROL_REQUEST_CODE:
                if (mCurrentHomeIndex < mAllDeviceFramentList.size() && mCurrentHomeIndex >= 0) {
                    mAllDeviceFramentList.get(mCurrentHomeIndex).dealGroupControlForResult();
                }
                break;
            case DashBoadConstant.DEVICE_CONTROL_REQUEST_CODE:
                if (data != null) {
                    if (mCurrentHomeIndex < mAllDeviceFramentList.size() && mCurrentHomeIndex >= 0) {
                        mAllDeviceFramentList.get(mCurrentHomeIndex).dealDeviceControlForResult(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public DashboardTitlePageIndicator getmDashboardPageIndicator() {
        return mDashboardPageIndicator;
    }

    public void reloadSelectedTitleStausFromAllDevice() {
        mViewPager.setScanScroll(true);
        mAllDeviceTitlePageIndicator.initPresenter(mCurrentAllDeviceIndex);
        if (mCurrentTab != MESSAGE) {
            mNavagateLayout.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 在alldevice界面里对设备进行操作后，需要把titlebar的状态设置成原来状态。
     * 调用方向：  AllDeviceFragment --->  Title indicator
     */
    public void resetSelectedTitleStatusFromAllDevice() {
        mViewPager.setScanScroll(true);
        mNavagateLayout.setVisibility(View.VISIBLE);
        mAllDeviceTitlePageIndicator.setTitleNormalStatus();
    }

    /**
     * 从title indicator中设置allDevice的编辑状态，
     * 设置viewPager是否可以滑动，在allDevice界面如果处于编辑状态，viewpager不可以滑动
     * 在alldevice界面中reset状态不能调用这个方法，否则会出现死循环调用
     * 调用方向： Title indicator ---> AllDeviceFragment
     *
     * @param isEditStatus
     */
    public void setAllDeviceEditStatusFromIndiacator(boolean isEditStatus) {
        mViewPager.setScanScroll(!isEditStatus);
        if (mCurrentAllDeviceIndex == mAllDeviceFramentList.size()) {
            mCurrentAllDeviceIndex = mAllDeviceFramentList.size() - 1;
        }
        if (mCurrentAllDeviceIndex < mAllDeviceFramentList.size()) {
            if (isEditStatus) {
                mNavagateLayout.setVisibility(View.GONE);
                mAllDeviceFramentList.get(mCurrentAllDeviceIndex).showBottomStatusView();
            } else {
                mNavagateLayout.setVisibility(View.VISIBLE);
                mAllDeviceFramentList.get(mCurrentAllDeviceIndex).normalStatusView();
            }
        }
    }


    public void refreshMadAirWeather() {
        if (!AppManager.isEnterPriseVersion()) {
            for (BaseRequestFragment homeFragment : mHomeFragmentList) {
                if (homeFragment instanceof MadAirFragment) {
                    ((MadAirFragment) homeFragment).refreshMadAirWeather();
                }
            }
        }
    }
}
