package cn.tklvyou.mediaconvergence.ui.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.fragment.BaseHttpRecyclerFragment
import cn.tklvyou.mediaconvergence.base.interfaces.AdapterCallBack
import cn.tklvyou.mediaconvergence.model.BannerModel
import cn.tklvyou.mediaconvergence.model.BasePageModel
import cn.tklvyou.mediaconvergence.model.NewsBean
import cn.tklvyou.mediaconvergence.model.HaveSecondModuleNewsModel
import cn.tklvyou.mediaconvergence.ui.adapter.WxCircleAdapter
import cn.tklvyou.mediaconvergence.ui.home.new_list.NewListContract
import cn.tklvyou.mediaconvergence.ui.home.new_list.NewListPresenter
import cn.tklvyou.mediaconvergence.ui.home.news_detail.NewsDetailActivity
import cn.tklvyou.mediaconvergence.ui.home.publish_news.PublishNewsActivity
import cn.tklvyou.mediaconvergence.utils.RecycleViewDivider
import cn.tklvyou.mediaconvergence.widget.dailog.CommonDialog
import com.adorkable.iosdialog.BottomSheetDialog
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.Serializable

class CameraFragment : BaseHttpRecyclerFragment<NewListPresenter, NewsBean, BaseViewHolder, WxCircleAdapter>(), NewListContract.View {
    override fun setJuZhengHeader(beans: MutableList<NewsBean>?) {
    }


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
                    .request(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
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
                                        startActivity(Intent(context, TakePhotoActivity::class.java))
                                    }
                                    .addSheetItem("从手机相册选择") { which ->
                                        // 进入相册 以下是例子：不需要的api可以不写
                                        PictureSelector.create(this@CameraFragment)
                                                .openGallery(PictureMimeType.ofImage())
                                                .theme(R.style.picture_default_style)
                                                .maxSelectNum(9)
                                                .minSelectNum(1)
                                                .selectionMode(PictureConfig.MULTIPLE)
                                                .previewImage(true)
                                                .isCamera(true)
                                                .enableCrop(false)
                                                .compress(true)
                                                .previewEggs(true)
                                                .openClickSound(false)
                                                .forResult(PictureConfig.CHOOSE_REQUEST)
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

        mPresenter.getNewList("随手拍", null,1)
    }

    override fun lazyData() {

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden && !isFirstResume){
            mPresenter.getNewList("随手拍", null,1)
        }
    }

    override fun onUserVisible() {
        super.onUserVisible()
        mPresenter.getNewList("随手拍", null,1)
    }


    override fun getListAsync(page: Int) {
        mPresenter.getNewList("随手拍", null,page)
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

    override fun deleteSuccess(position: Int) {
        adapter.remove(position)
    }


    override fun setBanner(bannerModelList: MutableList<BannerModel>?) {
        //该页面用不上此方法
    }

    override fun setHaveSecondModuleNews(p: Int, datas: MutableList<HaveSecondModuleNewsModel>?) {
        //该页面用不上此方法
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onItemClick(adapter, view, position)

        val bean = (adapter as WxCircleAdapter).data[position]
        val id = bean.id
        val type = if (bean.images != null && bean.images.size > 0) "图文" else "视频"
        NewsDetailActivity.startNewsDetailActivity(context!!, type, id)

    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onItemChildClick(adapter, view, position)
        if(view!!.id == R.id.deleteBtn){
            val dialog = CommonDialog(context)
            dialog.setTitle("温馨提示")
            dialog.setMessage("是否删除？")
            dialog.setYesOnclickListener("确认"){
                val bean = (adapter as WxCircleAdapter).data[position]
                mPresenter.deleteArticle(bean.id,position)
                dialog.dismiss()
            }
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
                val intent = Intent(context, PublishNewsActivity::class.java)
                intent.putExtra("page","随手拍")
                intent.putExtra("isVideo",false)
                intent.putExtra("data", selectList as Serializable)
                startActivity(intent)

            }
        }

    }

}