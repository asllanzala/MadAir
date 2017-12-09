package com.honeywell.hch.airtouch.ui.main.ui.messagecenter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.util.DateTimeUtil;
import com.honeywell.hch.airtouch.library.util.ViewHolderUtil;
import com.honeywell.hch.airtouch.plateform.http.model.message.MessageModel;
import com.honeywell.hch.airtouch.ui.common.ui.controller.HomeDeviceInfoBaseFragment;

/**
 * Created by Vincent on 15/8/16.
 */
public class MessageListItemView extends RelativeLayout {
    private final String TAG = "AllDeviceItemView";
    private Context mContext;
    private final int HUNDRED = 100;
    private final int THOUSAND = 1000;
    private final int MILLION = 100000;
    private final int KMILLION = 1000000;

    public MessageListItemView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public MessageListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.message_item, this);
    }

    public void initViewHolder(View view, MessageModel messageModel, HomeDeviceInfoBaseFragment.ButtomType buttomType) {


        TextView contentTv = ViewHolderUtil.get(view, R.id.message_content_tv);
        TextView timeTv = ViewHolderUtil.get(view, R.id.message_time_tv);
        CheckBox checkBox = ViewHolderUtil.get(view, R.id.message_check_box);
        ImageView messageUnReadIv = ViewHolderUtil.get(view, R.id.message_unread_iv);
        ImageView messageStatusIv = ViewHolderUtil.get(view, R.id.message_status_iv);
        ImageView expandImg = ViewHolderUtil.get(view, R.id.message_item_expand_iv);

        if (messageModel != null) {
            switch (messageModel.getmMessageCategory()) {
                case 1:   //authorize
                    messageStatusIv.setImageResource(R.drawable.message_auth_icon);
                    break;

                case 3:   //WaterError
                    messageStatusIv.setImageResource(R.drawable.message_air_alarm);
                    break;
                case 4:   //ragular notice
                case 2:   //RemoteControl
                    messageStatusIv.setImageResource(R.drawable.message_regular_notice_icon);
                    break;
                default:
                    messageStatusIv.setImageResource(R.drawable.message_auth_icon);
                    break;
            }
        }
        contentTv.setText(messageModel.getmMessageContent());
        timeTv.setText(DateTimeUtil.getDateTimeString(DateTimeUtil.AUTHORIZE_TIME_FORMAT, DateTimeUtil.AUTHORIZE_TIME_FORMAT2,
                DateTimeUtil.AUTHORIZE_TIME_TO_FORMAT, messageModel.getmMessageTime()));

        checkBox.setChecked(messageModel.isSelected());
        if (buttomType != HomeDeviceInfoBaseFragment.ButtomType.NORMAL) {
            checkBox.setVisibility(VISIBLE);
            expandImg.setVisibility(INVISIBLE);
            messageUnReadIv.setVisibility(GONE);
        } else {
            checkBox.setVisibility(GONE);
            expandImg.setVisibility(VISIBLE);
            if (messageModel.isReaded()) {
                messageUnReadIv.setVisibility(INVISIBLE);
            } else {
                messageUnReadIv.setVisibility(VISIBLE);
            }
        }
    }
}
