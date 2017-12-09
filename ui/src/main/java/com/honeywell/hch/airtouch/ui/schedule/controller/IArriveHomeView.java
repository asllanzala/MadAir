package com.honeywell.hch.airtouch.ui.schedule.controller;

/**
 * Created by honeywell on 26/12/2016.
 */

public interface IArriveHomeView {

    void showTimeAfterGetRunstatus(String arriveTime);

    void hasArriveHomeTimeLayout();

    void noArriveHomeTimeLayout();
}
