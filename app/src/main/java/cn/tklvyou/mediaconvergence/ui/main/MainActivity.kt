package cn.tklvyou.mediaconvergence.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.KeyEvent
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.MyApplication
import cn.tklvyou.mediaconvergence.base.activity.BaseBottomTabActivity
import cn.tklvyou.mediaconvergence.base.NullPresenter
import cn.tklvyou.mediaconvergence.model.SystemConfigModel
import cn.tklvyou.mediaconvergence.ui.home.HomeFragment
import cn.tklvyou.mediaconvergence.ui.account.LoginActivity
import cn.tklvyou.mediaconvergence.ui.camera.CameraFragment
import cn.tklvyou.mediaconvergence.ui.mine.MineFragment
import cn.tklvyou.mediaconvergence.ui.service.ServiceFragment
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.trello.rxlifecycle3.components.support.RxFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.min


class MainActivity : BaseBottomTabActivity<MainPresenter>(),MainContract.View {
    override fun setSystemConfig(model: SystemConfigModel) {
        SPUtils.getInstance().put("search",model.default_search)
    }

    override fun getFragments(): MutableList<RxFragment> {
        return mFragments!!
    }

    override fun getFragmentContainerResId(): Int {
        return R.id.mainContainer
    }


    override fun initPresenter(): MainPresenter {
        return MainPresenter()
    }

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_main
    }

    private var isLogin = false
    private var mFragments: MutableList<RxFragment>? = null

    private var homeFragment: HomeFragment? = null
    private var cameraFragment: CameraFragment? = null
    private var serviceFragment: ServiceFragment? = null
    private var mineFragment: MineFragment? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(Bundle())
    }

    override fun initView(savedInstanceState: Bundle?) {
        hideTitleBar()

        isLogin = SPUtils.getInstance().getBoolean("login")
        if (!isLogin) {
            startActivity(Intent(this, LoginActivity::class.java))
            return
        } else {
            if (MyApplication.showSplash) {
                startActivity(Intent(this, SplashActivity::class.java))
            }

            mFragments = ArrayList()

            homeFragment = HomeFragment()
            cameraFragment = CameraFragment()
            serviceFragment = ServiceFragment()
            mineFragment = MineFragment()

            mFragments!!.add(homeFragment!!)
            mFragments!!.add(cameraFragment!!)
            mFragments!!.add(serviceFragment!!)
            mFragments!!.add(mineFragment!!)


            bottomNavigationView.enableAnimation(false)
            bottomNavigationView.enableShiftingMode(false)
            bottomNavigationView.enableItemShiftingMode(false)

            bottomNavigationView.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.navigation_home -> selectFragment(0)
                    R.id.navigation_camera -> selectFragment(1)
                    R.id.navigation_service -> selectFragment(2)
                    R.id.navigation_mine -> selectFragment(3)
                }
                return@setOnNavigationItemSelectedListener true
            }

            selectFragment(0)

            mPresenter.getSystemConfig()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        homeFragment = null
        cameraFragment = null
        serviceFragment = null
        mineFragment = null
        if(mFragments != null){
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
