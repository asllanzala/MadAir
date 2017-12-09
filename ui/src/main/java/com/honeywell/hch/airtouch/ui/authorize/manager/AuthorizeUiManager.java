package com.honeywell.hch.airtouch.ui.authorize.manager;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthBaseModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthDeviceModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthGroupModel;
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
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.authorize.ui.controller.list.AuthorizeBaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vincent on 25/2/16.
 */

public class AuthorizeUiManager {
    private final int EMPTY = 0;
    private final int STATUSCODEOK = 200;
    private AuthorizeManager mAuthorizeManager;

    public AuthorizeUiManager() {
        mAuthorizeManager = new AuthorizeManager();
    }

    public List<AuthHomeList> parseAuthHomeList(ResponseResult responseResult) {
        List<AuthHomeModel> authHomeListModel = (List<AuthHomeModel>) responseResult.getResponseData()
                .getSerializable(AuthHomeModel.AUTH_HOME_DATA);
        List<AuthHomeList> tempList = null;
        if (authHomeListModel != null && authHomeListModel.size() != EMPTY) {
            tempList = new ArrayList<>();
            List<AuthHomeModel> myAuthHome = new ArrayList<AuthHomeModel>();
            List<AuthHomeModel> authToMeHome = new ArrayList<AuthHomeModel>();
            for (AuthHomeModel authHomeModel : authHomeListModel) {
                authHomeModel.setAuthDevices((List<AuthDeviceModel>) authHomeModel.getAuthDevices());
                authHomeModel.setmAuthGroups((List<AuthGroupModel>) authHomeModel.getmAuthGroups());
                authHomeModel.setmAuthBaseListModel();
                if (authHomeModel.isLocationOwner()) {
                    if (AppManager.getLocalProtocol().canSendAuthorization()) {
                        myAuthHome.add(authHomeModel);
                    }
                } else {
                    authToMeHome.add(authHomeModel);
                }
            }
            if (myAuthHome.size() != EMPTY) {
                AuthHomeList myAuthHomeList = new AuthHomeList(AuthHomeList.Type.MYHOME, myAuthHome);
                myAuthHomeList.setmOwnerName(AppManager.getInstance().getApplication().getString(R.string.authorize_my_devices));
                tempList.add(myAuthHomeList);
            }
            if (authToMeHome.size() != EMPTY) {
                parseAuthToList(authToMeHome, tempList);
            }
        }
        return tempList;
    }

    //遍历数据，改为人的家为分层
    private void parseAuthToList(List<AuthHomeModel> authToMeHome, List<AuthHomeList> tempList) {
        Map<Integer, String> ownerIdMap = new HashMap<>();

        //创建ownerId的map
        for (AuthHomeModel authHomeModel : authToMeHome) {
            List<AuthBaseModel> authBaseModelList = authHomeModel.getmAuthBaseModel();
            for (AuthBaseModel authBaseModel : authBaseModelList) {
                ownerIdMap.put(authBaseModel.getOwnerId(), authBaseModel.getOwnerName());
                authHomeModel.setmOwnerId(authBaseModel.getOwnerId());
                authHomeModel.setmOwnerName(authBaseModel.getOwnerName());
            }
        }
        for (Map.Entry<Integer, String> entry : ownerIdMap.entrySet()) {
            List<AuthHomeModel> authToMeHomeTemp = new ArrayList<AuthHomeModel>();
            String ownerName = entry.getValue();
            int ownerId = entry.getKey();

            for (AuthHomeModel authHomeModel : authToMeHome) {
                if (entry.getKey() == authHomeModel.getmOwnerId()) {
                    authToMeHomeTemp.add(authHomeModel);
                }
            }

            AuthHomeList toMeAuthHomeList = new AuthHomeList(AuthHomeList.Type.TOMEHOME, authToMeHomeTemp);
            toMeAuthHomeList.setmOwnerId(ownerId);
            toMeAuthHomeList.setmOwnerName(ownerName);
            tempList.add(toMeAuthHomeList);
        }
    }

