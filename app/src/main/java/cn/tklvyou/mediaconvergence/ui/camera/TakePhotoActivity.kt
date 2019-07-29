package cn.tklvyou.mediaconvergence.ui.camera

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.view.View
import com.cjt2325.cameralibrary.JCameraView
import com.cjt2325.cameralibrary.listener.JCameraListener
import java.io.File
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.NullPresenter
import cn.tklvyou.mediaconvergence.base.activity.BaseActivity
import cn.tklvyou.mediaconvergence.ui.home.PublishNewsActivity
import cn.tklvyou.mediaconvergence.ui.video_edit.VideoEditActivity
import com.blankj.utilcode.util.ImageUtils
import com.luck.picture.lib.entity.LocalMedia
import java.io.Serializable

class TakePhotoActivity : BaseActivity<NullPresenter>() {

    private var jCameraView: JCameraView? = null


    override fun initPresenter(): NullPresenter {
        return NullPresenter()
    }

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_take_photo
    }

    override fun initView() {
        hideTitleBar()

        jCameraView = findViewById(R.id.jcameraview)

        //设置视频保存路径
        jCameraView!!.setSaveVideoPath(Environment.getExternalStorageDirectory().path + File.separator + "JCamera")

        //JCameraView监听
        jCameraView!!.setJCameraLisenter(object : JCameraListener {
            override fun captureSuccess(bitmap: Bitmap) {
                val intent = Intent(this@TakePhotoActivity, PublishNewsActivity::class.java)
                intent.putExtra("page", "随手拍")
                intent.putExtra("isVideo", false)

                val selectList = ArrayList<LocalMedia>()

                val path = "$cacheDir/temp.png"
                if (ImageUtils.save(bitmap, path, Bitmap.CompressFormat.PNG)) {
                    val localMedia = LocalMedia()
                    localMedia.setPosition(0)
                    localMedia.path = path
                    selectList.add(localMedia)
                }

                intent.putExtra("data", selectList as Serializable)
                startActivity(intent)
                finish()
            }

            override fun recordSuccess(videoUrlPath: String, firstFrame: Bitmap) {
                //获取视频首帧图片以及视频地址
                val intent = Intent(this@TakePhotoActivity, VideoEditActivity::class.java)
                intent.putExtra("page", "随手拍")
                intent.putExtra("isVideo", true)

                val selectList = ArrayList<LocalMedia>()
                val localMedia = LocalMedia()
                localMedia.setPosition(0)
                localMedia.path = videoUrlPath
                selectList.add(localMedia)
                intent.putExtra("data", selectList as Serializable)
                startActivity(intent)
                finish()
            }

        })

        jCameraView!!.setLeftClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        jCameraView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        jCameraView!!.onPause()
    }

}