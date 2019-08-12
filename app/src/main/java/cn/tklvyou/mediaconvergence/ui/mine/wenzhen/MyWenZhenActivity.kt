package cn.tklvyou.mediaconvergence.ui.mine.wenzhen

import android.os.Bundle
import android.view.View
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.activity.BaseHttpRecyclerActivity
import cn.tklvyou.mediaconvergence.base.interfaces.AdapterCallBack
import cn.tklvyou.mediaconvergence.model.BasePageModel
import cn.tklvyou.mediaconvergence.model.NewsBean
import cn.tklvyou.mediaconvergence.ui.adapter.WenZhenAdapter
import cn.tklvyou.mediaconvergence.ui.home.news_detail.NewsDetailActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.layout_recycler.*
import kotlinx.android.synthetic.main.layout_refresh_recycler.*

/**
 *@description :问政记录
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2019年08月02日11:04
 * @Email: 971613168@qq.com
 */
class MyWenZhenActivity : BaseHttpRecyclerActivity<WenZhenPresenter, NewsBean, BaseViewHolder, WenZhenAdapter>(), WenZhenContract.View {
    override fun setDataList(page: Int, pageModel: BasePageModel<NewsBean>?) {
        if (pageModel != null) {
            onLoadSucceed(page, pageModel.data)
        } else {
            onLoadFailed(page, null)
        }
    }

    override fun setList(list: MutableList<NewsBean>?) {
        setList(object : AdapterCallBack<WenZhenAdapter> {

            override fun createAdapter(): WenZhenAdapter {
                return WenZhenAdapter()
            }

            override fun refreshAdapter() {
                adapter.setNewData(list)
            }
        })
        if (list != null) {
            if (list.isEmpty()) {
                showNoData()
            }
        }
    }


    override fun initPresenter(): WenZhenPresenter {
        return WenZhenPresenter()
    }


    override fun getActivityLayoutID(): Int {
        return R.layout.layout_refresh_recycler
    }

    override fun getListAsync(page: Int) {
        mPresenter.getDataPageList(page)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitle("问政")
        setNavigationImage()
        setNavigationOnClickListener { finish() }
        initSmartRefreshLayout(smartLayoutRoot)
        initRecyclerView(recyclerViewRoot)
        mPresenter.getDataPageList(1)
    }


    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onItemClick(adapter, view, position)
        val bean = (adapter as WenZhenAdapter).data[position] as NewsBean
        val id = bean.id
        val type = "问政"
        NewsDetailActivity.startNewsDetailActivity(this, type, id)
    }


}