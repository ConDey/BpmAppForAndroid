package com.eazytec.bpm.app.message.fragmet;

import com.eazytec.bpm.app.message.webservice.WebApi;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.RxPresenter;
import com.eazytec.bpm.lib.common.message.CurrentMessage;
import com.eazytec.bpm.lib.common.message.dataobject.MessageDataTObject;
import com.eazytec.bpm.lib.common.webservice.BPMRetrofit;
import com.eazytec.bpm.lib.common.webservice.WebDataTObject;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * @author Beckett_W
 * @version Id: MessagePresenter, v 0.1 2017/9/26 13:43 Beckett_W Exp $$
 */
public class MessagePresenter extends RxPresenter<MessageContract.View> implements MessageContract.Presenter<MessageContract.View> {

    /**
     * 从数据库中取出message
     */
    @Override
    public void loadMessages(String isRead, int pageNo, int pageSize) {
        //根据已读，未读取出数据！
        List<MessageDataTObject> messages = CurrentMessage.getCurrentMessage().getMessagesByPage(isRead, pageNo, pageSize);
        if (messages != null) {
            mView.loadSuccess(messages);
        }
        mView.completeLoading();
    }

    @Override
    public void setReaded(String id) {
        Subscription rxSubscription = BPMRetrofit.retrofit().create(WebApi.class).setReaded(id)
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
                .subscribe(new Observer<WebDataTObject>() {
                    @Override public void onNext(WebDataTObject data) {
                        if (data.isSuccess()) {
                            //不做任何处理
                        } else {
                            mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"读取消息失败:" + data.getErrorMsg());
                        }
                    }

                    @Override public void onCompleted() {
                    }

                    @Override public void onError(Throwable e) {
                        mView.toast(ToastDelegate.TOAST_TYPE_ERROR,"读取消息失败:网络错误,请稍后再试");
                    }
                });
        addSubscrebe(rxSubscription);
    }
}

