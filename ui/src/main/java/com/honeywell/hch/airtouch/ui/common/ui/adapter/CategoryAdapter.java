package com.honeywell.hch.airtouch.ui.common.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.ViewHolderUtil;
import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceGroupItem;
import com.honeywell.hch.airtouch.plateform.http.manager.model.SelectStatusDeviceItem;
import com.honeywell.hch.airtouch.ui.common.manager.model.Category;
import com.honeywell.hch.airtouch.ui.common.ui.controller.HomeDeviceInfoBaseFragment.ButtomType;
import com.honeywell.hch.airtouch.ui.common.ui.view.AllDeviceItemView;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.AllDeviceUIManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 3/6/16.
 */
public class CategoryAdapter extends BaseAdapter {
    private final String TAG = "CategoryAdapter";
    private static final int TYPE_CATEGORY_ITEM = 0;
    private static final int TYPE_ITEM = 1;

    private ArrayList<Category> mListData;
    private LayoutInflater mInflater;
    private boolean isShowCheckBox = false;
    private Context mContext;
    private ButtomType buttomType = ButtomType.NORMAL;
    private final int HEADHEIGHT = 30;
    private final int EMPTY = 0;


    public CategoryAdapter(Context context, ArrayList<Category> mData) {
        mContext = context;
        mListData = mData;
        mInflater = LayoutInflater.from(context);
    }

    public CategoryAdapter(Context context, ArrayList<Category> pData, ButtomType buttomType) {
        mContext = context;
        mListData = pData;
        mInflater = LayoutInflater.from(context);
        this.buttomType = buttomType;
    }

    public void changeData(ArrayList<Category> data) {
        this.setCategoryList(data);
        this.notifyDataSetChanged();
    }

    private void setCategoryList(ArrayList<Category> data) {
        if (mListData != null) {
            this.mListData = data;
        } else {
            this.mListData = new ArrayList<>();
        }
    }

    public boolean isShowCheckBox() {
        return isShowCheckBox;
    }

    public void setShowCheckBox(boolean isShow) {
        this.isShowCheckBox = isShow;
    }

    public ButtomType getButtomType() {
        return buttomType;
    }

    public void setButtomType(ButtomType buttomType) {
        this.buttomType = buttomType;

    }

    @Override
    public int getCount() {
        int count = EMPTY;

        if (null != mListData) {
            //  所有分类中item的总和是ListVIew  Item的总个数
            for (Category category : mListData) {
                count += category.getItemCount();
            }
        }
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "count: " + count);
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

        for (Category category : mListData) {
            int size = category.getItemCount();
//            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "size: " + size);
            // 在当前分类中的索引值
            int categoryIndex = position - categroyFirstIndex;
//            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "categoryIndex: " + categoryIndex);
            // item在当前分类内
            if (categoryIndex < size) {
                return category.getItem(categoryIndex);
            }

            // 索引移动到当前分类结尾，即下一个分类第一个元素索引
            categroyFirstIndex += size;
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "position111: " + position);

        // 异常情况处理
        if (null == mListData || position < EMPTY || position > getCount()) {
            return TYPE_ITEM;
        }


        int categroyFirstIndex = EMPTY;

        for (Category category : mListData) {
            int size = category.getItemCount();
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

                    convertView = mInflater.inflate(R.layout.all_device_listview_item_header, null);
                }
                RelativeLayout headRl = ViewHolderUtil.get(convertView, R.id.layout_header);
                TextView headerNameTv = ViewHolderUtil.get(convertView, R.id.home_manager_header_name_tv);
                ListView.LayoutParams params;
                if ("".equals(getItem(position))) {
                    params = new ListView.LayoutParams
                            (ListView.LayoutParams.MATCH_PARENT, 0);
                    headRl.setLayoutParams(params);
                    headerNameTv.setVisibility(View.GONE);
                    headRl.setVisibility(View.GONE);
                } else if (getItem(position) != null) {
                    params = new ListView.LayoutParams
                            (ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT);
                    headRl.setLayoutParams(params);
                    headerNameTv.setVisibility(View.VISIBLE);
                    //change select a group to group character.
                    String title = (String) getItem(position);
                    if (ButtomType.NORMAL == buttomType && mContext.getString(R.string.select_a_group).equals(title)) {
                        mListData.get(0).setmCategoryName(mContext.getString(R.string.group));
                        headerNameTv.setText(mContext.getString(R.string.group));
                    } else {
                        headerNameTv.setText(title);
                    }
                } else {
                    // add one group to group list
                    params = new ListView.LayoutParams
                            (ListView.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(HEADHEIGHT));
                    headRl.setLayoutParams(params);
                    headerNameTv.setVisibility(View.GONE);
                }

                break;

            case TYPE_ITEM:
                if (null == convertView) {
                    AllDeviceItemView itemView = new AllDeviceItemView(mContext);
                    convertView = itemView;
                }
                Object object = getItem(position);
                if (object != null && object instanceof DeviceGroupItem) {
                    ((AllDeviceItemView) convertView).initViewHolder(convertView, (DeviceGroupItem) object, buttomType);
                } else if (object != null && object instanceof SelectStatusDeviceItem) {
                    ((AllDeviceItemView) convertView).initViewHolder(convertView, (SelectStatusDeviceItem) object, buttomType);
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

    public void setAllDeviceSelectStatus(boolean isSelected) {
        if (mListData != null && mListData.size() > EMPTY) {
            for (Category category : mListData) {
                if (category.getType() == AllDeviceUIManager.DEVICE_TYPE) {
                    category.setAllDeviceSelectStatus(isSelected);
                }
            }
            notifyDataSetChanged();
        }
    }

    public boolean isSelectOneDevice() {
        if (mListData != null && mListData.size() > EMPTY) {
            for (Category category : mListData) {
                if (category.getType() == AllDeviceUIManager.DEVICE_TYPE) {
                    List<SelectStatusDeviceItem> selectStatusDeviceItemList = category.getmSelectDeviceListItem();
                    if (selectStatusDeviceItemList != null && selectStatusDeviceItemList.size() > EMPTY) {
                        for (SelectStatusDeviceItem selectStatusDeviceItem : selectStatusDeviceItemList) {
                            if (selectStatusDeviceItem.isSelected() == true) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<Category> getmListData() {
        return mListData;
    }
}
