package com.honeywell.hch.airtouch.ui.authorize.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.util.ListViewHeightUtil;
import com.honeywell.hch.airtouch.library.util.ViewHolderUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.AuthHomeList;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.AuthHomeModel;

import java.util.ArrayList;
import java.util.List;


public class AuthParentAdapter extends BaseExpandableListAdapter {

    private final String TAG = "AuthParentAdapter";

    private Context mContext;

    private List<AuthHomeList> mParents;

    private OnChildTreeViewClickListener mTreeViewClickListener;
    private OnChildItemClickListener mOnChildItemClickListener;

    public AuthParentAdapter() {
    }


    public AuthParentAdapter(Context context, List<AuthHomeList> parents) {
        this.mContext = context;
        this.mParents = parents;
    }

    public void changeData(List<AuthHomeList> parents) {
        this.setAuthHomeList(parents);
        this.notifyDataSetChanged();
    }

    public void setAuthHomeList(List<AuthHomeList> parents) {
        if (mParents != null) {
            this.mParents = parents;
        } else {
            this.mParents = new ArrayList<AuthHomeList>();
        }
    }

    @Override
    public AuthHomeModel getChild(int groupPosition, int childPosition) {
        return mParents.get(groupPosition).getmAuthHome().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mParents == null || mParents.size() <= groupPosition) {
            return 0;
        }
        int childrenCount = mParents.get(groupPosition).getmAuthHome() != null ? mParents.get(groupPosition).getmAuthHome().size() : 0;
        return childrenCount;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isExpanded, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.auth_child_listview, null);
        }
        ListView listView = ViewHolderUtil.get(convertView, R.id.auth_child_lv);
        ArrayList<AuthHomeModel> childs = new ArrayList<AuthHomeModel>();
        final AuthHomeModel child = getChild(groupPosition, childPosition);

        childs.add(child);

        final AuthChildAdapter childAdapter = new AuthChildAdapter(this.mContext,
                childs);


        listView.setAdapter(childAdapter);
        ListViewHeightUtil.setListViewHeightBasedOnChildren(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int childIndex, long id) {

                mOnChildItemClickListener.onClickPosition(parent, view, groupPosition, childPosition,
                        childIndex, id);
            }
        });
        return convertView;


    }

    @Override
    public AuthHomeList getGroup(int groupPosition) {
        return mParents.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        int groupCount = mParents != null ? mParents.size() : 0;
        return groupCount;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.auth_parent_title_item, null);
        }
        TextView parentGroupTV = ViewHolderUtil.get(convertView, R.id.auth_parent_title_tv);
        if (mParents.get(groupPosition).getType() == AuthHomeList.Type.MYHOME) {
            parentGroupTV.setText(mContext.getString(R.string.authorize_my_devices));
        } else {
            parentGroupTV.setText(mParents.get(groupPosition).getmOwnerName() +
                    AppManager.getInstance().getApplication().getString(R.string.authorize_s));
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void setOnChildTreeViewClickListener(
            OnChildTreeViewClickListener treeViewClickListener) {
        this.mTreeViewClickListener = treeViewClickListener;
    }

    public void setOnChildItemClickListener(OnChildItemClickListener onChildItemClickListener) {
        this.mOnChildItemClickListener = onChildItemClickListener;
    }

    public interface OnChildItemClickListener {
        void onClickPosition(AdapterView<?> parent, View view, int parentPosition, int groupPosition, int childPosition, long id);
    }

    public interface OnChildTreeViewClickListener {

        void onClickPosition(int parentPosition, int groupPosition,
                             int childPosition);
    }

}
