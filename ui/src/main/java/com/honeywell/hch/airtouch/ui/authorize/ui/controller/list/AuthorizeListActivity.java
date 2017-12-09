package com.honeywell.hch.airtouch.ui.authorize.ui.controller.list;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthBaseModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.AuthHomeList;
import com.honeywell.hch.airtouch.ui.authorize.ui.adapter.AuthParentAdapter;
import com.honeywell.hch.airtouch.ui.authorize.ui.controller.edit.AuthorizeOwnerEditActivity;
import com.honeywell.hch.airtouch.ui.authorize.ui.controller.edit.AuthorizeReceiverEditActivity;
import com.honeywell.hch.airtouch.ui.authorize.ui.controller.group.AuthorizeGroupDeviceListActivity;
import com.honeywell.hch.airtouch.ui.authorize.ui.controller.invite.AuthorizeInviteUserActivity;
import com.honeywell.hch.airtouch.ui.common.ui.view.DropDownAnimationManager;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;

import java.util.List;

/**
 * Created by Vincent on 29/1/16.
 */
public class AuthorizeListActivity extends AuthorizeBaseActivity implements AuthorizeBaseActivity.AuthorizeClick {
    protected String TAG = "AuthorizeListActivity";
    private Class mClass = this.getClass();
    private ExpandableListView mExpandableLv;
    private AuthParentAdapter adapter;
    private RelativeLayout mAuthRootLayout;
    private TextView mTitleTv;
    private TextView mNoHomeData;
    private List<AuthHomeList> mAuthHomeLists;
    private BroadcastReceiver latestMessageReceiver;

