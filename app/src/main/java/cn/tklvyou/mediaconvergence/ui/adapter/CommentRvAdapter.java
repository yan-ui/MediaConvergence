package cn.tklvyou.mediaconvergence.ui.adapter;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
import java.util.Locale;

import cn.tklvyou.mediaconvergence.R;
import cn.tklvyou.mediaconvergence.helper.GlideManager;
import cn.tklvyou.mediaconvergence.model.CommentModel;
import cn.tklvyou.mediaconvergence.model.NewsBean;

public class CommentRvAdapter extends BaseQuickAdapter<CommentModel, BaseViewHolder> {

    public CommentRvAdapter(int layoutResId, @Nullable List<CommentModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CommentModel bean) {

        if (!StringUtils.isEmpty(bean.getAvatar().trim())) {
            GlideManager.loadRoundImg(bean.getAvatar(), helper.getView(R.id.ivAvatar), 5f, R.mipmap.default_avatar, true);
        }

        if (bean.getAdmin_status() == 1) {
            helper.setVisible(R.id.tvTag,true);
        } else {
            helper.setVisible(R.id.tvTag,false);
        }

        helper.setText(R.id.tvNickName,bean.getNickname());
        helper.setText(R.id.tvTime,bean.getCreatetime());
        helper.setText(R.id.tvContent,bean.getDetail());
        helper.setVisible(R.id.tvTag,bean.getAdmin_status() == 1);

    }

}
