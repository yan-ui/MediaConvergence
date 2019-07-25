package cn.tklvyou.mediaconvergence.ui.main;

import android.os.Handler;
import android.text.TextUtils;

import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.widget.FrameLayout4Loading;


/**
 * 描述
 */

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter{

    @Override
    public void login(final FrameLayout4Loading frameLayout4Loading, final String name, final String password) {
        mView.showLoading();
//        frameLayout4Loading.showLoadingView();

//        RetrofitHelper.getInstance().getServer()
//                .getBooks("","","","")
//                .compose(RxSchedulers.<BookModel>applySchedulers())
//                .compose(mView.<BookModel>bindToLife())
//                .subscribe(new Consumer<BookModel>() {
//                    @Override
//                    public void accept(BookModel bookModel) throws Exception {
////                        mView.hideLoading();
//                        frameLayout4Loading.hideLoadingView();
////                        mView.setBook(bookModel);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
////                       mView.hideLoading();
//                        frameLayout4Loading.hideLoadingView();
//                        frameLayout4Loading.showDefaultExceptionView();
//                    }
//                });
        new Handler().postDelayed(new Runnable(){
            public void run() {
                if(TextUtils.equals(name,"1")&&TextUtils.equals(password,"2")){
//                    User user = new User();
//                    user.setName("veer");
//                    user.setPassword("v123456");
//                    DbUserDao.getInstance().addUser(user);
                    mView.loginSuccess("登录成功");
                }else{
                    mView.loginError("密码错误！");
                }
                mView.hideLoading();
//                frameLayout4Loading.hideLoadingView();
            }
        }, 2000);

    }
}
