package com.honeywell.hch.airtouch.ui.common.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.honeywell.hch.airtouch.library.util.DensityUtil;


/**
 * Created by h127856 on 16/8/29.
 */
public class LimitHeightScrollView extends ScrollView {

    public LimitHeightScrollView(Context context) {
        super(context);
    }

    public LimitHeightScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LimitHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(DensityUtil.dip2px(150), MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
