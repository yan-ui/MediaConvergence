package cn.tklvyou.mediaconvergence.ui.home.news_detail

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.activity.BaseWebViewActivity
import cn.tklvyou.mediaconvergence.common.Contacts
import cn.tklvyou.mediaconvergence.helper.GlideManager
import cn.tklvyou.mediaconvergence.model.NewsBean
import cn.tklvyou.mediaconvergence.model.VoteOptionModel
import cn.tklvyou.mediaconvergence.ui.home.AudioController
import cn.tklvyou.mediaconvergence.ui.home.ImagePagerActivity
import cn.tklvyou.mediaconvergence.ui.video_player.VodActivity
import cn.tklvyou.mediaconvergence.utils.GlideCircleTransform
import cn.tklvyou.mediaconvergence.utils.InterfaceUtils
import cn.tklvyou.mediaconvergence.utils.UrlUtils
import cn.tklvyou.mediaconvergence.widget.SharePopupWindow
import cn.tklvyou.mediaconvergence.widget.VoteLoadButton
import com.blankj.utilcode.util.*
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.sina.weibo.sdk.api.WebpageObject
import com.sina.weibo.sdk.api.WeiboMultiMessage
import com.sina.weibo.sdk.share.WbShareCallback
import com.sina.weibo.sdk.share.WbShareHandler
import com.sina.weibo.sdk.utils.Utility
import com.tencent.connect.share.QQShare
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import kotlinx.android.synthetic.main.activity_news_detail.*
import java.util.*

/**
 * Created by yiw on 2016/1/6.
 */
class NewsDetailActivity : BaseWebViewActivity<NewsDetailPresenter>(), NewsDetailContract.View {

    companion object {
        private val INTENT_TYPE = "type"
        private val INTENT_ID = "id"

        fun startNewsDetailActivity(context: Context, type: String, id: Int) {
            val intent = Intent(context, NewsDetailActivity::class.java)
            intent.putExtra(INTENT_ID, id)
            intent.putExtra(INTENT_TYPE, type)
            context.startActivity(intent)
        }
    }

    private var id: Int = 0
    private var type: String = ""
    private var shareTitle = ""

