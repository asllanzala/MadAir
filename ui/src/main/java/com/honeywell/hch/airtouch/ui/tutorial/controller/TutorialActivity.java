package com.honeywell.hch.airtouch.ui.tutorial.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.StatusBarUtil;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseActivity;
import com.honeywell.hch.airtouch.ui.splash.StartActivity;
import com.honeywell.hch.airtouch.ui.tutorial.adapter.TutorialViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by h122063 on 16/8/18.
 */
public class TutorialActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager vp;
    private TutorialViewPagerAdapter vpAdapter;
    private List<View> views;

    private TextView mTutorialSkipTv;
    private Button mTutorialStartTv;

    //引导图片资源
    private static final int[] layouts = {R.layout.activity_tutorial_one
            /*, R.layout.activity_tutorial_two*/};

    //底部小点图片
    private ImageView[] dots;

    //记录当前选中位置
    private int currentIndex;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);

        vp = (ViewPager) findViewById(R.id.viewpager);
        views = new ArrayList<View>();
        initStatusBar();
        LayoutInflater inflater = LayoutInflater.from(this);
        //初始化引导图片列表
        for (int i = 0; i < layouts.length; i++) {
            View view = inflater.inflate(layouts[i], null);
            views.add(view);
        }


        //初始化Adapter
        vpAdapter = new TutorialViewPagerAdapter(views);
        vp.setAdapter(vpAdapter);
        //绑定回调
        vp.setOnPageChangeListener(this);

        vp.setOnTouchListener(new View.OnTouchListener() {
            float startX;
            float startY;
            float endX;
            float endY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        endY = event.getY();
                        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

                        //获取屏幕的宽度
                        Point size = new Point();
                        windowManager.getDefaultDisplay().getSize(size);
                        int width = size.x;

                        //首先要确定的是，是否到了最后一页，然后判断是否向左滑动，并且滑动距离是否符合，我这里的判断距离是屏幕宽度的4分之一（这里可以适当控制）
                        if (currentIndex == (layouts.length - 1) && startX - endX >= (width / 4)) {
                            goToStartOrBackActivity();
                        }
                        break;
                }
                return false;
            }
        });

        //初始化底部小点
        initDots();

        mTutorialSkipTv = (TextView) findViewById(R.id.tutorial_skip);
        mTutorialStartTv = (Button) findViewById(R.id.tutorial_start);
        if (mTutorialSkipTv != null && mTutorialStartTv != null) {
            setListener();
        }

    }

    protected void initStatusBar() {
        StatusBarUtil.changeStatusBarTextColor(findViewById(R.id.root_view_id), View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mContext != null) {
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(vp.getLayoutParams());
            rl.topMargin = StatusBarUtil.getStatusBarHeight(mContext);
            vp.setLayoutParams(rl);
        }
    }

    private void setListener() {
        mTutorialSkipTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStartOrBackActivity();
            }
        });
        mTutorialStartTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStartOrBackActivity();
            }
        });
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.tutorial_ll);

        dots = new ImageView[layouts.length];

        //循环取得小点图片
        for (int i = 0; i < layouts.length; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);//都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);//设置为白色，即选中状态
    }

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position >= layouts.length) {
            return;
        }

        vp.setCurrentItem(position);
    }

    /**
     * 这只当前引导小点的选中
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > layouts.length - 1 || currentIndex == positon) {
            return;
        }

        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = positon;
    }

    //当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    //当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub
    }

    //当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
//        if (arg0 == layouts.length - 1) {
//            mTutorialSkipTv.setVisibility(View.GONE);
//            mTutorialStartTv.setVisibility(View.VISIBLE);
//        } else {
//            mTutorialSkipTv.setVisibility(View.VISIBLE);
//            mTutorialStartTv.setVisibility(View.GONE);
//        }
        //设置底部小点选中状态
        setCurDot(arg0);
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }


    private void goToStartOrBackActivity() {
        if (StartActivity.first) {
            Intent i = new Intent(this, StartActivity.class);
            i.putExtra(StartActivity.FROM_ANOTHER_ACTIVITY, true);
            startActivity(i);
        }

        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finish();
    }


}
