package cn.tklvyou.mediaconvergence.ui.adapter;

import android.graphics.Color;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.tklvyou.mediaconvergence.R;
import cn.tklvyou.mediaconvergence.model.MineRvModel;
import cn.tklvyou.mediaconvergence.model.ServiceModel;
import cn.tklvyou.mediaconvergence.utils.GridDividerItemDecoration;
import cn.tklvyou.mediaconvergence.utils.YResourceUtils;

/**
 * 我的页面
 * Created by yiwei on 16/5/17.
 */
public class MineRvAdapter extends BaseQuickAdapter<MineRvModel, BaseViewHolder> {

    public MineRvAdapter(int layoutResId, @Nullable List<MineRvModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MineRvModel item) {
        ((ImageView)helper.getView(R.id.ivImage)).setImageResource(YResourceUtils.getMipmapResId(item.getLocalImage()));
        helper.setText(R.id.tvName,item.getName());
    }

}