package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.manager;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirMotorSpeed;

/**
 * Created by Qian Jin on 11/17/16.
 */

public class MadAirSpeedUIManager {

    private ImageView mAutoImageView;
    private ImageView mLowImageView;
    private ImageView mMediumImageView;
    private ImageView mHighImageView;
    private TextView mAutoTextView;
    private TextView mLowTextView;
    private TextView mMediumTextView;
    private TextView mHighTextView;

    private MadAirMotorSpeed mMadAirMotorSpeed = MadAirMotorSpeed.AUTO_SPEED;
    private IMadAirBLEDataManager mMadAirBleDataManager;
    private MadAirDeviceModel mMadAirDevice;
    private View mTopView;
    private Context mContext;

    public MadAirSpeedUIManager(MadAirDeviceModel device, IMadAirBLEDataManager madAirBleDataManager,
                                View topView, Context context) {

        mMadAirDevice = device;

        mMadAirBleDataManager = madAirBleDataManager;

        mTopView = topView;

        mContext = context;

        initView();

        autoOn();

        setOnClick();

    }

    public MadAirMotorSpeed getMotorSpeed() {
        return mMadAirMotorSpeed;
    }

    public void displaySpeed(int speed) {

        if (speed == MadAirMotorSpeed.AUTO_SPEED.getSpeed()) {
            mMadAirMotorSpeed = MadAirMotorSpeed.AUTO_SPEED;
            autoOn();
        }

        if (speed == MadAirMotorSpeed.LOW_SPEED.getSpeed()) {
            mMadAirMotorSpeed = MadAirMotorSpeed.LOW_SPEED;
            lowOn();
        }

        if (speed == MadAirMotorSpeed.MEDIUM_SPEED.getSpeed()) {
            mMadAirMotorSpeed = MadAirMotorSpeed.MEDIUM_SPEED;
            mediumOn();
        }

        if (speed == MadAirMotorSpeed.HIGH_SPEED.getSpeed()) {
            mMadAirMotorSpeed = MadAirMotorSpeed.HIGH_SPEED;
            highOn();
        }

        if (speed == MadAirMotorSpeed.STOP.getSpeed()) {
            mMadAirMotorSpeed = MadAirMotorSpeed.STOP;
            stopSpeed();
        }

    }

    private void initView() {
        mAutoImageView = (ImageView) mTopView.findViewById(R.id.auto_speed_iv);
        mLowImageView = (ImageView) mTopView.findViewById(R.id.low_speed_iv);
        mMediumImageView = (ImageView) mTopView.findViewById(R.id.medium_speed_iv);
        mHighImageView = (ImageView) mTopView.findViewById(R.id.high_speed_iv);
        mAutoTextView = (TextView) mTopView.findViewById(R.id.mad_air_dashboard_speed_auto_tv);
        mLowTextView = (TextView) mTopView.findViewById(R.id.mad_air_dashboard_speed_low_tv);
        mMediumTextView = (TextView) mTopView.findViewById(R.id.mad_air_dashboard_speed_medium_tv);
        mHighTextView = (TextView) mTopView.findViewById(R.id.mad_air_dashboard_speed_high_tv);
    }

    private void setOnClick() {
        mAutoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSpeed(MadAirMotorSpeed.AUTO_SPEED);
            }
        });
        mAutoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSpeed(MadAirMotorSpeed.AUTO_SPEED);
            }
        });

        mLowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSpeed(MadAirMotorSpeed.LOW_SPEED);
            }
        });
        mLowTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSpeed(MadAirMotorSpeed.LOW_SPEED);
            }
        });

        mMediumImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSpeed(MadAirMotorSpeed.MEDIUM_SPEED);
            }
        });
        mMediumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSpeed(MadAirMotorSpeed.MEDIUM_SPEED);
            }
        });

        mHighImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSpeed(MadAirMotorSpeed.HIGH_SPEED);
            }
        });
        mHighTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSpeed(MadAirMotorSpeed.HIGH_SPEED);
            }
        });
    }

    private void changeSpeed(MadAirMotorSpeed madAirMotorSpeed) {

        if (mMadAirDevice == null)
            return;

        mMadAirBleDataManager.changeMotorSpeed(mMadAirDevice.getMacAddress(), madAirMotorSpeed);

        switch (madAirMotorSpeed) {
            case AUTO_SPEED:
                autoOn();
                break;

            case LOW_SPEED:
                lowOn();
                break;

            case MEDIUM_SPEED:
                mediumOn();
                break;

            case HIGH_SPEED:
                highOn();
                break;

            default:
                break;

        }

    }

    private void stopSpeed() {
        autoOff();
        lowOff();
        mediumOff();
        highOff();
    }

    private void autoOn() {
        stopSpeed();
        mAutoImageView.setImageResource(R.drawable.mad_air_dashboard_auto_check);
        mAutoTextView.setTextColor(mContext.getResources().getColor(R.color.blue_one));
    }

    private void lowOn() {
        stopSpeed();
        mLowImageView.setImageResource(R.drawable.mad_air_dashboard_low_check);
        mLowTextView.setTextColor(mContext.getResources().getColor(R.color.blue_one));
    }

    private void mediumOn() {
        stopSpeed();
        mMediumImageView.setImageResource(R.drawable.mad_air_dashboard_medium_check);
        mMediumTextView.setTextColor(mContext.getResources().getColor(R.color.blue_one));
    }

    private void highOn() {
        stopSpeed();
        mHighImageView.setImageResource(R.drawable.mad_air_dashboard_high_check);
        mHighTextView.setTextColor(mContext.getResources().getColor(R.color.blue_one));
    }

    private void autoOff() {
        mAutoImageView.setImageResource(R.drawable.mad_air_dashboard_auto_unchecked);
        mAutoTextView.setTextColor(mContext.getResources().getColor(R.color.particle_orientation_color));
    }

    private void lowOff() {
        mLowImageView.setImageResource(R.drawable.mad_air_dashboard_low_unchecked);
        mLowTextView.setTextColor(mContext.getResources().getColor(R.color.particle_orientation_color));
    }

    private void mediumOff() {
        mMediumImageView.setImageResource(R.drawable.mad_air_dashboard_medium_unchecked);
        mMediumTextView.setTextColor(mContext.getResources().getColor(R.color.particle_orientation_color));
    }

    private void highOff() {
        mHighImageView.setImageResource(R.drawable.mad_air_dashboard_high_unchecked);
        mHighTextView.setTextColor(mContext.getResources().getColor(R.color.particle_orientation_color));
    }

}
