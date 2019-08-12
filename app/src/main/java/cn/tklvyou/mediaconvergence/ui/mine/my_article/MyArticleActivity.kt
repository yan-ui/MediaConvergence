package cn.tklvyou.mediaconvergence.ui.mine.my_article

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.NullPresenter
import cn.tklvyou.mediaconvergence.base.activity.BaseTitleActivity
import cn.tklvyou.mediaconvergence.ui.adapter.ChannelPagerAdapter
import cn.tklvyou.mediaconvergence.utils.CommonUtil
import cn.tklvyou.mediaconvergence.utils.SizeUtil
import com.trello.rxlifecycle3.components.support.RxFragment
import com.wuhenzhizao.titlebar.widget.CommonTitleBar
import kotlinx.android.synthetic.main.activity_my_article.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView

/**
 *@description :我的帖子
 *@company :途酷科技
 * @author :JenkinsZhou
 * @date 2019年08月05日10:16
 * @Email: 971613168@qq.com
 */
class MyArticleActivity : BaseTitleActivity<NullPresenter>() {
    private val mFragments = ArrayList<RxFragment>()
    private var mTabNameList = ArrayList<String>()
    private lateinit var commonNavigator: CommonNavigator
    private var mChannelPagerAdapter: ChannelPagerAdapter? = null
    override fun initPresenter(): NullPresenter {
        return NullPresenter()
    }


    override fun setTitleBar(titleBar: CommonTitleBar?) {
        titleBar?.setMainTitle("我的帖子")
    }


    override fun getActivityLayoutID(): Int {
        return R.layout.activity_my_article
    }


    override fun initView(savedInstanceState: Bundle?) {
        mTabNameList.add("拍客")
        mTabNameList.add("V视")
        mFragments.add(MyCameraFragment())
        mFragments.add(MyVideoFragment())
        initMagicIndicator()
        mChannelPagerAdapter = ChannelPagerAdapter(mFragments, fragmentManager)
        articleViewPager.adapter = mChannelPagerAdapter
        articleViewPager.offscreenPageLimit = mTabNameList.size
        commonNavigator.notifyDataSetChanged()
    }


    private fun initMagicIndicator() {
        commonNavigator = CommonNavigator(this)
        commonNavigator.isSkimOver = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return mTabNameList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val clipPagerTitleView = ClipPagerTitleView(context)
                clipPagerTitleView.background = CommonUtil.getDrawable(R.color.redFF4A5C)
                clipPagerTitleView.text = mTabNameList[index]
                clipPagerTitleView.textSize = SizeUtil.sp2px(13f)
                clipPagerTitleView.textColor = Color.parseColor("#888888")
                clipPagerTitleView.clipColor = CommonUtil.getColor(R.color.white)
                clipPagerTitleView.setOnClickListener { articleViewPager.setCurrentItem(index, false) }
                return clipPagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                return null
            }
        }
        commonNavigator.isAdjustMode = true
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, articleViewPager)

    }


}