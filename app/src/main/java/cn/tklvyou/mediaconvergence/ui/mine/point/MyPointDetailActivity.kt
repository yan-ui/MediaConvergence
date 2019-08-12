package cn.tklvyou.mediaconvergence.ui.mine.point;

import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.activity.BaseHttpRecyclerActivity
import cn.tklvyou.mediaconvergence.base.interfaces.AdapterCallBack
import cn.tklvyou.mediaconvergence.helper.CustomRecycleViewDivider
import cn.tklvyou.mediaconvergence.model.BasePageModel
import cn.tklvyou.mediaconvergence.model.PointDetailModel
import cn.tklvyou.mediaconvergence.model.User
import cn.tklvyou.mediaconvergence.ui.adapter.MyPointAdapter
import cn.tklvyou.mediaconvergence.utils.CommonUtil
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_point.pointRefreshLayout
import kotlinx.android.synthetic.main.activity_point.pointTitleBar
import kotlinx.android.synthetic.main.activity_point_detail.*

/**
 * @author :JenkinsZhou
 * @description :积分明细列表
 * @company :途酷科技
 * @date 2019年08月01日17:47
 * @Email: 971613168@qq.com
 */
class MyPointDetailActivity : BaseHttpRecyclerActivity<PointDetailPresenter, PointDetailModel, BaseViewHolder, MyPointAdapter>(), PointDetailContract.View {


    override fun setList(list: MutableList<PointDetailModel>?) {
        setList(object : AdapterCallBack<MyPointAdapter> {

            override fun createAdapter(): MyPointAdapter {
                return MyPointAdapter()
            }

            override fun refreshAdapter() {
                adapter.setNewData(list)
            }
        })
    }

    override fun setPointDetails(page: Int, pageModel: BasePageModel<PointDetailModel>?) {
        if (pageModel != null) {
            onLoadSucceed(page, pageModel.data)
        } else {
            onLoadFailed(page, null)
        }
    }


    override fun initPresenter(): PointDetailPresenter {
        return PointDetailPresenter()
    }

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_point_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        hideTitleBar()
        pointTitleBar.setNavigationListener {
            finish()
        }
        setNavigationOnClickListener { finish() }
        pointTitleBar.setBackgroundColor(Color.TRANSPARENT)
        initSmartRefreshLayout(pointRefreshLayout)
        initRecyclerView(pointDetailRecyclerView)
        pointDetailRecyclerView.layoutManager = LinearLayoutManager(this)
        val divider = CustomRecycleViewDivider(mContext, LinearLayout.HORIZONTAL, 0.5f, CommonUtil.getColor(R.color.grayF4F4F4)).setDividerMarginLeft(10f)
        pointDetailRecyclerView.addItemDecoration(divider)
        mPresenter.getUser()
        mPresenter.getPointPageList(1)
    }

    override fun setUser(bean: User.UserinfoBean) {
        tvMyPoint.text = bean.score
    }


    override fun getListAsync(page: Int) {
        mPresenter.getPointPageList(page)
    }


}