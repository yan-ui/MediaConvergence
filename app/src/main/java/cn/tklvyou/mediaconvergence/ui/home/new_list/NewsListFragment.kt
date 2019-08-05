package cn.tklvyou.mediaconvergence.ui.home.new_list

import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.fragment.BaseHttpRecyclerFragment
import cn.tklvyou.mediaconvergence.base.interfaces.AdapterCallBack
import cn.tklvyou.mediaconvergence.model.*
import cn.tklvyou.mediaconvergence.ui.adapter.JuZhenHeaderRvAdapter
import cn.tklvyou.mediaconvergence.ui.adapter.NewsMultipleItemQuickAdapter
import cn.tklvyou.mediaconvergence.ui.adapter.SuixiHeaderRvAdapter
import cn.tklvyou.mediaconvergence.ui.home.AudioController
import cn.tklvyou.mediaconvergence.ui.home.BannerDetailsActivity
import cn.tklvyou.mediaconvergence.ui.home.all_juzheng.AllJuZhengActivity
import cn.tklvyou.mediaconvergence.ui.home.all_tv.AllTvActivity
import cn.tklvyou.mediaconvergence.ui.home.news_detail.NewsDetailActivity
import cn.tklvyou.mediaconvergence.ui.home.publish_wenzheng.PublishWenzhengActivity
import cn.tklvyou.mediaconvergence.ui.home.tv_news_detail.TVNewsDetailActivity
import cn.tklvyou.mediaconvergence.ui.video_player.LiveActivity
import cn.tklvyou.mediaconvergence.ui.video_player.VodActivity
import cn.tklvyou.mediaconvergence.utils.BannerGlideImageLoader
import cn.tklvyou.mediaconvergence.utils.GridDividerItemDecoration
import cn.tklvyou.mediaconvergence.utils.RecycleViewDivider
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.listener.OnBannerListener
import kotlinx.android.synthetic.main.fragment_news_list.*
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

/**
 * @description: 展示每个频道新闻列表的fragment
 */

