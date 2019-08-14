package cn.tklvyou.mediaconvergence.ui.home.comment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.NullPresenter
import cn.tklvyou.mediaconvergence.base.activity.BaseHttpRecyclerActivity
import cn.tklvyou.mediaconvergence.base.interfaces.AdapterCallBack
import cn.tklvyou.mediaconvergence.model.BasePageModel
import cn.tklvyou.mediaconvergence.model.CommentModel
import cn.tklvyou.mediaconvergence.ui.adapter.CommentRvAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_comment_list.*

class CommentListActivity : BaseHttpRecyclerActivity<CommentPresenter, CommentModel, BaseViewHolder, CommentRvAdapter>(),CommentContract.View {

    override fun initPresenter(): CommentPresenter {
        return CommentPresenter()
    }

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_comment_list
    }

    private var article_id = 0
    override fun initView(savedInstanceState: Bundle?) {
        setTitle("全部评论")
        setNavigationImage()
        setNavigationOnClickListener {
            finish()
        }

        article_id = intent.getIntExtra("id",0)
        initSmartRefreshLayout(smartRefreshLayout)
        initRecyclerView(recyclerView)

        mPresenter.getCommentList(article_id,1)
    }

    override fun setList(list: MutableList<CommentModel>?) {
        setList(object : AdapterCallBack<CommentRvAdapter> {

            override fun createAdapter(): CommentRvAdapter {
                return CommentRvAdapter(R.layout.item_news_comment_view,list)
            }

            override fun refreshAdapter() {
                adapter.setNewData(list)
            }
        })
    }

    override fun getListAsync(page: Int) {
        mPresenter.getCommentList(article_id,page)
    }

    override fun setCommentList(p: Int, model: BasePageModel<CommentModel>?) {
        if (model != null) {
            onLoadSucceed(p, model.data)
        } else {
            onLoadFailed(p, null)
        }
    }


}
