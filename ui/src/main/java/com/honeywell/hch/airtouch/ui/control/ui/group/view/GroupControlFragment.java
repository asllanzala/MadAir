package com.honeywell.hch.airtouch.ui.control.ui.group.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceGroupItem;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.common.ui.adapter.CategoryAdapter;
import com.honeywell.hch.airtouch.ui.common.ui.controller.HomeDeviceInfoBaseFragment;
import com.honeywell.hch.airtouch.ui.common.ui.view.BottomIconViewItem;
import com.honeywell.hch.airtouch.ui.common.ui.view.GroupContolListView;
import com.honeywell.hch.airtouch.ui.common.ui.view.HplusNetworkErrorLayout;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.control.manager.group.GroupControlUIManager;
import com.honeywell.hch.airtouch.ui.control.manager.model.DeviceMode;
import com.honeywell.hch.airtouch.ui.control.ui.device.controller.DeviceControlActivity;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.AllDeviceUIManager;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.DeviceInfoBaseUIManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 20/7/16.
 */
public class GroupControlFragment extends HomeDeviceInfoBaseFragment implements View.OnClickListener {
    protected String TAG = "GroupControlFragment";
    public static final int DEFAULT_GROUP_ID = 0;
    private InputMethodManager mInputMethodManager;
    private GroupControlUIManager mGroupControlUIManager;
    private ScrollView mScrollView;
    private GroupControlView mGroupControlView;

    private int mGroupId = DEFAULT_GROUP_ID;
    private DeviceGroupItem mDeviceGroupItem;
    private Boolean mIsEditGroupNameMode = false;
    private int mSendGroupMode = DeviceMode.MODE_UNDEFINE;
    private boolean isControlling = false;
    private IntentFilter mGroupControlBraodcastIntentFilter;

    private RelativeLayout mHomeRl;
    private RelativeLayout mSleepRl;
    private RelativeLayout mAwayRl;
    private RelativeLayout mAwakeRl;
    private LinearLayout mGroupControlLl;
    private FrameLayout mTitleBackIcon;
    private TextView mEndTextTip;
    private ResponseResult mGroupControlResult = null;


    public static GroupControlFragment newInstance(Activity activity) {
        GroupControlFragment fragment = new GroupControlFragment();
        fragment.initActivity(activity);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mParentActivity != null) {
            dealWithIntent();
            registerBroadCastReceiver();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_control, container, false);

