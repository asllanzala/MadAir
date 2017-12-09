package com.honeywell.hch.airtouch.ui.main.ui.common.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.honeywell.hch.airtouch.plateform.http.manager.model.CategoryHomeCityDashBoard;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.util.ViewHolderUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.http.manager.model.CategoryHomeCity;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeAndCity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by h127856 on 7/22/16.
 */
public class HomeListAdapter extends BaseAdapter {

    private static final int TYPE_CATEGORY_ITEM = 0;
    private static final int TYPE_ITEM = 1;

    private List<CategoryHomeCityDashBoard> mListData;
    private LayoutInflater mInflater;
    private Context mContext;
    private final int EMPTY = 0;


    public HomeListAdapter(Context context, List<CategoryHomeCityDashBoard> mData) {
        mContext = context;
        mListData = mData;
        mInflater = LayoutInflater.from(context);
    }


    public void changeData(List<CategoryHomeCityDashBoard> data) {
        this.setCategoryList(data);
        this.notifyDataSetChanged();
    }

    private void setCategoryList(List<CategoryHomeCityDashBoard> data) {
        if (mListData != null) {
            this.mListData = data;
        } else {
            this.mListData = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        int count = EMPTY;
        if (null != mListData) {
            for (CategoryHomeCityDashBoard categoryHomeCity : mListData) {
                count += categoryHomeCity.getItemCount();
            }
        }
        return count;
    }

    @Override
    public Object getItem(int position) {

        // 异常情况处理
        if (null == mListData || position < EMPTY || position > getCount()) {
            return null;
        }

        // 同一分类内，第一个元素的索引值
        int categroyFirstIndex = EMPTY;
        for (CategoryHomeCityDashBoard categoryHomeCity : mListData) {
            int size = categoryHomeCity.getItemCount();
            // 在当前分类中的索引值
            int categoryIndex = position - categroyFirstIndex;
            // item在当前分类内
            if (categoryIndex < size) {
                return categoryHomeCity.getItem(categoryIndex);
            }
            // 索引移动到当前分类结尾，即下一个分类第一个元素索引
            categroyFirstIndex += size;
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        // 异常情况处理
        if (null == mListData || position < EMPTY || position > getCount()) {
            return TYPE_ITEM;
        }

        int categroyFirstIndex = EMPTY;
        for (CategoryHomeCityDashBoard categoryHomeCity : mListData) {
            int size = categoryHomeCity.getItemCount();
            // 在当前分类中的索引值
            int categoryIndex = position - categroyFirstIndex;
            if (categoryIndex == EMPTY) {
                return TYPE_CATEGORY_ITEM;
            }

            categroyFirstIndex += size;
        }

        return TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case TYPE_CATEGORY_ITEM:
                if (null == convertView) {
                    convertView = mInflater.inflate(R.layout.dashboard_homelist_header, null);
                }
                TextView headerNameTv = ViewHolderUtil.get(convertView, R.id.home_owner_name);
                ImageView sperateLine = ViewHolderUtil.get(convertView, R.id.seperate_line);
                
                String homeOwnerName = (String) getItem(position);
                headerNameTv.setText(homeOwnerName);
                if (!AppManager.getInstance().getApplication().getString(R.string.dash_borad_home_menu).equals(homeOwnerName)){
                    sperateLine.setVisibility(View.VISIBLE);
                }else{
                    sperateLine.setVisibility(View.GONE);
                }
                break;

            case TYPE_ITEM:
                if (null == convertView) {
                    DashboardHomeListItemView itemView = new DashboardHomeListItemView(mContext);
                    convertView = itemView;

                }
                Object object = getItem(position);
                if (object != null && object instanceof HomeAndCity) {
                    ((DashboardHomeListItemView) convertView).setViewValue((HomeAndCity)object);
                }
                break;
        }

        return convertView;
    }


    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != TYPE_CATEGORY_ITEM;
    }


    /**
     * 获取除了TYPE_CATEGORY_ITEM之外的，这个position的位置
     * @param position
     * @return
     */
    public int getListPosition(int position) {

        int deltaPosition = 0;
        for (int i = 0 ; i < position ;i++){
            if (getItemViewType(i) == TYPE_CATEGORY_ITEM){
                deltaPosition++;
            }
        }


        return position - deltaPosition;
    }
}
