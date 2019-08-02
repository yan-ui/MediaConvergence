package cn.tklvyou.mediaconvergence.ui.mine.exchange;

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
public class ExchangePresenter extends BasePresenter<ExchangeRecordContract.View> implements ExchangeRecordContract.Presenter {

    @Override
    public void getExchangePageList(int page) {
        RetrofitHelper.getInstance().getServer()
                .getExchangeList(page)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if (result.getCode() == RequestConstant.CODE_REQUEST_SUCCESS) {
                        mView.setExchangeList(page, result.getData());
                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> throwable.printStackTrace());
    }


}
