package com.honeywell.hch.airtouch.ui.enroll.manager.presenter;

import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.database.model.City;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.AddLocationRequest;
import com.honeywell.hch.airtouch.plateform.http.task.AddLocationTask;
import com.honeywell.hch.airtouch.plateform.umeng.UmengUtil;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.manager.model.DropTextModel;
import com.honeywell.hch.airtouch.ui.control.manager.umeng.UmengUiManager;
import com.honeywell.hch.airtouch.ui.enroll.interfacefile.ISelectedLocationView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by h127856 on 16/10/13.
 */
public class EnrollSelectedLocationPresenter {

    private ISelectedLocationView mSelectedLocationView;

    public EnrollSelectedLocationPresenter(ISelectedLocationView selectedLocationView) {
        mSelectedLocationView = selectedLocationView;
    }


    public void initActivity(City mSelectedGPSCity) {
        if (isExistHomeInAccount()) {
            mSelectedLocationView.showSelectedHomeWayLayout();
            if (mSelectedGPSCity == null) {
                mSelectedLocationView.initSelctedCityText(mSelectedGPSCity);
            }
        } else {
            mSelectedLocationView.showOnlyNewHomeLayout();
        }

    }


    public void initExistHomeDropContent() {
        mSelectedLocationView.initDropEditText(getHomeStringArray());
    }

    /**
     * 获取下拉
     *
     * @return
     */
    private DropTextModel[] getHomeStringArray() {
        List<DropTextModel> dropTextModelsList = new ArrayList<>();

        List<UserLocationData> userLocations = UserAllDataContainer.shareInstance().getUserLocationDataList();

        if ((userLocations != null)) {
            for (int i = 0; i < userLocations.size(); i++) {
                if (userLocations.get(i).getCity() != null
                        && AppManager.getLocalProtocol().canEnrollToHome(userLocations.get(i))) {
                    DropTextModel dropTextModel = new DropTextModel(userLocations.get(i).getName(), UserDataOperator.getCityName(userLocations.get(i)));
                    dropTextModel.setLocationId(userLocations.get(i).getLocationID());
                    dropTextModelsList.add(dropTextModel);
                }

            }
        }
        DropTextModel[] stringsArray = new DropTextModel[dropTextModelsList.size()];
        return dropTextModelsList.toArray(stringsArray);
    }

    //有家，并且不全部为授权家
    private boolean isExistHomeInAccount() {
        List<UserLocationData> userLocationDataList = UserAllDataContainer.shareInstance().getUserLocationDataList();
        if (userLocationDataList.size() > 0) {
            for (UserLocationData userLocationData : userLocationDataList) {
                if (userLocationData.isIsLocationOwner()) {
                    return true;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    public void addHome(String homeName, String cityName) {
        IActivityReceive addLocationResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {
                if (responseResult.isResult()) {
                    switch (responseResult.getRequestId()) {
                        case ADD_LOCATION:
                            if (responseResult.getResponseCode() == StatusCode.OK) {
                                DIYInstallationState.setLocationId(responseResult.getResponseData()
                                        .getInt(HPlusConstants.LOCATION_ID_BUNDLE_KEY));
                                mSelectedLocationView.setAddHomeSuccessView();
                            } else {
                                mSelectedLocationView.setAddHomeErrorView(responseResult, R.string.new_place_failed);
                                UmengUtil.enrollEvent(UmengUiManager.getEnrollProductName(), UmengUtil.EnrollEventType.ENROLL_FAIL, "add_Home_faile reponse code_" + responseResult.getResponseCode());
                            }
                            return;
                        default:
                            break;
                    }
                } else {
                    mSelectedLocationView.setAddHomeErrorView(responseResult, R.string.new_place_failed);
                    UmengUtil.enrollEvent(UmengUiManager.getEnrollProductName(), UmengUtil.EnrollEventType.ENROLL_FAIL, "add_Home_faile_response_false_" + responseResult.getExeptionMsg());
                }
            }
        };

//        mLoadingCallback.onLoad(mContext.getString(R.string.adding_home));
        AddLocationRequest addLocationRequest = new AddLocationRequest();
        addLocationRequest.setCity(cityName);
        addLocationRequest.setName(homeName);
        AddLocationTask requestTask
                = new AddLocationTask(addLocationRequest, addLocationResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);

    }

    //是否有相同家的名字
    public boolean isSameName(String cityName, String homeName) {
        for (int i = 0; i < UserAllDataContainer.shareInstance().getUserLocationDataList().size(); i++) {
            UserLocationData userLocation = UserAllDataContainer.shareInstance().getUserLocationDataList().get(i);
            if (homeName.equals(userLocation.getName())
                    && userLocation.isIsLocationOwner()
                    && (userLocation.getCity().equals(cityName))) {
                mSelectedLocationView.addTheSameHome();
                return true;
            }
        }
        return false;
    }

}
