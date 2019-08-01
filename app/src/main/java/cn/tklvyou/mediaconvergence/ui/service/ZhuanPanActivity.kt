package cn.tklvyou.mediaconvergence.ui.service

import android.animation.ValueAnimator
import android.graphics.Color
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.NullPresenter
import cn.tklvyou.mediaconvergence.base.activity.BaseActivity
import com.blankj.utilcode.util.ToastUtils
import com.cretin.www.wheelsruflibrary.listener.RotateListener
import com.cretin.www.wheelsruflibrary.view.WheelSurfView
import kotlinx.android.synthetic.main.activity_zhuan_pan.*
import java.util.*


class ZhuanPanActivity : BaseActivity<NullPresenter>() {

    override fun initPresenter(): NullPresenter {
        return NullPresenter()
    }

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_zhuan_pan
    }

    override fun initView() {
        hideTitleBar()

        btnBack.setOnClickListener {
            finish()
        }

        //颜色
        val colors = arrayOf<Int>(Color.parseColor("#FFC6B1"),
                Color.parseColor("#FCB195"),
                Color.parseColor("#FFC6B1"),
                Color.parseColor("#FCB195"),
                Color.parseColor("#FFC6B1"),
                Color.parseColor("#FCB195"))
        //文字
        val des = arrayOf("王 者 皮 肤", "1 8 0 积 分", "L O L 皮 肤", "谢 谢 参 与", "2 8 积 分", "微 信 红 包")
//        //图标
//        var mListBitmap: List<Bitmap> = ArrayList()
//        for (i in colors.indices) {
//            mListBitmap.add(BitmapFactory.decodeResource(getResources(), R.mipmap.iphone))
//        }
//        //主动旋转一下图片
//        mListBitmap = WheelSurfView.rotateBitmaps(mListBitmap)

        //获取第三个视图
        val build = WheelSurfView.Builder()
                .setmColors(colors)
                .setmHuanImgRes(R.mipmap.zhuan_pan_yuan_bg)
                .setmGoImgRes(R.mipmap.zhuan_pan_go)
                .setmDeses(des)
                .setmTypeModel(1)
                .setmTypeNum(6)
                .build()
        wheelSurfView.setConfig(build)

        //添加滚动监听
        wheelSurfView.setRotateListener(object : RotateListener {
            override fun rotateEnd(position: Int, des: String) {
                wheelSurfView.goBtn.isEnabled = true
                ToastUtils.showShort("结束了 位置：$position   描述：$des")
            }

            override fun rotating(valueAnimator: ValueAnimator) {
                wheelSurfView.goBtn.isEnabled = false
            }

            override fun rotateBefore(goImg: ImageView) {
                val builder = AlertDialog.Builder(this@ZhuanPanActivity)
                builder.setTitle("温馨提示")
                builder.setMessage("确定要消耗一次抽奖机会？")
                builder.setPositiveButton("确定") { dialog, which ->
                    //模拟位置
                    val position = Random().nextInt(6) + 1
                    wheelSurfView.startRotate(position)
                }
                builder.setNegativeButton("取消") { dialog, which -> }
                builder.show()

            }
        })
    }

}
