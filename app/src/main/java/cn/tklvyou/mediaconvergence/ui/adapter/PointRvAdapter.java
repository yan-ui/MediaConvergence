package cn.tklvyou.mediaconvergence.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.tklvyou.mediaconvergence.R;
import cn.tklvyou.mediaconvergence.helper.GlideManager;
import cn.tklvyou.mediaconvergence.model.BasePageModel;
import cn.tklvyou.mediaconvergence.model.MineRvModel;
import cn.tklvyou.mediaconvergence.model.PointModel;
import cn.tklvyou.mediaconvergence.utils.YResourceUtils;

/**
 * 积分商城
 * Created by yiwei on 16/5/17.
 */
public class PointRvAdapter extends BaseQuickAdapter<PointModel, BaseViewHolder> {

    public PointRvAdapter(int layoutResId, @Nullable List<PointModel> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, PointModel item) {
        GlideManager.loadRoundImg(item.getImage(),helper.getView(R.id.ivGoods),10);
        helper.setText(R.id.tvTitle,item.getName());
        helper.setText(R.id.tvPoint,item.getScore()+"积分");
    }
}