package com.honeywell.hch.airtouch.ui.homemanage.ui.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.model.CategoryHomeCity;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeAndCity;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.homemanage.ui.adapter.HomeManageAdapter;
import com.honeywell.hch.airtouch.ui.homemanage.ui.view.RenameDialog;

import java.util.List;

/**
 * Created by Vincent on 13/7/16.
 */
public class HomeManagementActivity extends HomeManagementBaseActivity implements RenameDialog.RenameHomeNameCallBack {
    private final String TAG = "HomeManagementActivity";
    private ListView mListView;
    private List<CategoryHomeCity> mCategoryHomeCityList;
    private HomeManageAdapter mHomeManageAdapter;
    private HomeAndCity mHomeAndCity;
    private String mReName;
    private static final int ADD_HOME_REQUEST = 11;
    private final int HEADHEIGHT = 30;
    private View footView;
    private RelativeLayout mNoDataRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_manager);
        initStatusBar();
        initListFootView();
        initView();
        initTitleView();
        initHomeManagerUiManager();
        initData();
        initClickListenter();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.home_manager_el);
        mListView.addFooterView(footView, null, false);
        mNoDataRl = (RelativeLayout) findViewById(R.id.home_manager_no_data);
        initDragDownManager(R.id.root_view_id);
    }

    private void initListFootView() {
        footView = LayoutInflater.from(mContext).inflate(R.layout.home_manager_listview_item_header, null);
        RelativeLayout listViewHeadRl = (RelativeLayout) footView.findViewById(R.id.layout_header);
        TextView headerNameTv = (TextView) footView.findViewById(R.id.home_manager_header_name_tv);
        ListView.LayoutParams params = new ListView.LayoutParams
                (ListView.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(HEADHEIGHT));
        listViewHeadRl.setLayoutParams(params);
        headerNameTv.setVisibility(View.GONE);
    }

    private void initData() {
        mTitleTextview.setText(getString(R.string.home_manager_title));
        mEndTextTip.setText(getString(R.string.home_manager_add));
        mCategoryHomeCityList =  UserDataOperator.getHomeList();
        initAdapter();
    }

    public void doClick(View v) {
        if (v.getId() == R.id.enroll_back_layout) {
            super.backIntent();
        } else if (v.getId() == R.id.end_text_tip || v.getId() == R.id.home_manager_add_home) {
            Intent intent = new Intent(mContext, UserAddHomeActivity.class);
            startActivityForResult(intent, ADD_HOME_REQUEST);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }
    }

    private void initAdapter() {
        if (mCategoryHomeCityList.size() == 0) {
            mNoDataRl.setVisibility(View.VISIBLE);
        } else {
            mNoDataRl.setVisibility(View.GONE);
        }
        mHomeManagerUiManager.isOwnDefaultHome(mContext, mCategoryHomeCityList);
        mHomeManageAdapter = new HomeManageAdapter(mContext, mCategoryHomeCityList);
        mListView.setAdapter(mHomeManageAdapter);
        initItemCallBack();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case ADD_HOME_RESULT:
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "ADD_HOME_REQUEST: -------");
                initData();
                break;
        }
    }

    private void initItemCallBack() {
        // click image icon to remove home
        mHomeManageAdapter.setRemoveHomeCallback(new HomeManageAdapter.DeleteHomeCallback() {
            @Override
            public void callback(HomeAndCity homeAndCity) {

                if (isNetworkOff()){
                    return;
                }
                mHomeAndCity = homeAndCity;
                MessageBox.createTwoButtonDialog((Activity) mContext, null, getString(R.string.home_manager_delete_remind, homeAndCity.getHomeName()), getString(R.string.cancel), null,
                        getString(R.string.home_manager_delete), deleteHome);

            }
        });

        // click image icon to rename home
        mHomeManageAdapter.setRenameHomeCallback(new HomeManageAdapter.RenameHomeCallback() {
            @Override
            public void callback(HomeAndCity homeAndCity) {
                if (isNetworkOff()){
                    return;
                }
                RenameDialog.getInstance().showAlerDialog((Activity) mContext, homeAndCity, HomeManagementActivity.this);
            }
        });

        //click set default home
        mHomeManageAdapter.setmSetDefaultHomeCallback(new HomeManageAdapter.SetDefaultHomeCallback() {
            @Override
            public void callback(int locaionId) {
//                mHomeManagerUiManager.clearDefaultHomeId(locaionId, mCategoryHomeCityList);
                sendSetDefaultHomeBroadCast();
                mHomeManageAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initClickListenter() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "position: " + position);
                Object object = parent.getItemAtPosition(position);
                if (object instanceof HomeAndCity) {
                    HomeAndCity homeAndCity = ((HomeAndCity) object);
                    if (homeAndCity.isExpand()) {
                        homeAndCity.setIsExpand(false);
                    } else {
                        mHomeManagerUiManager.pickUpItem(mCategoryHomeCityList);
                        homeAndCity.setIsExpand(true);
                    }
                    mHomeManageAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    protected void dealSuccessCallBack(ResponseResult responseResult) {
        super.dealSuccessCallBack(responseResult);
        switch (responseResult.getRequestId()) {
            case SWAP_LOCATION:
                sendRenameBroadCast();
                mHomeAndCity.setmHomeName(mReName);
                mHomeManageAdapter.notifyDataSetChanged();
                break;
            case DELETE_LOCATION:

                inCaseRemoveDefaultHome();
                mCategoryHomeCityList = UserDataOperator.getHomeList();
                initAdapter();
                break;
        }
    }

    private MessageBox.MyOnClick deleteHome = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            mDialog = LoadingProgressDialog.show(mContext, getString(R.string.deleting_home));
            mHomeManagerUiManager.processRemoveHome(mHomeAndCity.getLocationId());
        }
    };

    private void inCaseRemoveDefaultHome() {
        if (mHomeAndCity.getLocationId() ==
                UserInfoSharePreference.getDefaultHomeId()) {
            sendSetDefaultHomeBroadCast();
            UserInfoSharePreference.saveDefaultHomeId(UserInfoSharePreference.DEFAULT_INT_VALUE);
        }
    }

    private void sendRenameBroadCast() {
        Intent intent = new Intent(HPlusConstants.RENAME_HOME);
        intent.putExtra("location_name", mReName);
        intent.putExtra("location_id", mHomeAndCity.getLocationId());
        sendBroadcast(intent);
    }

    private void sendSetDefaultHomeBroadCast() {
        Intent intent = new Intent(HPlusConstants.SET_DEFALUT_HOME);
        sendBroadcast(intent);
    }

    @Override
    public void doneBtnCallBack(String homeName, HomeAndCity homeAndCity) {
        if (StringUtil.isEmpty(homeName)) {
            RenameDialog.getInstance().setErrorText(getString(R.string.home_name_empty));
            return;
        }
        //名字相同，没有任何修改
        if (homeName.equals(homeAndCity.getHomeName())) {
            RenameDialog.getInstance().dismissDialog();
            return;
        }
        //该家中中有相同的名字
        if (UserDataOperator.isHaveSameHomeName(homeName, homeAndCity)) {
            RenameDialog.getInstance().setErrorText(getString(R.string.same_home));
            return;
        }
        RenameDialog.getInstance().dismissDialog();
        mDialog = LoadingProgressDialog.show(mContext, getString(R.string.renaming));
        mReName = homeName;
        mHomeAndCity = homeAndCity;
        mHomeManagerUiManager.processRenameHome(homeName, homeAndCity.getLocationId());
    }
}
