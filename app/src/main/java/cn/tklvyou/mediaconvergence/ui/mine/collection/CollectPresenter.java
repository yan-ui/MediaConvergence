package cn.tklvyou.mediaconvergence.ui.mine.collection;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.common.RequestConstant;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月02日15:25
 * @Email: 971613168@qq.com
 */
@SuppressLint("CheckResult")
public class CollectPresenter extends BasePresenter<CollectContract.View> implements CollectContract.Presenter {

    @Override
    public void getCollectPageList(int page) {
        RetrofitHelper.getInstance().getServer()
                .getMyCollectList(page)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if (result.getCode() == RequestConstant.CODE_REQUEST_SUCCESS) {
                        mView.setCollectList(page, result.getData());
                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> mView.showFailed("") );

    }
}
