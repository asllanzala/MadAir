package com.honeywell.hch.airtouch.ui.emotion.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.util.ViewHolderUtil;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.EmotionBottleResponse;

import java.text.DecimalFormat;

/**
 * Created by Vincent on 15/8/16.
 */
public class EmotionItemView extends RelativeLayout {
    private final String TAG = "AllDeviceItemView";
    private Context mContext;
    private final int HUNDRED = 100;
    private final int THOUSAND = 1000;
    private final int MILLION = 100000;
    private final int KMILLION = 1000000;

    public EmotionItemView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public EmotionItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.emotion_listview_item, this);
    }

    public void initViewHolder(View view, EmotionBottleResponse emotionBottleResponse, int position) {
        TextView deviceTitle = ViewHolderUtil.get(view, R.id.emotion_device_title);
        TextView deviceContent = ViewHolderUtil.get(view, R.id.emotion_device_title_content);
        TextView deviceData = ViewHolderUtil.get(view, R.id.emotion_data_tv);
        TextView deviceScal = ViewHolderUtil.get(view, R.id.emotion_scale_tv);
        ImageView deviceType = ViewHolderUtil.get(view, R.id.emotion_device_type_iv);
        ImageView deviceIc = ViewHolderUtil.get(view, R.id.emotion_itemt_ic_iv);
        TextView deviceMattter = ViewHolderUtil.get(view, R.id.emotion_matter_tv);
        TextView deviceIcTv = ViewHolderUtil.get(view, R.id.emotion_item_ic_tv);


        if (emotionBottleResponse.getEmtoionType() == HPlusConstants.EMOTION_TYPE_AIRTOUCH) {
            deviceTitle.setText(mContext.getString(R.string.emotion_title_air_title));
            deviceContent.setText(mContext.getString(R.string.emotion_title_air_content));
            deviceMattter.setText(mContext.getString(R.string.emotion_particular_matters));
            deviceIc.setImageResource(R.drawable.emotion_air_ic);
            deviceIc.setBackgroundColor(getResources().getColor(R.color.pm_emtoion_view));
            deviceType.setImageResource(R.drawable.emotion_air_bottle);
            deviceIcTv.setText(mContext.getString(R.string.emotion_air_ic));
            float cleanDust = emotionBottleResponse.getCleanDust();
            setAirData(deviceData, deviceScal, cleanDust);

        } else if (emotionBottleResponse.getEmtoionType() == HPlusConstants.EMOTION_TYPE_WATER) {
            deviceTitle.setText(mContext.getString(R.string.emotion_title_water_title));
            deviceContent.setText(mContext.getString(R.string.emotion_title_water_content));
            deviceData.setText(String.valueOf(emotionBottleResponse.getOutflowVolume()));
            deviceMattter.setText(mContext.getString(R.string.emotion_pure_water));
            deviceIc.setImageResource(R.drawable.emotion_water_ic);

            if (position == 0){
                deviceIc.setBackgroundColor(getResources().getColor(R.color.pm_emtoion_view));
            }else{
                deviceIc.setBackground(getResources().getDrawable(R.drawable.water_emotion_bg));

            }
            deviceType.setImageResource(R.drawable.emotion_water_cup);
            deviceIcTv.setText(mContext.getString(R.string.emotion_water_ic));
            float waterVolume = emotionBottleResponse.getOutflowVolume();
            setWaterData(deviceData, deviceScal, waterVolume);
        }

    }

    private void setAirData(TextView dataTv, TextView scaleTv, float cleanDust) {

        if (cleanDust < HUNDRED) {
            DecimalFormat decimalFormat = new DecimalFormat(HPlusConstants.FLOATFORMAT);//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String value = decimalFormat.format(cleanDust);//format 返回的是字符串
            dataTv.setText(String.valueOf(value));
            scaleTv.setText(mContext.getString(R.string.emotion_mg));

        } else if (cleanDust < MILLION) {
            DecimalFormat decimalFormat = new DecimalFormat(HPlusConstants.FLOATFORMAT);
            String value = decimalFormat.format(cleanDust / THOUSAND);
            dataTv.setText(String.valueOf(value));
            scaleTv.setText(mContext.getString(R.string.emotion_g));
        } else {
            DecimalFormat decimalFormat = new DecimalFormat(HPlusConstants.FLOATFORMAT);
            String value = decimalFormat.format(cleanDust / KMILLION);
            dataTv.setText(String.valueOf(value));
            scaleTv.setText(mContext.getString(R.string.emotion_kg));
        }
    }

    private void setWaterData(TextView dataTv, TextView scaleTv, float volume) {
        if (volume < HUNDRED) {
            DecimalFormat decimalFormat = new DecimalFormat(HPlusConstants.FLOATFORMAT);//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String value = decimalFormat.format(volume);//format 返回的是字符串
            dataTv.setText(String.valueOf(value));
            scaleTv.setText(mContext.getString(R.string.emotion_l));

        } else if (volume < MILLION) {
            DecimalFormat decimalFormat = new DecimalFormat(HPlusConstants.FLOATFORMAT);
            String value = decimalFormat.format(volume / THOUSAND);
            dataTv.setText(String.valueOf(value));
            scaleTv.setText(mContext.getString(R.string.emotion_m));
        } else {
            DecimalFormat decimalFormat = new DecimalFormat(HPlusConstants.FLOATFORMAT);
            String value = decimalFormat.format(volume / KMILLION);
            dataTv.setText(String.valueOf(value));
            scaleTv.setText(mContext.getString(R.string.emotion_km));
        }
    }

    public void hideSplitLine(View view, int visibility) {
        ImageView line = ViewHolderUtil.get(view, R.id.emotion_short_sepetate);
        line.setVisibility(visibility);
    }

}
