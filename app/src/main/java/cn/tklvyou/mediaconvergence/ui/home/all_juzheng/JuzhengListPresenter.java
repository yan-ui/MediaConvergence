package cn.tklvyou.mediaconvergence.ui.home.all_juzheng;


import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.ui.home.all_tv.TVListContract;


public class JuzhengListPresenter extends BasePresenter<JuzhengListContract.View> implements JuzhengListContract.Presenter {

    @Override
    public void getNewList(String module,String module_second, int p) {
        RetrofitHelper.getInstance().getServer()
                .getNewList(module, module_second,p)
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
