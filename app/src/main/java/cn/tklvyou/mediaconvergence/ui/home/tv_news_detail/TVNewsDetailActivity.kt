package cn.tklvyou.mediaconvergence.ui.home.tv_news_detail

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.activity.BaseWebViewActivity
import cn.tklvyou.mediaconvergence.helper.GlideManager
import cn.tklvyou.mediaconvergence.model.NewsBean
import cn.tklvyou.mediaconvergence.ui.home.AudioController
import cn.tklvyou.mediaconvergence.ui.home.ImagePagerActivity
import cn.tklvyou.mediaconvergence.ui.home.news_detail.NewsDetailContract
import cn.tklvyou.mediaconvergence.ui.home.news_detail.NewsDetailPresenter
import cn.tklvyou.mediaconvergence.ui.video_player.LiveActivity
import cn.tklvyou.mediaconvergence.ui.video_player.VodActivity
import cn.tklvyou.mediaconvergence.utils.GlideCircleTransform
import cn.tklvyou.mediaconvergence.utils.UrlUtils
import com.blankj.utilcode.util.*
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_point_rule.*
import kotlinx.android.synthetic.main.activity_tv_news_detail.*

/**
 * Created by yiw on 2016/1/6.
 */
class TVNewsDetailActivity : BaseWebViewActivity<TVNewsDetailPresenter>(), TVNewsDetailContract.View {

    companion object {
        private val INTENT_TYPE = "type"
        private val INTENT_ID = "id"

        fun startTVNewsDetailActivity(context: Context, type: String, id: Int) {
            val intent = Intent(context, TVNewsDetailActivity::class.java)
            intent.putExtra(INTENT_ID, id)
            intent.putExtra(INTENT_TYPE, type)
            context.startActivity(intent)
        }
    }

    private var id: Int = 0
    private var type: String = ""


