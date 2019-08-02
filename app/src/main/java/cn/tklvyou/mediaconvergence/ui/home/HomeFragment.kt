package cn.tklvyou.mediaconvergence.ui.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.model.NewsMultipleItem
import cn.tklvyou.mediaconvergence.base.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import cn.tklvyou.mediaconvergence.model.Channel
import cn.tklvyou.mediaconvergence.ui.adapter.ChannelPagerAdapter
import cn.tklvyou.mediaconvergence.ui.video_edit.VideoEditActivity
import cn.tklvyou.mediaconvergence.utils.JSON
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle3.components.support.RxFragment
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import java.io.Serializable


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

    override fun initView() {
        homeTitleBar.setBackgroundResource(R.drawable.shape_gradient_common_titlebar)
        homeTitleBar.rightCustomView.setOnClickListener {
            RxPermissions(this)
                    .request(Manifest.permission.CAMERA)
                    .subscribe { granted ->
                        if (granted) { // Always true pre-M
                            PictureSelector.create(this)
                                    .openCamera(PictureMimeType.ofVideo())
                                    .recordVideoSecond(60)
                                    .forResult(PictureConfig.CHOOSE_REQUEST)
                        } else {
                            ToastUtils.showShort("权限拒绝，无法使用")
                        }
                    }
        }

        initMagicIndicator()
        mPresenter.getHomeChannel()
    }

    override fun lazyData() {
        ToastUtils.showShort("dddd")

    }

    private fun initMagicIndicator() {
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
        for ((index, item) in mSelectedChannels.withIndex()) {
            val newsFragment = NewsListFragment()
            val bundle = Bundle()
            when (index) {
                0 -> {
                    bundle.putInt("type", NewsMultipleItem.VIDEO)
                }
                1 -> {
                    bundle.putInt("type", NewsMultipleItem.TV)
                }
                2 -> {
                    bundle.putInt("type", NewsMultipleItem.NEWS)
                }
                3 -> {
                    bundle.putInt("type", NewsMultipleItem.SHI_XUN)
                }
                4 -> {
                    bundle.putInt("type", NewsMultipleItem.WEN_ZHENG)
                }
                5 -> {
                    bundle.putInt("type", NewsMultipleItem.JU_ZHENG)
                }
                6 -> {
                    bundle.putInt("type", NewsMultipleItem.WECHAT_MOMENTS)
                }
                7 -> {
                    bundle.putInt("type", NewsMultipleItem.READING)
                }
//                8 -> {
//                    bundle.putInt("type", NewsMultipleItem.LISTEN)
//                }
                else -> {
                    bundle.putInt("type", NewsMultipleItem.VIDEO)
                }
            }

            newsFragment.arguments = bundle
            mChannelFragments.add(newsFragment)//添加到集合中
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST && data != null) {
                // 图片、视频、音频选择结果回调
                val selectList = PictureSelector.obtainMultipleResult(data)
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                val intent = Intent(context, VideoEditActivity::class.java)
                intent.putExtra("page", "V视")
                intent.putExtra("data", selectList as Serializable)
                startActivity(intent)

            }
        }

    }


}