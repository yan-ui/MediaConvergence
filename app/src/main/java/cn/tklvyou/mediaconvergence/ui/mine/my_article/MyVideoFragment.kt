package cn.tklvyou.mediaconvergence.ui.mine.my_article

import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.fragment.BaseHttpRecyclerFragment
import cn.tklvyou.mediaconvergence.base.interfaces.AdapterCallBack
import cn.tklvyou.mediaconvergence.model.BasePageModel
import cn.tklvyou.mediaconvergence.model.NewsBean
import cn.tklvyou.mediaconvergence.ui.adapter.MyVideoAdapter
import cn.tklvyou.mediaconvergence.ui.adapter.WxCircleAdapter
import cn.tklvyou.mediaconvergence.ui.home.news_detail.NewsDetailActivity
import cn.tklvyou.mediaconvergence.ui.video_player.VodActivity
import cn.tklvyou.mediaconvergence.utils.RecycleViewDivider
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_camera.*

/**
 *@description :我的V视
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2019年08月05日14:19
 * @Email: 971613168@qq.com
 */
class MyVideoFragment : BaseHttpRecyclerFragment<MyArticleListPresenter, NewsBean, BaseViewHolder, MyVideoAdapter>(), MyArticleContract.View {

    private val moduleName = "V视频"
    override fun initPresenter(): MyArticleListPresenter {
        return MyArticleListPresenter()
    }

    override fun getFragmentLayoutID(): Int {
        return R.layout.fragment_camera
    }

    override fun initView() {
        cameraTitleBar.visibility = View.GONE
        initSmartRefreshLayout(cameraSmartRefreshLayout)
        initRecyclerView(cameraRecyclerView)

        cameraRecyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayout.VERTICAL, 1, resources.getColor(R.color.common_bg)))

        cameraRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(context!!).resumeRequests()
                } else {
                    Glide.with(context!!).pauseRequests()
                }
            }
        })

        mPresenter.getNewList(moduleName, 1)
    }

    override fun lazyData() {
    }

    override fun getListAsync(page: Int) {
        mPresenter.getNewList(moduleName, page)
    }

    override fun setNewList(p: Int, model: BasePageModel<NewsBean>?) {
        if (model != null) {
            onLoadSucceed(p, model.data)
        } else {
            onLoadFailed(p, null)
        }
    }

    override fun setList(list: MutableList<NewsBean>?) {
        setList(object : AdapterCallBack<MyVideoAdapter> {

            override fun createAdapter(): MyVideoAdapter {
                return MyVideoAdapter()
            }

            override fun refreshAdapter() {
                adapter.setNewData(list)
            }
        })

    }


    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onItemClick(adapter, view, position)

        val bean = (adapter as MyVideoAdapter).data[position]
        val id = bean.id
        val type = if (bean.images != null && bean.images.size > 0) "图文" else "视频"
        NewsDetailActivity.startNewsDetailActivity(context!!, type, id)

    }


    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onItemChildClick(adapter, view, position)
        if (view != null) {
            when (view.id) {

                //V视频 播放按钮
                R.id.ivStartPlayer -> {
                    //打开新的Activity
                    val intent = Intent(context, VodActivity::class.java)
                    intent.putExtra("videoPath", ((adapter as BaseQuickAdapter).data[position] as NewsBean).video)
                    startActivity(intent)
                }


            }
        }
    }
}