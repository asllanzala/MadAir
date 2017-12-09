package com.honeywell.hch.airtouch.plateform.share;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.library.util.StatusBarUtil;
import com.honeywell.hch.airtouch.plateform.R;

import java.io.OutputStream;


/**
 * Created by Jin Qian on 6/24/2015.
 */
public class ShareUtility {
    public static final Uri EXTERNAL_CONTENT_URI =
            getContentUri("external");

    public static final String AUTHORITY = "media";
    private static final String CONTENT_AUTHORITY_SLASH = "content://" + AUTHORITY + "/";

    private static final String TAG = "AirTouchSharing";
    public static final String APPID = "wx390720e3d10d4b39";
    public static final String APPSECRET = "68217555d75cbf33774c59e048294045";

    private static Context mContext;
    private UmengShareListener mUmengShareListener;

    private static ShareUtility mUtility = null;

    //weibo
    public static final String WB_APPID = "1764920366";
    public static final String WB_APPSECRET = "a477eb74accf7a06159d2112a6761737";

    private Bitmap mShareBitmap;

    private static final int THUMB_SIZE = 150;


    public interface UmengShareListener {
        void onComplete();
    }

    public void setOnUmengShareListener(UmengShareListener listener) {
        mUmengShareListener = listener;
    }


    public static ShareUtility getInstance(Context context) {
        if (mUtility == null) {
            mUtility = new ShareUtility();
            mContext = context;
        }
        return mUtility;
    }


    public void shareImage(Bitmap mSocialShareBitmap) {
//        Uri uri = Uri.parse(insertImage(mContext.getContentResolver(), mSocialShareBitmap));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, insertImage2(mContext.getContentResolver(), mSocialShareBitmap));
        shareIntent.setType("image/jpeg");
        mContext.startActivity(Intent.createChooser(shareIntent, mContext.getResources().getText(R.string.tell_someone_care)));
    }


    public static final Uri insertImage2(ContentResolver cr, Bitmap source) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        Uri url = null;

        try {
            url = cr.insert(EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 100, imageOut);
                } finally {
                    imageOut.close();
                }

//                long id = ContentUris.parseId(url);
//                BitmapFactory.Options option = new BitmapFactory.Options();
//                // 压缩图片:表示缩略图大小为原始图片大小的几分之一，1为原图
//                option.inSampleSize = 1;
//                // Wait until MINI_KIND thumbnail is generated.
//                Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id,
//                        MediaStore.Images.Thumbnails.MINI_KIND, option);
//                // This is for backward compatibility.
//                Bitmap microThumb = StoreThumbnail(cr, miniThumb, id, 50F, 50F,
//                        MediaStore.Images.Thumbnails.MICRO_KIND);
            } else {
                Log.e(TAG, "Failed to create thumbnail, removing original");
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to insert image", e);
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        return url;
    }

    private static Uri getContentUri(String volumeName) {
        return Uri.parse(CONTENT_AUTHORITY_SLASH + volumeName +
                "/images/media");
    }

    /**
     * //图片质量要好，必须保证图片不被拉伸或是压缩，分享图片的高度和要当前手机可显示的图片宽高一致
     * 如果当前有statusbar必须要去除，如果当前有navigator 底部导航栏，也必须去除。
     *
     * @param activity
     * @return
     */
    public static int getShareSceenHeight(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return DensityUtil.getScreenHeight();
        } else {
            return DensityUtil.getScreenHeight() - StatusBarUtil.getStatusBarHeight(activity);
        }
    }


    public static boolean isNavigatorBarVisible(Activity context) {
        boolean hasSoftNavigateBar = DensityUtil.navigationBarExist2(context);
        int usableHeightSansKeyboard = getActivityParentView(context).getRootView().getHeight();
        int heightDifference = usableHeightSansKeyboard - computeUsableHeight(context);
        if (heightDifference > (usableHeightSansKeyboard / 4) || hasSoftNavigateBar) {
            return true;
        } else {
            return false;
        }
    }

    public static int getNavigateHeight(Activity mContext) {
        int resourceId = mContext.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        //获取NavigationBar的高度
        return mContext.getResources().getDimensionPixelSize(resourceId);
    }

    private static View getActivityParentView(Activity activity) {
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        return content.getChildAt(0);
    }

    private static int computeUsableHeight(Activity activity) {
        Rect r = new Rect();
        getActivityParentView(activity).getWindowVisibleDisplayFrame(r);
        return r.bottom;
    }

}
