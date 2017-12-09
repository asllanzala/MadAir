package com.honeywell.hch.airtouch.ui.main.ui.me.feedback.interfacefile;

import android.graphics.Bitmap;

import com.honeywell.hch.airtouch.ui.common.manager.model.DropTextModel;

/**
 * Created by Vincent on 4/11/16.
 */
public interface IFeedBackView {

    void dealWithGetImageFromAlbumBitmap(Bitmap bitmap,String path,String type);

    void dealWithGetImageFromCameraBitmap(Bitmap bitmap,String path,String type);

    void initDropEditText( DropTextModel[] dropTextModels);
}
