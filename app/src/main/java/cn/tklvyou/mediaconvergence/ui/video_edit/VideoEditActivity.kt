package cn.tklvyou.mediaconvergence.ui.video_edit

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.widget.SeekBar
import java.io.File

import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.NullPresenter
import cn.tklvyou.mediaconvergence.base.activity.BaseActivity
import cn.tklvyou.mediaconvergence.ui.home.publish_news.PublishNewsActivity
import cn.tklvyou.mediaconvergence.utils.mediaplayer.MPlayer
import cn.tklvyou.mediaconvergence.utils.mediaplayer.MPlayerException
import cn.tklvyou.mediaconvergence.utils.mediaplayer.MinimalDisplay
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.activity_video_edit.*
import wseemann.media.FFmpegMediaMetadataRetriever
import java.io.Serializable

class VideoEditActivity : BaseActivity<NullPresenter>() {

    override fun initPresenter(): NullPresenter? {
        return NullPresenter()
    }

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_video_edit
    }

    private lateinit var file_path: String
    private var player: MPlayer? = null

    private lateinit var selectList: MutableList<LocalMedia>

    private var hasBack = false
    private var page = ""

    override fun initView() {
        setTitle("封面选取")
        setNavigationImage()
        setPositiveText("选取")
        setNavigationOnClickListener {
            finish()
        }

        setPositiveOnClickListener {
            showLoading()
            Thread {
                val bitmap = getVideoThumbnail(file_path, mSeekBar.progress * 1000L)
                if (bitmap == null) {
                    runOnUiThread {
                        hideLoading()
                        ToastUtils.showShort("此处无法截取，请重新选取")
                    }

                }else{
                    runOnUiThread {
                        val path = "$cacheDir/videoImage.png"
                        if (ImageUtils.save(bitmap, path, Bitmap.CompressFormat.PNG)) {
                            hideLoading()

                            if (hasBack) {
                                val intent = Intent()
                                intent.putExtra("videoImage", path)
                                setResult(Activity.RESULT_OK, intent)
                            } else {
                                val intent = Intent(this, PublishNewsActivity::class.java)
                                intent.putExtra("isVideo", true)
                                intent.putExtra("page", page)
                                intent.putExtra("videoImage", path)
                                intent.putExtra("data", selectList as Serializable)
                                startActivity(intent)
                            }
                            finish()
                        } else {
                            hideLoading()
                            ToastUtils.showShort("图片保存失败")
                        }
                    }
                }
            }.start()
        }

        page = intent.getStringExtra("page")
        hasBack = intent.getBooleanExtra("hasBack", false)
        selectList = if (intent.getSerializableExtra("data") == null) ArrayList() else intent.getSerializableExtra("data") as MutableList<LocalMedia>
        file_path = selectList[0].path

        LogUtils.e(file_path)

        player = MPlayer()
        player!!.setDisplay(MinimalDisplay(mSurfaceView))

        try {
            player!!.setSource(file_path)
            //            player.play();
        } catch (e: MPlayerException) {
            e.printStackTrace()
            ToastUtils.showShort("视频地址有误")
        }

        player!!.setPlayStatusListener {
            mSeekBar!!.max = player!!.player.duration//为SeekBar设置最大值
            mSeekBar!!.progress = 0//为seekBar设置初始值
        }


        mSeekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (player!!.player != null && fromUser) {
                    player!!.player.seekTo(progress)
                }
            }
        })

    }


    /**
     * 获取本地视频缩略图
     * @param filePath
     * @return
     */
    fun getVideoThumbnail(filePath: String, time: Long): Bitmap? {
        var b: Bitmap? = null
        //FFmpegMediaMetadataRetriever
        val retriever = FFmpegMediaMetadataRetriever()
        val file = File(filePath)
        try {
            retriever.setDataSource(file.path)
            b = retriever.getFrameAtTime(time, FFmpegMediaMetadataRetriever.OPTION_CLOSEST)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            LogUtils.e(e.message)
        } catch (e: RuntimeException) {
            e.printStackTrace()
        } finally {
            try {
                retriever.release()
            } catch (e: RuntimeException) {
                e.printStackTrace()
            }

        }
        return b
    }


}