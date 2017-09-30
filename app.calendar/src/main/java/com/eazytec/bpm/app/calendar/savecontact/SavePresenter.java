package com.eazytec.bpm.app.calendar.savecontact;

import com.eazytec.bpm.app.calendar.dataobject.EventDetailDataObject;
import com.eazytec.bpm.app.calendar.dataobject.EventListDataObject;
import com.eazytec.bpm.app.calendar.dataobject.EventTypeObject;
import com.eazytec.bpm.app.calendar.webservice.WebApi;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.RxPresenter;
import com.eazytec.bpm.lib.common.webservice.BPMRetrofit;
import com.eazytec.bpm.lib.common.webservice.WebDataTObject;
import com.eazytec.bpm.lib.utils.ToastUtils;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Vivi on 2017/9/28.
 */

public class SavePresenter  extends RxPresenter<SaveContact.View> implements SaveContact.Presenter<SaveContact.View>{
    @Override
    public void editDetail(String startTime, String startDate, String endTime, String endDate, String description, String location, String eventName, String eventType, String id) {
        Subscription rxSubscription =BPMRetrofit.retrofit().create(WebApi.class).saveDetail(startTime,startDate,endTime,endDate,description,location,eventName,eventType,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WebDataTObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"信息加载失败:网络错误,请稍后再试");
                    }

                    @Override
                    public void onNext(WebDataTObject webDataTObject) {
                        if (webDataTObject.isSuccess()){
                            mView.postSuccess();
                        }else {
                            mView.toast(ToastDelegate.TOAST_TYPE_ERROR,webDataTObject.getErrorMsg());
                        }
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void loadSaveDetails(String eventId) {
        Subscription rxSubscription = BPMRetrofit.retrofit().create(com.eazytec.bpm.app.calendar.webservice.WebApi.class).getEventDetail(eventId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EventDetailDataObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        ToastUtils.showLong("网络问题，稍后再试！");
                    }

                    @Override
                    public void onNext(EventDetailDataObject eventDetailDataObject) {
                        if (eventDetailDataObject.isSuccess()){
                            mView.loadSuccess(eventDetailDataObject);
                        }else {
                            mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"信息加载失败："+eventDetailDataObject.getErrorMsg());
                        }
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void loadEventType() {
        Subscription rxSubscription = BPMRetrofit.retrofit().create(com.eazytec.bpm.app.calendar.webservice.WebApi.class).getEventType()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EventTypeObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"信息加载失败:网络错误,请稍后再试");
                    }

                    @Override
                    public void onNext(EventTypeObject eventTypeObject) {
                        if (eventTypeObject.isSuccess()) {
                            mView.loadEventType(eventTypeObject);
                        } else {
                            mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"信息加载失败："+eventTypeObject.getErrorMsg());
                        }
                    }
                });
        addSubscrebe(rxSubscription);
    }

}
