package cn.tklvyou.mediaconvergence.ui.mine.exchange

import androidx.recyclerview.widget.LinearLayoutManager
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.activity.BaseHttpRecyclerActivity
import cn.tklvyou.mediaconvergence.base.interfaces.AdapterCallBack
import cn.tklvyou.mediaconvergence.model.BasePageModel
import cn.tklvyou.mediaconvergence.model.ExchangeModel
import cn.tklvyou.mediaconvergence.ui.adapter.ExchangeRecordAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.layout_recycler.*
import kotlinx.android.synthetic.main.layout_refresh_recycler.*

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2019年08月02日11:04
 * @Email: 971613168@qq.com
 */
class MyExchangeRecordActivity : BaseHttpRecyclerActivity<ExchangePresenter, ExchangeModel, BaseViewHolder, ExchangeRecordAdapter>(), ExchangeRecordContract.View {
    override fun setList(list: MutableList<ExchangeModel>?) {
        setList(object : AdapterCallBack<ExchangeRecordAdapter> {

            override fun createAdapter(): ExchangeRecordAdapter {
                return ExchangeRecordAdapter()
            }

            override fun refreshAdapter() {
                adapter.setNewData(list)
                initItemChildClick()
            }
        })
        if (list != null) {
            if (list.isEmpty()) {
                showNoData()
            }
        }
    }


    override fun setExchangeList(page: Int, pageModel: BasePageModel<ExchangeModel>?) {
        if (pageModel != null) {
            onLoadSucceed(page, pageModel.data)
        } else {
            onLoadFailed(page, null)
        }
    }


    override fun initPresenter(): ExchangePresenter {
        return ExchangePresenter()
    }

    override fun getActivityLayoutID(): Int {
        return R.layout.layout_refresh_recycler
    }

    override fun initView() {
        setTitle("兑换记录")
        setNavigationImage()
        setNavigationOnClickListener { finish() }
        initSmartRefreshLayout(smartLayoutRoot)
        initRecyclerView(recyclerViewRoot)
        recyclerViewRoot.layoutManager = LinearLayoutManager(this)
        mPresenter.getExchangePageList(1)
    }


    override fun getListAsync(page: Int) {
        mPresenter.getExchangePageList(page)
    }


    private fun initItemChildClick() {
        adapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener() { adapter, view, position ->
            when (view.id) {
                 R.id.tvWaitReceive-> {
                     //todo 待领取逻辑
                }
                else -> {
                }
            }
        }
    }
}