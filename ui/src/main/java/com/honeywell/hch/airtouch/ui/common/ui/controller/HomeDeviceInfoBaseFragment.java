package com.honeywell.hch.airtouch.ui.common.ui.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.devices.water.model.UnSupportDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceGroupItem;
import com.honeywell.hch.airtouch.plateform.http.manager.model.SelectStatusDeviceItem;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.permission.Permission;
import com.honeywell.hch.airtouch.plateform.permission.PermissionListener;
import com.honeywell.hch.airtouch.plateform.update.UpdateManager;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.manager.model.Category;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.common.ui.adapter.CategoryAdapter;
import com.honeywell.hch.airtouch.ui.common.ui.view.AllDeviceItemView;
import com.honeywell.hch.airtouch.ui.common.ui.view.BottomIconViewItem;
import com.honeywell.hch.airtouch.ui.common.ui.view.BottomView;
import com.honeywell.hch.airtouch.ui.common.ui.view.GroupContolListView;
import com.honeywell.hch.airtouch.ui.common.ui.view.HplusNetworkErrorLayout;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.control.manager.model.ControlConstant;
import com.honeywell.hch.airtouch.ui.control.ui.device.controller.DeviceControlActivity;
import com.honeywell.hch.airtouch.ui.control.ui.group.controller.GroupControlActivity;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.AllDeviceUIBaseManager;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.DeviceInfoBaseUIManager;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.GroupManager;
import com.honeywell.hch.airtouch.ui.main.ui.common.MainActivity;
import com.honeywell.hch.airtouch.ui.trydemo.ui.TryDemoMainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 20/7/16.
 */
public class HomeDeviceInfoBaseFragment extends BaseRequestFragment implements PermissionListener {


    protected int mLocationId = 0;
    protected UserLocationData mUserLocationData;

    protected BottomView mBottomView;

    //should set gone in all device activity
    protected TextView mInputTextView;


    protected TextView mTitleTextview;
    protected CategoryAdapter mCustomBaseAdapter;

    protected TextView mGroupNameTextView;
    protected EditText mGroupNameEditText;

    protected GroupContolListView mDeviceListView;

    protected boolean isActivityResume = false;

    protected AlertDialog mAlertDlalog;
    protected AllDeviceUIBaseManager mAllDeviceUIManager;
    protected boolean isAfterInit = false;
    protected AlertDialog mMessageBoxDialog;

    protected ArrayList<Category> mListData;

    private boolean isPermerssion = false;


    public enum ButtomType {
        NORMAL(true, true),
        MOVEINTO(false, true),
        DELETE(true, false),
        NEWGROUP(false, false),
        SELECT(true, false);

        public boolean mDeviceAlphaEnable;

        public boolean mGroupAlphaEnable;

        private ButtomType(boolean deviceAlphaEnable, boolean groupAlphaEnable) {
            mDeviceAlphaEnable = deviceAlphaEnable;
            mGroupAlphaEnable = groupAlphaEnable;
        }

    }


