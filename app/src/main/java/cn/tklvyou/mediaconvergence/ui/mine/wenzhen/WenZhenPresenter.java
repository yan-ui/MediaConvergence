package cn.tklvyou.mediaconvergence.ui.mine.wenzhen;

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
public class WenZhenPresenter extends BasePresenter<WenZhenContract.View> implements WenZhenContract.Presenter {


    @Override
    public void getDataPageList(int page) {
        RetrofitHelper.getInstance().getServer()
                .getMyArticleList(page,"问政")
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if (result.getCode() == RequestConstant.CODE_REQUEST_SUCCESS) {
                        mView.setDataList(page, result.getData());
                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> mView.showFailed(""));
    }
}
