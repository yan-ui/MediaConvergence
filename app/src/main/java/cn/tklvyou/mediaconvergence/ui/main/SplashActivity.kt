package cn.tklvyou.mediaconvergence.ui.main

import android.os.Bundle
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.MyApplication
import cn.tklvyou.mediaconvergence.base.NullPresenter
import cn.tklvyou.mediaconvergence.base.activity.BaseActivity
import java.util.*

class SplashActivity : BaseActivity<NullPresenter>() {

    override fun initPresenter(): NullPresenter {
        return NullPresenter()
    }

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_splash_first
    }

    override fun initView(savedInstanceState: Bundle?) {
        hideTitleBar()
        MyApplication.showSplash = false
        val timer = Timer()
        val timerTask = object : TimerTask() {
            override fun run() {
                finish()
            }
        }
        timer.schedule(timerTask, 3000)
    }


}
