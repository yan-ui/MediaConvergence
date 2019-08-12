package cn.tklvyou.mediaconvergence.ui.service

import android.os.Bundle
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.NullPresenter
import cn.tklvyou.mediaconvergence.base.activity.BaseWebViewActivity
import kotlinx.android.synthetic.main.activity_service_webview.*

class ServiceWebviewActivity : BaseWebViewActivity<NullPresenter>() {

    override fun initPresenter(): NullPresenter {
        return NullPresenter()
    }

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_service_webview
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitle("服务")
        setNavigationImage()
        setNavigationOnClickListener { finish() }

        initWebView(webView)
        val url = intent.getStringExtra("url")
        loadUrl(url)
    }

    override fun setTitleContent(title: String) {
        setTitle(title)
    }



}
