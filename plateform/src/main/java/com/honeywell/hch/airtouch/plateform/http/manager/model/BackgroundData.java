package com.honeywell.hch.airtouch.plateform.http.manager.model;

import java.util.ArrayList;

/**
 * Created by wuyuan on 10/20/15.
 */
public class BackgroundData {


    /**
     * all the path of the background
     */
    private ArrayList<String> mCityBackgroundPathList = new ArrayList<>();



    public BackgroundData(){

    }

    public ArrayList<String> getCityBackgroundPathList() {
        return mCityBackgroundPathList;
    }

    public void addItemToCityPathList(String pathItem) {
        boolean isHasSame = false;
        for (String item : mCityBackgroundPathList){
            if (item.equals(pathItem)){
                isHasSame = true;
                break;
            }
        }
        if (!isHasSame){
            mCityBackgroundPathList.add(pathItem);
        }
    }


}
