package cn.tklvyou.mediaconvergence.ui.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.model.NewsMultipleItem
import cn.tklvyou.mediaconvergence.base.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import cn.tklvyou.mediaconvergence.model.Channel
import cn.tklvyou.mediaconvergence.ui.adapter.ChannelPagerAdapter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.trello.rxlifecycle3.components.support.RxFragment
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator


class HomeFragment : BaseFragment<HomePresenter>(), HomeContract.View {

    override fun initPresenter(): HomePresenter {
        return HomePresenter()
    }

    override fun getFragmentLayoutID(): Int {
        return R.layout.fragment_home
    }

    private var mSelectedChannels = ArrayList<String>()
    private val mUnSelectedChannels = ArrayList<Channel>()
    private val mChannelFragments = ArrayList<RxFragment>()
    private var mChannelPagerAdapter: ChannelPagerAdapter? = null

    private lateinit var commonNavigator: CommonNavigator
    private var selectList: List<LocalMedia> = ArrayList()

    override fun initView() {
        homeTitleBar.setBackgroundResource(R.drawable.shape_gradient_common_titlebar)
        homeTitleBar.rightCustomView.setOnClickListener {
            // 进入相册 以下是例子：不需要的api可以不写
            PictureSelector.create(activity)
                    .openGallery(PictureMimeType.ofVideo())
                    .theme(R.style.picture_default_style)
                    .maxSelectNum(1)
                    .minSelectNum(1)
                    .selectionMode(PictureConfig.SINGLE)
                    .previewImage(true)
                    .isCamera(true)
                    .enableCrop(false)
                    .compress(true)
                    .previewEggs(true)
                    .openClickSound(false)
                    .selectionMedia(selectList)
                    .forResult(PictureConfig.CHOOSE_REQUEST)

        }

        initMagicIndicator()
        mPresenter.getHomeChannel()
    }

    override fun lazyData() {
        ToastUtils.showShort("dddd")

    }

    private fun initMagicIndicator(){
        commonNavigator = CommonNavigator(context)
        commonNavigator.isSkimOver = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return mSelectedChannels.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val clipPagerTitleView = ClipPagerTitleView(context)
                clipPagerTitleView.text = mSelectedChannels[index]
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


    override fun setHomeChannel(channelList: MutableList<String>?) {
        mSelectedChannels = channelList as ArrayList<String>
        commonNavigator.notifyDataSetChanged()

        initChannelFragments()

        mChannelPagerAdapter = ChannelPagerAdapter(mChannelFragments, childFragmentManager)
        mViewPager.adapter = mChannelPagerAdapter
        mViewPager.offscreenPageLimit = mSelectedChannels.size



    }


    /**
     * 初始化已选频道的fragment的集合
     */
    private fun initChannelFragments() {
        LogUtils.e("initChannelFragments")
        for (channel in mSelectedChannels) {
            val newsFragment = NewsListFragment()
            val bundle = Bundle()
            bundle.putInt("type", NewsMultipleItem.VIDEO)
            newsFragment.arguments = bundle
            mChannelFragments.add(newsFragment)//添加到集合中
        }
    }


}