package cn.tklvyou.mediaconvergence.ui.account

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.MyApplication
import cn.tklvyou.mediaconvergence.base.activity.BaseActivity
import cn.tklvyou.mediaconvergence.common.Contacts
import cn.tklvyou.mediaconvergence.ui.main.MainActivity
import cn.tklvyou.mediaconvergence.utils.InterfaceUtils
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import com.sina.weibo.sdk.auth.sso.SsoHandler
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : BaseActivity<AccountLoginPresenter>(), AccountContract.LoginView, View.OnClickListener, InterfaceUtils.OnClickResult {

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_login
    }

    override fun initPresenter(): AccountLoginPresenter {
        return AccountLoginPresenter()
    }

    override fun initView(savedInstanceState: Bundle?) {
        hideTitleBar()
        //销毁非登录页的所有Activity
        ActivityUtils.finishOtherActivities(LoginActivity::class.java)

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
        btnWbLogin.setOnClickListener(this)
        btnQQLogin.setOnClickListener(this)

        RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (!granted) { // Always true pre-M
                        ToastUtils.showShort("权限拒绝，无法使用")
                        finish()

                    }
                }

        etPassword.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                login()
            }
            return@setOnEditorActionListener true
        }

    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btnLogin -> {
                login()
            }

            R.id.btnForget -> {
                startActivity(Intent(this, ForgetPasswordActivity::class.java))
            }

            R.id.btnRegister -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }

            R.id.btnWxLogin -> {
                InterfaceUtils.getInstance().add(this)
                startWxLogin()
            }

            R.id.btnWbLogin -> {
                startWbLogin()
            }

            R.id.btnQQLogin -> {
                startQQLogin()
            }

        }

    }

    private fun login() {
        hideSoftInput(etPassword.windowToken)

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

    private var mTencent: Tencent? = null
    private var iUiListener: IUiListener? = null
    private fun startQQLogin() {
        mTencent = Tencent.createInstance(Contacts.QQ_APPID, application)

        if (!mTencent!!.isQQInstalled(this)) {
            ToastUtils.showShort("您未安装QQ客户端")
            return
        }

        iUiListener = object : IUiListener {
            override fun onComplete(p0: Any?) {
                LogUtils.e(p0)
                val obj = p0 as JSONObject
                val openId = obj.getString("openid")
                val assess_token = obj.getString("access_token")

                val map = HashMap<String, String>()
                map.put("openid", openId)
                map.put("access_token", assess_token)
                mPresenter.thirdLogin("qq", Gson().toJson(map))
            }

            override fun onCancel() {
                ToastUtils.showShort("用户取消授权登录")
            }

            override fun onError(p0: UiError?) {
                ToastUtils.showShort(p0?.errorMessage)
            }

        }

        //all表示获取所有权限
        mTencent!!.login(this, "all", iUiListener)

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

    private var mSsoHandler: SsoHandler? = null
    private fun startWbLogin() {
        val pinfo = packageManager.getInstalledPackages(0)// 获取所有已安装程序的包信息
        var isInstall = false
        if (pinfo != null) {
            pinfo.forEach {
                val pn = it.packageName
                if (pn == "com.sina.weibo") {
                    isInstall = true
                }
            }
        }

        if (!isInstall) {
            ToastUtils.showShort("您未安装微博")
        } else {
            mSsoHandler = SsoHandler(this)
            mSsoHandler!!.authorize(object : WbAuthListener {
                override fun onSuccess(p0: Oauth2AccessToken?) {
                    val map = HashMap<String, String>()
                    map.put("token", p0!!.token)
                    map.put("uid", p0.uid)
                    mPresenter.thirdLogin("weibo", Gson().toJson(map))
                }

                override fun onFailure(p0: WbConnectErrorMessage?) {
                    ToastUtils.showShort(p0?.errorMessage)
                }

                override fun cancel() {
                    ToastUtils.showShort("用户取消授权登录")
                }

            })
        }
    }

    override fun bindMobile(third_id: Int) {
        val intent = Intent(this, BindPhoneActivity::class.java)
        intent.putExtra("third_id", third_id)
        startActivity(intent)
    }

    override fun loginSuccess() {
        MyApplication.showSplash = false
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun loginError() {

    }

    override fun onResult(msg: String) {
        mPresenter.thirdLogin("wechat", msg)
        InterfaceUtils.getInstance().remove(this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (mSsoHandler != null) {
            mSsoHandler!!.authorizeCallBack(requestCode, resultCode, data)
        }

        if (mTencent != null) {
            Tencent.onActivityResultData(requestCode, resultCode, data, null)
        }
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