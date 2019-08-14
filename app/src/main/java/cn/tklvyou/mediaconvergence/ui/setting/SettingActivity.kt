package cn.tklvyou.mediaconvergence.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.activity.BaseActivity
import cn.tklvyou.mediaconvergence.common.SpConstant.PREF_KEY_TOKEN
import cn.tklvyou.mediaconvergence.helper.AccountHelper
import cn.tklvyou.mediaconvergence.ui.account.LoginActivity
import cn.tklvyou.mediaconvergence.ui.setting.edit_pass.EditPasswordActivity
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.activity_setting.*


/**
 *@description :
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2019年07月30日17:07
 * @Email: 971613168@qq.com
 */
class SettingActivity : BaseActivity<SettingPresenter>(), View.OnClickListener ,SettingContract.LogoutView{

    override fun initPresenter(): SettingPresenter {
        return SettingPresenter()
    }

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_setting
    }


    override fun initView(savedInstanceState: Bundle?) {
        setTitle("系统设置")
        setNavigationImage()
        setNavigationOnClickListener { finish() }
        ivSkipEditPass.setOnClickListener(this)
        tvLogOut.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        if (v == null) {
            return
        }
        when (v.id) {
            R.id.ivSkipEditPass -> {
                if (!AccountHelper.getInstance().isLogin) {
                    ToastUtils.showShort("您还未登录")
                    return
                }
                startActivity(Intent(this, EditPasswordActivity::class.java))
            }
            R.id.tvLogOut -> {
                mPresenter.logout()

            }
        }

    }

    override fun logoutSuccess() {
        handleLogout()
    }


    private fun handleLogout() {
        SPUtils.getInstance().put(PREF_KEY_TOKEN, "")
        SPUtils.getInstance().put("login", false)
        SPUtils.getInstance().put("groupId", 0)
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

}