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
import cn.tklvyou.mediaconvergence.model.ServiceModel;
import cn.tklvyou.mediaconvergence.utils.GridDividerItemDecoration;
import cn.tklvyou.mediaconvergence.utils.YResourceUtils;

/**
 * Created by yiwei on 16/5/17.
 */
public class ServiceRvAdapter extends BaseQuickAdapter<ServiceModel, BaseViewHolder> {

    public ServiceRvAdapter(int layoutResId, @Nullable List<ServiceModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ServiceModel item) {
        helper.getView(R.id.ivHeader).setBackgroundResource(YResourceUtils.getMipmapResId(item.getName()));

        RecyclerView childRecyclerView = helper.getView(R.id.serviceChildRecyclerView);
        if(childRecyclerView.getAdapter() == null) {
            RecyclerView.LayoutManager manager = new GridLayoutManager(mContext, 2);
            manager.setAutoMeasureEnabled(true);
            childRecyclerView.setLayoutManager(manager);
            childRecyclerView.addItemDecoration(new GridDividerItemDecoration(30, Color.WHITE));
            childRecyclerView.setAdapter(new ServiceChildGridRvAdpater(R.layout.item_main_service_child_gird_view,item.getData()));
        }


    }

    private class ServiceChildGridRvAdpater extends BaseQuickAdapter<ServiceModel.DataBean,BaseViewHolder> {

        public ServiceChildGridRvAdpater(int layoutResId, @Nullable List<ServiceModel.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, ServiceModel.DataBean item) {
            ((ImageView)helper.getView(R.id.ivIcon)).setImageResource(YResourceUtils.getMipmapResId(item.getImagename()));
            helper.setText(R.id.tvChildTitle,item.getTitle());
            helper.setText(R.id.tvChildSubTitle,item.getSubTitle());
        }

    }

}