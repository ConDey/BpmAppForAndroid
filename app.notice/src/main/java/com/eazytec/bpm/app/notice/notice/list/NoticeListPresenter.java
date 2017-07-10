package com.eazytec.bpm.app.notice.notice.list;

import com.eazytec.bpm.app.notice.data.NoticeListDataTObject;
import com.eazytec.bpm.app.notice.webservice.WebApi;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.RxPresenter;
import com.eazytec.bpm.lib.common.webservice.BPMRetrofit;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Administrator
 * @version Id: NoticeListPresenter, v 0.1 2017/7/10 9:36 Administrator Exp $$
 */
public class NoticeListPresenter extends RxPresenter<NoticeListContract.View> implements NoticeListContract.Presenter<NoticeListContract.View> {

    @Override public void loadNoticeList(int pageNo, int pageSize, String title) {
        Subscription rxSubscription = BPMRetrofit.retrofit().create(WebApi.class).loadNoticeList(title, String.valueOf(pageNo), String.valueOf(pageSize))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NoticeListDataTObject>() {
                    @Override public void onNext(NoticeListDataTObject data) {
                        if (data.isSuccess()) {
                            mView.loadSuccess(data);
                        } else {
                            mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"通知公告加载失败" + data.getErrorMsg());
                        }
                        mView.completeLoading();
                    }

                    @Override public void onCompleted() {
                    }

                    @Override public void onError(Throwable e) {
                        mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"通知公告加载失败:网络错误,请稍后再试");
                    }
                });
        addSubscrebe(rxSubscription);
    }
}

