package com.eazytec.bpm.app.calendar.detailcontact;

import com.eazytec.bpm.app.calendar.R;
import com.eazytec.bpm.app.calendar.dataobject.EventDetailDataObject;
import com.eazytec.bpm.app.calendar.webservice.WebApi;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.RxPresenter;
import com.eazytec.bpm.lib.common.webservice.BPMRetrofit;
import com.eazytec.bpm.lib.utils.ToastUtils;

import io.reactivex.Scheduler;
import retrofit2.Retrofit;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Vivi on 2017/9/28.
 */

public class DetailPresenter extends RxPresenter<DetailContact.View> implements DetailContact.Presenter<DetailContact.View>{


    @Override
    public void loadDetail(String deId) {
        Subscription rxSubscription = BPMRetrofit.retrofit().create(WebApi.class).getEventDetail(deId)
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

                        }else {
                            mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"信息加载失败："+eventDetailDataObject.getErrorMsg());
                        }
                    }
                });
        addSubscrebe(rxSubscription);
    }
}
