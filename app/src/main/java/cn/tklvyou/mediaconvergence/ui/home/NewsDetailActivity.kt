package cn.tklvyou.mediaconvergence.ui.home

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
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.activity.BaseActivity
import cn.tklvyou.mediaconvergence.model.NewsBean
import cn.tklvyou.mediaconvergence.ui.video_player.VodActivity
import cn.tklvyou.mediaconvergence.utils.GlideCircleTransform
import cn.tklvyou.mediaconvergence.utils.UrlUtils
import com.blankj.utilcode.util.*
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_news_detail.*

/**
 * Created by yiw on 2016/1/6.
 */
class NewsDetailActivity : BaseActivity<NewsDetailPresenter>(), NewsDetailContract.View {

    companion object {

        private val INTENT_TYPE = "type"
        private val INTENT_ID = "id"

        val TYPE_VIDEO = 1
        val TYPE_PICTURE = 2
        val TYPE_ARTICLE = 3

        fun startNewsDetailActivity(context: Context, type: Int, id: Int) {
            val intent = Intent(context, NewsDetailActivity::class.java)
            intent.putExtra(INTENT_TYPE, type)
            intent.putExtra(INTENT_ID, id)
            context.startActivity(intent)
        }
    }

    private var id: Int = 0
    private var type: Int = 0
    private var titleBarTitle: String? = null


    override fun initPresenter(): NewsDetailPresenter {
        return NewsDetailPresenter()
    }

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_news_detail
    }

    private var isLike = false

    override fun initView() {
        getIntentData()

        setTitle(titleBarTitle)
        setNavigationImage()
        setNavigationOnClickListener { v -> finish() }


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

            mPresenter.addComment(id,content)
        })

        dianzanLayout.setOnClickListener {
            if(isLike) {
                mPresenter.cancelLikeNews(id)
            }else{
                mPresenter.addLikeNews(id)
            }
        }

        mPresenter.getDetailsById(id)
    }

    private fun getIntentData() {
        id = intent.getIntExtra(INTENT_ID, 0)
        type = intent.getIntExtra(INTENT_TYPE, 0)

        when (type) {
            TYPE_VIDEO -> titleBarTitle = "视频"
            TYPE_PICTURE -> titleBarTitle = "图文"
            TYPE_ARTICLE -> titleBarTitle = "文章"
            else -> titleBarTitle = "详情"
        }

    }

    override fun setDetails(item: NewsBean) {
        if (!StringUtils.isEmpty(item.avatar.trim { it <= ' ' })) {
            Glide.with(this).load(item.avatar)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.color.bg_no_photo)
                    .transform(GlideCircleTransform())
                    .into(headIv)
        }

        nameTv.text = item.nickname
        timeTv.text = item.begintime

        //收藏状态
        val hasCollect = item.collect_status == 1

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



        if (!TextUtils.isEmpty(item.name)) {
            contentTv.isExpand = item.isExpand
            contentTv.setExpandStatusListener { isExpand -> item.isExpand = isExpand }

            contentTv.setText(UrlUtils.formatUrlString(item.name))
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


        //评论列表
        item.comment.forEach {
            val commentView = View.inflate(this,R.layout.item_news_comment_view,null)
            val ivAvatar = commentView.findViewById<ImageView>(R.id.ivAvatar)
            val tvNickname = commentView.findViewById<TextView>(R.id.tvNickName)
            val tvTime = commentView.findViewById<TextView>(R.id.tvTime)
            val tvContent = commentView.findViewById<TextView>(R.id.tvContent)

            Glide.with(this).load(it.avatar).into(ivAvatar)
            tvNickname.text = it.nickname
            tvTime.text = ""+it.createtime
            tvContent.text = it.detail

            commentContainer.addView(commentView)
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
        } else {
            dianzanDrawable.colorFilter = PorterDuffColorFilter(Color.parseColor("#AAAAAA"), PorterDuff.Mode.SRC_ATOP)
            tvGoodStatus.text = "赞"
            tvGoodStatus.setTextColor(resources.getColor(R.color.default_gray_text_color))
        }

    }

    override fun addCommentSuccess() {
        updateEditTextBodyVisible(View.GONE)
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


}
