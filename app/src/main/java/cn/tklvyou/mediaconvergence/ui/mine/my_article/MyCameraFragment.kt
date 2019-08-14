package cn.tklvyou.mediaconvergence.ui.mine.my_article

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.fragment.BaseHttpRecyclerFragment
import cn.tklvyou.mediaconvergence.base.interfaces.AdapterCallBack
import cn.tklvyou.mediaconvergence.model.BasePageModel
import cn.tklvyou.mediaconvergence.model.NewsBean
import cn.tklvyou.mediaconvergence.ui.adapter.WxCircleAdapter
import cn.tklvyou.mediaconvergence.ui.home.news_detail.NewsDetailActivity
import cn.tklvyou.mediaconvergence.utils.RecycleViewDivider
import cn.tklvyou.mediaconvergence.widget.dailog.CommonDialog
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_camera.*

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2019年08月05日11:49
 * @Email: 971613168@qq.com
 */
class MyCameraFragment : BaseHttpRecyclerFragment<MyArticleListPresenter, NewsBean, BaseViewHolder, WxCircleAdapter>(), MyArticleContract.View {

    private val moduleName = "随手拍"
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
        setList(object : AdapterCallBack<WxCircleAdapter> {

            override fun createAdapter(): WxCircleAdapter {
                val adapter = WxCircleAdapter(R.layout.item_winxin_circle, list)
                adapter.setEnableDelete()
                return adapter
            }

            override fun refreshAdapter() {
                adapter.setNewData(list)
            }
        })

    }



    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onItemClick(adapter, view, position)

        val bean = (adapter as WxCircleAdapter).data[position]
        val id = bean.id
        val type = if (bean.images != null && bean.images.size > 0) "图文" else "视频"
        NewsDetailActivity.startNewsDetailActivity(mActivity!!, type, id)

    }


    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onItemChildClick(adapter, view, position)
        if (view!!.id == R.id.deleteBtn) {
            val dialog = CommonDialog(context)
            dialog.setTitle("温馨提示")
            dialog.setMessage("是否删除？")
            dialog.setYesOnclickListener("确认") {
                val bean = (adapter as WxCircleAdapter).data[position]
                mPresenter.deleteArticle(bean.id, position)
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    override fun deleteSuccess(position: Int) {
        adapter.remove(position)
    }



}