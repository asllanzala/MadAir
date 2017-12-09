package com.honeywell.hch.airtouch.ui.common.manager.model;


import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceGroupItem;
import com.honeywell.hch.airtouch.plateform.http.manager.model.SelectStatusDeviceItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 3/6/16.
 */
public class Category {

    private String mCategoryName;
    private List<DeviceGroupItem> mDeviceGroupListItem;
    private int mType = -1; // Group: 0, Others: 1

    private ArrayList<SelectStatusDeviceItem> mSelectDeviceListItem;

    public Category(String mCategroyName) {
        mCategoryName = mCategroyName;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public String getmCategoryName() {
        return mCategoryName;
    }

    public void setmCategoryName(String mCategoryName) {
        this.mCategoryName = mCategoryName;
    }

    public void addGoupItem(List<DeviceGroupItem> deviceGroupItemList) {
        mDeviceGroupListItem = deviceGroupItemList;
    }

    public void addSelectItem(ArrayList<SelectStatusDeviceItem> selectStatusDeviceItemList) {
        mSelectDeviceListItem = selectStatusDeviceItemList;
    }

    public List<DeviceGroupItem> getmDeviceGroupListItem() {
        return mDeviceGroupListItem;
    }

    public void setmDeviceGroupListItem(List<DeviceGroupItem> mDeviceGroupListItem) {
        this.mDeviceGroupListItem = mDeviceGroupListItem;
    }

    public ArrayList<SelectStatusDeviceItem> getmSelectDeviceListItem() {
        return mSelectDeviceListItem;
    }

    public void setmSelectDeviceListItem(ArrayList<SelectStatusDeviceItem> mSelectDeviceListItem) {
        this.mSelectDeviceListItem = mSelectDeviceListItem;
    }

    /**
     * 获取Item内容
     *
     * @param pPosition
     * @return
     */
    public Object getItem(int pPosition) {
        // Category排在第一位
        if (pPosition == 0) {
            return mCategoryName;
        } else {
            if (mType == 0) {
                return mDeviceGroupListItem.get(pPosition - 1);
            } else if (mType == 1) {
                return mSelectDeviceListItem.get(pPosition - 1);
            }
            return null;
        }
    }

    /**
     * 当前类别Item总数。Category也需要占用一个Item
     *
     * @return
     */
    public int getItemCount() {
        if (mType == 0) {
            return mDeviceGroupListItem.size() + 1;
        } else if (mType == 1) {
            return mSelectDeviceListItem.size() + 1;
        }
        return 0;

    }

    public void setAllDeviceSelectStatus(boolean isSelected) {
        if (mSelectDeviceListItem != null && mSelectDeviceListItem.size() > 0) {
            for (SelectStatusDeviceItem selectStatusDeviceItem : mSelectDeviceListItem) {
                selectStatusDeviceItem.setIsSelected(isSelected);
            }
        }
    }

}
