package com.honeywell.hch.airtouch.plateform.http.manager.model;


import java.io.Serializable;

/**
 * Created by h127856 on 6/3/16.
 */
public class SelectStatusDeviceItem implements Serializable{

    private boolean isSelected;

    private HomeDevice mDeviceItem;

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public HomeDevice getDeviceItem() {
        return mDeviceItem;
    }

    public void setDeviceItem(HomeDevice mDeviceItem) {
        this.mDeviceItem = mDeviceItem;
    }
}
