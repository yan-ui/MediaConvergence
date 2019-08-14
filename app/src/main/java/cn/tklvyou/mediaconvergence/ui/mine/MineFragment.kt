package cn.tklvyou.mediaconvergence.ui.mine

import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.fragment.BaseRecyclerFragment
import cn.tklvyou.mediaconvergence.base.interfaces.AdapterCallBack
import cn.tklvyou.mediaconvergence.helper.GlideManager
import cn.tklvyou.mediaconvergence.model.MineRvModel
import cn.tklvyou.mediaconvergence.model.User
import cn.tklvyou.mediaconvergence.ui.account.data.PersonalDataActivity
import cn.tklvyou.mediaconvergence.ui.adapter.MineRvAdapter
import cn.tklvyou.mediaconvergence.ui.mine.browse.RecentBrowseActivity
import cn.tklvyou.mediaconvergence.ui.mine.collection.MyCollectActivity
import cn.tklvyou.mediaconvergence.ui.mine.exchange.MyExchangeRecordActivity
import cn.tklvyou.mediaconvergence.ui.mine.message.MyMessageActivity
import cn.tklvyou.mediaconvergence.ui.mine.my_article.MyArticleActivity
import cn.tklvyou.mediaconvergence.ui.mine.point.MyPointDetailActivity
import cn.tklvyou.mediaconvergence.ui.mine.wenzhen.MyWenZhenActivity
import cn.tklvyou.mediaconvergence.ui.setting.AboutUsActivity
import cn.tklvyou.mediaconvergence.ui.setting.SettingActivity
import cn.tklvyou.mediaconvergence.utils.GridDividerItemDecoration
import cn.tklvyou.mediaconvergence.utils.JSON
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ResourceUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : BaseRecyclerFragment<MinePresenter, MineRvModel, BaseViewHolder, MineRvAdapter>(), MineContract.View, View.OnClickListener {

    override fun initPresenter(): MinePresenter {
        return MinePresenter()
    }

    override fun getFragmentLayoutID(): Int {
        return R.layout.fragment_mine
    }


    override fun initView() {
        mineTitleBar.setBackgroundResource(android.R.color.transparent)
        mineTitleBar.setPositiveListener {
            startActivity(Intent(context, SettingActivity::class.java))
        }
        ivAvatar.setOnClickListener(this)
        tvMobile.setOnClickListener(this)
        tvNickName.setOnClickListener(this)
        llMyPointDetail.setOnClickListener(this)
        initRecyclerView(mineRecyclerView)
        mineRecyclerView.layoutManager = GridLayoutManager(context, 4)
        mineRecyclerView.addItemDecoration(GridDividerItemDecoration(40, Color.WHITE))
        //todo:待添加
        //{
        //  "name": "关注板块",
        //  "localImage": "icon_mine_guanzhu"
        //},
        val json = ResourceUtils.readAssets2String("minelist.json")
        val data = JSON.parseArray(json, MineRvModel::class.java)
        onLoadSucceed(1, data)
        mPresenter.getUser()
    }


    override fun lazyData() {
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden && !isFirstResume) {
            mPresenter.getUser()
        }
    }

    override fun onUserVisible() {
        super.onUserVisible()
        mPresenter.getUser()
    }


    override fun onClick(v: View?) {
        if (v == null) {
            return
        }
        when (v.id) {
            R.id.ivAvatar -> {
                skipPersonalData()
            }
            R.id.tvMobile -> {
                skipPersonalData()
            }
            R.id.tvNickName -> {
                skipPersonalData()
            }
            R.id.llMyPointDetail -> {
                startActivity(Intent(context, MyPointDetailActivity::class.java))
            }
            else -> {
            }
        }
    }

    override fun setUser(user: User.UserinfoBean) {
        LogUtils.e(Gson().toJson(user))

        val avatar = user.avatar
        if (!avatar.isNullOrEmpty() && !avatar.contains("base64")) {
            GlideManager.loadCircleImg(avatar, ivAvatar, R.mipmap.default_avatar)
        }

        tvNickName.text = user.nickname
        tvMobile.text = user.mobile
        tvPoint.text = user.score

    }


    override fun setList(list: MutableList<MineRvModel>?) {
        setList(object : AdapterCallBack<MineRvAdapter> {

            override fun createAdapter(): MineRvAdapter {
                return MineRvAdapter(R.layout.item_mine_rv_view, list)
            }

            override fun refreshAdapter() {
                adapter.setNewData(list)
            }
        })
    }

    override fun getListAsync(page: Int) {
    }

    private fun skipPersonalData() {
        startActivity(Intent(context, PersonalDataActivity::class.java))
    }


    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onItemClick(adapter, view, position)
        when (position) {
            //我的收藏
            0 -> {
                startActivity(Intent(context, MyCollectActivity::class.java))
            }
            //最近浏览
            1 -> {
                startActivity(Intent(context, RecentBrowseActivity::class.java))
            }
            //我的帖子
            2 -> {
                startActivity(Intent(context, MyArticleActivity::class.java))
            }
            //问政记录
            3 -> {
                startActivity(Intent(context, MyWenZhenActivity::class.java))
            }
            //我的消息
            4 -> {
                startActivity(Intent(context, MyMessageActivity::class.java))
            }
            //兑换记录
            5 -> {
                startActivity(Intent(context, MyExchangeRecordActivity::class.java))
            }

            //关于我们
            6 -> {
                startActivity(Intent(context, AboutUsActivity::class.java))
            }
            else -> {
            }
        }

    }

}