    override fun initPresenter(): TVNewsDetailPresenter {
        return TVNewsDetailPresenter()
    }

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_tv_news_detail
    }

    private var isLike = false
    private var hasCollect = false
    private var like_num = 0

    private var mAudioControl: AudioController? = null

    override fun initView() {
        getIntentData()

        setTitle(type)
        setNavigationImage()
        setNavigationOnClickListener { v -> finish() }

        initWebView(tvWebView)

        setPositiveImage(R.mipmap.icon_collect_normal)
        val drawables = tvSeeNum.compoundDrawables


        when (type) {
            "电视" -> {
                llFMLayout.visibility = View.GONE
                optionLayout.visibility = View.GONE

                val eyeDrawable = mContext.resources.getDrawable(R.mipmap.icon_eye)
                eyeDrawable.bounds = drawables[0].bounds
                tvSeeNum.setCompoundDrawables(eyeDrawable, drawables[1], drawables[2], drawables[3])


            }

            "广播" -> {
                llFMLayout.visibility = View.VISIBLE
                optionLayout.visibility = View.VISIBLE

                val audioDrawable = mContext.resources.getDrawable(R.mipmap.icon_audio_list)
                audioDrawable.bounds = drawables[0].bounds
                tvSeeNum.setCompoundDrawables(audioDrawable, drawables[1], drawables[2], drawables[3])

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

            mPresenter.addComment(id, content)
        })

        dianzanLayout.setOnClickListener {
            if (isLike) {
                mPresenter.cancelLikeNews(id)
            } else {
                mPresenter.addLikeNews(id)
            }
        }

        mPresenter.getDetailsById(id)
    }

    private fun getIntentData() {
        id = intent.getIntExtra(INTENT_ID, 0)
        type = intent.getStringExtra(INTENT_TYPE)
    }


    override fun setDetails(item: NewsBean) {
        //收藏状态
        hasCollect = item.collect_status == 1

        setPositiveOnClickListener {
            if (hasCollect) {
                mPresenter.setCollectStatus(id, false)
            } else {
                mPresenter.setCollectStatus(id, true)
            }
        }

        val collectDrawable = resources.getDrawable(R.mipmap.icon_collect_normal)
        if (hasCollect) {
            collectDrawable.colorFilter = PorterDuffColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP)
        } else {
            collectDrawable.colorFilter = PorterDuffColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP)
        }
        commonTitleBar.rightImageButton.setImageDrawable(collectDrawable)

        tvTvName.text = item.name
        tvSeeNum.text = "" + item.visit_num

        var imageUrl: String
        if(item.images == null || item.images.size == 0){
            imageUrl = item.image
        }else{
            imageUrl = item.images[0]
        }
        
        Glide.with(this).load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.bg_no_photo)
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        ivVideo.background = resource
                    }
                })

        when (type) {
            "电视" -> {
                ivVideo.setBackgroundColor(Color.parseColor("#abb1b6"))
                ivVideo.setOnClickListener {
                    val intent = Intent(this, LiveActivity::class.java)
                    intent.putExtra("videoPath", item.video)
                    this.startActivity(intent)
                }


                if(item.tel_list != null && item.tel_list.size == 3) {
                    llTVLayout.visibility = View.VISIBLE
                    rbYesterday.text = SpanUtils().appendLine("昨天").append(item.tel_list[0].date).create()
                    rbToday.text = SpanUtils().appendLine("今天").append(item.tel_list[1].date).create()
                    rbTomorrow.text = SpanUtils().appendLine("明天").append(item.tel_list[2].date).create()

                    loadHtml(item.tel_list[1].content)

                    rgTime.setOnCheckedChangeListener { group, checkedId ->
                        when(checkedId){
                            R.id.rbYesterday ->{
                                loadHtml(item.tel_list[0].content)
                            }

                            R.id.rbToday ->{
                                loadHtml(item.tel_list[1].content)
                            }

                            R.id.rbTomorrow ->{
                                loadHtml(item.tel_list[2].content)
                            }
                        }

                    }

                }else{
                    llTVLayout.visibility = View.GONE
                }


            }

            "广播" -> {

                val drawables = tvGoodStatus.compoundDrawables
                val dianzanDrawable = resources.getDrawable(R.mipmap.icon_details_dianzan)
                dianzanDrawable.bounds = drawables[0].bounds
                tvGoodStatus.setCompoundDrawables(dianzanDrawable, drawables[1], drawables[2], drawables[3])

                isLike = item.like_status == 1

                if (item.like_status == 1) {
                    //SDK > 23 可用
                    //        tvGoodNum.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#ff9708")));
                    dianzanDrawable.colorFilter = PorterDuffColorFilter(Color.parseColor("#FF4A5C"), PorterDuff.Mode.SRC_ATOP)
                    tvGoodStatus.text = "已赞"
                    tvGoodStatus.setTextColor(resources.getColor(R.color.colorAccent))
                } else {
                    dianzanDrawable.colorFilter = PorterDuffColorFilter(Color.parseColor("#AAAAAA"), PorterDuff.Mode.SRC_ATOP)
                    tvGoodStatus.text = "赞"
                    tvGoodStatus.setTextColor(resources.getColor(R.color.default_gray_text_color))
                }

                tvCommentNum.text = "评论  ${item.comment_num}"
                tvGoodNum.text = "赞  ${item.like_num}"

                this.like_num = item.like_num

                commentContainer.removeAllViews()
                ToastUtils.showShort(""+item.comment.size)
                //评论列表
                item.comment.forEach {
                    val commentView = View.inflate(this, R.layout.item_news_comment_view, null)
                    val ivAvatar = commentView.findViewById<ImageView>(R.id.ivAvatar)
                    val tvNickname = commentView.findViewById<TextView>(R.id.tvNickName)
                    val tvTime = commentView.findViewById<TextView>(R.id.tvTime)
                    val tvContent = commentView.findViewById<TextView>(R.id.tvContent)

                    if (it.avatar.trim().isNotEmpty()) {
                        GlideManager.loadRoundImg(it.avatar, ivAvatar, 5f)
                    }

                    tvNickname.text = it.nickname
                    tvTime.text = "" + it.createtime
                    tvContent.text = it.detail

                    commentContainer.addView(commentView)
                }


            }

        }

    }

    override fun updateLikeStatus(isLike: Boolean) {
        this.isLike = isLike

        val drawables = tvGoodStatus.compoundDrawables
        val dianzanDrawable = resources.getDrawable(R.mipmap.icon_details_dianzan)
        dianzanDrawable.bounds = drawables[0].bounds
        tvGoodStatus.setCompoundDrawables(dianzanDrawable, drawables[1], drawables[2], drawables[3])
        if (isLike) {
            //SDK > 23 可用
            //        tvGoodNum.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#ff9708")));
            dianzanDrawable.colorFilter = PorterDuffColorFilter(Color.parseColor("#FF4A5C"), PorterDuff.Mode.SRC_ATOP)
            tvGoodStatus.text = "已赞"
            tvGoodStatus.setTextColor(resources.getColor(R.color.colorAccent))
            like_num++
            tvGoodNum.text = "赞  $like_num"

        } else {
            dianzanDrawable.colorFilter = PorterDuffColorFilter(Color.parseColor("#AAAAAA"), PorterDuff.Mode.SRC_ATOP)
            tvGoodStatus.text = "赞"
            tvGoodStatus.setTextColor(resources.getColor(R.color.default_gray_text_color))
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

        val collectDrawable = resources.getDrawable(R.mipmap.icon_collect_normal)
        if (hasCollect) {
            collectDrawable.colorFilter = PorterDuffColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP)
        } else {
            collectDrawable.colorFilter = PorterDuffColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP)
        }
        commonTitleBar.rightImageButton.setImageDrawable(collectDrawable)
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

}
