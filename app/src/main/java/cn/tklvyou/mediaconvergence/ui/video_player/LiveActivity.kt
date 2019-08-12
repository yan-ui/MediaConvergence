package cn.tklvyou.mediaconvergence.ui.video_player

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.WindowManager

import com.blankj.utilcode.util.ToastUtils
import com.netease.neliveplayer.playerkit.sdk.LivePlayer
import com.netease.neliveplayer.playerkit.sdk.LivePlayerObserver
import com.netease.neliveplayer.playerkit.sdk.PlayerManager
import com.netease.neliveplayer.playerkit.sdk.constant.CauseCode
import com.netease.neliveplayer.playerkit.sdk.model.MediaInfo
import com.netease.neliveplayer.playerkit.sdk.model.StateInfo
import com.netease.neliveplayer.playerkit.sdk.model.VideoBufferStrategy
import com.netease.neliveplayer.playerkit.sdk.model.VideoOptions
import com.netease.neliveplayer.playerkit.sdk.model.VideoScaleMode
import com.netease.neliveplayer.playerkit.sdk.view.AdvanceSurfaceView
import com.netease.neliveplayer.sdk.NELivePlayer

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.Locale

import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.NullPresenter
import cn.tklvyou.mediaconvergence.base.activity.BaseActivity
import cn.tklvyou.mediaconvergence.ui.receiver.Observer
import cn.tklvyou.mediaconvergence.ui.receiver.PhoneCallStateObserver
import cn.tklvyou.mediaconvergence.ui.services.PlayerService
import kotlinx.android.synthetic.main.activity_player.*


class LiveActivity : BaseActivity<NullPresenter>() {

