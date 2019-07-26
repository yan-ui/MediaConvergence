package cn.tklvyou.mediaconvergence.ui.account

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.NullContract
import cn.tklvyou.mediaconvergence.base.activity.BaseActivity
import cn.tklvyou.mediaconvergence.base.NullPresenter
import cn.tklvyou.mediaconvergence.common.Contacts
import cn.tklvyou.mediaconvergence.ui.main.MainActivity
import cn.tklvyou.mediaconvergence.utils.InterfaceUtils
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<AccountLoginPresenter>(), AccountContract.LoginView, View.OnClickListener,InterfaceUtils.OnClickResult {

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_login
    }

    override fun initPresenter(): AccountLoginPresenter {
        return AccountLoginPresenter()
    }

    override fun initView() {
        hideTitleBar()
        //销毁非登录页的所有Activity
        ActivityUtils.finishOtherActivities(this::class.java)

        etAccount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null) {
                    val account = p0.toString().trim()
                    if (account.length == 11 && account.substring(0, 1) == "1") {
                        ivRight.visibility = View.VISIBLE
                    } else {
                        ivRight.visibility = View.INVISIBLE
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })


        btnLogin.setOnClickListener(this)
        btnForget.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
        btnWxLogin.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btnLogin -> {
                val account = etAccount.text.toString().trim()
                val password = etPassword.text.toString().trim()
                if (ivRight.visibility != View.VISIBLE) {
                    ToastUtils.showShort("请输入正确的手机号")
                    return
                }

                if (password.isEmpty()) {
                    ToastUtils.showShort("请输入密码")
                    return
                }

                mPresenter.login(account, password)
            }

            R.id.btnForget -> {
                startActivity(Intent(this, ForgetPasswordActivity::class.java))
            }

            R.id.btnRegister -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }

            R.id.btnWxLogin ->{
                InterfaceUtils.getInstance().add(this)
                startWxLogin()
            }

        }

    }


    override fun loginSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun loginError() {

    }

    private fun startWxLogin() {
        val appID = Contacts.WX_APPID
        val api: IWXAPI = WXAPIFactory.createWXAPI(this, appID, true)
        if (!api.isWXAppInstalled) {
            ToastUtils.showShort("您还未安装微信")
        } else {
            val req = SendAuth.Req()
            //内容固定
            req.scope = "snsapi_userinfo"
            //自定义内容
            req.state = "sxmedia_wxlogin"
            api.sendReq(req)
        }
    }

    override fun onResult(msg: String) {
        mPresenter.thirdLogin("wechat",msg)
        InterfaceUtils.getInstance().remove(this)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            appExit()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private var isExit: Boolean = false

    @SuppressLint("HandlerLeak")
    private val exitHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            isExit = false
        }
    }

    private fun appExit() {
        if (!isExit) {
            isExit = true
            ToastUtils.showShort("再按一次退出程序")
            exitHandler.sendEmptyMessageDelayed(0, 2000)
        } else {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            startActivity(intent)
            System.exit(0)
        }
    }
}