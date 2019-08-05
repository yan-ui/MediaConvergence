package cn.tklvyou.mediaconvergence.ui.adapter;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.tklvyou.mediaconvergence.R;
import cn.tklvyou.mediaconvergence.model.NewsBean;

/**
 * @author :JenkinsZhou
 * @description :我都V视适配器
 * @company :途酷科技
 * @date 2019年08月05日14:33
 * @Email: 971613168@qq.com
 */
public class MyVideoAdapter extends BaseQuickAdapter<NewsBean, BaseViewHolder> {
    public MyVideoAdapter() {
        super(R.layout.item_news_video);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NewsBean bean) {
        helper.setText(R.id.tvNewsTitle, bean.getName());
        helper.setText(R.id.tvVideoTime, bean.getTime());
        helper.setText(R.id.tvCommentNum, "" + bean.getComment_num());
        helper.setText(R.id.tvGoodNum, "" + bean.getLike_num());
        helper.setText(R.id.tvName, bean.getNickname());

        TextView tvGoodNum = helper.getView(R.id.tvGoodNum);

        Drawable[] drawables = tvGoodNum.getCompoundDrawables();

        if (bean.getLike_status() == 1) {
            Drawable redGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_red_good);
            redGoodDrawable.setBounds(drawables[0].getBounds());
            tvGoodNum.setCompoundDrawables(redGoodDrawable, drawables[1], drawables[2], drawables[3]);
        } else {
            Drawable grayGoodDrawable = mContext.getResources().getDrawable(R.mipmap.icon_good);
            grayGoodDrawable.setBounds(drawables[0].getBounds());
            tvGoodNum.setCompoundDrawables(grayGoodDrawable, drawables[1], drawables[2], drawables[3]);
        }

        // 加载网络图片
        if (!StringUtils.isEmpty(bean.getAvatar().trim())) {
            Glide.with(mContext).load(bean.getAvatar()).into((ImageView) helper.getView(R.id.ivAvatar));
        }

        Glide.with(mContext).load(bean.getImage()).into((ImageView) helper.getView(R.id.ivVideoBg));

        helper.addOnClickListener(R.id.ivStartPlayer);
    }
}
