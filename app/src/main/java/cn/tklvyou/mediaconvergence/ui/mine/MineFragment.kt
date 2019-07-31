package cn.tklvyou.mediaconvergence.ui.mine

import android.content.Intent
import android.graphics.Color
import androidx.recyclerview.widget.GridLayoutManager
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.fragment.BaseRecyclerFragment
import cn.tklvyou.mediaconvergence.base.interfaces.AdapterCallBack
import cn.tklvyou.mediaconvergence.model.MineRvModel
import cn.tklvyou.mediaconvergence.model.User
import cn.tklvyou.mediaconvergence.ui.setting.SettingActivity
import cn.tklvyou.mediaconvergence.ui.adapter.MineRvAdapter
import cn.tklvyou.mediaconvergence.utils.GridDividerItemDecoration
import cn.tklvyou.mediaconvergence.utils.JSON
import com.blankj.utilcode.util.ResourceUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : BaseRecyclerFragment<MinePresenter, MineRvModel,BaseViewHolder, MineRvAdapter>(),MineContract.View {

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

        initRecyclerView(mineRecyclerView)
        mineRecyclerView.layoutManager = GridLayoutManager(context,4)
        mineRecyclerView.addItemDecoration(GridDividerItemDecoration(80, Color.WHITE))

        val json = ResourceUtils.readAssets2String("minelist.json")
        val data = JSON.parseArray(json,MineRvModel::class.java)

        onLoadSucceed(1,data)

        mPresenter.getUser()
    }


    override fun lazyData() {

    }

    override fun setUser(user: User.UserinfoBean) {
        val avatar = user.avatar
        if(!avatar.isNullOrEmpty() && !avatar.contains("base64")) {
            Glide.with(this).load(avatar)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivAvatar)
        }

        tvNickName.text = user.nickname
        tvMobile.text = user.mobile
        tvPoint.text = user.score

    }


    override fun setList(list: MutableList<MineRvModel>?) {
        setList(object : AdapterCallBack<MineRvAdapter> {

            override fun createAdapter(): MineRvAdapter {
                return MineRvAdapter(R.layout.item_mine_rv_view,list)
            }

            override fun refreshAdapter() {
                adapter.setNewData(list)
            }
        })
    }

    override fun getListAsync(page: Int) {
    }

}