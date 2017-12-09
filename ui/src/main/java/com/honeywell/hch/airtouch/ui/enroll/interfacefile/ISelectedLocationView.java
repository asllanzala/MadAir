package com.honeywell.hch.airtouch.ui.enroll.interfacefile;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.database.model.City;
import com.honeywell.hch.airtouch.ui.common.manager.model.DropTextModel;

/**
 * Created by h127856 on 16/10/13.
 */
public interface ISelectedLocationView {

    /**
     * 当有家的时候，显示A new Home and Exist Home两种创建家的方式
     */
    void showSelectedHomeWayLayout();

    /**
     * 当没有家的时候。上面两个按钮不需要显示，只默认显示新建家的布局
     */
    void showOnlyNewHomeLayout();


    void initDropEditText( DropTextModel[] dropTextModels);


    void initSelctedCityText(City city);


    void setAddHomeErrorView(ResponseResult responseResult,int errorMsgStrId);


    void setAddHomeSuccessView();

    void addTheSameHome();
}
