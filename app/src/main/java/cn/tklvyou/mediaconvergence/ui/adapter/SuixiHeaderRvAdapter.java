package cn.tklvyou.mediaconvergence.ui.adapter;

import android.graphics.Color;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.tklvyou.mediaconvergence.R;
import cn.tklvyou.mediaconvergence.model.ServiceModel;
import cn.tklvyou.mediaconvergence.model.SuixiTvModel;
import cn.tklvyou.mediaconvergence.utils.GridDividerItemDecoration;
import cn.tklvyou.mediaconvergence.utils.YResourceUtils;

/**
 * 首页 濉溪TV 头部适配器
 * Created by yiwei on 16/5/17.
 */
public class SuixiHeaderRvAdapter extends BaseQuickAdapter<SuixiTvModel.DataBean, BaseViewHolder> {

    public SuixiHeaderRvAdapter(int layoutResId, @Nullable List<SuixiTvModel.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SuixiTvModel.DataBean item) {

        helper.setText(R.id.tvSuixiHeaderName,item.getName());
        Glide.with(mContext).load(item.getImage()).into((ImageView) helper.getView(R.id.ivSuixiHeaderImage));
    }


}