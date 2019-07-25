package cn.tklvyou.mediaconvergence.utils;

import android.content.Context;
import android.widget.ImageView;

/**
 * 图片加载类
 * 防止更换框架
 */

public class ImageLoader {
    private static ImageLoader mInstance;

    public static ImageLoader getInstance(){
        if(mInstance == null){
            mInstance = new ImageLoader();
        }
        return mInstance;
    }

    /**
     * 显示图片
     * @param context
     * @param url
     * @param view
     */
    public void displayImage(Context context, String url, ImageView view) {
//        Glide.with(context)
//                .load(url)
//                .centerCrop()
//                .into(view);
    }
    public void displayImage(Context context, String url, ImageView view,int defaultViewId,int errorViewId) {
//        Glide.with(context)
//                .load(url)
//                .placeholder(defaultViewId)
//                .error(errorViewId)
//                .centerCrop()
//                .into(view);
    }

}
