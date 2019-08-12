package cn.tklvyou.mediaconvergence.ui.home.etstset

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.tklvyou.mediaconvergence.R
import cn.tklvyou.mediaconvergence.helper.GlideManager
import cn.tklvyou.mediaconvergence.model.NewsBean
import cn.tklvyou.mediaconvergence.ui.adapter.JuzhengHeaderViewholder
import cn.tklvyou.mediaconvergence.widget.page_recycler.PageRecyclerView
import com.blankj.utilcode.util.LogUtils

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val mRecyclerView = findViewById<PageRecyclerView>(R.id.customSwipeView)
        // 设置指示器
        mRecyclerView.setIndicator(findViewById(R.id.indicator))
        // 设置行数和列数
        mRecyclerView.setPageSize(1, 4)
        // 设置页间距
//        mRecyclerView.setPageMargin(30)

        LogUtils.e("--------------------- initJuzhengHeaderView:  ")

        val juzhengHeaderList = ArrayList<NewsBean>()
        (0..3).forEach{
            val bean = NewsBean()
            bean.nickname = "测试 "+it
            bean.avatar = "http://cdn.duitang.com/uploads/item/201508/02/20150802155755_YCynL.thumb.700_0.jpeg"
            juzhengHeaderList.add(bean)
        }



        val adapter = mRecyclerView.PageAdapter(juzhengHeaderList,object : PageRecyclerView.CallBack{
            override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
                LogUtils.e("--------------------- onCreateViewHolder:  ")

                return JuzhengHeaderViewholder(LayoutInflater.from(this@TestActivity).inflate(R.layout.item_juzheng_header_child_layout, parent, false))
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
                LogUtils.e("--------------------- position:  "+position)
                val bean = juzhengHeaderList[position]
                GlideManager.loadRoundImg(bean.avatar,(holder as JuzhengHeaderViewholder).ivAvatar)
                holder.tvNickName.text = bean.nickname
            }

            override fun onItemClickListener(view: View?, position: Int) {

            }

            override fun onItemLongClickListener(view: View?, position: Int) {
            }

        })
        mRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}
