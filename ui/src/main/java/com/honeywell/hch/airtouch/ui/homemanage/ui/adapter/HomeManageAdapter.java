package com.honeywell.hch.airtouch.ui.homemanage.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.util.ViewHolderUtil;
import com.honeywell.hch.airtouch.plateform.http.manager.model.CategoryHomeCity;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeAndCity;
import com.honeywell.hch.airtouch.ui.homemanage.ui.view.HomeManagerItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 13/7/16.
 */
public class HomeManageAdapter extends BaseAdapter {
    private final String TAG = "CategoryAdapter";
    private static final int TYPE_CATEGORY_ITEM = 0;
    private static final int TYPE_ITEM = 1;

    private List<CategoryHomeCity> mListData;
    private LayoutInflater mInflater;
    private Context mContext;
    private final int EMPTY = 0;
    private DeleteHomeCallback mDeleteHomeCallback;
    private RenameHomeCallback mRenameHomeCallback;
    private SetDefaultHomeCallback mSetDefaultHomeCallback;


    public HomeManageAdapter(Context context, List<CategoryHomeCity> mData) {
        mContext = context;
        mListData = mData;
        mInflater = LayoutInflater.from(context);
    }


    public void changeData(List<CategoryHomeCity> data) {
        this.setCategoryList(data);
        this.notifyDataSetChanged();
    }

    private void setCategoryList(List<CategoryHomeCity> data) {
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
            //  所有分类中item的总和是ListVIew  Item的总个数
            for (CategoryHomeCity categoryHomeCity : mListData) {
                count += categoryHomeCity.getItemCount();
            }
        }
//        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "count: " + count);
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

        for (CategoryHomeCity categoryHomeCity : mListData) {
            int size = categoryHomeCity.getItemCount();
//            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "size: " + size);
            // 在当前分类中的索引值
            int categoryIndex = position - categroyFirstIndex;
//            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "categoryIndex: " + categoryIndex);
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
//        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "position111: " + position);

        // 异常情况处理
        if (null == mListData || position < EMPTY || position > getCount()) {
            return TYPE_ITEM;
        }


        int categroyFirstIndex = EMPTY;

        for (CategoryHomeCity categoryHomeCity : mListData) {
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
//        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "position222: " + position);
        int itemViewType = getItemViewType(position);
//        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "itemViewType: " + itemViewType);
        switch (itemViewType) {
            case TYPE_CATEGORY_ITEM:
                if (null == convertView) {

                    convertView = mInflater.inflate(R.layout.home_manager_listview_item_header, null);
                }
                TextView headerNameTv = ViewHolderUtil.get(convertView, R.id.home_manager_header_name_tv);
                headerNameTv.setText((String) getItem(position));
                break;

            case TYPE_ITEM:
                if (null == convertView) {
//                    LogUtil.log(LogUtil.LogLevel.INFO, TAG, "convertView ＝＝ null ");
                    HomeManagerItemView itemView = new HomeManagerItemView(mContext);
                    itemView.setmSetDefaultHomeCallback(mSetDefaultHomeCallback);
                    itemView.setmDeleteHomeCallback(mDeleteHomeCallback);
                    itemView.setmRenameHomeCallback(mRenameHomeCallback);

                    convertView = itemView;

                }
                Object object = getItem(position);
                if (object != null && object instanceof HomeAndCity) {
                    ((HomeManagerItemView) convertView).initViewHolder(convertView, (HomeAndCity) object, mContext);
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

    public List<CategoryHomeCity> getmListData() {
        return mListData;
    }


    public interface SetDefaultHomeCallback {
        void callback(int locaionId);
    }

    public interface DeleteHomeCallback {
        void callback(HomeAndCity homeAndCity);
    }

    public interface RenameHomeCallback {
        void callback(HomeAndCity homeAndCity);
    }

    public void setmSetDefaultHomeCallback(SetDefaultHomeCallback mSetDefaultHomeCallback) {
        this.mSetDefaultHomeCallback = mSetDefaultHomeCallback;
    }

    public void setRemoveHomeCallback(DeleteHomeCallback deleteHomeCallback) {
        mDeleteHomeCallback = deleteHomeCallback;
    }

    public void setRenameHomeCallback(RenameHomeCallback renameHomeCallback) {
        mRenameHomeCallback = renameHomeCallback;
    }
}
