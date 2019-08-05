package cn.tklvyou.mediaconvergence.ui.home.all_tv

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.NullPresenter
import cn.tklvyou.mediaconvergence.base.activity.BaseActivity
import cn.tklvyou.mediaconvergence.ui.adapter.MyFragmentPagerAdapter
import com.trello.rxlifecycle3.components.support.RxFragment
import kotlinx.android.synthetic.main.activity_all_tv.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView

class AllTvActivity : BaseActivity<NullPresenter>() {

    override fun initPresenter(): NullPresenter {
        return NullPresenter()
    }

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_all_tv
    }

    private lateinit var commonNavigator: CommonNavigator
    private var mChannels = ArrayList<String>()
    private var mChannelFragments = ArrayList<RxFragment>()
    private var mChannelPagerAdapter:MyFragmentPagerAdapter? = null


    private var position = 0
    override fun initView() {
        setTitle("全部视频")
        setNavigationImage()
        setNavigationOnClickListener { finish() }

        position = intent.getIntExtra("position",0)

        mChannels.add("濉溪新闻")
        mChannels.add("乡音乡事")
        mChannels.add("政务直通车")
        mChannels.add("聚焦问政")
        mChannels.add("旗帜")
        mChannels.add("法制栏目")

        initMagicIndicator()

        initViewPagerFragment()
    }

    private fun initViewPagerFragment() {
        for ((index, item) in mChannels.withIndex()) {
            val newsFragment = TVFragment()
            val bundle = Bundle()
            bundle.putString("type", item)

            newsFragment.arguments = bundle
            mChannelFragments.add(newsFragment)//添加到集合中
        }

        mChannelPagerAdapter = MyFragmentPagerAdapter(fragmentManager,mChannelFragments)
        mViewPager.adapter = mChannelPagerAdapter
        mViewPager.offscreenPageLimit = mChannels.size
        mViewPager.setCurrentItem(position,false)
    }

    private fun initMagicIndicator() {
        commonNavigator = CommonNavigator(this)
        commonNavigator.isSkimOver = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return mChannels.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val clipPagerTitleView = ClipPagerTitleView(context)
                clipPagerTitleView.text = mChannels[index]
                clipPagerTitleView.textColor = Color.parseColor("#888888")
                clipPagerTitleView.clipColor = context.resources.getColor(R.color.colorAccent)
                clipPagerTitleView.setOnClickListener { mViewPager.setCurrentItem(index, false) }
                return clipPagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                return null
            }
        }
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, mViewPager)
    }


}
