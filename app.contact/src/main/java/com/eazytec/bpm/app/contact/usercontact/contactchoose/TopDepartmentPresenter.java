package com.eazytec.bpm.app.contact.usercontact.contactchoose;

import com.eazytec.bpm.app.contact.data.DepartmentDataTObject;
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
 * @author Administrator
 * @version Id: TopDepartmentPresenter, v 0.1 2017/7/24 13:10 Administrator Exp $$
 */
public class TopDepartmentPresenter extends RxPresenter<TopDepartmentContract.View> implements TopDepartmentContract.Presenter<TopDepartmentContract.View>{
    @Override public void loadDepSuccess() {

        Subscription rxSubscription = BPMRetrofit.retrofit().create(WebApi.class).loadDepartmentsAndUsers("Organization")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mView.showProgress();
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        mView.dismissProgress();
                    }
                })
                .subscribe(new Observer<DepartmentDataTObject>() {
                    @Override public void onNext(DepartmentDataTObject data) {
                        if (data.isSuccess()) {
                            mView.loadDepSuccess(data);
                        }
                    }

                    @Override public void onCompleted() {
                    }

                    @Override public void onError(Throwable e) {
                        mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"未知错误，请稍后再试！");
                    }
                });
        addSubscrebe(rxSubscription);
    }
}

