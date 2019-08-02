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
 * @description :问政适配器
 * @company :途酷科技
 * @date 2019年08月02日18:06
 * @Email: 971613168@qq.com
 */
public class WenZhenAdapter extends BaseQuickAdapter<NewsBean, BaseViewHolder> {

    public WenZhenAdapter() {
        super(R.layout.item_news_wen_zheng);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NewsBean bean) {
        helper.setText(R.id.tvTitle, bean.getName());
        helper.setText(R.id.tvTime, bean.getBegintime());
        helper.setText(R.id.tvCommentNum, "" + bean.getComment_num());

        if (!StringUtils.isEmpty(bean.getImage())) {
            //一张图片
            helper.getView(R.id.llMultiImage).setVisibility(View.GONE);
            helper.getView(R.id.ivImageOne).setVisibility(View.VISIBLE);

            GlideManager.loadRoundImg(bean.getImage(), helper.getView(R.id.ivImageOne));
        }
    }
}
