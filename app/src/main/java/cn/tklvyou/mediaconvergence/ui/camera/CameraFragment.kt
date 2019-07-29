package cn.tklvyou.mediaconvergence.ui.camera

import android.Manifest
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.fragment.BaseHttpRecyclerFragment
import cn.tklvyou.mediaconvergence.base.interfaces.AdapterCallBack
import cn.tklvyou.mediaconvergence.model.BannerModel
import cn.tklvyou.mediaconvergence.model.BasePageModel
import cn.tklvyou.mediaconvergence.model.NewsBean
import cn.tklvyou.mediaconvergence.ui.adapter.WxCircleAdapter
import cn.tklvyou.mediaconvergence.ui.home.NewListContract
import cn.tklvyou.mediaconvergence.ui.home.NewListPresenter
import cn.tklvyou.mediaconvergence.ui.home.NewsDetailActivity
import cn.tklvyou.mediaconvergence.utils.RecycleViewDivider
import com.adorkable.iosdialog.BottomSheetDialog
import com.adorkable.iosdialog.BottomSheetRadiusDialog
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_camera.*

class CameraFragment : BaseHttpRecyclerFragment<NewListPresenter, NewsBean, BaseViewHolder, WxCircleAdapter>(), NewListContract.View {


    override fun initPresenter(): NewListPresenter {
        return NewListPresenter()
    }

    override fun getFragmentLayoutID(): Int {
        return R.layout.fragment_camera
    }

    override fun initView() {
        cameraTitleBar.setBackgroundResource(R.drawable.shape_gradient_common_titlebar)
        cameraTitleBar.rightCustomView.setOnClickListener {
            RxPermissions(this)
                    .request(Manifest.permission.CAMERA)
                    .subscribe { granted ->
                        if (granted) { // Always true pre-M
                            BottomSheetDialog(context)
                                    .init()
                                    .setCancelable(true)    //设置手机返回按钮是否有效
                                    .setCanceledOnTouchOutside(true)  //设置 点击空白处是否取消 Dialog 显示
                                    //如果条目样式一样，可以直接设置默认样式
                                    .setDefaultItemStyle(BottomSheetDialog.SheetItemTextStyle("#000000", 16))
                                    .setBottomBtnStyle(BottomSheetDialog.SheetItemTextStyle("#ff0000", 18))
                                    .addSheetItem("拍摄") { which ->
                                        PictureSelector.create(this)
                                                .openCamera(PictureMimeType.ofAll())
                                                .recordVideoSecond(60)
                                                .forResult(PictureConfig.CHOOSE_REQUEST)
                                    }
                                    .addSheetItem("从手机相册选择") { which ->

                                    }
                                    .show()
                        } else {
                            ToastUtils.showShort("权限拒绝，无法使用")
                        }
                    }
        }

        initSmartRefreshLayout(cameraSmartRefreshLayout)
        initRecyclerView(cameraRecyclerView)

        cameraRecyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayout.VERTICAL, 1, resources.getColor(R.color.common_bg)))

        cameraRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(context!!).resumeRequests()
                } else {
                    Glide.with(context!!).pauseRequests()
                }
            }
        })

        mPresenter.getNewList("随手拍", 1)
    }

    override fun lazyData() {
        ToastUtils.showShort("camera")
    }

    override fun getListAsync(page: Int) {
        mPresenter.getNewList("随手拍", page)
    }

    override fun setNewList(p: Int, model: BasePageModel<NewsBean>?) {
        if (model != null) {
            onLoadSucceed(p, model.data)
        } else {
            onLoadFailed(p, null)
        }
    }

    override fun setList(list: MutableList<NewsBean>?) {
        setList(object : AdapterCallBack<WxCircleAdapter> {

            override fun createAdapter(): WxCircleAdapter {
                return WxCircleAdapter(R.layout.item_winxin_circle, list)
            }

            override fun refreshAdapter() {
                adapter.setNewData(list)
            }
        })

    }

    override fun setBanner(bannerModelList: MutableList<BannerModel>?) {
        //该页面用不上此方法
    }


    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onItemClick(adapter, view, position)

        val bean = (adapter as WxCircleAdapter).data[position]
        val id = bean.id
        val type = if (bean.images != null && bean.images.size > 0) NewsDetailActivity.TYPE_PICTURE else NewsDetailActivity.TYPE_VIDEO
        NewsDetailActivity.startNewsDetailActivity(context!!, type, id)

    }

}