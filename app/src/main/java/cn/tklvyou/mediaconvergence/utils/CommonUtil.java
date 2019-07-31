package cn.tklvyou.mediaconvergence.utils;

import android.text.TextUtils;

import androidx.core.content.ContextCompat;

import cn.tklvyou.mediaconvergence.base.MyApplication;

/**
 * @author :JenkinsZhou
 * @description :公共工具类
 * @company :途酷科技
 * @date 2019年07月30日15:04
 * @Email: 971613168@qq.com
 */
public class CommonUtil {

    public static int getColor(int colorId) {
        return ContextCompat.getColor(MyApplication.getInstance(), colorId);
    }


    public static String getNotNullValue(String value) {
        if (TextUtils.isEmpty(value)) {
            return "";
        }
        return value;
    }


}
