package cn.tklvyou.mediaconvergence.base.activity;

import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import cn.tklvyou.mediaconvergence.base.BaseAdapter;
import cn.tklvyou.mediaconvergence.base.BaseContract;
import cn.tklvyou.mediaconvergence.base.interfaces.OnLoadListener;
import cn.tklvyou.mediaconvergence.base.interfaces.OnStopLoadListener;


/**
 * 基础http网络列表的Activity
 *
 * @param <P>  Presenter类
 * @param <T>  数据模型(model/JavaBean)类
 * @param <VH> ViewHolder或其子类
 * @param <A>  管理LV的Adapter
 * @author Lemon
 * @see #getListAsync(int)
 * @see #onHttpResponse(int, String, Exception)
 * @see <pre>
 *       基础使用：<br />
 *       extends BaseHttpRecyclerActivity 并在子类onCreate中srlBaseHttpRecycler.autoRefresh(), 具体参考.DemoHttpRecyclerActivity
 *       <br /><br />
 *       列表数据加载及显示过程：<br />
 *       1.srlBaseHttpRecycler.autoRefresh触发刷新 <br />
 *       2.getListAsync异步获取列表数据 <br />
 *       3.onHttpResponse处理获取数据的结果 <br />
 *       4.setList把列表数据绑定到adapter <br />
 *   </pre>
 */
public abstract class BaseHttpRecyclerActivity<P extends BaseContract.BasePresenter, T, VH extends ViewHolder, A extends RecyclerView.Adapter<VH>>
        extends BaseRecyclerActivity<P, T, VH, A>
        implements OnStopLoadListener, OnRefreshListener, OnLoadMoreListener {

    private static final String TAG = "BaseHttpRecyclerActivity";

    protected SmartRefreshLayout srlBaseHttpRecycler;

    @Override
    public void initView() {
        super.initView();
        srlBaseHttpRecycler = getSmartRefreshLayout();

        setOnStopLoadListener(this);

        srlBaseHttpRecycler.setOnRefreshListener(this);
        srlBaseHttpRecycler.setOnLoadMoreListener(this);
    }

    protected abstract SmartRefreshLayout getSmartRefreshLayout();

    @Override
    public void setAdapter(A adapter) {
        if (adapter instanceof BaseAdapter) {
            ((BaseAdapter) adapter).setOnLoadListener(new OnLoadListener() {
                @Override
                public void onRefresh() {
                    srlBaseHttpRecycler.autoRefresh();
                }

                @Override
                public void onLoadMore() {
                    srlBaseHttpRecycler.autoLoadMore();
                }
            });
        }
        super.setAdapter(adapter);
    }


    /**
     * @param page 用-page作为requestCode
     */
    @Override
    public abstract void getListAsync(int page);


    /**
     * 重写后可自定义对这个事件的处理
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        onRefresh();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        onLoadMore();
    }


    @Override
    public void onStopRefresh() {
        runUiThread(new Runnable() {

            @Override
            public void run() {
                srlBaseHttpRecycler.finishRefresh();
            }
        });
    }

    @Override
    public void onStopLoadMore(final boolean isHaveMore) {
        runUiThread(new Runnable() {

            @Override
            public void run() {
                if (isHaveMore) {
                    srlBaseHttpRecycler.finishLoadMore();
                } else {
                    srlBaseHttpRecycler.finishLoadMoreWithNoMoreData();
                }
            }
        });
    }


    /**
     * 处理结果
     *
     * @param page
     * @param list
     * @param e
     */
    public void onResponse(int page, List<T> list, Exception e) {
        if ((list == null || list.isEmpty()) && e != null) {
            onLoadFailed(page, e);
        } else {
            onLoadSucceed(page, list);
        }
    }

}