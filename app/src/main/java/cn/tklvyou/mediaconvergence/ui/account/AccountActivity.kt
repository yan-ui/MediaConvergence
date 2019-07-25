package cn.tklvyou.mediaconvergence.ui.account

import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.NullContract
import cn.tklvyou.mediaconvergence.base.activity.BaseActivity
import cn.tklvyou.mediaconvergence.base.NullPresenter
import cn.tklvyou.mediaconvergence.ui.adapter.MyFragmentPagerAdapter
import com.blankj.utilcode.util.ToastUtils
import com.trello.rxlifecycle3.components.support.RxFragment
import kotlinx.android.synthetic.main.activity_account.*

class AccountActivity : BaseActivity<NullPresenter>(), NullContract.View {

    override fun initView() {
        val loginFragment = LoginFragment()
        val registerFragment = RegisterFragment()
        val data = ArrayList<RxFragment>()
        data.add(loginFragment)
        data.add(registerFragment)
        val adapter = MyFragmentPagerAdapter(supportFragmentManager, data)
        loginViewpager.adapter = adapter

        setTitle("测试标题")
        setNavigationImage()
        setNavigationOnClickListener {
            ToastUtils.showShort("left")
        }
        setPositiveImage(android.R.drawable.ic_input_add)
        setPositiveOnClickListener {
            ToastUtils.showShort("right")
        }

    }

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_account
    }

    override fun initPresenter(): NullPresenter {
        return NullPresenter()
    }



}