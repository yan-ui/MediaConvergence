package cn.tklvyou.mediaconvergence.ui.home;


import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;


public class NewListPresenter extends BasePresenter<NewListContract.View> implements NewListContract.Presenter {

    @Override
    public void getNewList(String module, int p) {
        RetrofitHelper.getInstance().getServer()
                .getNewList(module, p)
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

    @Override
    public void getHaveSecondModuleNews(int p, String module,String module_second) {
        RetrofitHelper.getInstance().getServer()
                .getHaveSecondModuleNews(module,module_second, p)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                            if (result.getCode() == 1) {
                                mView.setHaveSecondModuleNews(p, result.getData());
                            } else {
                                ToastUtils.showShort(result.getMsg());
                            }
                        }, throwable -> {
                            throwable.printStackTrace();
                            mView.setHaveSecondModuleNews(p, null);
                        }
                );
    }

    @Override
    public void getBanner(String module) {
        RetrofitHelper.getInstance().getServer()
                .getBanner(module)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if (result.getCode() == 1) {
                        mView.setBanner(result.getData());
                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> throwable.printStackTrace());
    }
}
