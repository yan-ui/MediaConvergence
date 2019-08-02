package cn.tklvyou.mediaconvergence.ui.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.tklvyou.mediaconvergence.R;
import cn.tklvyou.mediaconvergence.helper.GlideManager;
import cn.tklvyou.mediaconvergence.model.ExchangeModel;

/**
 * @author :JenkinsZhou
 * @description :兑换记录适配器
 * @company :途酷科技
 * @date 2019年08月02日10:43
 * @Email: 971613168@qq.com
 */
public class ExchangeRecordAdapter extends BaseQuickAdapter<ExchangeModel, BaseViewHolder> {
    private static final String STATUS_HIDDEN = "hidden";

    public ExchangeRecordAdapter() {
        super(R.layout.item_exchange_record);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ExchangeModel item) {
        if (item == null) {
            return;
        }
        ImageView roundedImageView = helper.getView(R.id.rvGoodsImage);
        GlideManager.loadRoundImg(item.getImage(), roundedImageView, 4);
        helper.setText(R.id.tvGoodsDesc, item.getName());
        helper.setText(R.id.tvDeduction, "消耗：" + item.getScore() + "分");
        boolean buttonEnable = STATUS_HIDDEN.equalsIgnoreCase(item.getStatus());
        helper.setGone(R.id.tvWaitReceive, buttonEnable);
        helper.addOnClickListener(R.id.tvWaitReceive);
    }

}
