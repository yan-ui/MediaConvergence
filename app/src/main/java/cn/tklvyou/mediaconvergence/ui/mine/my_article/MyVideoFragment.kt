package cn.tklvyou.mediaconvergence.ui.mine.my_article

import android.app.Activity
import android.content.Context
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
import cn.tklvyou.mediaconvergence.widget.dailog.CommonDialog
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
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

    override fun getLoadingView(): View {
        return cameraRecyclerView
    }

    override fun initView() {
        cameraTitleBar.visibility = View.GONE
        initSmartRefreshLayout(cameraSmartRefreshLayout)
        initRecyclerView(cameraRecyclerView)

        cameraRecyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayout.VERTICAL, 20, resources.getColor(R.color.common_bg)))

        mPresenter.getNewList(moduleName, 1)
    }

    override fun onRetry() {
        super.onRetry()
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
                return MyVideoAdapter(list)
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
        startNewsDetailActivity(context!!, type, id, position)

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

                R.id.deleteBtn ->{
                    val dialog = CommonDialog(context)
                    dialog.setTitle("温馨提示")
                    dialog.setMessage("是否删除？")
                    dialog.setYesOnclickListener("确认") {
                        val bean = (adapter as MyVideoAdapter).data[position]
                        mPresenter.deleteArticles(bean.id, position)
                        dialog.dismiss()
                    }
                    dialog.show()
                }

            }
        }
    }

    override fun deleteSuccess(position: Int) {
        adapter.remove(position)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val position = data.getIntExtra("position", 0)
            val seeNum = data.getIntExtra("seeNum", 0)
            val zanNum = data.getIntExtra("zanNum", 0)
            val commenNum = data.getIntExtra("commentNum", 0)
            val like_status = data.getIntExtra("like_status", 0)

            val bean = (adapter as MyVideoAdapter).data[position]
            bean.comment_num = commenNum
            bean.like_num = zanNum
            bean.visit_num = seeNum
            bean.like_status = like_status
            adapter.notifyItemChanged(position)

        }
    }

    private fun startNewsDetailActivity(context: Context, type: String, id: Int, position: Int) {
        val intent = Intent(context, NewsDetailActivity::class.java)
        intent.putExtra(NewsDetailActivity.INTENT_ID, id)
        intent.putExtra(NewsDetailActivity.INTENT_TYPE, type)
        intent.putExtra(NewsDetailActivity.POSITION, position)
        startActivityForResult(intent, 0)
    }


}