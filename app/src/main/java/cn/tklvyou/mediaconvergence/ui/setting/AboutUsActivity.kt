package cn.tklvyou.mediaconvergence.ui.setting

import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.MyApplication
import cn.tklvyou.mediaconvergence.base.NullPresenter
import cn.tklvyou.mediaconvergence.base.activity.BaseTitleActivity
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
class AboutUsActivity : BaseTitleActivity<NullPresenter>() {
    override fun initPresenter(): NullPresenter {
        return NullPresenter()
    }


    override fun setTitleBar(titleBar: CommonTitleBar?) {
        titleBar?.setMainTitle("关于我们")
    }


    override fun getActivityLayoutID(): Int {
        return R.layout.activity_about_us
    }


    override fun initView() {
        tvAppName.text = CommonUtil.getAppName(MyApplication.getAppContext())
        val name = "V " + CommonUtil.getVersionName(MyApplication.getAppContext())
        tvAppVersion.text = name
    }


}


