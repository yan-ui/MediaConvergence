package cn.tklvyou.mediaconvergence.ui.main

import android.content.Intent
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.activity.BaseBottomTabActivity
import cn.tklvyou.mediaconvergence.base.NullPresenter
import cn.tklvyou.mediaconvergence.ui.home.HomeFragment
import cn.tklvyou.mediaconvergence.ui.account.LoginActivity
import cn.tklvyou.mediaconvergence.ui.camera.CameraFragment
import cn.tklvyou.mediaconvergence.ui.mine.MineFragment
import cn.tklvyou.mediaconvergence.ui.service.ServiceFragment
import com.blankj.utilcode.util.SPUtils
import com.trello.rxlifecycle3.components.support.RxFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseBottomTabActivity<NullPresenter>() {
    override fun getFragments(): MutableList<RxFragment> {
        return mFragments!!
    }

    override fun getFragmentContainerResId(): Int {
        return R.id.mainContainer
    }


    override fun initPresenter(): NullPresenter {
        return NullPresenter()
    }

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_main
    }

    private var isLogin = false
    private var mFragments: MutableList<RxFragment>? = null
    override fun initView() {
        hideTitleBar()

        isLogin = SPUtils.getInstance().getBoolean("login")

        if (!isLogin) {
            startActivity(Intent(this, LoginActivity::class.java))
            return
        }

        mFragments = ArrayList()
        mFragments!!.add(HomeFragment())
        mFragments!!.add(CameraFragment())
        mFragments!!.add(ServiceFragment())
        mFragments!!.add(MineFragment())

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
    }


}
