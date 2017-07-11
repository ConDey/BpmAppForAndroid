package com.eazytec.bpm.appstub.view.textview;

import android.graphics.Bitmap;

import com.eazytec.bpm.appstub.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * @author Administrator
 * @version Id: ImageOptHelper, v 0.1 2017/7/10 13:52 Administrator Exp $$
 */
public class ImageOptHelper {

    public static DisplayImageOptions getImgOptions() {
        DisplayImageOptions imgOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.drawable.ic_htmltextview_image_loading)
                .showImageForEmptyUri(R.drawable.ic_htmltextview_image_loading)
                .showImageOnFail(R.drawable.ic_htmltextview_image_loading)
                .build();
        return imgOptions;
    }

    public static DisplayImageOptions getBigImgOptions() {
        DisplayImageOptions imgOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        return imgOptions;
    }

    public static DisplayImageOptions getAvatarOptions() {
        DisplayImageOptions avatarOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.drawable.ic_htmltextview_image_loading)
                .showImageForEmptyUri(R.drawable.ic_htmltextview_image_loading)
                .showImageOnFail(R.drawable.ic_htmltextview_image_loading)
                .build();
        return avatarOptions;
    }

    public static DisplayImageOptions getCornerOptions(int cornerRadiusPixels) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.drawable.ic_htmltextview_image_loading)
                .showImageForEmptyUri(R.drawable.ic_htmltextview_image_loading)
                .showImageOnFail(R.drawable.ic_htmltextview_image_loading)
                .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels)).build();
        return options;
    }
}
