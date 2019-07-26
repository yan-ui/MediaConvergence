package cn.tklvyou.mediaconvergence.base;

import android.content.Context;
import android.widget.ImageView;


import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.tklvyou.mediaconvergence.R;
import cn.tklvyou.mediaconvergence.model.NewListModel;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 * modify by AllenCoder
 */
public class NewsMultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<NewsMultipleItem, BaseViewHolder> {

    public NewsMultipleItemQuickAdapter(Context context, List data) {
        super(data);
        addItemType(NewsMultipleItem.VIDEO, R.layout.item_news_video);
//        addItemType(NewsMultipleItem.TEXT, R.layout.item_img_text_view);
    }



    @Override
    protected void convert(BaseViewHolder helper, NewsMultipleItem item) {
        switch (helper.getItemViewType()) {
            case NewsMultipleItem.VIDEO:
                NewListModel.DataBean bean = item.getDataBean();
                helper.setText(R.id.tvNewsTitle, bean.getName());
                helper.setText(R.id.tvVideoTime,bean.getTime());
                helper.setText(R.id.tvCommentNum,""+bean.getComment_num());
                helper.setText(R.id.tvGoodNum,""+bean.getLike_num());
                helper.setText(R.id.tvName,bean.getNickname());

                // 加载网络图片
                if(!StringUtils.isEmpty(bean.getAvatar().trim())) {
                    Glide.with(mContext).load(bean.getAvatar()).into((ImageView) helper.getView(R.id.ivAvatar));
                }
                Glide.with(mContext).load(bean.getImage()).into((ImageView) helper.getView(R.id.ivVideoBg));

                break;
            case NewsMultipleItem.TEXT:

                break;
            default:
                break;
        }
    }

}