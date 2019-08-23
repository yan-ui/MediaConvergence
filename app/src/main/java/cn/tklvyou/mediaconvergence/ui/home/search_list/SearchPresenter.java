package cn.tklvyou.mediaconvergence.ui.home.search_list;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.common.RequestConstant;
import cn.tklvyou.mediaconvergence.ui.mine.collection.CollectContract;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月02日15:25
 * @Email: 971613168@qq.com
 */
@SuppressLint("CheckResult")
public class SearchPresenter extends BasePresenter<SearchContract.View> implements SearchContract.Presenter {

    @Override
    public void searchNewList(String module, String name, int p) {
        RetrofitHelper.getInstance().getServer()
                .searchNewList(module, name, p)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                            if (result.getCode() == 1) {
                                mView.setNewList(p, result.getData());
                            } else {
                                ToastUtils.showShort(result.getMsg());
                            }
                        }, throwable -> {
                            mView.showFailed("");
                        }
                );
    }

}
