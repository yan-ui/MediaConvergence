package cn.tklvyou.mediaconvergence.ui.home;


import com.blankj.utilcode.util.ToastUtils;

import java.util.List;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.base.BaseResult;
import cn.tklvyou.mediaconvergence.model.BannerModel;
import cn.tklvyou.mediaconvergence.model.NewListModel;
import io.reactivex.functions.Consumer;


public class NewListPresenter extends BasePresenter<NewListContract.View> implements NewListContract.Presenter{

    @Override
    public void getNewList(String module, int p) {
        RetrofitHelper.getInstance().getServer()
                .getNewList(module, p)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(new Consumer<BaseResult<NewListModel>>() {
                    @Override
                    public void accept(BaseResult<NewListModel> result) {
                        if(result.getCode() ==1){
                            mView.setNewList(p,result.getData());
                        }else {
                            ToastUtils.showShort(result.getMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void getBanner(String module) {
        RetrofitHelper.getInstance().getServer()
                .getBanner(module)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(new Consumer<BaseResult<List<BannerModel>>>() {
                    @Override
                    public void accept(BaseResult<List<BannerModel>> result) {
                        if(result.getCode() ==1){
                            mView.setBanner(result.getData());
                        }else {
                            ToastUtils.showShort(result.getMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }
}
