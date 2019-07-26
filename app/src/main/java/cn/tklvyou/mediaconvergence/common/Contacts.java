package cn.tklvyou.mediaconvergence.common;

import cn.tklvyou.mediaconvergence.BuildConfig;

/**
 * 常量
 */

public class Contacts {
    public static final String PACKAGE_NAME  = BuildConfig.APPLICATION_ID;

    public static final String WX_APPID = "wx3314443bcadc6e01";

    public static final String SELECTED_CHANNEL_JSON ="SELECTED_CHANNEL";
    public static final String UNSELECTED_CHANNEL_JSON ="UNSELECTED_CHANNEL";
    public static final String CHANNEL_CODE ="CHANNEL_CODE";
    public static final String IS_VIDEO_LIST ="IS_VIDEO_LIST";

    public static String DEV_BASE_URL = "http://medium.tklvyou.cn/";
    public static String PRO_BASE_URL = "http://medium.tklvyou.cn/";

    public static boolean cache = true;//开启缓存
    public static boolean preload = true;//开启预加载


}
