package cn.tklvyou.mediaconvergence.ui.mine.collection

import android.os.Bundle
import android.view.View
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.activity.BaseHttpRecyclerActivity
import cn.tklvyou.mediaconvergence.base.interfaces.AdapterCallBack
import cn.tklvyou.mediaconvergence.model.BasePageModel
import cn.tklvyou.mediaconvergence.model.NewsBean
import cn.tklvyou.mediaconvergence.ui.adapter.MyCollectionAdapter
import cn.tklvyou.mediaconvergence.ui.home.news_detail.NewsDetailActivity
import cn.tklvyou.mediaconvergence.ui.home.tv_news_detail.TVNewsDetailActivity
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
            }
        })
    }

    override fun setCollectList(page: Int, pageModel: BasePageModel<NewsBean>?) {
        if (pageModel != null) {
            onLoadSucceed(page, pageModel.data)
        } else {
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

    override fun initView(savedInstanceState: Bundle?) {
        setTitle("我的收藏")
        setNavigationImage()
        setNavigationOnClickListener { finish() }
        initSmartRefreshLayout(smartLayoutRoot)
        initRecyclerView(recyclerViewRoot)
        mPresenter.getCollectPageList(1)
    }


    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onItemClick(adapter, view, position)
        val bean = (adapter as MyCollectionAdapter).data[position]
        val id = bean.id

        when(bean.module){
            "V视频" ->{
                val type = "视频"
                NewsDetailActivity.startNewsDetailActivity(this, type, id)
            }
            "濉溪TV" ->{
                if(bean.module_second =="置顶频道"){
                    val type = if (bean.type == "tv") "电视" else "广播"
                    TVNewsDetailActivity.startTVNewsDetailActivity(this, type, id)
                }else{
                    val type = "电视"
                    NewsDetailActivity.startNewsDetailActivity(this, type, id)
                }
            }
            "新闻","矩阵","专栏","党建" ->{
                val type = "文章"
                NewsDetailActivity.startNewsDetailActivity(this, type, id)
            }
            "视讯" ->{
                val type = "视讯"
                NewsDetailActivity.startNewsDetailActivity(this, type, id)
            }
            "问政" ->{
                val type = "问政"
                NewsDetailActivity.startNewsDetailActivity(this, type, id)
            }

            "原创","随手拍" ->{
                val type = if (bean.images != null && bean.images.size > 0) "图文" else "视频"
                NewsDetailActivity.startNewsDetailActivity(this, type, id)
            }
            "悦读" ->{
                val type = "悦读"
                NewsDetailActivity.startNewsDetailActivity(this, type, id)
            }
            "悦听" ->{
                val type = "悦听"
                NewsDetailActivity.startNewsDetailActivity(this, type, id)
            }
            "公告" ->{
                val type = "公告"
                NewsDetailActivity.startNewsDetailActivity(this, type, id)
            }

        }


    }
}