package cn.tklvyou.mediaconvergence.ui.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.NullPresenter
import cn.tklvyou.mediaconvergence.base.activity.BaseWebViewActivity
import cn.tklvyou.mediaconvergence.model.SystemConfigModel
import kotlinx.android.synthetic.main.activity_agreement.*

class AgreementActivity : BaseWebViewActivity<AgreenmentPresenter>(),AccountContract.AgreenmentView {

    override fun setTitleContent(title: String) {
    }


    override fun initPresenter(): AgreenmentPresenter {
        return AgreenmentPresenter()
    }

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_agreement
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitle("注册协议")
        setNavigationImage()
        setNavigationOnClickListener { finish() }

        initWebView(webView = webView)

        mPresenter.getSystemConfig()
    }

    override fun setSystemConfig(model: SystemConfigModel) {
        loadHtml(model.register)
    }


}
