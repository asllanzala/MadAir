package com.honeywell.hch.airtouch.ui.authorize.manager;

import android.app.Application;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.honeywell.hch.airtouch.library.LibApplication;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.LibraryInitUtil;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthBaseModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthDeviceModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthMessageModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.AuthGroupDeviceListModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.AuthHomeList;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.AuthHomeModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.AuthTo;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.CheckAuthUserResponse;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.GetAuthMessagesResponse;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.GrantAuthDevice;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.GrantAuthToDeviceResponse;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.HandleAuthMessageResponse;
import com.honeywell.hch.airtouch.ui.authorize.ui.controller.list.AuthorizeBaseActivity;
import com.honeywell.hch.airtouch.ui.main.manager.Message.manager.MessageUiManager;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 21/3/16.
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class AuthorizeUiManagerTest {
    private AuthorizeUiManager mAuthorizeUiManager;
    protected MessageUiManager mMessageUiManager;
    protected Application application = Mockito.mock(Application.class, Mockito.RETURNS_DEEP_STUBS);

    @Before
    public void setUp() throws Exception {
        Mockito.when(application.getApplicationContext()).thenReturn(application);
        AppManager.getInstance().setHPlusApplication(application);
        LibraryInitUtil.getInstance().initApplication(application);
        LibApplication.setApplication(application);
        mAuthorizeUiManager = new AuthorizeUiManager();
        mMessageUiManager = new MessageUiManager();
    }

    @Test
    public void parseAuthHomeListTest() {
        Bundle responseData = new Bundle();
        ResponseResult responseResult = new ResponseResult();
        responseResult.setResponseData(responseData);

        Assert.assertEquals(null, mAuthorizeUiManager.parseAuthHomeList(responseResult));

        List<AuthHomeModel> authHomeListModel = new ArrayList<AuthHomeModel>();
        for (int i = 0; i < 5; i++) {
            AuthHomeModel authHomeModel = new AuthHomeModel();
            authHomeModel.setIsLocationOwner(i % 2 == 0);
            authHomeListModel.add(authHomeModel);
        }
        responseData.putSerializable(AuthHomeModel.AUTH_HOME_DATA, (Serializable) authHomeListModel);
        responseResult.setResponseData(responseData);

//        Assert.assertEquals(true, mAuthorizeUiManager.parseAuthHomeList(responseResult).size() == 2);
    }

    @Test
    public void parseAuthHomeListSeceondTest() {
        List<AuthHomeList> mAuthHomeLists = new ArrayList<>();
        AuthHomeList authHomeList = new AuthHomeList();
        AuthDeviceModel mAuthDeviceModel = new AuthDeviceModel();
        AuthHomeModel authHomeModel = new AuthHomeModel();
        List<AuthDeviceModel> mAuthDeviceModelList = new ArrayList<>();
        List<AuthHomeModel> authHomeModelList = new ArrayList<>();
        mAuthDeviceModelList.add(mAuthDeviceModel);
        authHomeModel.setAuthDevices(mAuthDeviceModelList);
        authHomeModelList.add(authHomeModel);
        authHomeList.setmAuthHome(authHomeModelList);
        mAuthHomeLists.add(authHomeList);
        Bundle bundle = new Bundle();
        AuthBaseModel mAuthDeviceModeltest = new AuthDeviceModel();
        mAuthDeviceModeltest.setModelId(232);
        bundle.putSerializable(AuthorizeBaseActivity.INTENTPARAMETEROBJECT, mAuthDeviceModeltest);
        bundle.putSerializable(AuthorizeBaseActivity.INTENTPARAMETERCLICKTYPE, AuthorizeBaseActivity.ClickType.AUTHROIZE);

        mAuthorizeUiManager.parseAuthHomeList(mAuthHomeLists, bundle);
        Assert.assertEquals(232, ((AuthBaseModel) mAuthHomeLists.get(0).getmAuthHome().get(0).getmAuthBaseModel().get(0)).getModelId());

        bundle.clear();
        bundle.putSerializable(AuthorizeBaseActivity.INTENTPARAMETEROBJECT, mAuthDeviceModeltest);
        bundle.putSerializable(AuthorizeBaseActivity.INTENTPARAMETERCLICKTYPE, AuthorizeBaseActivity.ClickType.REVOKE);
        mAuthorizeUiManager.parseAuthHomeList(mAuthHomeLists, bundle);
        Assert.assertEquals(232, ((AuthBaseModel) mAuthHomeLists.get(0).getmAuthHome().get(0).getmAuthBaseModel().get(0)).getModelId());

        bundle.clear();
        bundle.putSerializable(AuthorizeBaseActivity.INTENTPARAMETEROBJECT, mAuthDeviceModeltest);
        bundle.putSerializable(AuthorizeBaseActivity.INTENTPARAMETERCLICKTYPE, AuthorizeBaseActivity.ClickType.REMOVE);
        mAuthorizeUiManager.parseAuthHomeList(mAuthHomeLists, bundle);
        Assert.assertNotNull(mAuthHomeLists);
    }

    @Test
    public void parseNotificatonRemindTest() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(AuthorizeBaseActivity.INTENTPARAMETERCLICKTYPE, AuthorizeBaseActivity.ClickType.AUTHROIZE);
        Assert.assertEquals(AuthorizeBaseActivity.ClickType.AUTHROIZE.getResource(), mAuthorizeUiManager.parseNotificatonRemind(bundle));
    }

    @Test
    public void parseAuthDeviceTest() {
        AuthHomeList authHomeList = new AuthHomeList();
        List<AuthHomeModel> authHomeModelList = new ArrayList<>();
        List<AuthDeviceModel> authDeviceModelList = new ArrayList<>();
        AuthDeviceModel authDeviceModel = new AuthDeviceModel();
        authDeviceModelList.add(authDeviceModel);
        AuthHomeModel authHomeModel = new AuthHomeModel();
        authHomeModel.setLocationName("aaaa");
        authHomeModel.setLocationNameId(23232);
        authHomeModel.setAuthDevices(authDeviceModelList);
        authHomeModelList.add(authHomeModel);
        authHomeList.setmAuthHome(authHomeModelList);

        Assert.assertEquals("aaaa", mAuthorizeUiManager.parseAuthDevice(authHomeList, 0, 0, 0, Object.class).getmLocationName());
    }

    @Test
    public void encloseGrantToUserNameListTest() {
        AuthDeviceModel authDeviceModel = new AuthDeviceModel();
        List<CheckAuthUserResponse> checkAuthUserResponseList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CheckAuthUserResponse checkAuthUserResponse = new CheckAuthUserResponse();
            checkAuthUserResponse.setPhoneNumber("1212323" + i);
            checkAuthUserResponse.setUserName("Vincent" + i);
            checkAuthUserResponseList.add(checkAuthUserResponse);
        }
        mAuthorizeUiManager.encloseGrantToUserNameList(authDeviceModel, checkAuthUserResponseList);
        Assert.assertNotNull(authDeviceModel.getmGrantUserName());

    }

    @Test
    public void encloseChoosePermissionTest() {
        AuthDeviceModel mAuthDeviceModel = new AuthDeviceModel();
        RadioGroup mRadioGroup = new RadioGroup(application);
        mRadioGroup.check(R.id.auth_permission_control_rb);
        mAuthorizeUiManager.encloseChoosePermission(mAuthDeviceModel, mRadioGroup);
        Assert.assertEquals(200, mAuthDeviceModel.getRole());

        mRadioGroup.check(R.id.auth_permission_read_rb);
        mAuthorizeUiManager.encloseChoosePermission(mAuthDeviceModel, mRadioGroup);
        Assert.assertEquals(100, mAuthDeviceModel.getRole());

    }

    @Test
    public void checkAuthUserResponsesListTest() {
        List<CheckAuthUserResponse> checkAuthUserResponseList = new ArrayList<>();
        ResponseResult responseResult = new ResponseResult();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CheckAuthUserResponse.AUTH_USER_DATA, (Serializable) checkAuthUserResponseList);
        responseResult.setResponseData(bundle);
        Assert.assertNotNull(mAuthorizeUiManager.checkAuthUserResponsesList(responseResult));
    }

    @Test
    public void grantAuthToDeviceInviteResponseTest() {
        AuthDeviceModel mAuthDeviceModel = new AuthDeviceModel();
        GrantAuthToDeviceResponse grantAuthToDeviceResponse = new GrantAuthToDeviceResponse();
        List<GrantAuthDevice> getGrantAuthDevicesList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            GrantAuthDevice grantAuthDevice = new GrantAuthDevice();
            grantAuthDevice.setCode(100);
            getGrantAuthDevicesList.add(grantAuthDevice);
        }
        grantAuthToDeviceResponse.setmGrantAuthDevices(getGrantAuthDevicesList);
        ResponseResult responseResult = new ResponseResult();
        Bundle bundle = new Bundle();
        bundle.putSerializable(GrantAuthToDeviceResponse.GRANT_AUTH_DATA, (Serializable) grantAuthToDeviceResponse);
        responseResult.setResponseData(bundle);

        Assert.assertEquals(false, mAuthorizeUiManager.grantAuthToDeviceInviteResponse(responseResult, mAuthDeviceModel));

        getGrantAuthDevicesList.clear();
        for (int i = 0; i < 5; i++) {
            GrantAuthDevice grantAuthDevice = new GrantAuthDevice();
            grantAuthDevice.setCode(200);
            getGrantAuthDevicesList.add(grantAuthDevice);
        }
        grantAuthToDeviceResponse.setmGrantAuthDevices(getGrantAuthDevicesList);
        responseResult.getResponseData().putSerializable(GrantAuthToDeviceResponse.GRANT_AUTH_DATA, (Serializable) grantAuthToDeviceResponse);
        Assert.assertEquals(false, mAuthorizeUiManager.grantAuthToDeviceInviteResponse(responseResult, mAuthDeviceModel));

        List<CheckAuthUserResponse> getmCheckAuthUserResponsesList = new ArrayList<>();
        mAuthDeviceModel.setmCheckAuthUserResponsesList(getmCheckAuthUserResponsesList);
        responseResult.getResponseData().putSerializable(GrantAuthToDeviceResponse.GRANT_AUTH_DATA, (Serializable) grantAuthToDeviceResponse);
        Assert.assertEquals(true, mAuthorizeUiManager.grantAuthToDeviceInviteResponse(responseResult, mAuthDeviceModel));
    }

    @Test
    public void parseRevokeResponseTest() {
        AuthDeviceModel mAuthDeviceModel = new AuthDeviceModel();
        GrantAuthToDeviceResponse grantAuthToDeviceResponse = new GrantAuthToDeviceResponse();
        List<GrantAuthDevice> grantAuthDeviceList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            GrantAuthDevice grantAuthDevice = new GrantAuthDevice();
            grantAuthDevice.setCode(300);
            grantAuthDeviceList.add(grantAuthDevice);
        }
        grantAuthToDeviceResponse.setmGrantAuthDevices(grantAuthDeviceList);
        ResponseResult responseResult = new ResponseResult();
        Bundle bundle = new Bundle();
        bundle.putSerializable(GrantAuthToDeviceResponse.GRANT_AUTH_DATA, (Serializable) grantAuthToDeviceResponse);
        responseResult.setResponseData(bundle);
        Assert.assertEquals(false, mAuthorizeUiManager.parseRevokeResponse(responseResult, mAuthDeviceModel));

        grantAuthDeviceList.clear();
        for (int i = 0; i < 5; i++) {
            GrantAuthDevice grantAuthDevice = new GrantAuthDevice();
            grantAuthDevice.setCode(200);
            grantAuthDeviceList.add(grantAuthDevice);
        }
        grantAuthToDeviceResponse.setmGrantAuthDevices(grantAuthDeviceList);
        responseResult.getResponseData().putSerializable(GrantAuthToDeviceResponse.GRANT_AUTH_DATA, (Serializable) grantAuthToDeviceResponse);
        Assert.assertEquals(true, mAuthorizeUiManager.parseRevokeResponse(responseResult, mAuthDeviceModel));
    }

    @Test
    public void parseRevokePhoneNumberListTest() {
        AuthDeviceModel authDeviceModel = new AuthDeviceModel();
        List<AuthTo> authToList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            AuthTo authTo = new AuthTo();
            authTo.setmPhoneNumber("121232");
            authToList.add(authTo);
        }
        authDeviceModel.setmAuthorityToList(authToList);

        Assert.assertEquals(5, mAuthorizeUiManager.parseRevokePhoneNumberList(authDeviceModel).size());
    }

    @Test
    public void isAddHomeTest() {
        AuthMessageModel authMessageModel = new AuthMessageModel();
        authMessageModel.setmLocationName("aaa");
        Assert.assertEquals(true, mAuthorizeUiManager.isAddHome(authMessageModel));
    }

    @Test
    public void isOwnDeviceTest() {
        Assert.assertEquals(true, mAuthorizeUiManager.isOwnDevice());
    }

    @Test
    public void getAuthMessagesResponseTest() {
        GetAuthMessagesResponse getAuthMessagesResponse = new GetAuthMessagesResponse();
        ResponseResult responseResult = new ResponseResult();
        Bundle bundle = new Bundle();
        bundle.putSerializable(GetAuthMessagesResponse.AUTH_MESSAGE_DATA, (Serializable) getAuthMessagesResponse);
        responseResult.setResponseData(bundle);
        Assert.assertNotNull(mAuthorizeUiManager.getAuthMessagesResponse(responseResult));
    }

    @Test
    public void getAuthMessageResponse() {
        AuthMessageModel authMessageModel = new AuthMessageModel();
        ResponseResult responseResult = new ResponseResult();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AuthMessageModel.AUTH_MESSAGE_DATA_BY_ID, (Serializable) authMessageModel);
        responseResult.setResponseData(bundle);
        Assert.assertNotNull(mAuthorizeUiManager.getAuthMessageResponse(responseResult));
    }

    @Test
    public void handleAuthMessageResponseTest() {
        HandleAuthMessageResponse handleAuthMessageResponse = new HandleAuthMessageResponse();
        ResponseResult responseResult = new ResponseResult();
        Bundle bundle = new Bundle();
        bundle.putSerializable(HandleAuthMessageResponse.AUTH_HANDLE_MESSAGE_DATA, (Serializable) handleAuthMessageResponse);
        responseResult.setResponseData(bundle);
        Assert.assertNotNull(mAuthorizeUiManager.handleAuthMessageResponse(responseResult));
    }

    @Test
    public void parseGetAuthMessagesResponseTest() {
        GetAuthMessagesResponse getAuthMessagesResponse = new GetAuthMessagesResponse();
        AuthMessageModel authMessageModel = new AuthMessageModel();
        Assert.assertNotNull(mAuthorizeUiManager.parseGetAuthMessagesResponse(getAuthMessagesResponse, authMessageModel).getAuthMessages());
    }

    @Test
    public void parseGroupDeviceListTest() {
        AuthGroupDeviceListModel authGroupDeviceListModel = new AuthGroupDeviceListModel();
        List<AuthGroupDeviceListModel> authGroupDeviceListModelList = new ArrayList<>();
        authGroupDeviceListModelList.add(authGroupDeviceListModel);
        ResponseResult responseResult = new ResponseResult();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AuthGroupDeviceListModel.AUTH_GROUP_DEVICE_LIST_DATA, (Serializable) authGroupDeviceListModelList);
        responseResult.setResponseData(bundle);
        Assert.assertNotNull(mAuthorizeUiManager.parseGroupDeviceList(responseResult));
    }

    @Test
    public void parseAuthGroupDeviceLisTest() {
        AuthGroupDeviceListModel authGroupDeviceListModel = new AuthGroupDeviceListModel();
        List<AuthDeviceModel> authDeviceModelList = new ArrayList<>();
        AuthDeviceModel authDeviceModel = new AuthDeviceModel();
        authDeviceModelList.add(authDeviceModel);
        authGroupDeviceListModel.setmAuthDevices(authDeviceModelList);
        AuthBaseModel authBaseModel = new AuthDeviceModel();
        authBaseModel.setParentPosition(0);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AuthorizeBaseActivity.INTENTPARAMETEROBJECT, (Serializable) authBaseModel);
        bundle.putSerializable(AuthorizeBaseActivity.INTENTPARAMETERCLICKTYPE, AuthorizeBaseActivity.ClickType.REVOKE);
        mAuthorizeUiManager.parseAuthGroupDeviceList(authGroupDeviceListModel, bundle);
        Assert.assertEquals(authBaseModel, authGroupDeviceListModel.getmAuthDevices().get(0));

        bundle.putSerializable(AuthorizeBaseActivity.INTENTPARAMETERCLICKTYPE, AuthorizeBaseActivity.ClickType.AUTHROIZE);
        mAuthorizeUiManager.parseAuthGroupDeviceList(authGroupDeviceListModel, bundle);
        Assert.assertEquals(authBaseModel, authGroupDeviceListModel.getmAuthDevices().get(0));
    }

    @Test
    public void parseAuthGroupDeviceTest() {
        AuthGroupDeviceListModel mAuthGroupDeviceListModel = new AuthGroupDeviceListModel();
        mAuthGroupDeviceListModel.setmLocationName("123");
        mAuthGroupDeviceListModel.setmLocationId(123);
        AuthBaseModel authBaseModel = new AuthBaseModel();
        mAuthorizeUiManager.parseAuthDevice(mAuthGroupDeviceListModel, 1, authBaseModel, Object.class);
        Assert.assertEquals(123, authBaseModel.getmLocationId());
    }

    @Test
    public void remindNotificationActionTest() {
        int action = 1;
        Assert.assertEquals(AppManager.getInstance().getApplication().getString(R.string.authorize_accept_success), mMessageUiManager.remindNotificationAction(action));
        action = 0;
        Assert.assertEquals(AppManager.getInstance().getApplication().getString(R.string.authorize_decline_success), mMessageUiManager.remindNotificationAction(action));
        action = -1;
        Assert.assertNull(mMessageUiManager.remindNotificationAction(action));
    }

}