        if (mParentActivity != null) {
            initStatusBar(view);
            initGroupView(view);
            initGroupTitleView(view);
            initGroupControlView(view);
            initBottomView(view);
            initClickListener();
            if (mDeviceGroupItem != null) {
                initData();
                initGroupContolBottomBtn();
                setupGroupNameEditText();
                isFlashingView();
                initItemSelectedListener();
                isAfterInit = true;
            }
        }
        return view;
    }


    private void dealWithIntent() {
        mLocationId = this.mParentActivity.getIntent().getIntExtra(HPlusConstants.LOCATION_ID, 0);
        mUserLocationData = UserDataOperator.getUserLocationByID(mLocationId, UserAllDataContainer.shareInstance().getUserLocationDataList());
        initAllDeviceUIManager();
        Bundle bundle = this.mParentActivity.getIntent().getExtras();
        if (bundle != null) {
            mGroupId = bundle.getInt(DashBoadConstant.ARG_GROUP_ID);
            mGroupControlUIManager.setGroupId(mGroupId);
            mDeviceGroupItem = mGroupControlUIManager.getDeviceGroupItemByGroupId(mGroupId);
        }
    }

    protected void initAllDeviceUIManager() {

        if (mAllDeviceUIManager == null) {
            mAllDeviceUIManager = new AllDeviceUIManager(mLocationId);
        } else {
            mAllDeviceUIManager.resetData(mLocationId);
        }

        mGroupControlUIManager = new GroupControlUIManager(mLocationId);
        initManagerCallBack(mGroupControlUIManager);
    }

    protected void initGroupView(View view) {
        initDragDownManager(view, R.id.root_view_id);
        mDeviceListView = (GroupContolListView) view.findViewById(R.id.group_control_listView);
        mInputMethodManager = (InputMethodManager) this.mParentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        mScrollView = (ScrollView) view.findViewById(R.id.group_control_scrollview);
        disableAutoScrollToBottom();
        mHomeRl = (RelativeLayout) view.findViewById(R.id.group_control_home_rl);
        mSleepRl = (RelativeLayout) view.findViewById(R.id.group_control_sleep_rl);
        mAwayRl = (RelativeLayout) view.findViewById(R.id.group_control_away_rl);
        mAwakeRl = (RelativeLayout) view.findViewById(R.id.group_control_awake_rl);
        mGroupControlLl = (LinearLayout) view.findViewById(R.id.group_control_ll);
        initAdapter();
    }

    private void initGroupTitleView(View view) {
        mEndTextTip = (TextView) view.findViewById(R.id.end_text_tip);
        mGroupNameEditText = (EditText) view.findViewById(R.id.group_title_edit_id);
        mGroupNameTextView = (TextView) view.findViewById(R.id.message_title_text_id);
        mTitleBackIcon = (FrameLayout) view.findViewById(R.id.enroll_back_layout);
        mEndTextTip.setText(getActivity().getResources().getString(R.string.all_device_title_select));

        if (UserAllDataContainer.shareInstance().isHasNetWorkError()) {
            dealNetworkoffAndOnAction();
        }
    }

    private void initClickListener() {
        mTitleBackIcon.setOnClickListener(this);
        mHomeRl.setOnClickListener(this);
        mSleepRl.setOnClickListener(this);
        mAwayRl.setOnClickListener(this);
        mAwakeRl.setOnClickListener(this);
        mGroupControlLl.setOnClickListener(this);
        mEndTextTip.setOnClickListener(this);
    }

    public void initGroupControlView(View view) {
        mGroupControlView = (GroupControlView) view.findViewById(R.id.group_control_GroupControlView);
        if (AppManager.isEnterPriseVersion()) {
            mGroupControlView.setVisibility(View.GONE);
        } else {
            mGroupControlView.setVisibility(View.VISIBLE);
        }
    }


    protected void showBottomStatusView() {
        DeviceInfoBaseUIManager.setIsEditStatus(true);
        super.showBottomStatusView();
        mEndTextTip.setText(getActivity().getResources().getString(R.string.all_device_title_cancel));
        mTitleBackIcon.setVisibility(View.INVISIBLE);
    }

    protected void normalStatusView() {
        DeviceInfoBaseUIManager.setIsEditStatus(false);
        super.normalStatusView();
        mEndTextTip.setText(getActivity().getResources().getString(R.string.all_device_title_select));
        mTitleBackIcon.setVisibility(View.VISIBLE);
        setClickButtonPreviousStatus();
    }

    private void registerBroadCastReceiver() {
        mGroupControlBraodcastIntentFilter = new IntentFilter();
        mGroupControlBraodcastIntentFilter.addAction(HPlusConstants.BROADCAST_ACTION_STOP_FLASHINGTASK);
        mGroupControlBraodcastIntentFilter.addAction(HPlusConstants.BROADCAST_ACTION_GROUP_REFLASH);
        this.mParentActivity.registerReceiver(mGroupCommandReceiver, mGroupControlBraodcastIntentFilter);
    }

    @Override
    protected void setEndTextTipVisible() {
        if (mUserLocationData.isIsLocationOwner()) {
            int visible = isHasDataInList() ? View.VISIBLE : View.GONE;
            setEndTextVisible(visible);
        } else {
            setEndTextVisible(View.GONE);
        }

        setClickButtonPreviousStatus();
    }

    public void dealNetworkoffAndOnAction() {
        setEditStatusNetworkErrorView();
    }

    private void setEditStatusNetworkErrorView() {
        if (mNetWorkErrorLayout != null) {
            if (!NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication())) {
                mNetWorkErrorLayout.setVisibility(View.VISIBLE);
                mNetWorkErrorLayout.setErrorMsg(HplusNetworkErrorLayout.NETWORK_OFF);
            } else if (UserAllDataContainer.shareInstance().isHasNetWorkError()) {
                mNetWorkErrorLayout.setVisibility(View.VISIBLE);
                mNetWorkErrorLayout.setErrorMsg(HplusNetworkErrorLayout.CONNECT_SERVER_ERROR);
            } else {
                mNetWorkErrorLayout.setVisibility(View.GONE);
            }
        }
    }

    private void setEndTextVisible(int visible) {
        if (mEndTextTip != null) {
            mEndTextTip.setVisibility(visible);
        }
    }


    private void initData() {
        setEndTextTipVisible();
        setGroupName();
        isControlling = mGroupControlUIManager.getIsFlashing(mGroupId);
        mSendGroupMode = mGroupControlUIManager.getSendGroupMode(mGroupId);
        mGroupControlView.setHeadLayout(mDeviceGroupItem.getScenarioOperation());
    }

    private void setGroupName() {
        mDeviceGroupItem = mGroupControlUIManager.getDeviceGroupItemByGroupId(mGroupId);
        if (mDeviceGroupItem != null) {
            mGroupNameTextView.setText(mDeviceGroupItem.getGroupName());
        }
    }

    private void isFlashingView() {
        if (isControlling) {
            mGroupControlView.startFlickMode(mSendGroupMode);
            mEndTextTip.setVisibility(View.GONE);
            mGroupNameTextView.setClickable(false);
        }
    }

    private void initAdapter() {
        mListData = mGroupControlUIManager.getGroupDeviceCategoryData(mGroupId);
        mCustomBaseAdapter = new CategoryAdapter(this.mParentActivity, mListData);

        //因为groupControlActivity里只有一个Category，并且是Devices的。
        if (mListData.size() != 0 && mListData.get(0) != null) {
            mGroupControlUIManager.setGroupModelDevieList(mListData.get(0).getmSelectDeviceListItem(), mGroupId);
        }
        mDeviceListView.setAdapter(mCustomBaseAdapter);
    }


    private void setScenarioMode(int scenarioMode) {
        // Observer can not control group
        if (!AppManager.getLocalProtocol().getRole()
                .canControlGroup(mDeviceGroupItem.getPermission())) {
            mDropDownAnimationManager.showDragDownLayout(getActivity().getString(R.string.group_no_auth), true);
            return;
        }
        mGroupControlUIManager.clearFlashPreference(this.mParentActivity);
        mSendGroupMode = scenarioMode;
        mGroupControlUIManager.sendScenarioToGroup(mGroupId, scenarioMode);
        switch (scenarioMode) {
            case DeviceMode.MODE_HOME:
                mGroupControlView.clickHomeBtn();
                break;
            case DeviceMode.MODE_SLEEP:
                mGroupControlView.clickSleepBtn();
                break;
            case DeviceMode.MODE_AWAY:
                mGroupControlView.clickAwayBtn();
                break;
            case DeviceMode.MODE_AWAKE:
                mGroupControlView.clickAwakeBtn();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        resetManagerCallBack(mGroupControlUIManager);
        this.mParentActivity.unregisterReceiver(mGroupCommandReceiver);
        mGroupControlView.stopAllFlick();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isAfterInit) {
            mGroupControlView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mParentActivity.finish();

                }
            }, 400);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void dealSuccessCallBack(ResponseResult responseResult) {
//        super.dealSuccessCallBack(responseResult);
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "success Callback requestId: " + responseResult.getRequestId());
        switch (responseResult.getRequestId()) {
            case GET_GROUP_BY_LOCATION_ID:
                if (!DeviceInfoBaseUIManager.isEditStatus()) {
                    refreshData();
                }
                break;

            case DELETE_GROUP:
            case DELETE_DEVICE:
            case DELETE_DEVICE_FROM_GROUP:
                mAllDeviceUIManager.getGroupInfoFromServer(false);
                normalStatusView();
                break;
            case UPDATE_GROUP_NAME:
                mAllDeviceUIManager.getGroupInfoFromServer(false);
                normalStatusView();

                quitEditGroupNameMode();
                mGroupNameTextView.setText(mGroupNameEditText.getText());
                break;
            case MULTI_COMM_TASK:
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "responseResult.getFlag(): " + responseResult.getFlag());
                mAllDeviceUIManager.getGroupInfoFromServer(false);
                mGroupControlResult = responseResult;
