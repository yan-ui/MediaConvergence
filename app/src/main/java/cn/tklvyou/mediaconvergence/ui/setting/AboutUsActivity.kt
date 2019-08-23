package cn.tklvyou.mediaconvergence.ui.setting

import android.os.Bundle
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.MyApplication
import cn.tklvyou.mediaconvergence.base.NullPresenter
import cn.tklvyou.mediaconvergence.base.activity.BaseActivity
import cn.tklvyou.mediaconvergence.base.activity.BaseTitleActivity
import cn.tklvyou.mediaconvergence.helper.GlideManager
import cn.tklvyou.mediaconvergence.model.SystemConfigModel
import cn.tklvyou.mediaconvergence.utils.CommonUtil
import com.wuhenzhizao.titlebar.widget.CommonTitleBar
import kotlinx.android.synthetic.main.activity_about_us.*

/**
 *@description :关于
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2019年08月02日14:08
 * @Email: 971613168@qq.com
 */
class AboutUsActivity : BaseActivity<AboutUsPresenter>(),AboutUsContract.View {
    override fun setSystemConfig(model: SystemConfigModel) {
        GlideManager.loadImg(model.qrcode,ivCode)
    }

    override fun initPresenter(): AboutUsPresenter {
        return AboutUsPresenter()
    }


    override fun getActivityLayoutID(): Int {
        return R.layout.activity_about_us
    }


    override fun initView(savedInstanceState: Bundle?) {
        setTitle("关于我们")
        setNavigationImage()
        setNavigationOnClickListener { finish() }


        tvAppName.text = CommonUtil.getAppName(MyApplication.getAppContext())
        val name = "V " + CommonUtil.getVersionName(MyApplication.getAppContext())
        tvAppVersion.text = name

        mPresenter.getSystemConfig()

    }


}


