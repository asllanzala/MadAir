package com.honeywell.hch.airtouch.ui.main.ui.me.feedback;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.database.manager.CityChinaDBService;
import com.honeywell.hch.airtouch.plateform.database.manager.CityIndiaDBService;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.permission.Permission;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.manager.model.DropTextModel;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseActivity;
import com.honeywell.hch.airtouch.ui.common.ui.view.DropEditText;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.enroll.ui.adapter.HomeSpinnerAdapter;
import com.honeywell.hch.airtouch.ui.homemanage.manager.HomeManagerUiManager;
import com.honeywell.hch.airtouch.ui.main.ui.me.feedback.interfacefile.IFeedBackView;
import com.honeywell.hch.airtouch.ui.main.ui.me.feedback.manager.FeedbackManager;
import com.honeywell.hch.airtouch.ui.main.ui.me.feedback.presenter.FeedBackPresenter;
import com.honeywell.hch.airtouch.ui.main.ui.me.feedback.view.ActionSheetDialog;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


/**
 * Created by Vincent on 3/11/16.
 */
public class FeedBackActivity extends BaseActivity implements IFeedBackView, ViewFactory, OnTouchListener, FeedbackManager.ErrorCallback, FeedbackManager.SuccessCallback {
    private ImageView mImageOneIv;
    private ImageView mImageTwoIv;
    private FeedBackPresenter mFeedBackPresenter;
    private ImageButton mImageAddIv;
    private RelativeLayout mRootRl;

    private ImageSwitcher mImageSwitcher;
    private int currentPosition;
    private float downX;
    private ArrayList<Bitmap> mBitmapArrayList = new ArrayList<>();
    private final int EMPTY = 0;
    private final int ONE = 1;
    private final int TWO = 2;
    private RelativeLayout mImageSwitchRl;
    private Button mSubmitBtn;
    private EditText mFeedbackEt;
    private TextView mPageSizeTv;
    private final String PAGE_SIZE_ZERO = "0/2";
    private final String PAGE_SIZE_ONE = "1/2";
    private final String PAGE_SIZE_TWO = "2/2";
    public static final int SUBMIT_FEEDBACK = 2003;
    public static final int SUBMIT_FEEDBACK_REQUEST_CODE = 13;
    private boolean isCameraPermission = false;
    private boolean isStoragePermission = false;
    private TextView mTitleTv;
    private FeedbackManager mFeedbackManager;
    private HomeManagerUiManager mHomeManagerUiManager;
    private CityChinaDBService mCityChinaDBService;
    private CityIndiaDBService mCityIndiaDBService;
    private ArrayList<String> imageUrl = new ArrayList<String>();
    private ArrayList<String> imageType = new ArrayList<String>();
    private DropEditText mDropEditText;
    private boolean isOnResume = false;
    private HomeSpinnerAdapter<DropTextModel> homeSpinnerTypeAdapter;
    private HomeDevice mHomeDevice = null;
    /*
        点击类型 0: take photo
        1: choose photo
     */
    private int CLICKTYPE = 0;

