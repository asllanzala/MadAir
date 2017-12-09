package com.honeywell.hch.airtouch.ui.main.ui.devices.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceGroupItem;
import com.honeywell.hch.airtouch.plateform.http.manager.model.RealUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.VirtualUserLocationData;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.common.ui.adapter.CategoryAdapter;
import com.honeywell.hch.airtouch.ui.common.ui.controller.HomeDeviceInfoBaseFragment;
import com.honeywell.hch.airtouch.ui.common.ui.view.BottomIconViewItem;
import com.honeywell.hch.airtouch.ui.common.ui.view.GroupContolListView;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.control.ui.device.controller.DeviceControlActivity;
import com.honeywell.hch.airtouch.ui.control.ui.group.controller.GroupControlActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.beforeplay.EnrollScanActivity;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.AllDeviceMadAirManager;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.AllDeviceUIBaseManager;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.AllDeviceUIManager;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.DeviceInfoBaseUIManager;
import com.honeywell.hch.airtouch.ui.main.ui.common.MainBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 20/7/16.
 */
public class AllDeviceFragment extends HomeDeviceInfoBaseFragment implements CreateGroupDialog.CreateGroupBtnCallBack {

    protected String TAG = "AllDeviceFragment";

    private DeviceGroupItem mDeviceGroupItem;
    private ScrollView mScrollView;
    private RelativeLayout mNoDeviceLayout;
    private ImageView mLoadingImage;
    private TextView mLoadingTextView;
    private View mParentView = null;

    public static AllDeviceFragment newInstance(UserLocationData userLocationData, Activity activity) {
        AllDeviceFragment fragment = new AllDeviceFragment();
        fragment.initActivity(activity);
        fragment.initUiManager(userLocationData);
        return fragment;
    }

