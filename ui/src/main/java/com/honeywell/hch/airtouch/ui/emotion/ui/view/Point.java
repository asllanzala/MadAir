package com.honeywell.hch.airtouch.ui.emotion.ui.view;

import java.io.Serializable;

/**
 * Created by Vincent on 21/12/16.
 */

public class Point  implements Serializable {
    private float mX;
    private float mY;

    public Point() {
    }

    public Point(float x, float y) {
        mX = x;
        mY = y;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    public void setX(float x) {
        mX = x;
    }

    public void setY(float y) {
        mY = y;
    }

    public void clearPoint(){

    }
}
