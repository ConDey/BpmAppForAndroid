package com.eazytec.bpm.app.contact.usercontact.userdetail;

import com.eazytec.bpm.app.contact.data.UserDetailDataTObject;
import com.eazytec.bpm.app.contact.webservice.WebApi;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.RxPresenter;
import com.eazytec.bpm.lib.common.webservice.BPMRetrofit;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/7/4.
 */

public class UserDetailPresenter extends RxPresenter<UserDetailContract.View> implements UserDetailContract.Presenter<UserDetailContract.View> {

    @Override public void loadUserDetail(String id) {
        Subscription rxSubscription = BPMRetrofit.retrofit().create(WebApi.class).loadUserDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override public void call() {
                        mView.showProgress();
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override public void call() {
                        mView.dismissProgress();
                    }
                })
                .subscribe(new Observer<UserDetailDataTObject>() {
                    @Override public void onNext(UserDetailDataTObject data) {
                        if (data.isSuccess()) {
                            mView.loadSuccess(data);
                        } else {
                            mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"用户信息加载失败:" + data.getErrorMsg());
                        }
                    }

                    @Override public void onCompleted() {
                    }

                    @Override public void onError(Throwable e) {
                        mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"用户信息加载失败:网络错误,请稍后再试");
                    }
                });
        addSubscrebe(rxSubscription);
    }
}

