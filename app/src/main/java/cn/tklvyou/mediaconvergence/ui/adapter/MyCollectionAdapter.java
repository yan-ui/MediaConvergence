package cn.tklvyou.mediaconvergence.ui.adapter;

import android.view.View;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.tklvyou.mediaconvergence.R;
import cn.tklvyou.mediaconvergence.helper.GlideManager;
import cn.tklvyou.mediaconvergence.model.NewsBean;

/**
 * @author :JenkinsZhou
 * @description :我的收藏
 * @company :途酷科技
 * @date 2019年08月02日15:00
 * @Email: 971613168@qq.com
 */
public class MyCollectionAdapter extends BaseQuickAdapter<NewsBean, BaseViewHolder> {

    public MyCollectionAdapter() {
        super(R.layout.item_news_news_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NewsBean bean) {
        helper.setText(R.id.tvTitle, bean.getName());
        helper.setText(R.id.tvTime, bean.getBegintime());
        helper.setText(R.id.tvSeeNum, "" + bean.getVisit_num());
        helper.setText(R.id.tvGoodNum, "" + bean.getLike_num());

        if (!StringUtils.isEmpty(bean.getImage())) {
            //一张图片
            helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
            helper.getView(R.id.ivImageOne).setVisibility(View.VISIBLE);

            GlideManager.loadRoundImg(bean.getImage(), helper.getView(R.id.ivImageOne));

        } else {
            if (bean.getImages() == null || bean.getImages().size() == 0) {
                //没有图片
                helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
                helper.getView(R.id.ivImageOne).setVisibility(View.GONE);
            } else {
                if (bean.getImages().size() < 3) {
                    //一张图片
                    helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
                    helper.getView(R.id.ivImageOne).setVisibility(View.VISIBLE);

                    GlideManager.loadRoundImg(bean.getImage(), helper.getView(R.id.ivImageOne));
                } else {
                    //多张图片
                    helper.getView(R.id.ivImageOne).setVisibility(View.GONE);
                    helper.getView(R.id.llMultiImage).setVisibility(View.VISIBLE);

                    GlideManager.loadRoundImg(bean.getImages().get(0), helper.getView(R.id.ivImageFirst));
                    GlideManager.loadRoundImg(bean.getImages().get(1), helper.getView(R.id.ivImageSecond));
                    GlideManager.loadRoundImg(bean.getImages().get(2), helper.getView(R.id.ivImageThree));
                }
            }
        }
    }
}
