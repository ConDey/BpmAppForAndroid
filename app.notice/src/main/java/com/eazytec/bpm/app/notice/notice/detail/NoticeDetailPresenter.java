package com.eazytec.bpm.app.notice.notice.detail;

import com.eazytec.bpm.app.notice.data.NoticeDetailDataTObject;
import com.eazytec.bpm.app.notice.webservice.WebApi;
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
 * @version Id: NoticeDetailPresenter, v 0.1 2017/7/10 14:04 Administrator Exp $$
 */
public class NoticeDetailPresenter extends RxPresenter<NoticeDetailContract.View> implements NoticeDetailContract.Presenter<NoticeDetailContract.View> {

    @Override public void loadNoticeDetail(String id) {

        Subscription rxSubscription = BPMRetrofit.retrofit().create(WebApi.class).loadNoticeDetail(id)
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
                .subscribe(new Observer<NoticeDetailDataTObject>() {
                    @Override public void onNext(NoticeDetailDataTObject data) {
                        if (data.isSuccess()) {
                            mView.loadSuccess(data);
                        } else {
                            mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"公告详情加载失败:" + data.getErrorMsg());
                        }
                    }

                    @Override public void onCompleted() {
                    }

                    @Override public void onError(Throwable e) {
                        mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"公告详情加载失败:网络错误,请稍后再试");
                    }
                });
        addSubscrebe(rxSubscription);
    }
}