    override fun initPresenter(): NullPresenter {
        return NullPresenter()
    }

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_player
    }


    override fun initView(savedInstanceState: Bundle?) {

        hideTitleBar()
        btnBack.setOnClickListener {
            mBackPressed = true
            releasePlayer()
            finish()
        }


        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) //保持屏幕常亮
        PhoneCallStateObserver.getInstance().observeLocalPhoneObserver(localPhoneObserver, true)
        parseIntent()
        findViews()
        initPlayer()
    }

    private val surfaceView: AdvanceSurfaceView? = null

    protected var player: LivePlayer? = null

    private var mediaInfo: MediaInfo? = null

    private var mDecodeType: String? = null//解码类型，硬解或软解

    private var mMediaType: String? = null //媒体类型

    private var mVideoPath: String? = null //文件路径

    private val mHardware = true

    private var mUri: Uri? = null

    private var mTitle: String? = null

    private var mBackPressed: Boolean = false

    private val mPaused = false

    private var isMute = false

    private var mIsFullScreen = false

    protected var isPauseInBackground: Boolean = false

    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {

        override fun handleMessage(msg: Message) {
            var msg = msg
            val position: Long
            when (msg.what) {
                SHOW_PROGRESS -> {
                    position = setProgress()
                    msg = obtainMessage(SHOW_PROGRESS)
                    sendMessageDelayed(msg, 1000 - position % 1000)
                }
            }
        }
    }

    private fun setProgress(): Long {
        if (player == null) {
            return 0
        }
        val position = player!!.currentPosition.toInt()
        if (mCurrentTime != null) {
            mCurrentTime!!.text = stringForTime(position.toLong())
        }
        return position.toLong()
    }

    private val mMuteListener = View.OnClickListener {
        if (!isMute) {
            mMuteButton!!.setImageResource(R.drawable.nemediacontroller_mute01)
            player!!.setMute(true)
            isMute = true
        } else {
            mMuteButton!!.setImageResource(R.drawable.nemediacontroller_mute02)
            player!!.setMute(false)
            isMute = false
        }
    }

    //这里直播可以用 LivePlayerObserver 点播用 VodPlayerObserver
    private val playerObserver = object : LivePlayerObserver {

        override fun onPreparing() {}

        override fun onPrepared(info: MediaInfo) {
            mediaInfo = info
        }

        override fun onError(code: Int, extra: Int) {
            mBuffer!!.visibility = View.INVISIBLE
            if (code == CauseCode.CODE_VIDEO_PARSER_ERROR) {
                ToastUtils.showShort("视频解析出错")
            } else {
                val build = AlertDialog.Builder(this@LiveActivity)
                build.setTitle("播放错误").setMessage("错误码：$code").setPositiveButton("确定", null).setCancelable(false)
                        .show()
            }

        }

        override fun onFirstVideoRendered() {
            //            ToastUtils.showShort("视频第一帧已解析");

        }

        override fun onFirstAudioRendered() {
            //            showToast("音频第一帧已解析");
        }

        override fun onBufferingStart() {
            mBuffer!!.visibility = View.VISIBLE
        }

        override fun onBufferingEnd() {
            mBuffer!!.visibility = View.GONE
        }

        override fun onBuffering(percent: Int) {
            Log.d(TAG, "缓冲中...$percent%")
        }

        override fun onVideoDecoderOpen(value: Int) {
            //            showToast("使用解码类型：" + (value == 1 ? "硬件解码" : "软解解码"));
        }

        override fun onStateChanged(stateInfo: StateInfo?) {
            if (stateInfo != null && stateInfo.causeCode == CauseCode.CODE_VIDEO_STOPPED_AS_NET_UNAVAILABLE) {
                ToastUtils.showShort("网络已断开")
            }
        }

        override fun onHttpResponseInfo(code: Int, header: String) {
            Log.i(TAG, "onHttpResponseInfo,code:$code header:$header")
        }
    }

    private val mSnapShotListener = View.OnClickListener {
        if (mMediaType == "localaudio" || mHardware) {
            if (mMediaType == "localaudio") {
                ToastUtils.showShort("音频播放不支持截图！")
            } else if (mHardware) {
                ToastUtils.showShort("硬件解码不支持截图！")
            }
            return@OnClickListener
        }
        getSnapshot()
    }

    private val mSetPlayerScaleListener = View.OnClickListener {
        if (mIsFullScreen) {
            mSetPlayerScaleButton!!.setImageResource(R.drawable.nemediacontroller_scale01)
            mIsFullScreen = false
            player!!.setVideoScaleMode(VideoScaleMode.FIT)

        } else {
            mSetPlayerScaleButton!!.setImageResource(R.drawable.nemediacontroller_scale02)
            mIsFullScreen = true
            player!!.setVideoScaleMode(VideoScaleMode.FULL)

        }
    }

    /**
     * 时间戳回调
     */
    private val mOnCurrentSyncTimestampListener = NELivePlayer.OnCurrentSyncTimestampListener { timestamp -> Log.v(TAG, "OnCurrentSyncTimestampListener,onCurrentSyncTimestamp:$timestamp") }

    private val mOnCurrentSyncContentListener = NELivePlayer.OnCurrentSyncContentListener { content ->
        val sb = StringBuffer()
        for (str in content) {
            sb.append(str + "\r\n")
        }
        ToastUtils.showShort("onCurrentSyncContent,收到同步信息:$sb")
        Log.v(TAG, "onCurrentSyncContent,收到同步信息:$sb")
    }

    //处理与电话逻辑
    private val localPhoneObserver = Observer<Int> { phoneState ->
        if (phoneState == TelephonyManager.CALL_STATE_IDLE) {
            player!!.start()
        } else if (phoneState == TelephonyManager.CALL_STATE_RINGING) {
            player!!.stop()
        } else {
            Log.i(TAG, "localPhoneObserver onEvent " + phoneState!!)
        }
    }



    private fun parseIntent() {
        //接收MainActivity传过来的参数
//        mMediaType = intent.getStringExtra("media_type")
//        mDecodeType = intent.getStringExtra("decode_type")
        mMediaType = "livestream"
        mDecodeType = "hardware"
        mVideoPath = intent.getStringExtra("videoPath")
        mUri = Uri.parse(mVideoPath)

        //        if (mMediaType != null && mMediaType.equals("localaudio")) { //本地音频文件采用软件解码
        //            mDecodeType = "software";
        //        }
        //        if (mDecodeType != null && mDecodeType.equals("hardware")) {
        //            mHardware = true;
        //        } else {
        //            mHardware = false;
        //        }

    }

    protected fun findViews() {
        if (mMediaType != null && mMediaType == "localaudio") {
            mAudioRemind!!.visibility = View.VISIBLE
        } else {
            mAudioRemind!!.visibility = View.INVISIBLE
        }
        mPlayPauseButton!!.setOnControlStatusChangeListener { view, state ->
            if (state) {
                player!!.start()
            } else {
                player!!.stop()
            }
        }

        mMuteButton!!.setOnClickListener(mMuteListener)
        mProgressBar!!.isEnabled = false
        mEndTime!!.text = "--:--:--"
        mCurrentTime!!.text = "--:--:--"
        mHandler.sendEmptyMessage(SHOW_PROGRESS)
        mSnapshotButton!!.setOnClickListener(mSnapShotListener)
        mSetPlayerScaleButton!!.setOnClickListener(mSetPlayerScaleListener)


    }


    private fun initPlayer() {
        val options = VideoOptions()
        options.hardwareDecode = mHardware
        /**
         * isPlayLongTimeBackground 控制退到后台或者锁屏时是否继续播放，开发者可根据实际情况灵活开发,我们的示例逻辑如下：
         * 使用软件解码：
         * isPlayLongTimeBackground 为 false 时，直播进入后台停止播放，进入前台重新拉流播放
         * isPlayLongTimeBackground 为 true 时，直播进入后台不做处理，继续播放,
         *
         * 使用硬件解码：
         * 直播进入后台停止播放，进入前台重新拉流播放
         */
        options.isPlayLongTimeBackground = !isPauseInBackground
        options.bufferStrategy = VideoBufferStrategy.LOW_LATENCY
        player = PlayerManager.buildLivePlayer(this, mVideoPath, options)
        intentToStartBackgroundPlay()
        start()
        if (surfaceView == null) {
            player!!.setupRenderView(textureView, VideoScaleMode.FIT)
        } else {
            player!!.setupRenderView(surfaceView, VideoScaleMode.FIT)
        }

    }

    private fun start() {
        player!!.registerPlayerObserver(playerObserver, true)
        //        player.registerPlayerCurrentSyncTimestampListener(pullIntervalTime, mOnCurrentSyncTimestampListener, true);
        //        player.registerPlayerCurrentSyncContentListener(mOnCurrentSyncContentListener, true);
        player!!.start()
    }


    override fun onResume() {
        super.onResume()
        if (player != null) {
            player!!.onActivityResume(true)
        }
    }

    override fun onStop() {
        super.onStop()
        enterBackgroundPlay()
        if (player != null) {
            player!!.onActivityStop(true)
        }
    }


    override fun onBackPressed() {
        mBackPressed = true
        finish()
        releasePlayer()
        super.onBackPressed()
    }


    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()

    }

    private fun releasePlayer() {
        if (player == null) {
            return
        }
        Log.i(TAG, "releasePlayer")
        player!!.registerPlayerObserver(playerObserver, false)
        //        player.registerPlayerCurrentSyncTimestampListener(0, null, false);
        //        player.registerPlayerCurrentSyncContentListener(null, false);
        PhoneCallStateObserver.getInstance().observeLocalPhoneObserver(localPhoneObserver, false)
        player!!.setupRenderView(null, VideoScaleMode.NONE)
        textureView.releaseSurface()
        player!!.stop()
        player = null
        intentToStopBackgroundPlay()
        mHandler.removeCallbacksAndMessages(null)

    }

    fun getSnapshot() {
        if (mediaInfo == null) {
            Log.d(TAG, "mediaInfo is null,截图不成功")
            ToastUtils.showShort("截图不成功")
        } else if (mediaInfo!!.videoDecoderMode == "MediaCodec") {
            Log.d(TAG, "hardware decoder unsupport snapshot")
            ToastUtils.showShort("截图不支持硬件解码")
        } else {
            val bitmap = player!!.snapshot
            val picName = "/sdcard/NESnapshot" + System.currentTimeMillis() + ".jpg"
            val f = File(picName)
            try {
                f.createNewFile()
            } catch (e1: IOException) {
                e1.printStackTrace()
            }

            var fOut: FileOutputStream? = null
            try {
                fOut = FileOutputStream(f)
                if (picName.substring(picName.lastIndexOf(".") + 1, picName.length) == "jpg") {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                } else if (picName.substring(picName.lastIndexOf(".") + 1, picName.length) == "png") {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
                }
                fOut.flush()
                fOut.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            ToastUtils.showShort("截图成功")
        }
    }

    /**
     * 处理service后台播放逻辑
     */
    private fun intentToStartBackgroundPlay() {
        if (!mHardware && !isPauseInBackground) {
            PlayerService.intentToStart(this)
        }
    }

    private fun intentToStopBackgroundPlay() {
        if (!mHardware && !isPauseInBackground) {
            PlayerService.intentToStop(this)
            player = null
        }
    }


    private fun enterBackgroundPlay() {
        if (!mHardware && !isPauseInBackground) {
            PlayerService.setMediaPlayer(player)
        }
    }

    private fun stopBackgroundPlay() {
        if (!mHardware && !isPauseInBackground) {
            PlayerService.setMediaPlayer(null)
        }
    }

    companion object {

        private val TAG = "LiveActivity"


        private val SHOW_PROGRESS = 0x01


        private fun stringForTime(position: Long): String {
            val totalSeconds = (position / 1000.0 + 0.5).toInt()
            val seconds = totalSeconds % 60
            val minutes = totalSeconds / 60 % 60
            val hours = totalSeconds / 3600
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
        }
    }
}