class NewsListFragment : BaseHttpRecyclerFragment<NewListPresenter, NewsMultipleItem<Any>, BaseViewHolder, NewsMultipleItemQuickAdapter>(), NewListContract.View {


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
    }

    override fun lazyData() {
        when (type) {
            NewsMultipleItem.VIDEO -> {
                recyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayout.VERTICAL, 20, resources.getColor(R.color.common_bg)))
                mPresenter.getBanner("V视频")
            }

            NewsMultipleItem.TV -> {
                recyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayout.VERTICAL, 20, resources.getColor(R.color.common_bg)))
                mPresenter.getHaveSecondModuleNews(1, "濉溪TV")
            }

            NewsMultipleItem.NEWS -> {
                recyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayout.VERTICAL, 1, resources.getColor(R.color.common_bg)))
                mPresenter.getBanner("新闻")
            }

            NewsMultipleItem.SHI_XUN -> {
                recyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayout.VERTICAL, 1, resources.getColor(R.color.common_bg)))
                mPresenter.getNewList("视讯", null,1)
            }

            NewsMultipleItem.WEN_ZHENG -> {
                floatButton.visibility = View.VISIBLE
                floatButton.setOnClickListener {
                    startActivity(Intent(context,PublishWenzhengActivity::class.java))
                }
                recyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayout.VERTICAL, 1, resources.getColor(R.color.common_bg)))
                mPresenter.getNewList("问政", null,1)
            }

            NewsMultipleItem.JU_ZHENG -> {
                recyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayout.VERTICAL, 20, resources.getColor(R.color.common_bg)))
                mPresenter.getBanner("矩阵")
            }

            NewsMultipleItem.WECHAT_MOMENTS -> {
                mPresenter.getNewList("原创", null,1)
            }

            NewsMultipleItem.READING -> {
                recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                recyclerView.addItemDecoration(GridDividerItemDecoration(30, resources.getColor(R.color.common_bg),true))
                mPresenter.getNewList("悦读", null,1)
            }

            NewsMultipleItem.LISTEN -> {
                recyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayout.VERTICAL, 30, resources.getColor(R.color.common_bg),true))
                mPresenter.getNewList("悦听", null,1)
            }

            NewsMultipleItem.DANG_JIAN -> {
                recyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayout.VERTICAL, 1, resources.getColor(R.color.common_bg)))
                mPresenter.getNewList("党建", null,1)
            }

            NewsMultipleItem.ZHUAN_LAN -> {
                recyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayout.VERTICAL, 1, resources.getColor(R.color.common_bg)))
                mPresenter.getNewList("专栏", null,1)
            }

        }

    }

    private lateinit var bannerModelList: MutableList<BannerModel>

    override fun setBanner(bannerModelList: MutableList<BannerModel>) {
        this.bannerModelList = bannerModelList
        when (type) {
            NewsMultipleItem.VIDEO -> {
                mPresenter.getNewList("V视频", null,1)
            }

            NewsMultipleItem.NEWS -> {
                mPresenter.getNewList("新闻", null,1)
            }

            NewsMultipleItem.JU_ZHENG -> {
                mPresenter.getHaveSecondModuleNews(1, "矩阵")
            }

        }

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
                intent.putExtra("title", bannerModelList[position].name)
                intent.putExtra("content", bannerModelList[position].content)
                startActivity(intent)
            }

        })
        //banner设置方法全部调用完毕时最后调用
        banner.start()
    }

    private var juzhengList :MutableList<String> = ArrayList<String>()
    private var juzhengView: View? = null
    private var audioController: AudioController? = null

    override fun setList(list: MutableList<NewsMultipleItem<Any>>) {
        setList(object : AdapterCallBack<NewsMultipleItemQuickAdapter> {

            override fun createAdapter(): NewsMultipleItemQuickAdapter {
                val adapter = NewsMultipleItemQuickAdapter(context, list)
                when (type) {
                    NewsMultipleItem.VIDEO -> {
                        val view = View.inflate(context, R.layout.item_normal_banner, null)
                        initBannerView(view, bannerModelList)
                        adapter.addHeaderView(view)
                    }

                    NewsMultipleItem.NEWS -> {
                        val view = View.inflate(context, R.layout.item_search_and_banner_header, null)
                        initBannerView(view, bannerModelList)
                        //todo:添加SearchView
//                        initSearchView(view,bannerModelList)
                        adapter.addHeaderView(view)
                    }

                    NewsMultipleItem.JU_ZHENG -> {
                        val view = View.inflate(context, R.layout.item_normal_banner, null)
                        initBannerView(view, bannerModelList)
                        adapter.addHeaderView(view)
                    }

                    NewsMultipleItem.LISTEN -> {
                        audioController = AudioController(context)
                        adapter.setAudioController(audioController)
                    }

                }
                return adapter
            }

            override fun refreshAdapter() {
                when (type) {
                    NewsMultipleItem.VIDEO -> {
                        adapter.setNewData(list)
                    }

                    NewsMultipleItem.TV -> {
                        adapter.removeAllHeaderView()

                        val headerModelList = list.filter { (it.dataBean as HaveSecondModuleNewsModel).module_second == "置顶频道" }
                                .toList()

                        val contentList = list.filter { (it.dataBean as HaveSecondModuleNewsModel).module_second != "置顶频道" }
                                .toList()

                        val view = View.inflate(context, R.layout.item_suixi_tv_header, null)
                        val suixiHeaderRecyclerView = view.findViewById<RecyclerView>(R.id.suixiHeaderRecyclerView)
                        suixiHeaderRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
                        suixiHeaderRecyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayout.HORIZONTAL, 20, Color.WHITE))
                        val suixiHeaderRvAdapter = SuixiHeaderRvAdapter(R.layout.item_suixi_tv_header_child, (headerModelList[0].dataBean as HaveSecondModuleNewsModel).data)
                        suixiHeaderRecyclerView.adapter = suixiHeaderRvAdapter
                        suixiHeaderRvAdapter.setOnItemClickListener { adapter, view, position ->
                            val bean = (adapter as SuixiHeaderRvAdapter).data[position]
                            val id = bean.id
                            val type = if(bean.type == "tv") "电视" else "广播"
                            TVNewsDetailActivity.startTVNewsDetailActivity(context!!, type, id)
                        }
                        adapter.addHeaderView(view)

                        adapter.setNewData(contentList)
                        adapter.loadMoreEnd()
                    }

                    NewsMultipleItem.NEWS -> {
                        adapter.setNewData(list)
                    }

                    NewsMultipleItem.SHI_XUN -> {
                        adapter.setNewData(list)
                    }

                    NewsMultipleItem.WEN_ZHENG -> {
                        adapter.setNewData(list)
                    }

                    NewsMultipleItem.JU_ZHENG -> {
                        if (juzhengView != null) {
                            adapter.removeHeaderView(juzhengView)
                        }

                        val headerModelList = list.filter { (it.dataBean as HaveSecondModuleNewsModel).module_second == "矩阵列表" }
                                .toList()

                        juzhengList = (headerModelList[0].dataBean as HaveSecondModuleNewsModel).data.map { it.nickname }.toMutableList()

                        val contentList = list.filter { (it.dataBean as HaveSecondModuleNewsModel).module_second != "矩阵列表" }
                                .toList()

                        juzhengView = View.inflate(context, R.layout.item_suixi_tv_header, null)
                        val juzhengHeaderRecyclerView = juzhengView!!.findViewById<RecyclerView>(R.id.suixiHeaderRecyclerView)
                        juzhengHeaderRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
                        juzhengHeaderRecyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayout.HORIZONTAL, 20, Color.WHITE))
                        val juzhengHeaderRvAdapter = JuZhenHeaderRvAdapter(R.layout.item_ju_zheng_header_child, (headerModelList[0].dataBean as HaveSecondModuleNewsModel).data)
                        juzhengHeaderRecyclerView.adapter = juzhengHeaderRvAdapter
                        juzhengHeaderRvAdapter.setOnItemClickListener { adapter, view, position ->
                            val intent = Intent(context!!,AllJuZhengActivity::class.java)
                            intent.putExtra("position",position)
                            intent.putExtra("list",juzhengList as Serializable)
                            startActivity(intent)
                        }

                        adapter.addHeaderView(juzhengView)

                        adapter.setNewData(contentList)
                        adapter.loadMoreEnd()
                    }


                    NewsMultipleItem.WECHAT_MOMENTS -> {
                        adapter.setNewData(list)
                    }

                    NewsMultipleItem.READING -> {
                        adapter.setNewData(list)
                    }

                    NewsMultipleItem.LISTEN -> {
                        adapter.setNewData(list)
                    }

                    NewsMultipleItem.DANG_JIAN -> {
                        adapter.setNewData(list)
                    }

                    NewsMultipleItem.ZHUAN_LAN -> {
                        adapter.setNewData(list)
                    }
                }

            }
        })

    }


    /**
     * 分页加载 自动调用的方法
     */
    override fun getListAsync(page: Int) {
        when (type) {
            NewsMultipleItem.VIDEO -> {
                mPresenter.getNewList("V视频",null, page)
            }
            NewsMultipleItem.TV -> {
                mPresenter.getHaveSecondModuleNews(page, "濉溪TV")
            }

            NewsMultipleItem.NEWS -> {
                mPresenter.getNewList("新闻",null, page)
            }

            NewsMultipleItem.SHI_XUN -> {
                mPresenter.getNewList("视讯",null, page)
            }

            NewsMultipleItem.WEN_ZHENG -> {
                mPresenter.getNewList("问政",null, page)
            }

            NewsMultipleItem.JU_ZHENG -> {
                mPresenter.getHaveSecondModuleNews(page, "矩阵")
            }

            NewsMultipleItem.WECHAT_MOMENTS -> {
                mPresenter.getNewList("原创",null, page)
            }

            NewsMultipleItem.READING -> {
                mPresenter.getNewList("悦读",null, page)
            }

            NewsMultipleItem.LISTEN -> {
                mPresenter.getNewList("悦听",null, page)
            }

            NewsMultipleItem.DANG_JIAN -> {
                mPresenter.getNewList("党建",null, page)
            }

            NewsMultipleItem.ZHUAN_LAN -> {
                mPresenter.getNewList("专栏", null,page)
            }

        }
    }

    override fun setNewList(p: Int, model: BasePageModel<NewsBean>?) {
        if (model != null) {
            val newList = ArrayList<NewsMultipleItem<Any>>()
            var modelStr = ""
            when (type) {
                NewsMultipleItem.VIDEO -> {
                    modelStr = "V视频"
                }

                NewsMultipleItem.NEWS -> {
                    modelStr = "新闻"
                }

                NewsMultipleItem.SHI_XUN -> {
                    modelStr = "视讯"
                }

                NewsMultipleItem.WEN_ZHENG -> {
                    modelStr = "问政"
                }

                NewsMultipleItem.JU_ZHENG -> {
                    modelStr = "矩阵"
                }

                NewsMultipleItem.WECHAT_MOMENTS -> {
                    modelStr = "原创"
                }

                NewsMultipleItem.READING -> {
                    modelStr = "悦读"
                }

                NewsMultipleItem.LISTEN -> {
                    modelStr = "悦听"
                }

                NewsMultipleItem.DANG_JIAN -> {
                    modelStr = "党建"
                }

                NewsMultipleItem.ZHUAN_LAN -> {
                    modelStr = "专栏"
                }
            }

            model.data.forEach {
                newList.add(NewsMultipleItem(modelStr, it))
            }

            onLoadSucceed(p, newList)
        } else {
            onLoadFailed(p, null)
        }
    }

    override fun setHaveSecondModuleNews(p: Int, datas: MutableList<HaveSecondModuleNewsModel>?) {
        if (datas != null) {
            val newList = ArrayList<NewsMultipleItem<Any>>()
            var modelStr = ""
            when (type) {
                NewsMultipleItem.TV -> {
                    modelStr = "濉溪TV"
                }

                NewsMultipleItem.JU_ZHENG -> {
                    modelStr = "矩阵"
                }
            }
            datas.forEach {
                newList.add(NewsMultipleItem(modelStr, it))
            }
            onLoadSucceed(p, newList)
        } else {
            onLoadFailed(p, null)
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onItemClick(adapter, view, position)
        when (type) {
            NewsMultipleItem.VIDEO -> {
                val bean = (adapter as NewsMultipleItemQuickAdapter).data[position].dataBean as NewsBean
                val id = bean.id
                val type = "视频"
                NewsDetailActivity.startNewsDetailActivity(context!!, type, id)
            }

            NewsMultipleItem.NEWS -> {
                val bean = (adapter as NewsMultipleItemQuickAdapter).data[position].dataBean as NewsBean
                val id = bean.id
                val type = "文章"
                NewsDetailActivity.startNewsDetailActivity(context!!, type, id)
            }

            NewsMultipleItem.WECHAT_MOMENTS -> {
                val bean = (adapter as NewsMultipleItemQuickAdapter).data[position].dataBean as NewsBean
                val id = bean.id
                val type = if (bean.images != null && bean.images.size > 0) "图片" else "视频"
                NewsDetailActivity.startNewsDetailActivity(context!!, type, id)
            }

            NewsMultipleItem.SHI_XUN -> {
                val bean = (adapter as NewsMultipleItemQuickAdapter).data[position].dataBean as NewsBean
                val id = bean.id
                val type = "视讯"
                NewsDetailActivity.startNewsDetailActivity(context!!, type, id)
            }

            NewsMultipleItem.WEN_ZHENG -> {
                val bean = (adapter as NewsMultipleItemQuickAdapter).data[position].dataBean as NewsBean
                val id = bean.id
                val type = "问政"
                NewsDetailActivity.startNewsDetailActivity(context!!, type, id)
            }

            NewsMultipleItem.READING -> {
                val bean = (adapter as NewsMultipleItemQuickAdapter).data[position].dataBean as NewsBean
                val id = bean.id
                val type = "悦读"
                NewsDetailActivity.startNewsDetailActivity(context!!, type, id)
            }

            NewsMultipleItem.LISTEN -> {
                val bean = (adapter as NewsMultipleItemQuickAdapter).data[position].dataBean as NewsBean
                val id = bean.id
                val type = "悦听"
                NewsDetailActivity.startNewsDetailActivity(context!!, type, id)
            }

            NewsMultipleItem.DANG_JIAN -> {
                val bean = (adapter as NewsMultipleItemQuickAdapter).data[position].dataBean as NewsBean
                val id = bean.id
                val type = "党建"
                NewsDetailActivity.startNewsDetailActivity(context!!, type, id)
            }

        }


    }


    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onItemChildClick(adapter, view, position)
        if (view != null) {
            when (view.id) {

                //V视频 播放按钮
                R.id.ivStartPlayer -> {
                    //打开新的Activity
                    val intent = Intent(context, VodActivity::class.java)
                    intent.putExtra("videoPath", ((adapter as NewsMultipleItemQuickAdapter).data[position].dataBean as NewsBean).video)
                    startActivity(intent)
                }

                //濉溪TV 第一个 播放按钮
                R.id.ivSuiXiTVFirstStartPlayer -> {
                    val intent = Intent(context, LiveActivity::class.java)
                    intent.putExtra("videoPath", ((adapter as NewsMultipleItemQuickAdapter).data[position].dataBean as HaveSecondModuleNewsModel).data[0].video)
                    startActivity(intent)
                }

                //濉溪TV 第二个 播放按钮
                R.id.ivSuiXiTVSecondStartPlayer -> {
                    val intent = Intent(context, LiveActivity::class.java)
                    intent.putExtra("videoPath", ((adapter as NewsMultipleItemQuickAdapter).data[position].dataBean as HaveSecondModuleNewsModel).data[1].video)
                    startActivity(intent)
                }

                //濉溪TV 第一个视频布局
                R.id.llSuixiTvFirst -> {
                    val bean = ((adapter as NewsMultipleItemQuickAdapter).data[position].dataBean as HaveSecondModuleNewsModel).data[0]
                    val id = bean.id
                    val type = "视讯"
                    NewsDetailActivity.startNewsDetailActivity(context!!, type, id)
                }

                //濉溪TV 第二个视频布局
                R.id.llSuixiTvSecond -> {
                    val bean = ((adapter as NewsMultipleItemQuickAdapter).data[position].dataBean as HaveSecondModuleNewsModel).data[1]
                    val id = bean.id
                    val type = "视讯"
                    NewsDetailActivity.startNewsDetailActivity(context!!, type, id)
                }

                //濉溪Tv 全部
                R.id.tvSuixiTvAll ->{
                    val intent = Intent(context!!,AllTvActivity::class.java)
                    intent.putExtra("position",position)
                    startActivity(intent)
                }

                //矩阵 全部
                R.id.tvJuzhengModuleSecond ->{
                    val intent = Intent(context!!,AllJuZhengActivity::class.java)
                    intent.putExtra("position",position)
                    intent.putExtra("list",juzhengList as Serializable)
                    startActivity(intent)
                }

                else -> {
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audioController?.release()
    }

}