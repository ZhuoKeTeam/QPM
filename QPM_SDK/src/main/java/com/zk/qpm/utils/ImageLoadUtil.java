package com.zk.qpm.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

public class ImageLoadUtil {

    public static void load(Context context, ImageView imageView, int imgId) {
        if (context == null || imageView == null || imgId <= 0) {
            return;
        }

//        Picasso.with(context)
//                .load(imgId)
//                .into(imageView);

        imageView.setImageResource(imgId);
    }

}