    private void initUiManager(UserLocationData locationData) {
        mUserLocationData = locationData;

        mLocationId = mUserLocationData.getLocationID();

        if (mAllDeviceUIManager == null || (mAllDeviceUIManager instanceof AllDeviceMadAirManager && mUserLocationData instanceof RealUserLocationData)
                || (mAllDeviceUIManager instanceof AllDeviceUIManager && mUserLocationData instanceof VirtualUserLocationData)){
            mAllDeviceUIManager = mUserLocationData instanceof RealUserLocationData ? new AllDeviceUIManager(mUserLocationData) :
                    new AllDeviceMadAirManager(mUserLocationData);
        }
        mAllDeviceUIManager.resetUserLocationData(mUserLocationData);
        initManagerCallBack(mAllDeviceUIManager);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mParentView == null) {
            mParentView = inflater.inflate(R.layout.activity_alldevice, container, false);
            initView(mParentView);
        }
        isAfterInit = true;
        initItemSelectedListener();
        initData();
        initManagerCallBack(mAllDeviceUIManager);
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
        isAfterInit = false;
        resetManagerCallBack(mAllDeviceUIManager);
        if (mAlertDlalog != null && mAlertDlalog.isShowing()) {
            mAlertDlalog.dismiss();
            mAlertDlalog = null;
        }
    }

    @Override
    protected void dealSuccessCallBack(ResponseResult responseResult) {
        switch (responseResult.getRequestId()) {
            case GET_GROUP_BY_LOCATION_ID:
                super.dealSuccessCallBack(responseResult);
                if (!isInSelectStatus()) {
                    initAllDeviceView();
                    sendReflashGroupBroadCast();
                }
                break;
            case CREATE_GROUP:
            case ADD_DEVICE_TO_GROUP:
            case DELETE_DEVICE:
                normalStatusView();
                setClickButtonPreviousStatus();
                getGroupInfoFromServier(false);
                break;
            case DELETE_MADAIR_DEVICE:
                super.dealSuccessCallBack(responseResult);
                normalStatusView();
                setClickButtonPreviousStatus();
                initAdapter();
                break;
        }

    }

    @Override
    protected void setBottomViewBtnStatusAfterSelected() {
        if (mAllDeviceUIManager.getAllDeviceSelectedIds() != null && mAllDeviceUIManager.getAllDeviceSelectedIds().length > 0) {
            mBottomView.setAllBtnExceptSelectClickable(true);
        } else {
            mBottomView.setAllBtnExceptSelectClickable(false);
        }
        //if all items are selected ,should change the first button text to "Deselected all"
        if (mAllDeviceUIManager.isAllDeviceItemSelected()) {
            mBottomView.setFirstBtnSelectStatusText(mParentActivity.getString(R.string.all_device_bottom_deselectall));
        } else {
            mBottomView.setFirstBtnSelectStatusText(mParentActivity.getString(R.string.all_device_bottom_selectall));
        }
    }


    @Override
    public void setClickButtonPreviousStatus() {
        ((MainBaseActivity) mParentActivity).resetSelectedTitleStatusFromAllDevice();
    }

    private void initAllDeviceUIManager() {
        if (mAllDeviceUIManager == null) {
            mAllDeviceUIManager = new AllDeviceUIManager(mLocationId);
        } else {
            mAllDeviceUIManager.resetData(mLocationId);
        }
        initManagerCallBack(mAllDeviceUIManager);
    }

    public void dealGroupControlForResult() {
        setClickButtonPreviousStatus();
        initAllDeviceView();
    }

    public void dealDeviceControlForResult(Intent intent) {
        int deviceId = intent.getIntExtra(DeviceControlActivity.ARG_DEVICE, 0);
        Object object = intent.getBundleExtra(DashBoadConstant.ARG_LAST_RUNSTATUS);
        if (deviceId != 0 && object != null) {
            mAllDeviceUIManager.setDeviceControlResult(deviceId, mListData, object);
            mCustomBaseAdapter.notifyDataSetChanged();
        }
    }

    public void initAllDeviceUIBottomBtn(boolean isHasGroup) {
        if (AppManager.isEnterPriseVersion() || mUserLocationData instanceof VirtualUserLocationData) {
            initEnterpriseAccountBottomBtn();
        } else {
            initPersonalAccountBottonBtn(isHasGroup);
        }

    }

    private void initPersonalAccountBottonBtn(boolean isHasGroup) {
        List<BottomIconViewItem> bottomIconViewItemList = new ArrayList<>();
        bottomIconViewItemList.add(addBottomViewItem(R.drawable.bottom_new_group, R.string.all_device_bottom_newgroup, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ifNetworkOff()) {
                    return;
                }
                if (mAllDeviceUIManager.getGroupMasterDeviceId() == AllDeviceUIManager.NO_MASTER_ID
                        && (mAlertDlalog == null || !mAlertDlalog.isShowing())) {
                    mAlertDlalog = MessageBox.createSimpleDialog(mParentActivity, null, mParentActivity.getString(R.string.need_master_device_tip), null, null);
                    return;
                }
                CreateGroupDialog.getInstance().showAlerDialog(mParentActivity, mAllDeviceUIManager, AllDeviceFragment.this);
            }
        }));
        if (isHasGroup) {
            bottomIconViewItemList.add(addBottomViewItem(R.drawable.bottom_move_into, R.string.all_device_bottom_movein, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ifNetworkOff()) {
                        return;
                    }
                    if (mCustomBaseAdapter.isSelectOneDevice() == true) {
                        mCustomBaseAdapter.setButtomType(ButtomType.MOVEINTO);
                        mCustomBaseAdapter.getmListData().get(0).setmCategoryName(mParentActivity.getString(R.string.select_a_group));
                        mCustomBaseAdapter.notifyDataSetChanged();

                        //如果是move into操作，底部的所有按钮都不可点击，包括select
                        mBottomView.setAllBtnClickable(false);
                    }
                }
            }));
        }
        addDeleteBtn(bottomIconViewItemList);
        mBottomView.addBtnToThisView(bottomIconViewItemList);
    }

    protected void initView(View view) {
        initBottomView(view);
        mDeviceListView = (GroupContolListView) view.findViewById(R.id.all_device_listView);
        mScrollView = (ScrollView) view.findViewById(R.id.aqua_scrollview);
        mScrollView.smoothScrollTo(0, 0);
        initAdapter();

        mNoDeviceLayout = (RelativeLayout) view.findViewById(R.id.no_device_layout);
        mLoadingImage = (ImageView) view.findViewById(R.id.add_gray_icon);
        mLoadingTextView = (TextView) view.findViewById(R.id.loading_text_id);

        if (mUserLocationData != null) {
            setAllDevieLayoutVisible(mUserLocationData.isHaveSelectedDeviceThisLocation(), mUserLocationData instanceof RealUserLocationData);
        } else {
            setLoadDataFailedLayout();
        }

        mLoadingImage.setClickable(false);
        mLoadingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentStartActivity(EnrollScanActivity.class);
            }
        });
    }

    public void showBottomStatusView() {
        DeviceInfoBaseUIManager.setIsEditStatus(true);
        super.showBottomStatusView();
    }

    @Override
    public void normalStatusView() {
        DeviceInfoBaseUIManager.setIsEditStatus(false);
        super.normalStatusView();
    }


    private void setAllDevieLayoutVisible(boolean isHasDevice, boolean isHome) {
        if (isHome) {
            if (mUserLocationData.isDeviceListLoadingData()) {

                //正在loading情况，但是没有网络的，或是网络请求有错误的，需要显示loaded failed
                if (!NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication()) || UserAllDataContainer.shareInstance().isHasNetWorkError()) {
                    setLoadDataFailedLayout();
                } else {
                    setLoadingDataLayout();
                }
            } else if (mUserLocationData.isDeviceListLoadDataFailed()) {
                setLoadDataFailedLayout();
            } else {
                if (!isHasDevice) {
                    setNoDeviceLayout();
                } else {
                    mNoDeviceLayout.setVisibility(View.GONE);
                    mScrollView.setVisibility(View.VISIBLE);
                }
            }
            if (!isInSelectStatus() && mUserLocationData instanceof RealUserLocationData) {
                ((MainBaseActivity) mParentActivity).reloadSelectedTitleStausFromAllDevice();
            }
        } else {
            mNoDeviceLayout.setVisibility(View.GONE);
            mScrollView.setVisibility(View.VISIBLE);
        }
    }


    private void setLoadingDataLayout() {
        mNoDeviceLayout.setVisibility(View.VISIBLE);
        mScrollView.setVisibility(View.GONE);
        mBottomView.setVisibility(View.GONE);

        mLoadingImage.setVisibility(View.GONE);
        mLoadingTextView.setText(mParentActivity.getString(R.string.no_content_yet));
    }

    private void setLoadDataFailedLayout() {
        mNoDeviceLayout.setVisibility(View.VISIBLE);
        mScrollView.setVisibility(View.GONE);
        mBottomView.setVisibility(View.GONE);
        mLoadingImage.setVisibility(View.GONE);
        mLoadingTextView.setText(mParentActivity.getString(R.string.no_content_yet));

    }

    private void setNoDeviceLayout() {
        mNoDeviceLayout.setVisibility(View.VISIBLE);
        mScrollView.setVisibility(View.GONE);
        mBottomView.setVisibility(View.GONE);

        mLoadingImage.setVisibility(View.VISIBLE);
        mLoadingTextView.setVisibility(View.VISIBLE);

        mLoadingImage.setImageResource(R.drawable.ds_top_no_device);
        mLoadingImage.setClickable(true);
        mLoadingTextView.setText(mParentActivity.getString(R.string.tap_add_device_msg));

    }


    public boolean isHaveLoadedDeviceListInThisLocation() {
        if (mAllDeviceUIManager != null) {
            return mAllDeviceUIManager.isLoadedDeviceListInLocation();
        }
        return false;
    }

    private void initData() {
        initDragDownManager(((MainBaseActivity) mParentActivity).getTopView(), ((MainBaseActivity) mParentActivity).getTopViewId());
        if (mUserLocationData != null && mUserLocationData instanceof RealUserLocationData) {
            getGroupInfoFromServier(true);

        }
        initAllDeviceView();

    }

    private void initAdapter() {
        mListData = mAllDeviceUIManager.getCategoryData();

        ButtomType buttomType = ButtomType.NORMAL;
        mCustomBaseAdapter = new CategoryAdapter(mParentActivity, mListData, buttomType);
        mDeviceListView.setAdapter(mCustomBaseAdapter);
        mCustomBaseAdapter.notifyDataSetChanged();
    }

    private MessageBox.MyOnClick moveIntoGroup = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            addDeviceToGroup(mDeviceGroupItem);

        }
    };

    private void addDeviceToGroup(DeviceGroupItem deviceGroupItem) {
        mDialog = LoadingProgressDialog.show(mParentActivity, mParentActivity.getString(R.string.enroll_loading));
        mAllDeviceUIManager.addDeviceToGroup(deviceGroupItem.getGroupId());
    }

    @Override
    public void doneBtnCallBack(String groupName) {

        if (StringUtil.isEmpty(groupName)) {
            CreateGroupDialog.getInstance().setErrorText(mParentActivity.getString(R.string.group_name_cannot_empty));
            return;
        }
        if (mAllDeviceUIManager.isHaveSameGroupName(groupName)) {
            CreateGroupDialog.getInstance().setErrorText(mParentActivity.getString(R.string.duplication_group_name));
            return;
        }

        if (mDialog == null || !mDialog.isShowing()) {
            mDialog = LoadingProgressDialog.show(mParentActivity, mParentActivity.getString(R.string.enroll_loading));
        }

        CreateGroupDialog.getInstance().dismissDialog();
        mAllDeviceUIManager.createNewGroup(groupName);
    }


    private void startGroupControlActivity(int groupId) {
        Intent intent = new Intent(mParentActivity, GroupControlActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        intent.putExtra(HPlusConstants.LOCATION_ID, mLocationId);
        intent.putExtra(DashBoadConstant.ARG_GROUP_ID, groupId);
        mParentActivity.startActivityForResult(intent, DashBoadConstant.GROUP_CONTROL_REQUEST_CODE);
        mParentActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

    }

    @Override
    protected void deleteDevice() {
        if (mAllDeviceUIManager.getAllDeviceSelectedIds().length > 0 && !isAlertDialogShowing()) {
            if (mUserLocationData instanceof RealUserLocationData) {
                showDeleteDeviceMessageBox(getString(R.string.authorize_delete_confirm));
            } else {
                showDeleteDeviceMessageBox(getString(R.string.mad_air_delete));
            }

        }
    }

    private void showDeleteDeviceMessageBox(String message) {
        mAlertDlalog = MessageBox.createTwoButtonDialog(mParentActivity, null,
                message, mParentActivity.getString(R.string.cancel), null,
                mParentActivity.getString(R.string.all_device_bottom_delete), deleteDevice);
    }

    private MessageBox.MyOnClick deleteDevice = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            if (mDialog == null || !mDialog.isShowing()) {
                mDialog = LoadingProgressDialog.show(mParentActivity, mParentActivity.getString(R.string.deleting_device));
            }
            mAllDeviceUIManager.deleteDevice();
        }
    };

    @Override
    protected void clickGroupItemOpr(Object object) {
        mDeviceGroupItem = (DeviceGroupItem) object;
        if (mCustomBaseAdapter.getButtomType() == ButtomType.MOVEINTO && !isAlertDialogShowing()) {
            mAlertDlalog = MessageBox.createTwoButtonDialog(mParentActivity, null, mParentActivity.getString(R.string.all_device_moveinto_confirm, mDeviceGroupItem.getGroupName())
                    , mParentActivity.getString(R.string.cancel), null, mParentActivity.getString(R.string.all_device_bottom_movein), moveIntoGroup);
        } else if (mCustomBaseAdapter.getButtomType() == ButtomType.NORMAL) {
            startGroupControlActivity(mDeviceGroupItem.getGroupId());
        }

    }

    @Override
    protected void showGetGroupByLocationIdFailed() {
        if (mUserLocationData != null && mUserLocationData.isDeviceListLoadDataFailed()) {
            setLoadDataFailedLayout();
        }
    }

    @Override
    protected void startDeviceControlActivity(Intent intent) {
        mParentActivity.startActivityForResult(intent, DashBoadConstant.DEVICE_CONTROL_REQUEST_CODE);
    }

    private void sendReflashGroupBroadCast() {
        Intent intent = new Intent(HPlusConstants.BROADCAST_ACTION_GROUP_REFLASH);
        mParentActivity.sendBroadcast(intent);
    }

    private void initAllDeviceView() {

        initAdapter();
        initAllDeviceUIBottomBtn(mAllDeviceUIManager.isHasGroup());
        setAllDevieLayoutVisible(mUserLocationData.isHaveSelectedDeviceThisLocation(), mUserLocationData instanceof RealUserLocationData);
    }

    public void refreshAllDeviceFragment(UserLocationData userLocationData) {
        initUiManager(userLocationData);
        if (!isInSelectStatus() && isAfterInit && mAllDeviceUIManager != null && (mDialog == null || !mDialog.isShowing())) {
            LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "initAllDeviceView");
            initAllDeviceView();
            if (!UserAllDataContainer.shareInstance().isHasNetWorkError() && NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication())) {
                mAllDeviceUIManager.getGroupInfoFromServer(true);
            }
        }
    }

}
