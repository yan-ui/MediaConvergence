package cn.tklvyou.mediaconvergence.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.tklvyou.mediaconvergence.R;
import cn.tklvyou.mediaconvergence.model.HaveSecondModuleNewsModel;

/**
 * 首页 濉溪TV 头部适配器
 * Created by yiwei on 16/5/17.
 */
public class SuixiHeaderRvAdapter extends BaseQuickAdapter<HaveSecondModuleNewsModel.DataBean, BaseViewHolder> {

    public SuixiHeaderRvAdapter(int layoutResId, @Nullable List<HaveSecondModuleNewsModel.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HaveSecondModuleNewsModel.DataBean item) {

        helper.setText(R.id.tvSuixiHeaderName,item.getName());
        Glide.with(mContext).load(item.getImage()).into((ImageView) helper.getView(R.id.ivSuixiHeaderImage));
    }


}