    public void parseAuthHomeList(List<AuthHomeList> mAuthHomeLists, Bundle bundle) {
        AuthBaseModel mAuthDeviceModel = (AuthBaseModel) bundle.get(AuthorizeBaseActivity.INTENTPARAMETEROBJECT);
        AuthorizeBaseActivity.ClickType clickType = (AuthorizeBaseActivity.ClickType) bundle.get(AuthorizeBaseActivity.INTENTPARAMETERCLICKTYPE);
        switch (clickType) {
            case AUTHROIZE:
            case REVOKE:
                //删除原来
                mAuthHomeLists.get(mAuthDeviceModel.getParentPosition()).getmAuthHome().get(mAuthDeviceModel.getGroupPosition())
                        .getmAuthBaseModel().remove(mAuthDeviceModel.getChildPosition());
                ((List<AuthBaseModel>) mAuthHomeLists.get(mAuthDeviceModel.getParentPosition()).getmAuthHome().get(mAuthDeviceModel.getGroupPosition())
                        .getmAuthBaseModel()).add(mAuthDeviceModel);

                break;
            case REMOVE:
                //delete specific list，verify three hierachy
                mAuthHomeLists.get(mAuthDeviceModel.getParentPosition()).getmAuthHome().get(mAuthDeviceModel.getGroupPosition())
                        .getmAuthBaseModel().remove(mAuthDeviceModel.getChildPosition());
                if (mAuthHomeLists.get(mAuthDeviceModel.getParentPosition()).getmAuthHome().get(mAuthDeviceModel.getGroupPosition())
                        .getmAuthBaseModel().size() == EMPTY) {
                    mAuthHomeLists.get(mAuthDeviceModel.getParentPosition()).getmAuthHome().remove(mAuthDeviceModel.getGroupPosition());
                }
                if (mAuthHomeLists.get(mAuthDeviceModel.getParentPosition()).getmAuthHome().size() == EMPTY) {
                    mAuthHomeLists.remove(mAuthDeviceModel.getParentPosition());
                }
                break;
        }
    }

    public int parseNotificatonRemind(Bundle bundle) {
        AuthorizeBaseActivity.ClickType clickType = (AuthorizeBaseActivity.ClickType) bundle.get(AuthorizeBaseActivity.INTENTPARAMETERCLICKTYPE);
        return clickType.resource;
    }

    //transplate parameter,attached to intent
    public AuthBaseModel parseAuthDevice(AuthHomeList authHomeList, int parentPosition, int groupPosition, int childPosition, Class mClass) {
        AuthBaseModel authDeviceModel = authHomeList.getmAuthHome().get(groupPosition).getmAuthBaseModel().get(childPosition);
        authDeviceModel.setLocationNameAndPosition(authHomeList.getmAuthHome().get(groupPosition).getLocationName(), parentPosition,
                groupPosition, childPosition, authHomeList.getmAuthHome().get(groupPosition).getLocationNameId(), mClass);
        return authDeviceModel;
    }

    public void parseAuthDevice(AuthHomeList authHomeList, AuthBaseModel authDeviceModel, int parentPosition, int groupPosition, int childPosition, Class mClass) {
        authDeviceModel.setLocationNameAndPosition(authHomeList.getmAuthHome().get(groupPosition).getLocationName(), parentPosition,
                groupPosition, childPosition, authHomeList.getmAuthHome().get(groupPosition).getLocationNameId(), mClass);
    }

    public void encloseGrantToUserNameList(AuthBaseModel mAuthDeviceModel, List<CheckAuthUserResponse> mCheckAuthUserResponsesList) {
        List phoneNumberList = new ArrayList();
        List phoneUserNameList = new ArrayList();
        for (CheckAuthUserResponse checkAuthUserResponse : mCheckAuthUserResponsesList) {
            phoneNumberList.add(checkAuthUserResponse.getPhoneNumber());
            phoneUserNameList.add(checkAuthUserResponse.getmUserName());
        }
        mAuthDeviceModel.setmGrantUserPhoneNumber(phoneNumberList);
        mAuthDeviceModel.setmGrantUserName(phoneUserNameList);
        mAuthDeviceModel.setmCheckAuthUserResponsesList(mCheckAuthUserResponsesList);
    }

    public void encloseChoosePermission(AuthBaseModel mAuthDeviceModel, RadioGroup mRadioGroup) {
        if (mRadioGroup.getCheckedRadioButtonId() == R.id.auth_permission_control_rb) {
            mAuthDeviceModel.setRole(AuthTo.CONTROLLER);
        } else if (mRadioGroup.getCheckedRadioButtonId() == R.id.auth_permission_read_rb) {
            mAuthDeviceModel.setRole(AuthTo.OBSERVER);
        }
    }

    public List<CheckAuthUserResponse> checkAuthUserResponsesList(ResponseResult responseResult) {
        return (List<CheckAuthUserResponse>) responseResult.getResponseData()
                .getSerializable(CheckAuthUserResponse.AUTH_USER_DATA);
    }

