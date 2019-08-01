package cn.tklvyou.mediaconvergence.ui.home

import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.NullPresenter
import cn.tklvyou.mediaconvergence.base.activity.BaseWebViewActivity
import kotlinx.android.synthetic.main.activity_banner_details.*

class BannerDetailsActivity : BaseWebViewActivity<NullPresenter>() {

    override fun initPresenter(): NullPresenter {
        return NullPresenter()
    }

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_banner_details
    }

    override fun initView() {
        val title = intent.getStringExtra("title")
        val html = intent.getStringExtra("content")

        setTitle(title)
        setNavigationImage()
        setNavigationOnClickListener {
            finish()
        }

        initWebView(webView = webView)

        loadHtml(html)
    }

    override fun setTitleContent(title: String) {

    }

}
