package cn.tklvyou.mediaconvergence.ui.mine.collection

import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.activity.BaseHttpRecyclerActivity
import cn.tklvyou.mediaconvergence.base.interfaces.AdapterCallBack
import cn.tklvyou.mediaconvergence.model.BasePageModel
import cn.tklvyou.mediaconvergence.model.NewsBean
import cn.tklvyou.mediaconvergence.ui.adapter.MyCollectionAdapter
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.layout_recycler.*
import kotlinx.android.synthetic.main.layout_refresh_recycler.*

/**
 *@description :我的收藏
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2019年08月02日11:04
 * @Email: 971613168@qq.com
 */
class MyCollectActivity : BaseHttpRecyclerActivity<CollectPresenter, NewsBean, BaseViewHolder, MyCollectionAdapter>(), CollectContract.View {
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

    override fun setCollectList(page: Int, pageModel: BasePageModel<NewsBean>?) {
        if (pageModel != null) {
            onLoadSucceed(page, pageModel.data)
            LogUtils.i("执行了")
        } else {
            LogUtils.e("执行了")
            onLoadFailed(page, null)
        }
    }

    override fun initPresenter(): CollectPresenter {
        return CollectPresenter()
    }


    override fun getActivityLayoutID(): Int {
        return R.layout.layout_refresh_recycler
    }

    override fun getListAsync(page: Int) {
        mPresenter.getCollectPageList(page)
    }

    override fun initView() {
        setTitle("我的收藏")
        setNavigationImage()
        setNavigationOnClickListener { finish() }
        initSmartRefreshLayout(smartLayoutRoot)
        initRecyclerView(recyclerViewRoot)
        mPresenter.getCollectPageList(1)
    }


    private fun initItemClick() {
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
          //todo 点击事件
        }
    }
}