    public boolean grantAuthToDeviceInviteResponse(ResponseResult responseResult, AuthBaseModel authBaseModel) {
        GrantAuthToDeviceResponse grantAuthToDeviceResponse = (GrantAuthToDeviceResponse) responseResult.getResponseData()
                .getSerializable(GrantAuthToDeviceResponse.GRANT_AUTH_DATA);
        List<GrantAuthDevice> grantAuthDeviceList = grantAuthToDeviceResponse.getGrantAuthDevices();
        List<AuthTo> mAuthorityToList = new ArrayList<AuthTo>();
        authBaseModel.setmAuthorityToList(null);
        if (grantAuthDeviceList != null) {
            for (GrantAuthDevice grantAuthDevice : grantAuthDeviceList) {
                if (grantAuthDevice.getCode() != STATUSCODEOK) {
                    return false;
                } else {
                    AuthTo authTo = new AuthTo();
                    authTo.setmRole(authBaseModel.getRole());
                    if (authBaseModel.getmCheckAuthUserResponsesList() != null) {
                        for (CheckAuthUserResponse checkAuthUserResponse : authBaseModel.getmCheckAuthUserResponsesList()) {
                            if (checkAuthUserResponse.getPhoneNumber().equals(grantAuthDevice.getTelephone())) {
                                authTo.setmGrantToUserName(checkAuthUserResponse.getmUserName());
                                authTo.setmPhoneNumber(grantAuthDevice.getTelephone());
                            }
                        }
                        authTo.setmStatus(AuthTo.WAIT);
                        mAuthorityToList.add(authTo);
                        authBaseModel.setmAuthorityToList(mAuthorityToList);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean parseRevokeResponse(ResponseResult responseResult, AuthBaseModel authBaseModel) {
        GrantAuthToDeviceResponse grantAuthToDeviceResponse = (GrantAuthToDeviceResponse) responseResult.getResponseData()
                .getSerializable(GrantAuthToDeviceResponse.GRANT_AUTH_DATA);
        List<GrantAuthDevice> grantAuthDeviceList = grantAuthToDeviceResponse.getGrantAuthDevices();
        if (grantAuthDeviceList == null) {
            return false;
        } else {
            for (GrantAuthDevice grantAuthDevice : grantAuthDeviceList) {
                if (grantAuthDevice.getCode() != STATUSCODEOK) {
                    return false;
                }
            }
        }
        authBaseModel.setmAuthorityToList(null);
        return true;
    }

    public List<String> parseRevokePhoneNumberList(AuthBaseModel authBaseModel) {
        List<String> phoneNumberList = new ArrayList<>();
        for (AuthTo authTo : authBaseModel.getmAuthorityToList()) {
            if (authTo.getmPhoneNumber() != null) {
                phoneNumberList.add(authTo.getmPhoneNumber());
            } else {
                phoneNumberList.add(authTo.getGrantToUserName());
            }
        }
        return phoneNumberList;
    }

    public boolean isAddHome(AuthMessageModel authMessageModel) {
        List<UserLocationData> mUserLocations = UserAllDataContainer.shareInstance().getUserLocationDataList();
        if (mUserLocations.size() > EMPTY) {
            for (int i = EMPTY; i < mUserLocations.size(); i++) {
                if (mUserLocations.get(i).getName().equals(authMessageModel.getLocationName())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isOwnDevice() {
        List<UserLocationData> mUserLocations = UserAllDataContainer.shareInstance().getUserLocationDataList();
        if (mUserLocations.size() > EMPTY) {
            for (int i = EMPTY; i < mUserLocations.size(); i++) {
                if (mUserLocations.get(i).isHaveDeviceInThisLocation()) {
                    return true;
                }
            }
        }
        return false;
    }

    public GetAuthMessagesResponse getAuthMessagesResponse(ResponseResult responseResult) {
        return (GetAuthMessagesResponse) responseResult.getResponseData()
                .getSerializable(GetAuthMessagesResponse.AUTH_MESSAGE_DATA);
    }

    public AuthMessageModel getAuthMessageResponse(ResponseResult responseResult) {
        return (AuthMessageModel) responseResult.getResponseData().getSerializable(AuthMessageModel.AUTH_MESSAGE_DATA_BY_ID);
    }

    public HandleAuthMessageResponse handleAuthMessageResponse(ResponseResult responseResult) {
        return (HandleAuthMessageResponse) responseResult.getResponseData()
                .getSerializable(HandleAuthMessageResponse.AUTH_HANDLE_MESSAGE_DATA);
    }

    public GetAuthMessagesResponse parseGetAuthMessagesResponse(GetAuthMessagesResponse getAuthMessagesResponse, AuthMessageModel mAuthMessageModel) {
        getAuthMessagesResponse = new GetAuthMessagesResponse();
        List<AuthMessageModel> authMessageModelList = new ArrayList<>();
        authMessageModelList.add(mAuthMessageModel);
        getAuthMessagesResponse.setmAuthMessages(authMessageModelList);
        return getAuthMessagesResponse;
    }

    public void revokeOrRemoveDevice(AuthBaseModel authBaseModel) {
        UserLocationData userLocationData = UserDataOperator.getLocationWithId(authBaseModel.getmLocationId(),UserAllDataContainer.shareInstance().getUserLocationDataList(),
                UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList());
        List<HomeDevice> homeDeviceList = userLocationData.getHomeDevicesList();
        for (int i = EMPTY; i < homeDeviceList.size(); i++) {
            if (homeDeviceList.get(i).getDeviceInfo().getDeviceID() == authBaseModel.getModelId()) {
                homeDeviceList.remove(i);
            }
        }
        if (homeDeviceList.size() <= EMPTY) {
            UserAllDataContainer.shareInstance().deleteUserLocation(userLocationData);
        }
    }

    public AuthGroupDeviceListModel parseGroupDeviceList(ResponseResult responseResult) {
        List<AuthGroupDeviceListModel> authGroupDeviceListModels = (List<AuthGroupDeviceListModel>) responseResult.getResponseData()
                .getSerializable(AuthGroupDeviceListModel.AUTH_GROUP_DEVICE_LIST_DATA);
        return authGroupDeviceListModels.size() == EMPTY ? null : authGroupDeviceListModels.get(EMPTY);
    }

    public void parseAuthGroupDeviceList(AuthGroupDeviceListModel mAuthGroupDeviceListModel, Bundle bundle) {
        AuthBaseModel mAuthDeviceModel = (AuthBaseModel) bundle.get(AuthorizeBaseActivity.INTENTPARAMETEROBJECT);
        AuthorizeBaseActivity.ClickType clickType = (AuthorizeBaseActivity.ClickType) bundle.get(AuthorizeBaseActivity.INTENTPARAMETERCLICKTYPE);
        switch (clickType) {
            case AUTHROIZE:
            case REVOKE:
                //删除原来
                mAuthGroupDeviceListModel.getmAuthDevices().remove(mAuthDeviceModel.getParentPosition());
                mAuthGroupDeviceListModel.getmAuthDevices().add((AuthDeviceModel) mAuthDeviceModel);
                break;
        }
    }

    public void parseAuthDevice(AuthGroupDeviceListModel mAuthGroupDeviceListModel, int position, AuthBaseModel authBaseModel, Class mClass) {
        authBaseModel.setLocationNameAndPosition(mAuthGroupDeviceListModel.getmLocationName(),
                position, mAuthGroupDeviceListModel.getmLocationId(), mClass);
    }

    /**
     * 设置success callBack
     *
     * @param successCallback
     */
    public void setSuccessCallback(AuthorizeManager.SuccessCallback successCallback) {
        mAuthorizeManager.setSuccessCallback(successCallback);
    }

    /**
     * 设置error callBack
     *
     * @param errorCallback
     */
    public void setErrorCallback(AuthorizeManager.ErrorCallback errorCallback) {
        mAuthorizeManager.setErrorCallback(errorCallback);
    }

    public void getInvitations(int pageIndex, int pageSize, int loadMode) {
        mAuthorizeManager.getInvitations(pageIndex, pageSize, loadMode);
    }

    public void grantAuthToDevice(int deviceId, int assignRole, List<String> phoneNumbers) {
        mAuthorizeManager.grantAuthToDevice(deviceId, assignRole, phoneNumbers);
    }

    public void removeAuthDevice(int deviceId) {
        mAuthorizeManager.removeAuthDevice(deviceId);
    }

    public void removeAuthGroup(int groupId) {
        mAuthorizeManager.removeAuthGroup(groupId);
    }

    public void getDeviceListByGroupId(List<Integer> groupIds, boolean isAutoRefresh) {
        mAuthorizeManager.getDeviceListByGroupId(groupIds, isAutoRefresh);
    }

    public void checkAuthUser(List<String> phoneNumbers) {
        mAuthorizeManager.checkAuthUser(phoneNumbers);
    }

    public void getAuthGroupDevices() {
        mAuthorizeManager.getAuthGroupDevices();
    }

    public void getInvitationsById(int messageId) {
        mAuthorizeManager.getInvitationsById(messageId);
    }

    public void handleInvitation(int invitationId, int actionId) {
        mAuthorizeManager.handleInvitation(invitationId, actionId);
    }
}
