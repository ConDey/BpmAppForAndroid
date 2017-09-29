package com.eazytec.bpm.app.calendar.contact;

import com.eazytec.bpm.app.calendar.dataobject.EventListDataObject;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.RxPresenter;
import com.eazytec.bpm.lib.common.webservice.BPMRetrofit;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Vivi on 2017/9/27.
 */

public class ItemListPresenter extends RxPresenter<ItemListContract.View> implements ItemListContract.Presenter<ItemListContract.View> {
    @Override
    public void loadItemById(String datas) {
        Subscription rxSubscription = BPMRetrofit.retrofit().create(com.eazytec.bpm.app.calendar.webservice.WebApi.class).loadItInfo(datas)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EventListDataObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"信息加载失败:网络错误,请稍后再试");
                    }

                    @Override
                    public void onNext(EventListDataObject eventListDataObject) {
                        if (eventListDataObject.isSuccess()){
                            mView.loadItemList(eventListDataObject);
                        }else {
                            mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"信息加载失败："+eventListDataObject.getErrorMsg());
                        }
                    }
                });
        addSubscrebe(rxSubscription);
    }
}
