package cn.tklvyou.mediaconvergence.ui.home

import android.content.Intent
import android.view.View
import android.widget.LinearLayout


import java.util.ArrayList

import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.model.NewsMultipleItem
import cn.tklvyou.mediaconvergence.base.fragment.BaseHttpRecyclerFragment
import cn.tklvyou.mediaconvergence.base.interfaces.AdapterCallBack
import cn.tklvyou.mediaconvergence.model.BannerModel
import cn.tklvyou.mediaconvergence.model.BasePageModel
import cn.tklvyou.mediaconvergence.model.NewsBean
import cn.tklvyou.mediaconvergence.ui.adapter.NewsMultipleItemQuickAdapter
import cn.tklvyou.mediaconvergence.ui.video_player.VodActivity
import cn.tklvyou.mediaconvergence.utils.BannerGlideImageLoader
import cn.tklvyou.mediaconvergence.utils.RecycleViewDivider
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.listener.OnBannerListener
import kotlinx.android.synthetic.main.fragment_news_list.*

/**
 * @author ChayChan
 * @description: 展示每个频道新闻列表的fragment
 * @date 2017/6/16  21:22
 */

class NewsListFragment : BaseHttpRecyclerFragment<NewListPresenter, NewsMultipleItem, BaseViewHolder, NewsMultipleItemQuickAdapter>(), NewListContract.View {

    override fun initPresenter(): NewListPresenter {
        return NewListPresenter()
    }

    override fun getFragmentLayoutID(): Int {
        return R.layout.fragment_news_list
    }

    private var type = -1
    override fun initView() {
        initSmartRefreshLayout(refreshLayout)
        initRecyclerView(recyclerView)
        type = mBundle.getInt("type", -1)

        recyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayout.VERTICAL, 20, resources.getColor(R.color.common_bg)))

    }

    override fun lazyData() {
        mPresenter.getBanner("V视频")

    }

    private lateinit var bannerModelList: MutableList<BannerModel>

    override fun setBanner(bannerModelList: MutableList<BannerModel>) {
        this.bannerModelList = bannerModelList
        mPresenter.getNewList("V视频", 1)
    }

    private fun initBannerView(view: View, bannerModelList: MutableList<BannerModel>) {

        val images = ArrayList<String>()
        val titles = ArrayList<String>()

        bannerModelList.forEach {
            titles.add(it.name)
            images.add(it.image)
        }

        val banner = view.findViewById<Banner>(R.id.banner)
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
        //设置图片加载器
        banner.setImageLoader(BannerGlideImageLoader())
        //设置图片集合
        banner.setImages(images)
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage)
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titles)
        //设置自动轮播，默认为true
        banner.isAutoPlay(true)
        //设置轮播时间
        banner.setDelayTime(3000)
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.RIGHT)
        banner.setOnBannerListener(object : OnBannerListener {
            override fun OnBannerClick(position: Int) {
                val intent = Intent(context, BannerDetailsActivity::class.java)
                intent.putExtra("title",bannerModelList[position].name )
                intent.putExtra("content",bannerModelList[position].content )
                startActivity(intent)
            }

        })
        //banner设置方法全部调用完毕时最后调用
        banner.start()
    }

    override fun setList(list: MutableList<NewsMultipleItem>) {
        setList(object : AdapterCallBack<NewsMultipleItemQuickAdapter> {

            override fun createAdapter(): NewsMultipleItemQuickAdapter {
                val view = View.inflate(context, R.layout.item_normal_banner, null)
                val adapter = NewsMultipleItemQuickAdapter(context, list)
                initBannerView(view, bannerModelList)
                adapter.addHeaderView(view)
                return adapter
            }

            override fun refreshAdapter() {
                adapter.setNewData(list)
            }
        })

    }


    override fun getListAsync(page: Int) {
        mPresenter.getNewList("V视频", page)
    }

    override fun setNewList(p: Int, model: BasePageModel<NewsBean>?) {
        if (model != null) {
            val newList = ArrayList<NewsMultipleItem>()
            model.data.forEach {
                newList.add(NewsMultipleItem(it))
            }
            onLoadSucceed(p, newList)
        }else{
            onLoadFailed(p,null)
        }
    }


    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onItemChildClick(adapter, view, position)
        if (view != null) {
            when (view.id) {
                R.id.videoLayout -> {
                    //打开新的Activity
                    val intent = Intent(context, VodActivity::class.java)
//                    intent.putExtra("media_type", "livestream")
                    intent.putExtra("videoPath", (adapter as NewsMultipleItemQuickAdapter).data[position].dataBean.video)
                    startActivity(intent)
                }
                else -> {
                }
            }
        }
    }

}