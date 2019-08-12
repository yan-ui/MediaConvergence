package cn.tklvyou.mediaconvergence.ui.service

import android.os.Bundle
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.base.activity.BaseWebViewActivity
import cn.tklvyou.mediaconvergence.helper.GlideManager
import cn.tklvyou.mediaconvergence.model.PointModel
import cn.tklvyou.mediaconvergence.widget.dailog.ConfirmDialog
import com.blankj.utilcode.util.SpanUtils
import kotlinx.android.synthetic.main.activity_goods_details.*

class GoodsDetailsActivity : BaseWebViewActivity<GoodsDetailPresenter>(), GoodsDetailContract.View {

    override fun initPresenter(): GoodsDetailPresenter {
        return GoodsDetailPresenter()
    }

    override fun getActivityLayoutID(): Int {
        return R.layout.activity_goods_details
    }

    private var id = 0

    override fun initView(savedInstanceState: Bundle?) {
        setTitle("商品详情")
        setNavigationImage()
        setNavigationOnClickListener { finish() }

        initWebView(webView)

        id = intent.getIntExtra("id", 0)
        mPresenter.getGoodsDetails(id)

    }

    override fun setTitleContent(title: String) {

    }

    override fun setGoodsDetail(model: PointModel) {
        GlideManager.loadImg(model.image, iVBanner)
        tvName.text = model.name
        tvScore.text = "兑换：${model.score}分"
        tvStore.text = "库存：${model.stock}"
        loadHtml(model.content)

        btnExchange.setOnClickListener {

            val dialog = ConfirmDialog(this)
            dialog.setTitle("商品兑换")
            dialog.setStyleMessage(SpanUtils().append("是否消耗")
                    .append(""+model.score+"分").setForegroundColor(resources.getColor(R.color.colorAccent))
                    .append("兑换此商品？")
                    .create())

            dialog.setYesOnclickListener("立即兑换") {
                mPresenter.exchangeGoods(id)
                dialog.dismiss()
            }

            dialog.show()
        }

    }


}
