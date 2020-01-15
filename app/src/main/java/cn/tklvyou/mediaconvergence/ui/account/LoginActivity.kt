package cn.tklvyou.mediaconvergence.ui.account

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.MyApplication
import cn.tklvyou.mediaconvergence.base.activity.BaseActivity
import cn.tklvyou.mediaconvergence.common.Contacts
import cn.tklvyou.mediaconvergence.ui.main.MainActivity
import cn.tklvyou.mediaconvergence.utils.InterfaceUtils
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ConvertUtils
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
import com.trello.rxlifecycle3.components.support.RxFragment
import kotlinx.android.synthetic.main.activity_login.*
import net.lucode.hackware.magicindicator.FragmentContainerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView
import org.json.JSONObject

class LoginActivity : BaseActivity<AccountLoginPresenter>(), AccountContract.LoginView, View.OnClickListener, InterfaceUtils.OnClickResult {

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_login
    }

    override fun initPresenter(): AccountLoginPresenter {
        return AccountLoginPresenter()
    }

    private var mDataList = arrayListOf("密码登录", "验证码登录")
    private val mFragmentContainerHelper = FragmentContainerHelper()
    private var mFragments: MutableList<RxFragment>? = null

    override fun initView(savedInstanceState: Bundle?) {
        hideTitleBar()
        //销毁非登录页的所有Activity
        ActivityUtils.finishOtherActivities(LoginActivity::class.java)

        mFragments = ArrayList()
        mFragments!!.add(PasswordFragment())
        mFragments!!.add(MobileCodeFragment())

        RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
                .subscribe { granted ->
                    if (!granted) { // Always true pre-M
                        ToastUtils.showShort("权限拒绝，无法使用")
                        finish()

                    }
                }


        btnWxLogin.setOnClickListener(this)
        btnWbLogin.setOnClickListener(this)
        btnQQLogin.setOnClickListener(this)

        initMagicIndicator()

        mFragmentContainerHelper.handlePageSelected(0, false)
        switchPages(0)
    }

    private fun initMagicIndicator() {
        magicIndicator.setBackgroundResource(R.drawable.round_indicator_bg)
        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val clipPagerTitleView = ClipPagerTitleView(context)
                clipPagerTitleView.text = mDataList.get(index)
                clipPagerTitleView.textSize = ConvertUtils.sp2px(12F).toFloat()
                clipPagerTitleView.textColor = Color.parseColor("#999999")
                clipPagerTitleView.clipColor = Color.WHITE
                clipPagerTitleView.setOnClickListener {
                    mFragmentContainerHelper.handlePageSelected(index)
                    switchPages(index)
                }

                return clipPagerTitleView;
            }

            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getIndicator(context: Context?): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                val navigatorHeight = ConvertUtils.dp2px(34F).toFloat()
                val borderWidth = UIUtil.dip2px(context, 1.0).toFloat()
                val lineHeight = navigatorHeight - 2 * borderWidth
                indicator.lineHeight = lineHeight
                indicator.roundRadius = lineHeight / 2
                indicator.yOffset = borderWidth
                indicator.setColors(Color.parseColor("#FF4A5C"))
                return indicator
            }


        }

        magicIndicator.navigator = commonNavigator
        mFragmentContainerHelper.attachMagicIndicator(magicIndicator)
    }

    override fun onClick(p0: View) {
        when (p0.id) {

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


    override fun loginSuccess() {
        MyApplication.showSplash = false
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun loginError() {
        ToastUtils.showShort("登录失败，请重试")
    }

    override fun bindMobile(third_id: Int) {
        val intent = Intent(this, BindPhoneActivity::class.java)
        intent.putExtra("third_id", third_id)
        startActivity(intent)
    }


    override fun onResult(msg: String) {
        mPresenter.thirdLogin("wechat", msg)
        InterfaceUtils.getInstance().remove(this)
    }


    private fun switchPages(index: Int) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        var fragment: Fragment
        var i = 0
        val j = mFragments!!.size
        while (i < j) {
            if (i == index) {
                i++
                continue
            }
            fragment = mFragments!![i]
            if (fragment.isAdded()) {
                fragmentTransaction.hide(fragment)
            }
            i++
        }
        fragment = mFragments!![index]
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment)
        } else {
            fragmentTransaction.add(R.id.container, fragment)
        }
        fragmentTransaction.commitAllowingStateLoss()
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


    override fun onDestroy() {
        super.onDestroy()
        if (mFragments != null) {
            mFragments!!.clear()
            mFragments = null
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