    private static final String IMG_UPLOAD_DATA = "img_upload_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mFeedbackManager = new FeedbackManager();
        mFeedbackManager.setSuccessCallback(this);
        mFeedbackManager.setErrorCallback(this);
        mHomeManagerUiManager = new HomeManagerUiManager();
        mCityChinaDBService = new CityChinaDBService(this);
        mCityIndiaDBService = new CityIndiaDBService(this);
        initStatusBar();
        init();
        initData();
    }

    private void init() {
        mImageOneIv = (ImageView) findViewById(R.id.feedback_photo_one_iv);
        mImageTwoIv = (ImageView) findViewById(R.id.feedback_photo_two_iv);
        mImageAddIv = (ImageButton) findViewById(R.id.feedback_photo_add_iv);
        mRootRl = (RelativeLayout) findViewById(R.id.root_view_id);
        mImageSwitcher = (ImageSwitcher) findViewById(R.id.image_switcher);
        mImageSwitchRl = (RelativeLayout) findViewById(R.id.feedback_image_switch_rl);
        mSubmitBtn = (Button) findViewById(R.id.feedback_sub_btn);
        mFeedbackEt = (EditText) findViewById(R.id.feedback_comment_et);
        mPageSizeTv = (TextView) findViewById(R.id.feedback_photo_page_tv);
        mTitleTv = (TextView) findViewById(R.id.title_textview_id);
        mDropEditText = (DropEditText) findViewById(R.id.feedback_select_device_et);
        mDropEditText.setfterSelectResultInterface(new DropEditText.IAfterSelectResult() {
            @Override
            public void setSelectResult(String cityCode) {
            }

            @Override
            public void setSelectResult(DropTextModel dropTextModel) {
                //选择了一个已有的家时候，需要记录当前选择的locationId
                mHomeDevice = dropTextModel.getmHomeDevice();

            }
        });
    }

    private void initData() {
        //set Factory
        mImageSwitcher.setFactory(this);
        mImageSwitcher.setOnTouchListener(this);
        mTitleTv.setText(getString(R.string.try_demo_feedback_title));
        mFeedBackPresenter = new FeedBackPresenter(mContext, this);
        mFeedbackEt.addTextChangedListener(mEditTextWatch);
        setupUI(findViewById(R.id.root_view_id));
        mPageSizeTv.setText(PAGE_SIZE_ZERO);
        buttonStatus(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isOnResume) {
            mDropEditText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFeedBackPresenter.initExistHomeDropContent();
                }
            }, 300);
            isOnResume = true;
        }
    }

    private void checkPermission(boolean isAll) {
        if (isAll) {
            mHPlusPermission.checkAndRequestPermission(Permission.PermissionCodes.STORAGE_AND_CAMERA_CODE, this);
        } else {
            mHPlusPermission.checkAndRequestPermission(Permission.PermissionCodes.STORAGE_REQUEST_CODE, this);
        }
    }

    private TextWatcher mEditTextWatch = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            buttonStatus(!StringUtil.isEmpty(mFeedbackEt.getText().toString()));
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    public void doClick(View v) {
        if (v.getId() == R.id.enroll_back_layout) {
            backIntent();
        } else if (v.getId() == R.id.feedback_photo_one_iv) {
            browseImage(EMPTY);
        } else if (v.getId() == R.id.feedback_photo_two_iv) {
            browseImage((mBitmapArrayList.size() - 1) % 2);  // index 2 -->1  1-- 0
        } else if (v.getId() == R.id.feedback_photo_add_iv) {
            showPhotoDialog();
        } else if (v.getId() == R.id.feedback_full_screen_iv) {
            exitBrowseImage();
        } else if (v.getId() == R.id.feedback_delete_iv) {
            deleteImage();
        } else if (v.getId() == R.id.feedback_sub_btn) {
            if (!TextUtils.isEmpty(mFeedbackEt.getText())) {
                dealFeedbackEvent(mFeedbackEt.getText().toString().trim());
            }
        }
    }

    private void dealFeedbackEvent(String feedbackText) {
        if (!isNetworkOff()) {
            String cityName = mHomeManagerUiManager.getCurrentCity(mCityChinaDBService, mCityIndiaDBService);
            mFeedbackManager.feedback(this, feedbackText, cityName, imageUrl, imageType, mHomeDevice);
            setResult(SUBMIT_FEEDBACK);
            backIntent();
        } else {
//            backIntent();
        }
    }

    private void dealFeedbackImgEvent(String path) {
        if (!isNetworkOff()) {
            byte[] bytes = getByteFromBitmap(path);
            String string = Base64.encodeToString(bytes, Base64.DEFAULT);
            if (string == null) {
                return;
            }
            mFeedbackManager.feedbackImg(string);
        }
    }

    private void deleteFeedBackImgEvent(String imageUrl) {
        if (!isNetworkOff()) {
            mFeedbackManager.deleteFeedbackImg(imageUrl);
        }
    }


    public byte[] getByteFromBitmap(String path) {
        Bitmap resizeBmp = compressImageFromFile(path);
        int bytes = resizeBmp.getByteCount();
        Log.e("____", "long:" + resizeBmp.getHeight() + "width:" + resizeBmp.getWidth() + "大小：" + bytes);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        resizeBmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appIcon = baos.toByteArray();// 转为byte数组
        return appIcon;
    }

    private void exitBrowseImage() {
        mRootRl.setVisibility(View.VISIBLE);
        mImageSwitchRl.setVisibility(View.GONE);
    }

    private void browseImage(int index) {
        currentPosition = index;
        mImageSwitcher.setImageDrawable(mFeedBackPresenter.bitmapToDrawable(mBitmapArrayList.get(index)));
        mRootRl.setVisibility(View.GONE);
        mImageSwitchRl.setVisibility(View.VISIBLE);
    }

    private void deleteImage() {
        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "CurretPosition: " + currentPosition);
        Bitmap bitmap = mBitmapArrayList.get(currentPosition);
