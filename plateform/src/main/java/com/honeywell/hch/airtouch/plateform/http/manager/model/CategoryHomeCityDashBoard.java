package com.honeywell.hch.airtouch.plateform.http.manager.model;

import java.util.List;

/**
 * Created by Vincent on 13/7/16.
 * DashBoard 使用
 */
public class CategoryHomeCityDashBoard {
    private String mCategoryName;
    private int mType = -1; // Group: 0, Others: 1

    private List<HomeAndCity> mHomeAndCityList;

    public CategoryHomeCityDashBoard() {
    }

    public CategoryHomeCityDashBoard(String mCategroyName) {
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

    public List<HomeAndCity> getmHomeAndCityList() {
        return mHomeAndCityList;
    }

    public void setmHomeAndCityList(List<HomeAndCity> mHomeAndCityList) {
        this.mHomeAndCityList = mHomeAndCityList;
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
            return mHomeAndCityList.get(pPosition - 1);
        }
    }

    /**
     * 当前类别Item总数。Category也需要占用一个Item
     *
     * @return
     */
    public int getItemCount() {
        return mHomeAndCityList.size() + 1;
    }

}
