package com.honeywell.hch.airtouch.ui.main.ui.dashboard.presenter;

/**
 * Created by h127856 on 7/21/16.
 */
public interface IHomeFragmentPresenter {

    /**
     * 初始化
     */
     void initHomeFragment();

    void controlAtHomeModel();
    void controlSleepModel();
    void controlAwakeModel();
    void controlAwayModel();

}
