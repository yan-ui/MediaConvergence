package cn.tklvyou.mediaconvergence.ui.account

import android.util.Log
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.fragment.BaseFragment
import com.blankj.utilcode.util.LogUtils

class RegisterFragment : BaseFragment<AccountRegisterPresenter>(), AccountContract.RegisterView {
    override fun lazyData() {
        LogUtils.e("懒加载数据 --- 2")
    }

    override fun registerSuccess(msg: String?) {
    }

    override fun registerError(msg: String?) {
    }

    override fun initView() {
        Log.e("test","initing  2 ...")
    }

    override fun initPresenter(): AccountRegisterPresenter {
        return AccountRegisterPresenter()
    }

    override fun getFragmentLayoutID(): Int {
        return R.layout.register_view
    }

}