//                switch (responseResult.getFlag()) {
//
//                    case HPlusConstants.COMM_TASK_SUCCEED:
//                        handleSucceededDeviceStatusView(responseResult);
//                        break;
//
//                    case HPlusConstants.COMM_TASK_PART_FAILED:
//                        handleSucceededDeviceStatusView(responseResult);
//                        mDropDownAnimationManager.showDragDownLayout(getActivity().getString(R.string.group_control_part_fail), true);
//                        break;
//
//                    case HPlusConstants.COMM_TASK_ALL_FAILED:
//                        handleSucceededDeviceStatusView(responseResult);
//                        mDropDownAnimationManager.showDragDownLayout(getActivity().getString(R.string.group_control_all_fail), true);
//                        break;
//
//                    default:
//                        break;
//
//                }
        }
    }

    @Override
    protected void dealErrorCallBack(ResponseResult responseResult, int id) {
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "error Callback requestId: " + responseResult.getRequestId());
        super.dealErrorCallBack(responseResult, id);
        switch (responseResult.getRequestId()) {
            case UPDATE_GROUP_NAME:
                quitEditGroupNameMode();
                mGroupNameTextView.setText(mDeviceGroupItem.getGroupName());
                errorHandle(responseResult, getActivity().getString(id));
                break;
            case MULTI_COMM_TASK:
            case SEND_SCENARIO_TO_GROUP:
                setHeadLayoutAndStopFlick();
                errorHandle(responseResult, getActivity().getString(id));
                break;
        }
    }

    private void setHeadLayoutAndStopFlick() {
        setEndTextTipVisible();
        mGroupControlView.setHeadLayout(mGroupControlUIManager.getSuccessGroupMode(mGroupId));
        mGroupControlView.stopAllFlick();
    }

    private void handleSucceededDeviceStatusView(ResponseResult responseResult) {
        Bundle bundle = responseResult.getResponseData();
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "bundle: " + bundle);
        if (bundle != null) {
            ArrayList<Integer> deviceIds = bundle.getIntegerArrayList(HPlusConstants.BUNDLE_DEVICES_IDS);
            if (deviceIds != null) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "deviceIds: " + deviceIds);
                for (int deviceId : deviceIds) {
                    LogUtil.log(LogUtil.LogLevel.INFO, TAG, "deviceId: " + deviceId);
                    mGroupControlUIManager.groupControlSelectStatusDeviceItem(deviceId, mListData);
                    mCustomBaseAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void judgeIfCloseActvitity() {
        if (!mGroupControlUIManager.isHasDeviceInThisGroup(mGroupId)) {
            this.mParentActivity.finish();
        }
    }

    private void setupGroupNameEditText() {
        if (!AppManager.getInstance().isEnterPriseVersion()) {
            mGroupNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Observer can not control group
                    if (mDeviceGroupItem != null && !AppManager.getLocalProtocol().getRole()
                            .canRenameGroup(mDeviceGroupItem.getPermission())) {
                        return;
                    }
                    checkClickStatus(mGroupNameTextView);

                    mGroupNameEditText.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER
                                    && mInputMethodManager.isActive()) {
                                updateNameGroupProcess();
                                return true;
                            }
                            return false;
                        }
                    });
                    mInputMethodManager
                            .showSoftInput(mGroupNameEditText, InputMethodManager.SHOW_FORCED);
                }
            });

            mGroupNameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    StringUtil.maxCharacterFilter(mGroupNameEditText);
                    StringUtil.addOrEditHomeFilter(mGroupNameEditText);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    @Override
    protected void deleteDevice() {
        if (mGroupControlUIManager.getGroupDeviceSelectedIds(mGroupId).length > 0) {
            if (mGroupControlUIManager.isHasMasterDeviceInSelectedIds()) {
                MessageBox.createSimpleDialog(this.mParentActivity, null,
                        getActivity().getString(R.string.group_master_not_delete), null, null);
            } else {
                MessageBox.createTwoButtonDialog(this.mParentActivity, null,
                        getActivity().getString(R.string.authorize_delete_confirm),
                        getActivity().getString(R.string.cancel), null, getActivity().getString(R.string.all_device_bottom_delete), deleteDevice);
            }
        }
    }

    @Override
    protected void setBottomViewBtnStatusAfterSelected() {

        Integer[] selectedId = mGroupControlUIManager.getGroupDeviceSelectedIds(mGroupId);
        if (selectedId != null && selectedId.length > 0) {
            mBottomView.setAllBtnExceptSelectClickable(true);
        } else {
            mBottomView.setAllBtnExceptSelectClickable(false);
        }
        //if all items are selected ,should change the first button text to "Deselected all"
        if (mGroupControlUIManager.isAllDeviceInGroupItemSelected(mGroupId)) {
            mBottomView.setFirstBtnSelectStatusText(getActivity().getString(R.string.all_device_bottom_deselectall));
        } else {
            mBottomView.setFirstBtnSelectStatusText(getActivity().getString(R.string.all_device_bottom_selectall));
        }
    }

    @Override
    protected void startDeviceControlActivity(Intent intent) {
        startActivityForResult(intent, DashBoadConstant.DEVICE_CONTROL_REQUEST_CODE);
    }

    private MessageBox.MyOnClick deleteDevice = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            showLoadingDialog(R.string.deleting_device);
            mGroupControlUIManager.deleteDeviceFromGroup(mGroupId);
        }
    };

    public void initGroupContolBottomBtn() {
        List<BottomIconViewItem> bottomIconViewItemList = new ArrayList<>();
        if (!AppManager.isEnterPriseVersion()) {
            bottomIconViewItemList.add(addBottomViewItem(R.drawable.bottom_move_out, R.string.all_device_bottom_moveout, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ifNetworkOff()) {
                        return;
                    }
                    dealWithMoveOutClick();
                }
            }));
        }
        addDeleteBtn(bottomIconViewItemList);
        mBottomView.addBtnToThisView(bottomIconViewItemList);
    }

    private void dealWithMoveOutClick() {
        if (mGroupControlUIManager.getGroupDeviceSelectedIds(mGroupId).length > 0) {
            if (mGroupControlUIManager.isHasMasterDeviceInSelectedIds()) {
                MessageBox.createTwoButtonDialog(this.mParentActivity, null,
                        getActivity().getString(R.string.move_out_master_device),
                        getActivity().getString(R.string.cancel), null, getActivity().getString(R.string.all_device_bottom_moveout), deletGroupClick);
            } else {
                MessageBox.createTwoButtonDialog(this.mParentActivity, null,
                        getActivity().getString(R.string.move_out_to_group, mGroupControlUIManager.getGroupName(mGroupId)),
                        getActivity().getString(R.string.cancel), null, getActivity().getString(R.string.all_device_bottom_moveout), moveOutGroupClick);
            }
        }
    }

    private MessageBox.MyOnClick moveOutGroupClick = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            showLoadingDialog(R.string.enroll_loading);
            mGroupControlUIManager.moveDeviceOutFromGroup(mGroupId);
        }
    };

    private MessageBox.MyOnClick deletGroupClick = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            showLoadingDialog(R.string.enroll_loading);
            mGroupControlUIManager.deleteGroup(mGroupId);
        }
    };

    private void updateNameGroupProcess() {
        mInputMethodManager.hideSoftInputFromWindow(mGroupNameEditText.getWindowToken(), 0);

        if (mGroupNameEditText.getText().toString().equals("")) {
            quitEditGroupNameMode();
            return;
        }
        if (mGroupNameEditText.getText().toString().equals(mGroupNameTextView.getText().toString())) {
            quitEditGroupNameMode();
            return;
        }
        mDialog = LoadingProgressDialog.show(this.mParentActivity, getActivity().getString(R.string.updating_group_name));

        mGroupControlUIManager.updateGroupName(mGroupNameEditText.getText().toString(),
                mDeviceGroupItem.getGroupId());
    }

    private void quitEditGroupNameMode() {
        if (mDialog != null) {
            mDialog.cancel();
        }
        setEndTextTipVisible();
        mIsEditGroupNameMode = false;

        mGroupNameEditText.setVisibility(View.GONE);
        mGroupNameTextView.setVisibility(View.VISIBLE);
    }

    private BroadcastReceiver mGroupCommandReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isInSelectStatus() /*&& (mDialog == null || !mDialog.isShowing())*/) {
                String intentAction = intent.getAction();
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "intentAction: " + intentAction);
                if (HPlusConstants.BROADCAST_ACTION_STOP_FLASHINGTASK.equals(intentAction)) {
//                        setHeadLayoutAndStopFlick();
                } else if (HPlusConstants.BROADCAST_ACTION_GROUP_REFLASH.equals(intentAction)) {
                    disMissDialog();
                    refreshData();
                }
            }

        }
    };

    //更新数据
    private void refreshData() {
        if (!DeviceInfoBaseUIManager.isEditStatus() && isAfterInit) {
            setGroupName();
            initAdapter();
            judgeIfCloseActvitity();
            isControlling = mGroupControlUIManager.getIsFlashing(mGroupId);
            if (mGroupControlResult != null) {
//                mGroupControlView.stopAllFlick();
//                setEndTextTipVisible();
                switch (mGroupControlResult.getFlag()) {

                    case HPlusConstants.COMM_TASK_SUCCEED:
                        handleSucceededDeviceStatusView(mGroupControlResult);
                        break;

                    case HPlusConstants.COMM_TASK_PART_FAILED:
                        handleSucceededDeviceStatusView(mGroupControlResult);
                        mDropDownAnimationManager.showDragDownLayout(getActivity().getString(R.string.group_control_part_fail), true);
                        break;

                    case HPlusConstants.COMM_TASK_ALL_FAILED:
                        handleSucceededDeviceStatusView(mGroupControlResult);
                        mDropDownAnimationManager.showDragDownLayout(getActivity().getString(R.string.group_control_all_fail), true);
                        break;

                    default:
                        break;

                }
                mGroupControlResult = null;
            }
            if (/*!mGroupControlView.isStarting() &&*/ mDeviceGroupItem != null && !isControlling) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "refreshData operation: " + mDeviceGroupItem.getScenarioOperation());
                mGroupControlView.stopAllFlick();
                setEndTextTipVisible();
                mGroupControlView.setHeadLayout(mDeviceGroupItem.getScenarioOperation());
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DashBoadConstant.DEVICE_CONTROL_REQUEST_CODE:
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "onActivityResult");
                dealActivityForResult(data);
                break;
            default:
                break;
        }
    }

    private void dealActivityForResult(Intent intent) {
        int deviceId = intent.getIntExtra(DeviceControlActivity.ARG_DEVICE, 0);
        Object object = intent.getBundleExtra(DashBoadConstant.ARG_LAST_RUNSTATUS);
        if (object != null) {
            mGroupControlUIManager.setDeviceControlResult(deviceId, mListData, object);
        }
        mCustomBaseAdapter.notifyDataSetChanged();
    }

    public void setClickButtonNormalStatus() {
        mGroupControlView.setControlButton(DeviceMode.MODE_DISABLE_CLICK);
        mGroupNameTextView.setClickable(false);
    }

    @Override
    public void setClickButtonPreviousStatus() {
        mGroupControlView.setControlButton(mGroupControlUIManager.getSuccessGroupMode(mGroupId));
        mGroupNameTextView.setClickable(true);
        DeviceInfoBaseUIManager.setIsEditStatus(false);
        mEndTextTip.setText(getActivity().getResources().getString(R.string.all_device_title_select));

    }


    private void checkClickStatus(View v) {
        if (v.getId() == R.id.message_title_text_id) {
            mEndTextTip.setVisibility(View.GONE);
            setClickButtonNormalStatus();
            mIsEditGroupNameMode = true;
            mGroupNameEditText.setVisibility(View.VISIBLE);
            mGroupNameTextView.setVisibility(View.GONE);
            mGroupNameEditText.setText(mGroupNameTextView.getText().toString());
            mGroupNameEditText.setFocusable(true);
            mGroupNameEditText.setFocusableInTouchMode(true);
            mGroupNameEditText.requestFocus();
        } else if (v.getId() == R.id.group_control_home_rl ||
                v.getId() == R.id.group_control_sleep_rl || v.getId() == R.id.group_control_away_rl
                || v.getId() == R.id.group_control_awake_rl) {
            mEndTextTip.setVisibility(View.GONE);
            mGroupNameTextView.setClickable(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.enroll_back_layout) {
            this.mParentActivity.finish();
            this.mParentActivity.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            mInputMethodManager.hideSoftInputFromWindow(mGroupNameTextView.getWindowToken(), 0);
        } else if (v.getId() == R.id.group_control_power_on_rl) {
        } else if (v.getId() == R.id.group_control_home_rl) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "home");
            if (mGroupControlUIManager.canClcikable() && !isNetworkOff()) {
                setScenarioMode(DeviceMode.MODE_HOME);
                checkClickStatus(v);
            }
        } else if (v.getId() == R.id.group_control_sleep_rl) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "sleep");
            if (mGroupControlUIManager.canClcikable() && !isNetworkOff()) {
                setScenarioMode(DeviceMode.MODE_SLEEP);
                checkClickStatus(v);
            }
        } else if (v.getId() == R.id.group_control_away_rl) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "away");
            if (mGroupControlUIManager.canClcikable() && !isNetworkOff()) {
                setScenarioMode(DeviceMode.MODE_AWAY);
                checkClickStatus(v);
            }
        } else if (v.getId() == R.id.group_control_awake_rl) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "awake");
            if (mGroupControlUIManager.canClcikable() && !isNetworkOff()) {
                setScenarioMode(DeviceMode.MODE_AWAKE);
                checkClickStatus(v);
            }
        } else if (v.getId() == R.id.group_control_ll) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Linear view");
            if (mIsEditGroupNameMode) {
                updateNameGroupProcess();
            }
        } else if (v.getId() == R.id.end_text_tip) {
            setEndTip();
        }
    }

    public boolean setEndTip() {
        if (isInSelectStatus()) {
            normalStatusView();
            return true;
        } else {
            setClickButtonNormalStatus();
            showBottomStatusView();
            return false;
        }
    }

    public boolean setEndTipBackEvent() {
        if (isInSelectStatus()) {
            normalStatusView();
            return true;
        } else {
            return false;
        }
    }


    public void reGetGroupInfoFromServer() {
        mAllDeviceUIManager.getGroupInfoFromServer(false);
    }


    /**
     * 让scrollview获取到焦点，这样子view就不会获取到焦点，就不会自动滑动到底部
     */
    private void disableAutoScrollToBottom() {
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
