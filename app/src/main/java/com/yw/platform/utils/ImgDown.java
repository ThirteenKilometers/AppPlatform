package com.yw.platform.utils;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by cxb on 2018/5/23.
 */

public class ImgDown {

    public static void oldImgDown(String url, ImageView iv){
        ImageLoader.getInstance().displayImage(url, iv);
    }


}
