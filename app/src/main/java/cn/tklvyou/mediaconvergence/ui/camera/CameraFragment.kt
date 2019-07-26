package cn.tklvyou.mediaconvergence.ui.camera

import android.content.Intent
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.NullPresenter
import cn.tklvyou.mediaconvergence.base.fragment.BaseFragment
import cn.tklvyou.mediaconvergence.ui.main.MainActivity
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils

class CameraFragment : BaseFragment<NullPresenter>() {


    override fun initPresenter(): NullPresenter {
        return NullPresenter()
    }

    override fun getFragmentLayoutID(): Int {
        return R.layout.fragment_camera
    }

    override fun initView() {

    }

    override fun lazyData() {

    }

    override fun onAutoRefresh() {
        super.onAutoRefresh()
        ToastUtils.showShort("自动刷新数据")
    }

}