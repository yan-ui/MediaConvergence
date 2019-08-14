package cn.tklvyou.mediaconvergence.ui.mine.message

/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2019年08月01日19:06
 * @Email: 971613168@qq.com
 */


import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.activity.BaseHttpRecyclerActivity
import cn.tklvyou.mediaconvergence.base.interfaces.AdapterCallBack
import cn.tklvyou.mediaconvergence.model.BasePageModel
import cn.tklvyou.mediaconvergence.model.MessageModel
import cn.tklvyou.mediaconvergence.ui.adapter.SystemMsgAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.layout_recycler.*
import kotlinx.android.synthetic.main.layout_refresh_recycler.*

/**
 * @author :JenkinsZhou
 * @description :积分明细列表
 * @company :途酷科技
 * @date 2019年08月01日17:47
 * @Email: 971613168@qq.com
 */
class MyMessageActivity : BaseHttpRecyclerActivity<MessagePresenter, MessageModel, BaseViewHolder, SystemMsgAdapter>(), MessageContract.View {
    val EXTRA_KEY_MESSAGE_ID = "EXTRA_KEY_MESSAGE_ID"
    override fun setList(list: MutableList<MessageModel>?) {

        setList(object : AdapterCallBack<SystemMsgAdapter> {

            override fun createAdapter(): SystemMsgAdapter {
                return SystemMsgAdapter()
            }

            override fun refreshAdapter() {
                adapter.setNewData(list)
            }
        })

    }

    override fun setMessageList(page: Int, pageModel: BasePageModel<MessageModel>?) {
        if (pageModel != null) {
            onLoadSucceed(page, pageModel.data)
        } else {
            onLoadFailed(page, null)
        }
    }


    override fun initPresenter(): MessagePresenter {
        return MessagePresenter()
    }

    override fun getActivityLayoutID(): Int {
        return R.layout.layout_refresh_recycler
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitle("我的消息")
        setNavigationImage()
        setNavigationOnClickListener { finish() }
        initSmartRefreshLayout(smartLayoutRoot)
        initRecyclerView(recyclerViewRoot)
        recyclerViewRoot.layoutManager = LinearLayoutManager(this)
        mPresenter.getMsgPageList(1)
    }


    override fun getListAsync(page: Int) {
        mPresenter.getMsgPageList(page)
    }


}