    protected void getGroupInfoFromServier(boolean isRefreshOpr) {
        if (!NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication()) || !mAllDeviceUIManager.getGroupInfoFromServer(isRefreshOpr)) {
            disMissDialog();
            return;
        }
    }

    protected GroupManager.SuccessCallback mSuccessCallback = new GroupManager.SuccessCallback() {
        @Override
        public void onSuccess(ResponseResult responseResult) {
            dealSuccessCallBack(responseResult);
        }
    };

    protected GroupManager.ErrorCallback mErrorCallBack = new GroupManager.ErrorCallback() {
        @Override
        public void onError(ResponseResult responseResult, int id) {
            dealErrorCallBack(responseResult, id);
        }
    };

    private void resetErrorResponse() {
        setEndTextTipVisible();
        normalStatusView();
        setClickButtonPreviousStatus();
    }

    /**
     * 需要子类进行重写
     */
    protected void setEndTextTipVisible() {
    }


    /*
    group control 不能点击
     */
    public void setClickButtonNormalStatus() {

    }

    /*
    恢复之前状态
     */
    public void setClickButtonPreviousStatus() {

    }

    /*
        group control update group name need overide
     */
    @Override
    protected void dealErrorCallBack(ResponseResult responseResult, int id) {
        super.dealErrorCallBack(responseResult, id);
        switch (responseResult.getRequestId()) {
            case DELETE_DEVICE:
//                getGroupInfoFromServier(false);
                resetErrorResponse();
                HplusNetworkErrorLayout mNetWorkErrorLayout = getNetWorkErrorLayout();
                if (mNetWorkErrorLayout != null
                        && mNetWorkErrorLayout.getVisibility() != View.VISIBLE && !isAlertDialogShowing()) {
                    mAlertDlalog = MessageBox.createSimpleDialog(mParentActivity, null,
                            mParentActivity.getResources().getString(R.string.delete_device_fail), null, null);
                } else {
                    errorHandle(responseResult, mParentActivity.getString(id));
                }
                break;

            case GET_GROUP_BY_LOCATION_ID:
                showGetGroupByLocationIdFailed();
                resetErrorResponse();
                mUserLocationData.setDeviceListLoadFailed();
                errorHandle(responseResult, mParentActivity.getString(id));
                break;
            case CREATE_GROUP:
            case ADD_DEVICE_TO_GROUP:
            case DELETE_DEVICE_FROM_GROUP:
            case DELETE_GROUP:
                resetErrorResponse();
                errorHandle(responseResult, mParentActivity.getString(id));
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        isActivityResume = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isActivityResume = false;
    }


    /**
     * 当界面创建的时候调用
     *
     * @param deviceInfoBaseUIManager
     */
    protected void initManagerCallBack(DeviceInfoBaseUIManager deviceInfoBaseUIManager) {

        deviceInfoBaseUIManager.setErrorCallback(mErrorCallBack);
        deviceInfoBaseUIManager.setSuccessCallback(mSuccessCallback);
    }

    /**
     * 当界面销毁的时候
     *
     * @param deviceInfoBaseUIManager
     */
    protected void resetManagerCallBack(DeviceInfoBaseUIManager deviceInfoBaseUIManager) {
        deviceInfoBaseUIManager.setErrorCallback(null);
        deviceInfoBaseUIManager.setSuccessCallback(null);
    }

    protected void showBottomStatusView() {
        showBottomView();
        //没有选中任何设备的时候底部按钮不可点击且设置为灰
        mBottomView.setAllBtnExceptSelectClickable(false);
        mCustomBaseAdapter.setButtomType(ButtomType.SELECT);
        mCustomBaseAdapter.notifyDataSetChanged();
    }

    protected void normalStatusView() {
        if (isAfterInit) {
            hideBottomView();
            mCustomBaseAdapter.setButtomType(ButtomType.NORMAL);
            resetSelectAllBtn();
            mBottomView.setAllBtnClickable(true);
        }
    }

    /**
     * 是否处于选择状态，处于选择状态时候，需要有特殊的操作，比如点击是选择设备列表，并且把“>”隐藏等操作
     * 子类需要重写
     *
     * @return
     */
    protected boolean isInSelectStatus() {
        return DeviceInfoBaseUIManager.isEditStatus();
    }

    protected void initBottomView(View v) {
        mBottomView = (BottomView) v.findViewById(R.id.this_bottom_layout_id);
        mBottomView.setFirstSelectBtnClickListen(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomView.isAllSelectStrInFirstBtn()) {
                    mCustomBaseAdapter.setAllDeviceSelectStatus(true);
                    mBottomView.setAllBtnClickable(true);
                    mBottomView.setFirstBtnSelectStatusText(mParentActivity.getString(R.string.all_device_bottom_deselectall));
                } else {
                    resetSelectAllBtn();
                    mBottomView.setAllBtnExceptSelectClickable(false);
                }
            }
        });
    }


    private void resetSelectAllBtn() {
        mCustomBaseAdapter.setAllDeviceSelectStatus(false);
        mBottomView.setFirstBtnSelectStatusText(mParentActivity.getString(R.string.all_device_bottom_selectall));
    }


    public BottomIconViewItem addBottomViewItem(int imageId, int strId, View.OnClickListener onClickListener) {
        BottomIconViewItem bottomIconViewItem = new BottomIconViewItem(this.mParentActivity);
        bottomIconViewItem.setElementOfTheView(imageId, strId);
        bottomIconViewItem.setOnClickListener(onClickListener);
        return bottomIconViewItem;
    }


    public void initEnterpriseAccountBottomBtn() {
        List<BottomIconViewItem> bottomIconViewItemList = new ArrayList<>();
        addDeleteBtn(bottomIconViewItemList);
        mBottomView.addBtnToThisView(bottomIconViewItemList);
    }


    public void addDeleteBtn(List<BottomIconViewItem> bottomIconViewItemList) {

        bottomIconViewItemList.add(addBottomViewItem(R.drawable.bottom_delete, R.string.all_device_bottom_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ifNetworkOff()) {
                    return;
                }
                deleteDevice();
            }
        }));
    }

    protected boolean ifNetworkOff() {
        if (!NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication()) && mParentActivity != null) {
            if (mParentActivity instanceof MainActivity) {
                ((MainActivity) mParentActivity).flingTheDeviceTitle();
            } else {
                dealWithNetWorkResponse();
            }
            return true;
        }
        return false;
    }

    /**
     * 删除设备，在alldevice 界面和GroupControl界面需要实现不同的删除操作
     * 需要子类进行重写
     */
    protected void deleteDevice() {
    }

    /**
     * 显示bottom view
     */
    public void showBottomView() {
        Animation showFromBottom = AnimationUtils.loadAnimation(mParentActivity, R.anim.in_from_bottom);
        showFromBottom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBottomView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBottomView.startAnimation(showFromBottom);

    }

    /**
     * 隐藏bottom view
     */
    public void hideBottomView() {
        if (mBottomView.getVisibility() != View.GONE) {
            Animation showFromBottom = AnimationUtils.loadAnimation(mParentActivity, R.anim.out_to_bottom);
            mBottomView.setVisibility(View.GONE);
            mBottomView.startAnimation(showFromBottom);
        }
    }


    public UserLocationData getUserLocationData() {
        return mUserLocationData;
    }


    /**
     * 判断list view列表里是否有数据
     *
     * @return
     */
    public boolean isHasDataInList() {
        if (mCustomBaseAdapter != null && mCustomBaseAdapter.getmListData() != null
                && mCustomBaseAdapter.getmListData().size() > 0) {
            return true;
        }
        return false;
    }

    protected void initItemSelectedListener() {
        mDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = parent.getItemAtPosition(position);
                if (object instanceof SelectStatusDeviceItem) {
                    SelectStatusDeviceItem selectStatusDeviceItem = ((SelectStatusDeviceItem) object);
                    //点击设备列表
                    if (mCustomBaseAdapter.getButtomType() != ButtomType.NORMAL &&
                            mCustomBaseAdapter.getButtomType().mDeviceAlphaEnable == true) {

                        selectStatusDeviceItem.setIsSelected(!selectStatusDeviceItem.isSelected());

                        ((AllDeviceItemView) view).initViewHolder(view, selectStatusDeviceItem, mCustomBaseAdapter.getButtomType());

                        //设置按钮可点击的效果
                        setBottomViewBtnStatusAfterSelected();

                    } else if (mCustomBaseAdapter.getButtomType() == ButtomType.NORMAL) {
                        if (selectStatusDeviceItem.getDeviceItem() != null && selectStatusDeviceItem.getDeviceItem() instanceof UnSupportDeviceObject) {
                            showMessageBox();
                            mHPlusPermission.checkAndRequestPermission(Permission.PermissionCodes.STORAGE_REQUEST_CODE, mParentActivity);
                        } else {
                            if (mParentActivity instanceof GroupControlActivity || mParentActivity instanceof MainActivity) {
                                startControlActivity(selectStatusDeviceItem);
                            } else if (mParentActivity instanceof TryDemoMainActivity) {
                                startTryDemoControlActivity(parent, view, position, id);
                            }
                        }

                    }
                } else if (object instanceof DeviceGroupItem) {
                    clickGroupItemOpr(object);
                }
            }
        });
    }


    private void startTryDemoControlActivity(AdapterView<?> parent, View view, int position, long id) {
        Object object = parent.getItemAtPosition(position);
        SelectStatusDeviceItem selectStatusDeviceItem = ((SelectStatusDeviceItem) object);
        //点击设备列表
        Intent intent = new Intent(mParentActivity, DeviceControlActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(DeviceControlActivity.ARG_LOCATION, mLocationId);
        bundle.putInt(DeviceControlActivity.ARG_FROM_TYPE, ControlConstant.FROM_DEMO_CONTROL);
        bundle.putInt(DeviceControlActivity.ARG_DEVICE_ID, selectStatusDeviceItem.getDeviceItem().getDeviceId());
        intent.putExtras(bundle);
        startActivityForResult(intent, DashBoadConstant.DEVICE_CONTROL_REQUEST_CODE);
        mParentActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }


    private void startControlActivity(SelectStatusDeviceItem selectStatusDeviceItem) {
        if (AppManager.getLocalProtocol().getRole().canShowDeviceDetail()) {
            Intent intent = new Intent(HomeDeviceInfoBaseFragment.this.mParentActivity, DeviceControlActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(DeviceControlActivity.ARG_LOCATION, mLocationId);
            bundle.putInt(DeviceControlActivity.ARG_FROM_TYPE, ControlConstant.FROM_NORMAL_CONTROL);
            bundle.putInt(DeviceControlActivity.ARG_DEVICE_ID, selectStatusDeviceItem.getDeviceItem().getDeviceId());
            intent.putExtras(bundle);
            startDeviceControlActivity(intent);
            HomeDeviceInfoBaseFragment.this.mParentActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }
    }


    /**
     * 启动DeviceControlActivity，在AllDevice界面用 activity去启动，而在GroupControlFrament里直接启动
     *
     * @param intent
     */
    protected void startDeviceControlActivity(Intent intent) {

    }

    /**
     * 用户选择了一个设备后，需要在allDevice界面和GroupActivity界面设置底部按钮的状态
     */
    protected void setBottomViewBtnStatusAfterSelected() {

    }

    /**
     * 用于点击组的操作，在GroupControlActivity里不需要实现
     * 需要子类进行重写
     *
     * @param object
     */
    protected void clickGroupItemOpr(Object object) {
    }


    protected void showGetGroupByLocationIdFailed() {

    }

    /**
     * 操作的时候显示的loading
     *
     * @param strId
     */
    protected void showLoadingDialog(int strId) {
        if (mDialog == null || !mDialog.isShowing()) {
            mDialog = LoadingProgressDialog.show(this.mParentActivity, mParentActivity.getString(strId));
        }
    }

    protected boolean isAlertDialogShowing() {
        if (mAlertDlalog == null || !mAlertDlalog.isShowing()) {
            return false;
        }
        return true;
    }


    @Override
    public void onPermissionGranted(int permissionCode) {
        if (permissionCode == Permission.PermissionCodes.STORAGE_REQUEST_CODE) {
            isPermerssion = true;
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
                isPermerssion = mHPlusPermission.verifyPermissions(grantResults);
                break;
        }
    }


    private void showMessageBox() {
        if (mMessageBoxDialog == null || !mMessageBoxDialog.isShowing()) {
            mMessageBoxDialog = MessageBox.createTwoButtonDialog(mParentActivity, null, mParentActivity.getString(R.string.unsupport_update_msg)
                    , mParentActivity.getString(R.string.cancel), null, mParentActivity.getString(R.string.unsupport_update_btn), new MessageBox.MyOnClick() {
                        @Override
                        public void onClick(View v) {
                            starDownLoad(mParentActivity);
                            mMessageBoxDialog.dismiss();
                        }
                    });
        }

    }

    private void dialogDismiss() {
        if (mMessageBoxDialog != null) {
            mMessageBoxDialog.cancel();
        }
        if (mDialog != null) {
            mDialog.cancel();
        }
    }

    private void starDownLoad(final Context context) {
        UpdateManager.getInstance().starDownLoadAPK(context, isPermerssion, false);
    }

}