    private long mUpdateTime = HPlusConstants.DEFAULT_TIME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_list);
        initStatusBar();
        init();
        initReceiver();
        initUiData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "onNewIntent");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mAuthorizeUiManager.parseAuthHomeList(mAuthHomeLists, bundle);
            boolean isOwnerDevice = mAuthHomeLists != null && mAuthHomeLists.size() != EMPTY;
            changeAdapter(isOwnerDevice);
            mDropDownAnimationManager = new DropDownAnimationManager(mAuthRootLayout, mContext);
            mDropDownAnimationManager.showDragDownLayout(getString(mAuthorizeUiManager.parseNotificatonRemind(bundle)), false);
        }
        //reflash list
        mAuthorizeUiManager.getAuthGroupDevices();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();
    }

    @Override
    protected void dealErrorCallBack(ResponseResult responseResult, int id) {
        disMissDialog();
        if (NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication()) && !UserAllDataContainer.shareInstance().isHasNetWorkError()){
            errorHandle(responseResult, getString(id));
        }
    }

    private void init() {
        mExpandableLv = (ExpandableListView) findViewById(R.id.auth_request_elv);
        mAuthRootLayout = (RelativeLayout) findViewById(R.id.root_view_id);
        mTitleTv = (TextView) findViewById(R.id.title_textview_id);
        mNoHomeData = (TextView) findViewById(R.id.auth_no_device_tv);
        mDialog = LoadingProgressDialog.show(mContext, getString(R.string.enroll_loading));
        mDropDownAnimationManager = new DropDownAnimationManager(mAuthRootLayout, mContext);
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(HPlusConstants.AUTH_NOTIFY_MESSAGE);
        latestMessageReceiver = new LatestMessageReceiver();
        registerReceiver(latestMessageReceiver, intentFilter);
    }

    private void initUiData() {
        mTitleTv.setText(R.string.authorize_list);
        mAuthorizeUiManager.getAuthGroupDevices();
        showmNoHomeDataTv(mAuthorizeUiManager.isOwnDevice());
    }

    private void unRegisterReceiver() {
        if (latestMessageReceiver != null) {
            unregisterReceiver(latestMessageReceiver);
        }
    }

    @Override
    protected void dealSuccessCallBack(ResponseResult responseResult) {
        super.dealSuccessCallBack(responseResult);
        mAuthHomeLists = mAuthorizeUiManager.parseAuthHomeList(responseResult);
        loadUiData();
        mUpdateTime = System.currentTimeMillis();
    }

    private void initAdapter() {
        adapter = new AuthParentAdapter(mContext, mAuthHomeLists);
        mExpandableLv.setAdapter(adapter);
    }

    private void loadUiData() {
        boolean isOwnerDevice = mAuthHomeLists != null && mAuthHomeLists.size() != EMPTY;
        showmNoHomeDataTv(isOwnerDevice);
        if (adapter == null) {
            initAdapter();
            initExpandGroup(isOwnerDevice);
            initListener();
        } else {
            changeAdapter(isOwnerDevice);
        }

    }

    private void showmNoHomeDataTv(boolean isOwnerDevice) {
        if (isOwnerDevice) {
            mNoHomeData.setVisibility(View.GONE);
        } else {
            mNoHomeData.setVisibility(View.VISIBLE);
        }
    }

    private void initExpandGroup(boolean isOwnerDevice) {
        if (isOwnerDevice) {
            for (int i = EMPTY; i < mAuthHomeLists.size(); i++) {
                mExpandableLv.expandGroup(i);
            }
        }
    }

    private void initListener() {
        mExpandableLv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        adapter.setOnChildTreeViewClickListener(new AuthParentAdapter.OnChildTreeViewClickListener() {
            @Override
            public void onClickPosition(int parentPosition, int groupPosition, int childPosition) {
                AuthHomeList authHomeList = mAuthHomeLists.get(parentPosition);
                AuthBaseModel authDeviceModel = mAuthorizeUiManager.parseAuthDevice(authHomeList, parentPosition, groupPosition, childPosition, mClass);
                if (authHomeList.getType() == AuthHomeList.Type.TOMEHOME) {
                    startIntent(AuthorizeReceiverEditActivity.class, authDeviceModel, ClickType.REMOVE);
                } else {
                    if (authDeviceModel.getmAuthorityToList() == null && authDeviceModel.authorizeClickAble()) {
                        startIntent(AuthorizeInviteUserActivity.class, authDeviceModel, ClickType.AUTHROIZE);
                    } else if (authDeviceModel.revokeClickAble()) {
                        startIntent(AuthorizeOwnerEditActivity.class, authDeviceModel, ClickType.REVOKE);
                    } else {
                        startIntent(AuthorizeGroupDeviceListActivity.class, authDeviceModel, null);
                    }
                }
            }
        });

        adapter.setOnChildItemClickListener(new AuthParentAdapter.OnChildItemClickListener() {

            @Override
            public void onClickPosition(AdapterView<?> parent, View view, int parentPosition, int groupPosition, int childPosition, long id) {
                AuthHomeList authHomeList = mAuthHomeLists.get(parentPosition);
                Object object = parent.getItemAtPosition(childPosition);
                if (object instanceof AuthBaseModel) {
                    AuthBaseModel authBaseModel = (AuthBaseModel) object;
                    mAuthorizeUiManager.parseAuthDevice(authHomeList, authBaseModel, parentPosition, groupPosition, childPosition - 1, mClass);
                    if (authHomeList.getType() == AuthHomeList.Type.TOMEHOME) {
                        startIntent(AuthorizeReceiverEditActivity.class, authBaseModel, ClickType.REMOVE);
                    } else {
                        if (((AuthBaseModel) object).getmAuthorityToList() == null && ((AuthBaseModel) object).authorizeClickAble()) {
                            startIntent(AuthorizeInviteUserActivity.class, authBaseModel, ClickType.AUTHROIZE);
                        } else if (((AuthBaseModel) object).revokeClickAble()) {
                            startIntent(AuthorizeOwnerEditActivity.class, authBaseModel, ClickType.REVOKE);
                        } else {
                            startIntent(AuthorizeGroupDeviceListActivity.class, authBaseModel, null);
                        }
                    }
                }
            }

        });
    }

    @Override
    public void doClick(View v) {
        super.doClick(v);
    }

    private class LatestMessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (HPlusConstants.AUTH_UNREAD_REFLASH_MESSAGE.equals(action)) {
            } else if (HPlusConstants.AUTH_NOTIFY_MESSAGE.equals(action)) {
            }
        }
    }

    private void changeAdapter(boolean isOwnerDevice) {
        adapter.changeData(mAuthHomeLists);
        initExpandGroup(isOwnerDevice);
    }
}
