package cn.tklvyou.mediaconvergence.ui.home.new_list;


import android.annotation.SuppressLint;

import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;


public class NewListPresenter extends BasePresenter<NewListContract.View> implements NewListContract.Presenter {

    @Override
    public void getDetailsById(int id) {
        RetrofitHelper.getInstance().getServer()
                .getArticleDetail(id)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void deleteArticle(int id, int position) {
        RetrofitHelper.getInstance().getServer()
                .deleteArticle(id)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if(result.getCode() ==1){
                        ToastUtils.showShort("删除成功");
                        mView.deleteSuccess(position);
                    }else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void getJuZhengHeader(String module) {
        RetrofitHelper.getInstance().getServer()
                .getJuZhengHeader(module)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if(result.getCode() ==1){
                        mView.setJuZhengHeader(result.getData());
                    }else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void getNewList(String module, String module_second, int p) {
        RetrofitHelper.getInstance().getServer()
                .getNewList(module,module_second, p)
                .compose(RxSchedulers.applySchedulers())
//                .compose(mView.bindToLife())
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
    public void searchNewList(String module, String name, int p) {
        RetrofitHelper.getInstance().getServer()
                .searchNewList(module,name, p)
                .compose(RxSchedulers.applySchedulers())
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
    public void getHaveSecondModuleNews(int p, String module) {
        RetrofitHelper.getInstance().getServer()
                .getHaveSecondModuleNews(module, p)
                .compose(RxSchedulers.applySchedulers())
//                .compose(mView.bindToLife())
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
