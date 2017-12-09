package com.honeywell.hch.airtouch.plateform.devices.feature.speed;

import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;

/**
 * Created by h127856 on 16/10/25.
 */
public class SpeedStatusBaseFeatureImpl implements ISpeedStatusFeature {

    protected AirtouchRunStatus mRunStatus;
    protected int mMaxSpeed;
    protected int mWorseSpeed;
    protected int mWorstSpeed;
    protected int mControlPoint;
    protected int mPointNumberPerSpped;

    protected int mSleepSpeed;
    protected int mSlientSpeed;

    public SpeedStatusBaseFeatureImpl(AirtouchRunStatus mRunStatus, int mMaxSpeed, int mWorseSpeed,
                                      int mWorstSpeed, int mControlPoint, int mPointNumberPerSpped
                                    , int mSleepSpeed, int mSlientSpeed){
          this.mRunStatus = mRunStatus;
          this.mMaxSpeed = mMaxSpeed;
          this.mWorseSpeed = mWorseSpeed;
          this.mWorstSpeed = mWorstSpeed;
          this.mControlPoint = mControlPoint;
          this.mPointNumberPerSpped = mPointNumberPerSpped;
          this.mSleepSpeed = mSleepSpeed;
          this.mSlientSpeed = mSlientSpeed;
    }

    @Override
    public int getMaxSpeed() {
        return mMaxSpeed;
    }

    @Override
    public int getWorstSpeed() {
        return mWorstSpeed;
    }

    @Override
    public int getWorseSpeed() {
        return mWorseSpeed;
    }

    @Override
    public int getDeviceControlPoint() {
        return mControlPoint;
    }

    @Override
    public int getPointsPerSpeed() {
        return mPointNumberPerSpped;
    }

    @Override
    public String getSpeedLevel(String speed) {
        return null;
    }


    @Override
    public int getSleepSpeed() {
        return mSleepSpeed;
    }

    @Override
    public int getSilentSpeed() {
        return mSlientSpeed;
    }
}
