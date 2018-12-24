package com.jm.android.gt.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

public class ImageLoadUtil {

    public static void load(Context context, ImageView imageView, int imgId) {
        if (context == null || imageView == null || imgId <= 0) {
            return;
        }

        // TODO: 2018/12/20 暂时去除 Picasso（减小体积），组件内的图片需要外界传入进来。
//        Picasso.with(context)
//                .load(imgId)
//                .into(imageView);

        imageView.setImageResource(imgId);
    }


    /**
     * TODO: 2018/12/20 暂时去除 Picasso（减小体积），组件内的图片需要外界传入进来。
     * @param context   context
     * @param imageView imageView
     * @param url       url
     */
    private static void load(Context context, ImageView imageView, String url) {
        if (context == null || imageView == null || TextUtils.isEmpty(url)) {
            return;
        }
        //
//        Picasso.with(context)
//                .load(url)
//                .placeholder(R.drawable.jm_gt_icon)
//                .error(R.drawable.jm_gt_icon)
//                .into(imageView);
    }
}