//        mFeedBackPresenter.releaseBitMap(bitmap);
        mBitmapArrayList.remove(currentPosition);
        if (currentPosition == EMPTY
                && mImageOneIv.getVisibility() == View.VISIBLE) {
            mImageOneIv.setVisibility(View.GONE);
//            mFeedBackPresenter.releaseImageViewResouce(mImageOneIv.getDrawable());
            if (imageType.size() > 0) {
                imageType.remove(0);
            }
            if (imageUrl.size() > 0) {
                deleteFeedBackImgEvent(imageUrl.get(0));
                imageUrl.remove(0);
            }
        } else {
            mImageTwoIv.setVisibility(View.GONE);
//            mFeedBackPresenter.releaseImageViewResouce(mImageTwoIv.getDrawable());
            if (imageType.size() > 0) {
                imageType.remove(0);
            }
            if (imageUrl.size() > 0) {
                deleteFeedBackImgEvent(imageUrl.get(imageUrl.size() - 1));
                imageUrl.remove(imageUrl.size() - 1);
            }
        }
        setPageSizeAndAddIv();
        exitBrowseImage();
    }

    private void showPhotoDialog() {
        new ActionSheetDialog(mContext)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem(getString(R.string.try_demo_feedback_take_photo),
                        ActionSheetDialog.SheetItemColor.Black, R.drawable.feedback_take_photo,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                checkPermission(true);
                                CLICKTYPE = 0;
                                if (isCameraPermission && isStoragePermission) {
                                    mFeedBackPresenter.getImageFromCamera();
                                }
                            }
                        })
                .addSheetItem(getString(R.string.try_demo_feedback_choose_photo),
                        ActionSheetDialog.SheetItemColor.Black, R.drawable.feedback_choose_photo,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                CLICKTYPE = 1;
                                checkPermission(false);
                                //选择手机相册照片
                                if (isStoragePermission) {
                                    mFeedBackPresenter.getImageFromAlbum();
                                }
                            }
                        }).show();
    }

    public void openPermissionDialog(String message) {
        mAlertDialog = MessageBox.createTwoButtonDialog(this, "", message, getString(R.string.cancel), null, getString(R.string.go_to_setting), goToSetting);
    }


    protected MessageBox.MyOnClick goToSetting = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            goToPermissionSetting();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case FeedBackPresenter.REQUEST_CODE_PICK_IMAGE:
                if (intent == null) {
                    return;
                }
                mFeedBackPresenter.setImageFromAlbumBitmap(intent);
                break;
            case FeedBackPresenter.REQUEST_CODE_CAPTURE_CAMEIA:
                mFeedBackPresenter.setImageFromCameraBitmap();
                break;
        }
    }

    @Override
    public void dealWithGetImageFromAlbumBitmap(Bitmap bitmap, String path, String type) {
        if (mImageOneIv.getVisibility() == View.GONE) {
            showImageViewOne(bitmap, path);
        } else {
            showImageViewTwo(bitmap, path);
        }
        setPageSizeAndAddIv();
        imageType.add(type);
    }

    @Override
    public void dealWithGetImageFromCameraBitmap(Bitmap bitmap, String path, String type) {
        if (mImageOneIv.getVisibility() == View.GONE) {
            showImageViewOne(bitmap, path);
        } else {
            showImageViewTwo(bitmap, path);
        }
        setPageSizeAndAddIv();
        imageType.add(type);
    }

    private void showImageViewOne(Bitmap bitmap, String path) {
        mImageOneIv.setVisibility(View.VISIBLE);
        mImageOneIv.setDrawingCacheEnabled(false);
        mImageOneIv.setImageBitmap(bitmap);
        mBitmapArrayList.add(EMPTY, bitmap);
        dealFeedbackImgEvent(path);
    }

    private void showImageViewTwo(Bitmap bitmap, String path) {
        mImageTwoIv.setVisibility(View.VISIBLE);
            /*一定要调用setDrawingCacheEnabled(false)方法：
              以清空画图缓冲区，否则，下一次从ImageView对象iv_photo中获取的图像，还是原来的图像。
            */
        mImageTwoIv.setDrawingCacheEnabled(false);
        mImageTwoIv.setImageBitmap(bitmap);
        mBitmapArrayList.add(ONE, bitmap);
        dealFeedbackImgEvent(path);
    }


    private void setPageSizeAndAddIv() {
        if (mBitmapArrayList.size() == ONE) {
            mPageSizeTv.setText(PAGE_SIZE_ONE);
        } else if (mBitmapArrayList.size() == EMPTY) {
            mPageSizeTv.setText(PAGE_SIZE_ZERO);
        } else if (mBitmapArrayList.size() == TWO) {
            mPageSizeTv.setText(PAGE_SIZE_TWO);
        }
        if (mBitmapArrayList.size() < TWO) {
            mImageAddIv.setVisibility(View.VISIBLE);
        } else {
            mImageAddIv.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFeedBackPresenter.releaseImageViewResouce(mImageOneIv.getDrawable());
        mFeedBackPresenter.releaseImageViewResouce(mImageTwoIv.getDrawable());
        for (Bitmap bitmap : mBitmapArrayList) {
            mFeedBackPresenter.releaseBitMap(bitmap);
        }
    }

    @Override
    public View makeView() {
        final ImageView imageView = new ImageView(this);
        imageView.setBackgroundColor(getResources().getColor(R.color.black));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageSwitcher.LayoutParams layoutParams = new ImageSwitcher.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(DensityUtil.dip2px(20), DensityUtil.dip2px(20), DensityUtil.dip2px(20), 0);

        layoutParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                break;
            }
            case MotionEvent.ACTION_UP: {
                float lastX = event.getX();
                if (lastX > downX && Math.abs(lastX - downX) > 5) {
                    if (currentPosition > 0) {
                        mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.in_from_left));
                        mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.out_to_right));
                        currentPosition--;
                        mImageSwitcher.setImageDrawable(mFeedBackPresenter.bitmapToDrawable(mBitmapArrayList.get(currentPosition % mBitmapArrayList.size())));
                    }
                    break;
                }
                if (lastX < downX && Math.abs(lastX - downX) > 5) {
                    if (currentPosition < mBitmapArrayList.size() - 1) {
                        mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.in_from_right));
                        mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getApplication(), R.anim.out_to_left));
                        currentPosition++;
                        mImageSwitcher.setImageDrawable(mFeedBackPresenter.bitmapToDrawable(mBitmapArrayList.get(currentPosition)));
                    }
                    break;
                }
                //click empty area should exitBrowseImage
                exitBrowseImage();
            }
            break;
        }
        return true;
    }

    private void buttonStatus(boolean isClickable) {
        mSubmitBtn.setClickable(isClickable);
        mSubmitBtn.setEnabled(isClickable);
    }

    @Override
    public void onPermissionGranted(int permissionCode) {
        LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "onPermissionGranted");
        switch (permissionCode) {
            case Permission.PermissionCodes.CAMERA_REQUEST_CODE:
                isCameraPermission = true;
                break;
            case Permission.PermissionCodes.STORAGE_AND_CAMERA_CODE:
                isCameraPermission = true;
                isStoragePermission = true;
                break;
            case Permission.PermissionCodes.STORAGE_REQUEST_CODE:
                isStoragePermission = true;
                break;
        }
    }

    @Override
    public void onPermissionNotGranted(String[] permission, int permissionCode) {
        LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "onPermissionNotGranted");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(permission, permissionCode);
        }
    }

    @Override
    public void onPermissionDenied(int permissionCode) {
        LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "onPermissionDenied");
        if (permissionCode == Permission.PermissionCodes.CAMERA_REQUEST_CODE) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "onRequestPermissionsResult request code： " + requestCode);
        switch (requestCode) {
            case Permission.PermissionCodes.STORAGE_AND_CAMERA_CODE:
            case Permission.PermissionCodes.CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            if (permissions[i].equals(Permission.CAMERA)) {
                                openPermissionDialog(getString(R.string.feedback_camera_permission));
                            } else {
                                openPermissionDialog(getString(R.string.feedback_storage_permission));
                            }
                            isCameraPermission = false;
                            return;
                        }
                    }
                    isCameraPermission = true;
                    mFeedBackPresenter.getImageFromCamera();
                } else {
                    isCameraPermission = false;
                    openPermissionDialog(getString(R.string.feedback_camera_permission));
                }
                break;
            case Permission.PermissionCodes.STORAGE_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isStoragePermission = true;
                    if (CLICKTYPE == 1) {
                        mFeedBackPresenter.getImageFromAlbum();
                    } else if (isCameraPermission) {
                        mFeedBackPresenter.getImageFromCamera();
                    }
                } else {
                    isStoragePermission = false;
                    openPermissionDialog(getString(R.string.feedback_storage_permission));
                }
                break;
        }
    }

    @Override
    public void onError(ResponseResult responseResult) {

    }

    @Override
    public void onSuccess(ResponseResult responseResult) {
        Bundle bundle = responseResult.getResponseData();
        String str = bundle.getString(HPlusConstants.IMG_UPLOAD_CALLBACK);

        //接口调用成功
        imageUrl.add(str.replace("\"", ""));
    }

    private Bitmap compressImageFromFile(String srcPath) {

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 400f;//
        float ww = 280f;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;//设置采样率

        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;//当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // when the progress is finding the device , can not be back
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return backAction();
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean backAction() {
        if (mImageSwitchRl.getVisibility() == View.VISIBLE) {
            exitBrowseImage();
        } else {
            backIntent();
        }
        return false;
    }

    @Override
    public void initDropEditText(DropTextModel[] dropTextModels) {
        if (dropTextModels != null && dropTextModels.length > 0) {
            mDropEditText.getmEditText().setFocusable(false);
            mDropEditText.getmEditText().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mDropEditText.dealWithClickEvent();
                    return false;
                }
            });

            homeSpinnerTypeAdapter = new HomeSpinnerAdapter<>(this, dropTextModels);
            mDropEditText.setAdapter(homeSpinnerTypeAdapter, mDropEditText.getWidth());
            mDropEditText.getmDropImage().setVisibility(View.VISIBLE);

        } else {
            mDropEditText.setViewEnable(false);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (mDropEditText != null && !mDropEditText.isTouchInThisDropEditText(event.getX(), event.getY())) {
            mDropEditText.closeDropPopWindow();
        }
        return super.dispatchTouchEvent(event);

    }
}