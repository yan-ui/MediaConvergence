package cn.tklvyou.mediaconvergence.ui.home.all_tv;


import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;


public class TVListPresenter extends BasePresenter<TVListContract.View> implements TVListContract.Presenter {

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
                            throwable.printStackTrace();
                            mView.setNewList(p, null);
                        }
                );
    }

}
