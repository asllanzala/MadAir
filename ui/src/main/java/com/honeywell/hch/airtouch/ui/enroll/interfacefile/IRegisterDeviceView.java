package com.honeywell.hch.airtouch.ui.enroll.interfacefile;

/**
 * Created by h127856 on 16/10/13.
 */
public interface IRegisterDeviceView {

    void registerTimeoutError();

    void registerByOtherError();

    void commNotGetResultFailed();

    void otherFailed();

    void addDeviceSuccess();
}
