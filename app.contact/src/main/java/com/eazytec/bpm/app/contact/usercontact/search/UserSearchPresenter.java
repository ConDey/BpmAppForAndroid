package com.eazytec.bpm.app.contact.usercontact.search;

import com.eazytec.bpm.app.contact.data.UsersDataTObject;
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

public class UserSearchPresenter extends RxPresenter<UserSearchContract.View> implements UserSearchContract.Presenter<UserSearchContract.View> {

    @Override public void loadUserDetail(String id) {
        Subscription rxSubscription = BPMRetrofit.retrofit().create(WebApi.class).loadUserSearch(id)
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
                .subscribe(new Observer<UsersDataTObject>() {
                    @Override public void onNext(UsersDataTObject data) {
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