    override fun initPresenter(): NewsDetailPresenter {
        return NewsDetailPresenter()
    }

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_news_detail
    }

    private var isLike = false
    private var hasCollect = false
    private var like_num = 0

    private var mAudioControl: AudioController? = null

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    override fun initView(savedInstanceState: Bundle?) {
        id = intent.getIntExtra(INTENT_ID, 0)
        type = intent.getStringExtra(INTENT_TYPE)

        if (type == "电视") {
            setTitle("视讯")
        } else {
            setTitle(type)
        }

        setNavigationImage()
        setNavigationOnClickListener { v ->
            release()
            finish()
        }

        initWebView(newsDetailWebView)

        when (type) {
            "视频", "图片" -> {
                llWXHeader.visibility = View.VISIBLE
                llArticle.visibility = View.GONE

            }

            "电视" ->{
                setPositiveImage(R.mipmap.icon_collect_normal)
                llWXHeader.visibility = View.GONE
                contentTv.visibility = View.GONE
                llArticle.visibility = View.VISIBLE

                (ivVideo.layoutParams as LinearLayout.LayoutParams).setMargins(0, 0, 0, 0)
            }


            "视讯"  -> {
                llWXHeader.visibility = View.GONE
                contentTv.visibility = View.GONE
                llArticle.visibility = View.VISIBLE

                (ivVideo.layoutParams as LinearLayout.LayoutParams).setMargins(0, 0, 0, 0)
            }


            "公告" -> {
                llWXHeader.visibility = View.GONE
                contentTv.visibility = View.GONE
                ivVideo.visibility = View.GONE
                llArticle.visibility = View.VISIBLE
            }


            "文章", "悦读" -> {
                setPositiveImage(R.mipmap.icon_collect_normal)
                llWXHeader.visibility = View.GONE
                contentTv.visibility = View.GONE
                ivVideo.visibility = View.GONE
                llArticle.visibility = View.VISIBLE
            }

            "问政" -> {
                llWXHeader.visibility = View.GONE
                contentTv.visibility = View.GONE
                ivVideo.visibility = View.GONE
                llArticle.visibility = View.VISIBLE
            }

            "悦听" -> {
                setPositiveImage(R.mipmap.icon_collect_normal)
                llWXHeader.visibility = View.GONE
                contentTv.visibility = View.GONE
                ivVideo.visibility = View.GONE
                llArticle.visibility = View.VISIBLE
                llYueTing.visibility = View.VISIBLE
                mAudioControl = AudioController(this)
            }

        }

        commentLayout.setOnClickListener {
            updateEditTextBodyVisible(View.VISIBLE)
        }

        //发表评论
        sendIv.setOnClickListener(View.OnClickListener {
            //发布评论
            val content = circleEt.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(content)) {
                ToastUtils.showShort("评论内容不能为空...")
                return@OnClickListener
            }
            circleEt.setText("")
            mPresenter.addComment(id, content)
        })

        dianzanLayout.setOnClickListener {
            if (isLike) {
                mPresenter.cancelLikeNews(id)
            } else {
                mPresenter.addLikeNews(id)
            }
        }

        //分享
        shareLayout.setOnClickListener {
            val sharePopupWindow = SharePopupWindow(this)
            sharePopupWindow.setISharePopupWindowClickListener(object : SharePopupWindow.ISharePopupWindowClickListener {
                override fun onWxClick() {
                    shareToWX()
                    sharePopupWindow.dismiss()
                }

                override fun onWxFriendClick() {
                    shareToWXFriend()
                    sharePopupWindow.dismiss()
                }

                override fun onQQClick() {
                    shareToQQ()
                    sharePopupWindow.dismiss()
                }

                override fun onWBClick() {
                    shareToWB()
                    sharePopupWindow.dismiss()
                }

            })
            sharePopupWindow.showAtScreenBottom(shareLayout)
        }

        btnShareQQ.setOnClickListener {
            shareToQQ()
        }

        btnShareWB.setOnClickListener {
            shareToWB()
        }

        btnShareWX.setOnClickListener {
            shareToWX()
        }

        btnShareWXFriend.setOnClickListener {
            shareToWXFriend()
        }

        mPresenter.getDetailsById(id)

        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                mPresenter.getScoreByRead(id)
            }
        }

        when (type) {
            "视讯", "视频" -> {
                mPresenter.getScoreByRead(id)
            }

            "文章", "问政", "悦读", "悦听", "公告", "专栏", "党建" -> {
                timer!!.schedule(timerTask, 15 * 1000)
            }

            "电视" -> {
                timer!!.schedule(timerTask, 6 * 60 * 1000)
            }

        }

    }

    override fun setDetails(item: NewsBean) {
        shareTitle = item.name
        //收藏状态
        hasCollect = item.collect_status == 1

        when (type) {
            "视频", "图文" -> {
                if (!StringUtils.isEmpty(item.avatar.trim { it <= ' ' })) {
                    Glide.with(this).load(item.avatar)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.color.bg_no_photo)
                            .transform(GlideCircleTransform())
                            .into(headIv)
                }

                nameTv.text = item.nickname
                timeTv.text = item.begintime


                if (!TextUtils.isEmpty(item.name)) {
                    contentTv.text = UrlUtils.formatUrlString(item.name)
                }
                contentTv.visibility = if (TextUtils.isEmpty(item.name)) View.GONE else View.VISIBLE

                if (item.images != null && item.images.size > 0) {
                    //上传的是图片
                    ivVideo.visibility = View.GONE

                    multiImagView.visibility = View.VISIBLE
                    multiImagView.setList(item.images)
                    multiImagView.setOnItemClickListener { view, position ->
                        //imagesize是作为loading时的图片size
                        val imageSize = ImagePagerActivity.ImageSize(view.measuredWidth, view.measuredHeight)
                        ImagePagerActivity.startImagePagerActivity(this, item.images, position, imageSize)
                    }

                } else {
                    //上传的是视频

                    multiImagView.visibility = View.GONE

                    ivVideo.visibility = View.VISIBLE
                    ivVideo.setBackgroundColor(Color.parseColor("#abb1b6"))
                    ivVideo.setOnClickListener {
                        val intent = Intent(this, VodActivity::class.java)
                        //                    intent.putExtra("media_type", "livestream")
                        intent.putExtra("videoPath", item.video)
                        this.startActivity(intent)
                    }

                    Glide.with(this).load(item.image)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.color.bg_no_photo)
                            .into(object : SimpleTarget<Drawable>() {
                                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                                    ivVideo.background = resource
                                }
                            })

                }

            }

            "电视" ->{
                setPositiveOnClickListener {
                    if (hasCollect) {
                        mPresenter.setCollectStatus(id, false)
                    } else {
                        mPresenter.setCollectStatus(id, true)
                    }
                }

                if (hasCollect) {
                    commonTitleBar.rightImageButton.setImageDrawable(resources.getDrawable(R.mipmap.icon_collect))
                } else {
                    commonTitleBar.rightImageButton.setImageDrawable(resources.getDrawable(R.mipmap.icon_collect_normal))
                }

                ivVideo.visibility = View.VISIBLE
                ivVideo.setBackgroundColor(Color.parseColor("#abb1b6"))
                ivVideo.setOnClickListener {
                    val intent = Intent(this, VodActivity::class.java)
                    intent.putExtra("videoPath", item.video)
                    this.startActivity(intent)
                }

                Glide.with(this).load(item.image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.color.bg_no_photo)
                        .into(object : SimpleTarget<Drawable>() {
                            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                                ivVideo.background = resource
                            }
                        })

                tvTitle.text = item.name
                tvNickName.text = item.nickname
                tvBeginTime.text = item.begintime
                tvSeeNum.text = "" + item.visit_num

                loadHtml(item.content)

            }

            "视讯" -> {
                ivVideo.visibility = View.VISIBLE
                ivVideo.setBackgroundColor(Color.parseColor("#abb1b6"))
                ivVideo.setOnClickListener {
                    val intent = Intent(this, VodActivity::class.java)
                    intent.putExtra("videoPath", item.video)
                    this.startActivity(intent)
                }

                Glide.with(this).load(item.image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.color.bg_no_photo)
                        .into(object : SimpleTarget<Drawable>() {
                            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                                ivVideo.background = resource
                            }
                        })

                tvTitle.text = item.name
                tvNickName.text = item.nickname
                tvBeginTime.text = item.begintime
                tvSeeNum.text = "" + item.visit_num

                loadHtml(item.content)

            }

            "公告" -> {
                tvTitle.text = item.name
                tvNickName.text = item.nickname
                tvBeginTime.text = item.begintime
                tvSeeNum.text = "" + item.visit_num

                loadHtml(item.content)
            }

            "悦读", "文章" -> {
                setPositiveOnClickListener {
                    if (hasCollect) {
                        mPresenter.setCollectStatus(id, false)
                    } else {
                        mPresenter.setCollectStatus(id, true)
                    }
                }

                if (hasCollect) {
                    commonTitleBar.rightImageButton.setImageDrawable(resources.getDrawable(R.mipmap.icon_collect))
                } else {
                    commonTitleBar.rightImageButton.setImageDrawable(resources.getDrawable(R.mipmap.icon_collect_normal))
                }

                tvTitle.text = item.name
                tvNickName.text = item.nickname
                tvBeginTime.text = item.begintime
                tvSeeNum.text = "" + item.visit_num

                loadHtml(item.content)
            }

            "问政" -> {

                tvTitle.text = item.name
                tvNickName.text = item.nickname
                tvBeginTime.text = item.begintime
                tvSeeNum.text = "" + item.visit_num

                loadHtml(item.content)

                if (item.images != null && item.images.size > 0) {
                    multiImagView.visibility = View.VISIBLE
                    multiImagView.setList(item.images)
                    multiImagView.setOnItemClickListener { view, position ->
                        //imagesize是作为loading时的图片size
                        val imageSize = ImagePagerActivity.ImageSize(view.measuredWidth, view.measuredHeight)
                        ImagePagerActivity.startImagePagerActivity(this, item.images, position, imageSize)
                    }
                }

            }

            "悦听" -> {
                setPositiveOnClickListener {
                    if (hasCollect) {
                        mPresenter.setCollectStatus(id, false)
                    } else {
                        mPresenter.setCollectStatus(id, true)
                    }
                }

                if (hasCollect) {
                    commonTitleBar.rightImageButton.setImageDrawable(resources.getDrawable(R.mipmap.icon_collect))
                } else {
                    commonTitleBar.rightImageButton.setImageDrawable(resources.getDrawable(R.mipmap.icon_collect_normal))
                }

                tvTitle.text = item.name
                tvNickName.text = item.nickname
                tvBeginTime.text = item.begintime
                tvSeeNum.text = "" + item.visit_num
                tvYueTingTime.text = item.time

                mAudioControl!!.setOnAudioControlListener(object : AudioController.AudioControlListener {
                    override fun setCurPositionTime(position: Int, curPositionTime: Long) {
                    }

                    override fun setDurationTime(position: Int, durationTime: Long) {
                    }

                    override fun setBufferedPositionTime(position: Int, bufferedPosition: Long) {
                    }

                    override fun setCurTimeString(position: Int, curTimeString: String?) {
                        tvYueTingTime.text = curTimeString
                    }

                    override fun isPlay(position: Int, isPlay: Boolean) {
                        if (isPlay) {
                            ivYueTingPlay.visibility = View.GONE
                            ivYueTingPause.visibility = View.VISIBLE
                        } else {
                            ivYueTingPlay.visibility = View.VISIBLE
                            ivYueTingPause.visibility = View.GONE
                        }
                    }

                    override fun setDurationTimeString(position: Int, durationTimeString: String?) {
                    }

                })

                if (item.playStatus) {
                    ivYueTingPlay.visibility = View.GONE
                    ivYueTingPause.visibility = View.VISIBLE
                } else {
                    ivYueTingPlay.visibility = View.VISIBLE
                    ivYueTingPause.visibility = View.GONE
                }

                ivYueTingPlay.setOnClickListener(View.OnClickListener {
                    mAudioControl!!.onPrepare(item.audio)
                    mAudioControl!!.onStart(0)
                })

                ivYueTingPause.setOnClickListener(View.OnClickListener {
                    mAudioControl!!.onPause()
                })

                loadHtml(item.content)
            }

        }


        //投票活动
        if (item.vote_id != 0) {
            llTouPiao.visibility = View.VISIBLE
            tvVoteName.text = item.vote.name


            if (item.vote_status == 0) {
                //未投票
                item.vote_option.forEach { voteBean ->
                    val optionView = View.inflate(this, R.layout.item_vote_option_button, null)
                    val voteOptionButton = optionView.findViewById<VoteLoadButton>(R.id.voteOptionButton)
                    voteOptionButton.text = voteBean.name
                    voteOptionButton.setOnClickListener {
                        mPresenter.sendVote(item.vote_id, voteBean.id)
                    }
                    voteOptionContainer.addView(optionView)
                }


            } else {
                //已投票
                var totalCount = 0
                item.vote_option.forEach {
                    totalCount += it.count
                }

                item.vote_option.forEach { voteBean ->
                    val optionView = View.inflate(this, R.layout.item_vote_option_button, null)
                    val voteOptionButton = optionView.findViewById<VoteLoadButton>(R.id.voteOptionButton)
                    voteOptionButton.text = voteBean.name
                    if (voteBean.check == 0) {
                        voteOptionButton.setState(VoteLoadButton.STATE_USED)
                    } else {
                        voteOptionButton.setState(VoteLoadButton.STATE_DOWNLOADING)
                    }

                    voteOptionButton.setDownLoadProgress(voteBean.count * 100 / totalCount, voteBean.count)
                    voteOptionContainer.addView(optionView)
                }

            }

        } else {
            llTouPiao.visibility = View.GONE
        }


        val drawables = tvGoodStatus.compoundDrawables

        isLike = item.like_status == 1

        if (item.like_status == 1) {
            tvGoodStatus.text = "已赞"
            tvGoodStatus.setTextColor(resources.getColor(R.color.colorAccent))
            val redGoodDrawable = mContext.resources.getDrawable(R.mipmap.icon_good_select)
            redGoodDrawable.bounds = drawables[0].getBounds()
            tvGoodStatus.setCompoundDrawables(redGoodDrawable, drawables[1], drawables[2], drawables[3])
        } else {
            tvGoodStatus.text = "赞"
            tvGoodStatus.setTextColor(resources.getColor(R.color.default_gray_text_color))
            val grayGoodDrawable = mContext.resources.getDrawable(R.mipmap.icon_details_dianzan)
            grayGoodDrawable.bounds = drawables[0].getBounds()
            tvGoodStatus.setCompoundDrawables(grayGoodDrawable, drawables[1], drawables[2], drawables[3])
        }


        tvCommentNum.text = "评论  ${item.comment_num}"
        tvGoodNum.text = "赞  ${item.like_num}"

        this.like_num = item.like_num

        commentContainer.removeAllViews()
        //评论列表
        item.comment.forEach {
            val commentView = View.inflate(this, R.layout.item_news_comment_view, null)
            val ivAvatar = commentView.findViewById<ImageView>(R.id.ivAvatar)
            val tvNickname = commentView.findViewById<TextView>(R.id.tvNickName)
            val tvTime = commentView.findViewById<TextView>(R.id.tvTime)
            val tvContent = commentView.findViewById<TextView>(R.id.tvContent)
            val tvTag = commentView.findViewById<TextView>(R.id.tvTag)

            if (it.admin_status == 1) {
                tvTag.visibility = View.VISIBLE
            } else {
                tvTag.visibility = View.INVISIBLE
            }

            if (it.avatar.trim().isNotEmpty()) {
                GlideManager.loadRoundImg(it.avatar, ivAvatar, 5f)
            }

            tvNickname.text = it.nickname
            tvTime.text = "" + it.createtime
            tvContent.text = it.detail

            commentContainer.addView(commentView)
        }

    }

    override fun sendVoteSuccess(optionModelList: MutableList<VoteOptionModel>) {
        voteOptionContainer.removeAllViews()

        var totalCount = 0
        optionModelList.forEach {
            totalCount += it.count
        }

        optionModelList.forEach { voteBean ->
            val optionView = View.inflate(this, R.layout.item_vote_option_button, null)
            val voteOptionButton = optionView.findViewById<VoteLoadButton>(R.id.voteOptionButton)
            voteOptionButton.text = voteBean.name
            if (voteBean.check == 0) {
                voteOptionButton.setState(VoteLoadButton.STATE_USED)
            } else {
                voteOptionButton.setState(VoteLoadButton.STATE_DOWNLOADING)
            }

            voteOptionButton.setDownLoadProgress(voteBean.count * 100 / totalCount, voteBean.count)
            voteOptionContainer.addView(optionView)
        }

    }

    override fun updateLikeStatus(isLike: Boolean) {
        this.isLike = isLike

        val drawables = tvGoodStatus.compoundDrawables

        if (isLike) {
            tvGoodStatus.text = "已赞"
            tvGoodStatus.setTextColor(resources.getColor(R.color.colorAccent))
            val redGoodDrawable = mContext.resources.getDrawable(R.mipmap.icon_good_select)
            redGoodDrawable.bounds = drawables[0].getBounds()
            tvGoodStatus.setCompoundDrawables(redGoodDrawable, drawables[1], drawables[2], drawables[3])

            like_num++
            tvGoodNum.text = "赞  $like_num"

        } else {

            tvGoodStatus.text = "赞"
            tvGoodStatus.setTextColor(resources.getColor(R.color.default_gray_text_color))
            val grayGoodDrawable = mContext.resources.getDrawable(R.mipmap.icon_details_dianzan)
            grayGoodDrawable.bounds = drawables[0].getBounds()
            tvGoodStatus.setCompoundDrawables(grayGoodDrawable, drawables[1], drawables[2], drawables[3])

            like_num--
            tvGoodNum.text = "赞  $like_num"
        }

    }

    override fun addCommentSuccess() {
        updateEditTextBodyVisible(View.GONE)

        mPresenter.getDetailsById(id)
    }

    override fun setCollectStatusSuccess(isCollect: Boolean) {
        this.hasCollect = isCollect

        if (hasCollect) {
            commonTitleBar.rightImageButton.setImageDrawable(resources.getDrawable(R.mipmap.icon_collect))
        } else {
            commonTitleBar.rightImageButton.setImageDrawable(resources.getDrawable(R.mipmap.icon_collect_normal))
        }

    }

    override fun setTitleContent(title: String) {
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        if (ev!!.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideInput(v, ev)) {//点击的是其他区域，则调用系统方法隐藏软键盘
                hideSoftInput(v.windowToken)
            }
            return super.dispatchTouchEvent(ev)
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return if (window.superDispatchTouchEvent(ev)) {
            true
        } else onTouchEvent(ev)
    }

    /**
     * 判断是否是输入框区域
     */
    private fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null) {
            when (v.id) {
                R.id.circleEt -> {
                    val leftTop = intArrayOf(0, 0)
                    //获取输入框当前的location位置
                    v.getLocationInWindow(leftTop)
                    val left = leftTop[0]
                    val top = leftTop[1]
                    val bottom = top + v.height
                    val right = ScreenUtils.getAppScreenWidth()
                    return !(event.x > left && event.x < right
                            && event.y > top && event.y < bottom)
                }
                else -> {
                    return false
                }
            }
        }
        return false
    }

    private fun updateEditTextBodyVisible(visibility: Int) {
        editTextBodyLl.visibility = visibility

        if (View.VISIBLE == visibility) {
            circleEt.requestFocus()
            //弹出键盘
            showSoftInput(circleEt)

        } else if (View.GONE == visibility) {
            //隐藏键盘
            hideSoftInput(circleEt.windowToken)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mAudioControl?.release()
        mAudioControl = null
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (shareHandler != null) {
            shareHandler!!.doResultIntent(intent, object : WbShareCallback {
                override fun onWbShareFail() {
                    ToastUtils.showShort("分享失败")
                }

                override fun onWbShareCancel() {
                    ToastUtils.showShort("取消分享")
                }

                override fun onWbShareSuccess() {
                    mPresenter.getScoreByShare(id)
                    ToastUtils.showShort("分享成功")
                }

            })
        }
    }

    private fun shareToQQ() {
        val mTencent = Tencent.createInstance(Contacts.QQ_APPID, this)
        val params = Bundle()
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
        params.putString(QQShare.SHARE_TO_QQ_TITLE, shareTitle)
//        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "摘要") //可选，最长40个字
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, Contacts.SHARE_BASE_URL + id) //必填 	这条分享消息被好友点击后的跳转URL。
//        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://avatar.csdn.net/C/3/D/1_u013451048.jpg") // 可选 分享图片的URL或者本地路径
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "濉溪发布")
        mTencent.shareToQQ(this, params, object : IUiListener {
            override fun onComplete(p0: Any?) {
                mPresenter.getScoreByShare(id)
                ToastUtils.showShort("分享成功")
            }

            override fun onCancel() {
                ToastUtils.showShort("取消分享")
            }

            override fun onError(p0: UiError?) {
                ToastUtils.showShort("分享失败：" + p0?.errorMessage)
            }

        })
    }

    private fun shareToWX() {
        if (!isWeiXinAppInstall()) {
            return
        }
        if (!isWXAppSupportAPI()) {
            ToastUtils.showShort("您的微信版本不支持分享功能")
            return
        }

        val webpage = WXWebpageObject()
        webpage.webpageUrl = Contacts.SHARE_BASE_URL + id
        val msg = WXMediaMessage(webpage)
        msg.title = shareTitle
        msg.description = "濉溪发布"
        val bmp = BitmapFactory.decodeResource(resources, R.mipmap.default_avatar)
        val thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true)
        bmp.recycle()
        msg.thumbData = ImageUtils.bitmap2Bytes(thumbBmp, Bitmap.CompressFormat.PNG)
        val req = SendMessageToWX.Req()
        req.transaction = "webpage" + System.currentTimeMillis()
        req.message = msg
        req.scene = SendMessageToWX.Req.WXSceneSession
        wxapi!!.sendReq(req)

        InterfaceUtils.getInstance().add {
            mPresenter.getScoreByShare(id)
            InterfaceUtils.getInstance().remove(this)
        }

    }

    private fun shareToWXFriend() {
        if (!isWeiXinAppInstall()) {
            return
        }
        if (!isWXAppSupportAPI()) {
            ToastUtils.showShort("您的微信版本不支持分享功能")
            return
        }

        val webpage = WXWebpageObject()
        webpage.webpageUrl = Contacts.SHARE_BASE_URL + id
        val msg = WXMediaMessage(webpage)
        msg.title = shareTitle
        msg.description = "濉溪发布"
        val bmp = BitmapFactory.decodeResource(resources, R.mipmap.default_avatar)
        val thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true)
        bmp.recycle()
        msg.thumbData = ImageUtils.bitmap2Bytes(thumbBmp, Bitmap.CompressFormat.PNG)
        val req = SendMessageToWX.Req()
        req.transaction = "webpage" + System.currentTimeMillis()
        req.message = msg
        req.scene = SendMessageToWX.Req.WXSceneTimeline
        wxapi!!.sendReq(req)

        InterfaceUtils.getInstance().add {
            mPresenter.getScoreByShare(id)
            InterfaceUtils.getInstance().remove(this)
        }
    }

    private var shareHandler: WbShareHandler? = null
    private fun shareToWB() {
        shareHandler = WbShareHandler(this)
        shareHandler!!.registerApp()

        val weiboMessage = WeiboMultiMessage()

        val mediaObject = WebpageObject()
        mediaObject.identify = Utility.generateGUID()
        mediaObject.title = shareTitle
        mediaObject.description = "濉溪发布"
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.default_avatar)
        mediaObject.setThumbImage(bitmap)
        mediaObject.actionUrl = Contacts.SHARE_BASE_URL + id
        mediaObject.defaultText = "Webpage"
        weiboMessage.mediaObject = mediaObject

        shareHandler!!.shareMessage(weiboMessage, false)
    }


    private var wxapi: IWXAPI? = null
    /**
     * 判断是否安装微信
     */
    fun isWeiXinAppInstall(): Boolean {
        if (wxapi == null)
            wxapi = WXAPIFactory.createWXAPI(this, Contacts.WX_APPID)
        if (wxapi!!.isWXAppInstalled) {
            return true
        } else {
            ToastUtils.showShort("您未安装微信客户端")
            return false
        }
    }


    /**
     * 是否支持分享到朋友圈
     */
    fun isWXAppSupportAPI(): Boolean {
        if (isWeiXinAppInstall()) {
            val wxSdkVersion = wxapi!!.wxAppSupportAPI
            return wxSdkVersion >= 0x21020001
        } else {
            return false
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            release()
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun release() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }

}
