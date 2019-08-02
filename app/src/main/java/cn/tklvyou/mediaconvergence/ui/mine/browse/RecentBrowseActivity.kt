package cn.tklvyou.mediaconvergence.ui.mine.browse
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.activity.BaseHttpRecyclerActivity
import cn.tklvyou.mediaconvergence.base.interfaces.AdapterCallBack
import cn.tklvyou.mediaconvergence.model.BasePageModel
import cn.tklvyou.mediaconvergence.model.NewsBean
import cn.tklvyou.mediaconvergence.ui.adapter.MyCollectionAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.layout_recycler.*
import kotlinx.android.synthetic.main.layout_refresh_recycler.*

/**
 *@description :最近浏览
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2019年08月02日16:51
 * @Email: 971613168@qq.com
 */
class RecentBrowseActivity : BaseHttpRecyclerActivity<BrowsePresenter, NewsBean, BaseViewHolder, MyCollectionAdapter>(), BrowseContract.View {
    override fun setBrowseList(page: Int, pageModel: BasePageModel<NewsBean>?) {
        if (pageModel != null) {
            onLoadSucceed(page, pageModel.data)
        } else {
            onLoadFailed(page, null)
        }
    }

    override fun setList(list: MutableList<NewsBean>?) {
        setList(object : AdapterCallBack<MyCollectionAdapter> {

            override fun createAdapter(): MyCollectionAdapter {
                return MyCollectionAdapter()
            }

            override fun refreshAdapter() {
                adapter.setNewData(list)
                initItemClick()
            }
        })
        if (list != null) {
            if (list.isEmpty()) {
                showNoData()
            }
        }
    }



    override fun initPresenter(): BrowsePresenter {
        return BrowsePresenter()
    }


    override fun getActivityLayoutID(): Int {
        return R.layout.layout_refresh_recycler
    }

    override fun getListAsync(page: Int) {
        mPresenter.getBrowsePageList(page)
    }

    override fun initView() {
        setTitle("最近浏览")
        setNavigationImage()
        setNavigationOnClickListener { finish() }
        initSmartRefreshLayout(smartLayoutRoot)
        initRecyclerView(recyclerViewRoot)
        mPresenter.getBrowsePageList(1)
    }


    private fun initItemClick() {
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
          //todo 点击事件
        }
    }
}