package cn.tklvyou.mediaconvergence.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class NewsMultipleItem<T> implements MultiItemEntity {
    public static final int VIDEO = 1;                              //视频
    public static final int TV = 2;                                 //濉溪TV
    public static final int TEXT = 3;                               //纯文字
    public static final int TEXT_AND_SINGLE_IMAGE_FOR_LEFT = 4;     //文字+单张靠左的图片
    public static final int TEXT_AND_SINGLE_IMAGE_FOR_RIGHT = 5;    //文字+单张靠右的图片
    public static final int TEXT_AND_MULTI_IMAGE = 6;               //文字+多张图片
    public static final int WECHAT_MOMENTS = 7;                     //微信朋友圈
    public static final int READING = 8;                            //悦读（瀑布流卡片）
    public static final int LISTEN = 8;                              //悦听

    private int itemType;
    private T dataBean;

    public NewsMultipleItem(String model,T dataBean) {
        switch (model) {
            case "V视频":
                itemType = 1;
                break;
            case "濉溪TV":
                itemType = 2;
                break;
            default:
                itemType = 3;
                break;
        }
        this.dataBean = dataBean;
    }

    public T getDataBean() {
        return dataBean;
    }

    public void setDataBean(T dataBean) {
        this.dataBean = dataBean;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}