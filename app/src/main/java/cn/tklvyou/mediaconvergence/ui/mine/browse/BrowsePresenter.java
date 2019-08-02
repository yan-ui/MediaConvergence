package cn.tklvyou.mediaconvergence.ui.mine.browse;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.common.RequestConstant;

/**
 * @author :JenkinsZhou
 * @description :最近浏览
 * @company :途酷科技
 * @date 2019年08月02日15:25
 * @Email: 971613168@qq.com
 */
@SuppressLint("CheckResult")
public class BrowsePresenter extends BasePresenter<BrowseContract.View> implements BrowseContract.Presenter {

    @Override
    public void getBrowsePageList(int page) {
        RetrofitHelper.getInstance().getServer()
                .getRecentBrowseList(page)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if (result.getCode() == RequestConstant.CODE_REQUEST_SUCCESS) {
                        mView.setBrowseList(page, result.getData());
                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> LogUtils.e("异常:" + throwable.toString()));
    }


}
