package cn.tklvyou.mediaconvergence.ui.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.tklvyou.mediaconvergence.R;
import cn.tklvyou.mediaconvergence.model.PointDetailModel;

/**
 * @author :JenkinsZhou
 * @description :积分明细适配器
 * @company :途酷科技
 * @date 2019年08月01日16:50
 * @Email: 971613168@qq.com
 */
public class MyPointAdapter extends BaseQuickAdapter<PointDetailModel, BaseViewHolder> {

    public MyPointAdapter() {
        super(R.layout.item_my_integral_detail);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PointDetailModel item) {
        if (item == null) {
            return;
        }
        helper.setText(R.id.tvCreateTime, item.getCreatetime());
        helper.setText(R.id.tvIntegralDesc, item.getName());
        helper.setText(R.id.tvIntegralChangeAmount, item.getSymbol() + item.getAmount());

    }
}
