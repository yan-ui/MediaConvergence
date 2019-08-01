package cn.tklvyou.mediaconvergence.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.tklvyou.mediaconvergence.R;
import cn.tklvyou.mediaconvergence.model.NewsBean;
import cn.tklvyou.mediaconvergence.model.NewsMultipleItem;
import cn.tklvyou.mediaconvergence.model.BasePageModel;
import cn.tklvyou.mediaconvergence.model.SuixiTvModel;


/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 * modify by AllenCoder
 */
public class NewsMultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<NewsMultipleItem, BaseViewHolder> {

    public NewsMultipleItemQuickAdapter(Context context, List data) {
        super(data);
        addItemType(NewsMultipleItem.VIDEO, R.layout.item_news_video);
        addItemType(NewsMultipleItem.TV, R.layout.item_news_suixi_tv);
    }


    @Override
    protected void convert(BaseViewHolder helper, NewsMultipleItem item) {
        switch (helper.getItemViewType()) {
            case NewsMultipleItem.VIDEO:
                NewsBean bean = (NewsBean) item.getDataBean();
                helper.setText(R.id.tvNewsTitle, bean.getName());
                helper.setText(R.id.tvVideoTime, bean.getTime());
                helper.setText(R.id.tvCommentNum, "" + bean.getComment_num());
                helper.setText(R.id.tvGoodNum, "" + bean.getLike_num());
                helper.setText(R.id.tvName, bean.getNickname());

                // 加载网络图片
                if (!StringUtils.isEmpty(bean.getAvatar().trim())) {
                    Glide.with(mContext).load(bean.getAvatar()).into((ImageView) helper.getView(R.id.ivAvatar));
                }

                Glide.with(mContext).load(bean.getImage()).into((ImageView) helper.getView(R.id.ivVideoBg));

                helper.addOnClickListener(R.id.videoLayout);

                break;
            case NewsMultipleItem.TV:
                SuixiTvModel suixiTvModel = (SuixiTvModel) item.getDataBean();

                if (suixiTvModel.getData().size() == 1) {
                    helper.getView(R.id.llSecondLayout).setVisibility(View.INVISIBLE);
                }

                helper.setText(R.id.tvModuleSecond, suixiTvModel.getModule_second());

                for (int i = 0; i < suixiTvModel.getData().size(); i++) {
                    if (i == 0) {
                        helper.setText(R.id.tvNameFirst, suixiTvModel.getData().get(0).getName());
                        helper.setText(R.id.tvTimeFirst, suixiTvModel.getData().get(0).getTime());
                        Glide.with(mContext).load(suixiTvModel.getData().get(0).getImage()).into((ImageView) helper.getView(R.id.ivImageFirst));
                    } else if (i == 1) {
                        helper.setText(R.id.tvNameSecond, suixiTvModel.getData().get(1).getName());
                        helper.setText(R.id.tvTimeSecond, suixiTvModel.getData().get(1).getTime());
                        Glide.with(mContext).load(suixiTvModel.getData().get(1).getImage()).into((ImageView) helper.getView(R.id.ivImageSecond));
                    }
                }

                break;
            default:
                break;
        }
    }
}