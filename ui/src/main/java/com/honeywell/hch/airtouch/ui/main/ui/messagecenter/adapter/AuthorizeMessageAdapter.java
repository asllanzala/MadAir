package com.honeywell.hch.airtouch.ui.main.ui.messagecenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.http.model.message.MessageModel;
import com.honeywell.hch.airtouch.ui.common.ui.controller.HomeDeviceInfoBaseFragment;
import com.honeywell.hch.airtouch.ui.main.ui.messagecenter.MessagesFragment;
import com.honeywell.hch.airtouch.ui.main.ui.messagecenter.view.MessageListItemView;

import java.util.ArrayList;

/**
 * Created by Jesse on 12/8/16.
 */
public class AuthorizeMessageAdapter extends BaseAdapter {
    private final String TAG = "AuthorizeMessageAdapter";
    private LayoutInflater inflater;
    private ArrayList<MessageModel> mMessageModelArrayList;
    private Context mContext;
    private HomeDeviceInfoBaseFragment.ButtomType mButtomType = HomeDeviceInfoBaseFragment.ButtomType.NORMAL;
    private final int EMPTY = 0;


    public AuthorizeMessageAdapter(Context context, HomeDeviceInfoBaseFragment.ButtomType mButtomType, ArrayList<MessageModel> messageModelArrayList) {
        mContext = context;
        mButtomType = mButtomType;
        this.inflater = LayoutInflater.from(context);
        setmGetAuthMessagesResponse(messageModelArrayList);
    }

    @Override
    public int getCount() {
        return mMessageModelArrayList.size();
    }

    @Override
    public MessageModel getItem(int position) {
        return mMessageModelArrayList.size() == 0 ?
                null : mMessageModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void setmGetAuthMessagesResponse(ArrayList<MessageModel> messageModelArrayList) {
        if (messageModelArrayList != null) {
            this.mMessageModelArrayList = messageModelArrayList;
        } else {
            this.mMessageModelArrayList = new ArrayList<>();
        }
    }

    public void removeMessage(ArrayList<MessageModel> messageModelArrayList) {
        this.setmGetAuthMessagesResponse(messageModelArrayList);
        this.notifyDataSetChanged();
    }

    public void removeMessage(int position) {
        if (position < mMessageModelArrayList.size()) {
            mMessageModelArrayList.remove(position);
            this.notifyDataSetChanged();
        }
    }

    public void changeMessageReadStatus(int position) {
        if (position < mMessageModelArrayList.size()) {
            MessageModel messageModel = mMessageModelArrayList.get(position);
            messageModel.setIsReaded(true);
            this.notifyDataSetChanged();
        }
    }

    public void append(ArrayList<MessageModel> messageModelArrayList) {
        if (messageModelArrayList != null) {
            if (MessagesFragment.mLoadMode != MessagesFragment.MANUAL_LOADING) {
                this.mMessageModelArrayList.addAll(0, messageModelArrayList);
            } else {
                this.mMessageModelArrayList.addAll(messageModelArrayList);
            }
            this.notifyDataSetChanged();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            MessageListItemView messageListItemView = new MessageListItemView(mContext);
            convertView = messageListItemView;
        }
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "positionL " + position);
        ((MessageListItemView) convertView).initViewHolder(convertView, getItem(position), mButtomType);
        //hide the split line
        return convertView;
    }

    public ArrayList<MessageModel> getMessageModelArrayList() {
        return mMessageModelArrayList;
    }

    public void setAllDeviceSelectStatus(boolean isSelected) {
        if (mMessageModelArrayList != null && mMessageModelArrayList.size() > EMPTY) {
            for (MessageModel messageModel : mMessageModelArrayList) {
                messageModel.setIsSelected(isSelected);
            }
            notifyDataSetChanged();
        }
    }

    public boolean isSelectOneDevice() {
        if (mMessageModelArrayList != null && mMessageModelArrayList.size() > EMPTY) {
            for (MessageModel messageModel : mMessageModelArrayList) {
                if (messageModel.isSelected() == true) {
                    return true;
                }
            }
        }
        return false;
    }

    public HomeDeviceInfoBaseFragment.ButtomType getmButtomType() {
        return mButtomType;
    }

    public void setmButtomType(HomeDeviceInfoBaseFragment.ButtomType buttomType) {
        mButtomType = buttomType;

    }
}