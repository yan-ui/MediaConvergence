package cn.tklvyou.mediaconvergence.ui.service;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.common.RequestConstant;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月01日19:10
 * @Email: 971613168@qq.com
 */
@SuppressLint("CheckResult")
public class MsgPresenter extends BasePresenter<MsgContract.View> implements MsgContract.Presenter {
    @Override
    public void getMsgPageList(int page) {
        RetrofitHelper.getInstance().getServer()
                .getSystemMsgList(page)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if (result.getCode() == RequestConstant.CODE_REQUEST_SUCCESS) {
                        mView.setMessageList(page, result.getData());
                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> throwable.printStackTrace());
    }
}
