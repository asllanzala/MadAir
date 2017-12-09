package com.honeywell.hch.airtouch.ui.authorize.ui.controller.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthBaseModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.AuthGroupDeviceListModel;
import com.honeywell.hch.airtouch.ui.authorize.ui.adapter.AuthorizeGroupDeviceAdapter;
import com.honeywell.hch.airtouch.ui.authorize.ui.controller.edit.AuthorizeOwnerEditActivity;
import com.honeywell.hch.airtouch.ui.authorize.ui.controller.invite.AuthorizeInviteUserActivity;
import com.honeywell.hch.airtouch.ui.authorize.ui.controller.list.AuthorizeBaseActivity;
import com.honeywell.hch.airtouch.ui.common.ui.view.DropDownAnimationManager;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 11/4/16.
 */
public class AuthorizeGroupDeviceListActivity extends AuthorizeBaseActivity {
    protected String TAG = "AuthorizeGroupDeviceList";
    private Class mClass = this.getClass();
    private TextView mTitleTv;
    private RelativeLayout mAuthRootLayout;
    private ListView mGroupDeviceLv;
    private AuthBaseModel mAuthBaseModel;
    private AuthorizeGroupDeviceAdapter mGroupDeviceAdapter;
    private AuthGroupDeviceListModel mAuthGroupDeviceListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_group_list);
        initStatusBar();
        init();
        dealWithIntent();
        initListener();
        initUiData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "onNewIntent");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mAuthorizeUiManager.parseAuthGroupDeviceList(mAuthGroupDeviceListModel, bundle);
            mGroupDeviceAdapter.changeData(mAuthGroupDeviceListModel);
            mDropDownAnimationManager = new DropDownAnimationManager(mAuthRootLayout, mContext);
            mDropDownAnimationManager.showDragDownLayout(getString(mAuthorizeUiManager.parseNotificatonRemind(bundle)), false);
        }
        //reflash list
        initUiData();
    }

    private void init() {
        mGroupDeviceLv = (ListView) findViewById(R.id.auth_group_lv);
        mAuthRootLayout = (RelativeLayout) findViewById(R.id.root_view_id);
        mTitleTv = (TextView) findViewById(R.id.title_textview_id);
        mGroupDeviceAdapter = new AuthorizeGroupDeviceAdapter(mContext);
        mGroupDeviceLv.setAdapter(mGroupDeviceAdapter);
        mDropDownAnimationManager = new DropDownAnimationManager(mAuthRootLayout, mContext);

        if (NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication())){
            mDialog = LoadingProgressDialog.show(mContext, getString(R.string.enroll_loading));
        }
    }

    private void dealWithIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mAuthBaseModel = (AuthBaseModel) bundle.get(INTENTPARAMETEROBJECT);
            mTitleTv.setText(mAuthBaseModel.getModelName());
        }
    }

    private void initListener() {
        mGroupDeviceLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AuthBaseModel authBaseModel = mGroupDeviceAdapter.getItem(position);
                mAuthorizeUiManager.parseAuthDevice(mAuthGroupDeviceListModel, position, authBaseModel, mClass);
                if (authBaseModel.getmAuthorityToList() == null && authBaseModel.authorizeClickAble()) {
                    startIntent(AuthorizeInviteUserActivity.class, authBaseModel, ClickType.AUTHROIZE);
                } else if (authBaseModel.revokeClickAble()) {
                    startIntent(AuthorizeOwnerEditActivity.class, authBaseModel, ClickType.REVOKE);
                }
            }
        });
    }

    private void initUiData() {
        List<Integer> groupIds = new ArrayList<>();
        groupIds.add(mAuthBaseModel.getModelId());
        mAuthorizeUiManager.getDeviceListByGroupId(groupIds,true);
    }

    @Override
    protected void dealSuccessCallBack(ResponseResult responseResult) {
        super.dealSuccessCallBack(responseResult);
        mAuthGroupDeviceListModel = mAuthorizeUiManager.parseGroupDeviceList(responseResult);
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "mAuthGroupDeviceListModel: " + mAuthGroupDeviceListModel);
        loadUiDatas(mAuthGroupDeviceListModel);
    }

    private void loadUiDatas(AuthGroupDeviceListModel mAuthGroupDeviceListModel) {
        if (mAuthGroupDeviceListModel != null && mAuthGroupDeviceListModel.getmAuthDevices().size() != EMPTY) {
            mGroupDeviceAdapter.changeData(mAuthGroupDeviceListModel);
        }
    }
}
