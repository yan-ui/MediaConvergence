package cn.tklvyou.mediaconvergence.ui.home.publish_wenzheng

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.activity.BaseActivity
import cn.tklvyou.mediaconvergence.ui.adapter.GridImageAdapter
import cn.tklvyou.mediaconvergence.ui.adapter.WenzhengGridImageAdapter
import cn.tklvyou.mediaconvergence.ui.video_edit.VideoEditActivity
import cn.tklvyou.mediaconvergence.utils.GridDividerItemDecoration
import com.blankj.utilcode.util.ToastUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.activity_publish_wenzheng.*
import java.io.File
import java.io.Serializable
import java.lang.StringBuilder

class PublishWenzhengActivity : BaseActivity<PublishWenzhengPresenter>(), PublishWenzhengContract.View {

    override fun initPresenter(): PublishWenzhengPresenter {
        return PublishWenzhengPresenter()
    }

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_publish_wenzheng
    }

    private var selectList: MutableList<LocalMedia>? = null
    private var adapter: WenzhengGridImageAdapter? = null
    private var imagesBuilder: StringBuilder = StringBuilder()
    private val imageFiles = ArrayList<File>()

    private var moduleSecond = ""
    private var name = ""
    private var content = ""


    override fun initView() {
        setTitle("发布问政")
        setNavigationText("图片",R.mipmap.icon_titlebar_back)
        setNavigationOnClickListener { finish() }

        selectList = ArrayList()

        picRecyclerView.layoutManager = GridLayoutManager(this, 4)
        adapter = WenzhengGridImageAdapter(this, onAddPicClickListener)
        adapter!!.list = selectList
        adapter!!.setSelectMax(9)
        picRecyclerView.adapter = adapter
        picRecyclerView.addItemDecoration(GridDividerItemDecoration(30, Color.WHITE))
        adapter!!.setOnItemClickListener { position, v ->
            val media = selectList!![position]
            val pictureType = media.pictureType
            val mediaType = PictureMimeType.pictureToVideo(pictureType)
            when (mediaType) {
                1 ->
                    // 预览图片
                    PictureSelector.create(this).themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList)
            }
        }



        btnSubmit.setOnClickListener {
            moduleSecond = etModuleSecond.text.toString().trim()
            name = etName.text.toString().trim()
            content = etContent.text.toString().trim()

            if(moduleSecond.isEmpty()){
                ToastUtils.showShort("请输入问政对象")
                return@setOnClickListener
            }

            if(name.isEmpty()){
                ToastUtils.showShort("请输入标题")
                return@setOnClickListener
            }

            if(content.isEmpty()){
                ToastUtils.showShort("请输入内容")
                return@setOnClickListener
            }

            showLoading()

            selectList!!.forEach {
                if (it.isCompressed || (it.isCut && it.isCompressed)) {
                    imageFiles.add(File(it.compressPath))
                } else {
                    imageFiles.add(File(it.path))
                }
            }
            if(imageFiles.size != 0) {
                mPresenter.uploadMultiImage(imageFiles)
            }else{
                mPresenter.publishWenZheng(moduleSecond,name,content, imagesBuilder.toString())
            }
        }

    }


    override fun uploadImagesSuccess(urls: MutableList<String>) {
        for ((index, item) in urls.withIndex()) {
            if (index != urls.size - 1) {
                imagesBuilder.append("$item,")
            } else {
                imagesBuilder.append(item)
            }
        }
        mPresenter.publishWenZheng(moduleSecond,name,content, imagesBuilder.toString())
    }


    override fun publishSuccess() {
        hideLoading()
        finish()
    }


    private val onAddPicClickListener = object : WenzhengGridImageAdapter.onAddPicClickListener {
        override fun onAddPicClick() {
            // 进入相册 以下是例子：不需要的api可以不写
            PictureSelector.create(this@PublishWenzhengActivity)
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
                    .selectionMedia(selectList)
                    .forResult(PictureConfig.CHOOSE_REQUEST)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && resultCode == Activity.RESULT_OK) {

            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    // 图片选择
                    selectList = PictureSelector.obtainMultipleResult(data)
                    adapter!!.list = selectList
                    adapter!!.notifyDataSetChanged()
                }
            }

        }
    }


}
