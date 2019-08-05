package cn.tklvyou.mediaconvergence.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.tklvyou.mediaconvergence.R;
import cn.tklvyou.mediaconvergence.helper.GlideManager;
import cn.tklvyou.mediaconvergence.model.NewsBean;

public class SuixiTVGridRvAdpater extends BaseQuickAdapter<NewsBean, BaseViewHolder> {

    public SuixiTVGridRvAdpater(int layoutResId, @Nullable List<NewsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NewsBean item) {
        helper.setText(R.id.tvName, item.getName());
        helper.setText(R.id.tvTime, item.getTime());
        GlideManager.loadImg(item.getImage(), helper.getView(R.id.ivImage));
        helper.addOnClickListener(R.id.ivSuiXiTVStartPlayer);

    }

}
