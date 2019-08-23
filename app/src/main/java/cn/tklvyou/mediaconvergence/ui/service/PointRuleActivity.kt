package cn.tklvyou.mediaconvergence.ui.service

import android.os.Bundle
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.activity.BaseWebViewActivity
import cn.tklvyou.mediaconvergence.model.PointRuleModel
import kotlinx.android.synthetic.main.activity_point_rule.*

class PointRuleActivity : BaseWebViewActivity<PointRulePresenter>(),PointRuleContract.View {

    override fun initPresenter(): PointRulePresenter {
        return PointRulePresenter()
    }

    override fun getActivityLayoutID(): Int {
       return R.layout.activity_point_rule
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitle("积分规则")
        setNavigationImage()
        setNavigationOnClickListener { finish() }

        initWebView(webView)

        mPresenter.getPointRule()
    }


    override fun onRetry() {
        super.onRetry()
        mPresenter.getPointRule()
    }

    override fun setTitleContent(title: String) {
    }

    override fun setPointRule(model: PointRuleModel) {
        tvTitle.text = model.name
        tvName.text = model.nickname
        tvTime.text = model.time
        loadHtml(model.content)
    }


}
