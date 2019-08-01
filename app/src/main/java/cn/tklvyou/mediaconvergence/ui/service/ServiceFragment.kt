package cn.tklvyou.mediaconvergence.ui.service

import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.NullPresenter
import cn.tklvyou.mediaconvergence.base.fragment.BaseRecyclerFragment
import cn.tklvyou.mediaconvergence.base.interfaces.AdapterCallBack
import cn.tklvyou.mediaconvergence.model.ServiceModel
import cn.tklvyou.mediaconvergence.ui.adapter.ServiceRvAdapter
import cn.tklvyou.mediaconvergence.utils.JSON
import cn.tklvyou.mediaconvergence.utils.RecycleViewDivider
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_service.*

class ServiceFragment : BaseRecyclerFragment<NullPresenter, ServiceModel, BaseViewHolder, ServiceRvAdapter>() {

    override fun initPresenter(): NullPresenter {
        return NullPresenter()
    }

    override fun getFragmentLayoutID(): Int {
        return R.layout.fragment_service
    }

    override fun initView() {
        serviceTitleBar.setBackgroundResource(R.drawable.shape_gradient_common_titlebar)

        initRecyclerView(serviceRecyclerView)
        serviceRecyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayout.VERTICAL, 30, Color.WHITE))

        val json = ResourceUtils.readAssets2String("servicelist.json")
        val data = JSON.parseArray(json, ServiceModel::class.java)

        onLoadSucceed(1, data)
    }

    override fun lazyData() {

    }


    override fun getListAsync(page: Int) {

    }


    override fun setList(list: MutableList<ServiceModel>?) {
        setList(object : AdapterCallBack<ServiceRvAdapter> {

            override fun createAdapter(): ServiceRvAdapter {
                val adapter = ServiceRvAdapter(R.layout.item_main_service_view, list)
                val headerView = ImageView(context)
                headerView.setBackgroundResource(R.mipmap.icon_service_point)
                adapter.addHeaderView(headerView)
                headerView.setOnClickListener {
                    startActivity(Intent(context,PointActivity::class.java))
                }
                return adapter
            }

            override fun refreshAdapter() {
                adapter.setNewData(list)
            }
        })
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onItemClick(adapter, view, position)
        ToastUtils.showShort("-  1 -")
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onItemChildClick(adapter, view, position)

        ToastUtils.showShort("---")
    }


}