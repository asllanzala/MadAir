package com.honeywell.hch.airtouch.ui.authorize.ui.controller.list;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.GetAuthUnreadMsgResponse;
import com.honeywell.hch.airtouch.ui.authorize.manager.AuthorizeManager;
import com.honeywell.hch.airtouch.ui.authorize.manager.AuthorizeUiManager;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseActivity;

import java.io.Serializable;

/**
 * Created by Vincent on 29/1/16.
 */
public class AuthorizeBaseActivity extends BaseActivity {
    protected Context mContext;
    public static final String INTENTPARAMETEROBJECT = "intentParameterObject";
    public static final String INTENTPARAMETERCLICKTYPE = "intentParameterClickType"; //0 invitation 1 remove 2revoke

    //    protected AuthorizeManager mAuthManager;
    protected AuthorizeUiManager mAuthorizeUiManager;
    protected final int EMPTY = 0;

    public enum ClickType {
        AUTHROIZE(R.string.authorize_send_invitation_successfully),
        REVOKE(R.string.authorize_send_revoke_successfully),
        REMOVE(R.string.authorize_send_remove_successfully);

        public int resource;

        public int getResource() {
            return resource;
        }

        public void setResource(int resource) {
            this.resource = resource;
        }

        private ClickType(int resource) {
            this.resource = resource;
        }

        public int getResource(ClickType clickType) {
            return clickType.resource;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initAuthManager();
    }


    private void init() {
        mContext = this;
        mAlertDialog = new AlertDialog.Builder(mContext).create();
    }

    protected void initAuthManager() {
        mAuthorizeUiManager = new AuthorizeUiManager();
        mAuthorizeUiManager.setErrorCallback(mErrorCallBack);
        mAuthorizeUiManager.setSuccessCallback(mSuccessCallback);
    }

    protected AuthorizeManager.SuccessCallback mSuccessCallback = new AuthorizeManager.SuccessCallback() {
        @Override
        public void onSuccess(ResponseResult responseResult) {
            dealSuccessCallBack(responseResult);
        }
    };

    protected AuthorizeManager.ErrorCallback mErrorCallBack = new AuthorizeManager.ErrorCallback() {
        @Override
        public void onError(ResponseResult responseResult, int id) {
            dealErrorCallBack(responseResult, id);
        }
    };

    @Override
    protected void dealErrorCallBack(ResponseResult responseResult, int id) {
        super.dealErrorCallBack(responseResult, id);
        errorHandle(responseResult, getString(id));
    }

    public void doClick(View v) {
        if (v.getId() == R.id.enroll_back_layout) {
            backIntent();
        }
    }

    public void backClick(View v) {
        backIntent();
    }

    protected void startIntent(Class mClass, Object object, ClickType clickType) {
        Intent intent = new Intent(mContext, mClass);
        Bundle bundle = new Bundle();
        bundle.putSerializable(INTENTPARAMETEROBJECT, (Serializable) object);
        bundle.putSerializable(INTENTPARAMETERCLICKTYPE, clickType);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    protected void startIntent(Class mClass) {
        Intent intent = new Intent(mContext, mClass);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    protected void startIntentBack(Class mClass, Object object, ClickType clickType) {
        Intent intent = new Intent(mContext, mClass);
        Bundle bundle = new Bundle();
        bundle.putSerializable(INTENTPARAMETEROBJECT, (Serializable) object);
        bundle.putSerializable(INTENTPARAMETERCLICKTYPE, clickType);
        intent.putExtras(bundle);
        startActivity(intent);
        backIntent();
    }

    protected void startIntentBack(Class mClass, Object object) {
        Intent intent = new Intent(mContext, mClass);
        Bundle bundle = new Bundle();
        bundle.putSerializable(GetAuthUnreadMsgResponse.AUTH_UNREAD_MSG_DATA, (Serializable) object);
        intent.putExtras(bundle);
        startActivity(intent);
        backIntent();
    }

    public interface AuthorizeClick {
        public void doClick(View v);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.cancel();
            mAlertDialog = null;
        }
    }
}
