package com.honeywell.hch.airtouch.ui.main.ui.me.feedback.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.ui.common.manager.model.DropTextModel;
import com.honeywell.hch.airtouch.ui.main.ui.me.feedback.interfacefile.IFeedBackView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vincent on 4/11/16.
 */
public class FeedBackPresenter {
    private Context mContext;
    private IFeedBackView mIFeedBackView;
    public final static int REQUEST_CODE_PICK_IMAGE = 1001;
    public final static int REQUEST_CODE_CAPTURE_CAMEIA = 1002;
    private String capturePath = null;
    private Uri photoUri;

    public FeedBackPresenter(Context context, IFeedBackView feedBackView) {
        mContext = context;
        mIFeedBackView = feedBackView;
    }

    public FeedBackPresenter(Context context) {
        mContext = context;
    }

    public void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    public void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = createImgFile();
        photoUri = Uri.fromFile(photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
    }

    public String getCapturePath() {
        return capturePath;
    }

    /**
     * 自定义图片名，获取照片的file
     */
    private File createImgFile() {
        //创建文件
        String fileName = "img_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png";//确定文件名
        File dir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dir = Environment.getExternalStorageDirectory();
        } else {
            dir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }
        File tempFile = new File(dir, fileName);
        try {
            if (tempFile.exists()) {
                tempFile.delete();
            }
            tempFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取文件路径
        capturePath = tempFile.getAbsolutePath();
        return tempFile;
    }

    /**
     * 设置相册中图片
     */
    public void setImageFromCameraBitmap() {
        Bitmap bitmap = BitmapFactory.decodeFile(capturePath);
        if (bitmap != null) {
            String prefix=capturePath.substring(capturePath.lastIndexOf(".")+1);
            mIFeedBackView.dealWithGetImageFromCameraBitmap(bitmap,capturePath,prefix);
        }
    }

    public void setImageFromAlbumBitmap(Intent intent) {
        if (intent == null) {
            return;
        }
        Uri uri = intent.getData();
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = ((Activity) mContext).managedQuery(uri, proj, null, null, null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);

        String prefix=path.substring(path.lastIndexOf(".")+1);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        mIFeedBackView.dealWithGetImageFromAlbumBitmap(bitmap,path,prefix);
    }

    //将图片添加进手机相册
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(photoUri);
        mContext.sendBroadcast(mediaScanIntent);
    }

    public void releaseImageViewResouce(Drawable drawable) {
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    public void releaseBitMap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public Drawable bitmapToDrawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }

    public void initExistHomeDropContent() {
        mIFeedBackView.initDropEditText(getHomeStringArray());
    }

    /**
     * 获取下拉
     *
     * @return
     */
    private DropTextModel[] getHomeStringArray() {
        List<DropTextModel> dropTextModelsList = new ArrayList<>();

        List<UserLocationData> userLocations = UserAllDataContainer.shareInstance().getUserLocationDataList();

        if ((userLocations != null)) {
            for (int i = 0; i < userLocations.size(); i++) {
                ArrayList<HomeDevice> homeDeviceArrayList=userLocations.get(i).getHomeDevicesList();
                for(HomeDevice homeDevice:homeDeviceArrayList){
                    DropTextModel dropTextModel = new DropTextModel(homeDevice);
                    dropTextModel.setTextViewString(homeDevice.getDeviceInfo().getName());
                    dropTextModelsList.add(dropTextModel);
                }
            }
        }
        DropTextModel[] stringsArray = new DropTextModel[dropTextModelsList.size()];
        return dropTextModelsList.toArray(stringsArray);
    }

}
