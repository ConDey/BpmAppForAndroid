package com.eazytec.bpm.app.message.main;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.eazytec.bpm.app.message.webservice.WebApi;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.RxPresenter;
import com.eazytec.bpm.lib.common.authentication.CurrentUser;
import com.eazytec.bpm.lib.common.message.CurrentMessage;
import com.eazytec.bpm.lib.common.message.dataobject.MessageDataTObject;
import com.eazytec.bpm.lib.common.message.dataobject.MessageListDataTObject;
import com.eazytec.bpm.lib.common.message.dataobject.MessageTopicDataTObject;
import com.eazytec.bpm.lib.common.message.topic.CurrentTopic;
import com.eazytec.bpm.lib.common.webservice.BPMRetrofit;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Beckett_W
 * @version Id: MessageMainPresenter, v 0.1 2017/9/18 13:11 Beckett_W Exp $$
 */
public class MessageMainPresenter  extends RxPresenter<MessageMainContract.View> implements MessageMainContract.Presenter<MessageMainContract.View> {
    @Override
    public void loadTopics() {
        // 获取上次请求时间
        String date = CurrentUser.getCurrentUser().getLastRequestTime(true);
        Subscription rxSubscription = BPMRetrofit.retrofit().create(WebApi.class).loadMessages(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MessageListDataTObject>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("MESSAGE", e.toString());
                    }

                    @Override
                    public void onNext(final MessageListDataTObject data) {
                        if (data.isSuccess()) {
                            // 更新请求时间
                            CurrentUser.getCurrentUser().updateLastRequestTime();
                            if (data.getDatas().size() > 0) {
                                final Handler handler = new Handler(){
                                    public void handleMessage(Message msg) {
                                        if (msg.obj == "DB") {
                                            mView.loadSuccess(CurrentTopic.getCurrentTopic().getTopicFromDB());
                                        }
                                    }
                                };

                                // 在这里开一个线程执行插数据耗时操作
                                new Thread() {
                                    public void run() {
                                        CurrentTopic.getCurrentTopic().saveTopicIntoDB(getLatestTopics(data.getDatas()));
                                        CurrentMessage.getCurrentMessage().saveMessagesIntoDB(data.getDatas());
                                        Message msg = handler.obtainMessage();
                                        msg.obj = "DB";
                                        handler.sendMessage(msg);
                                    }
                                }.start();

                            }else{
                                // 这里是给刷新标识用
                                mView.loadSuccess(CurrentTopic.getCurrentTopic().getTopicFromDB());
                            }
                        } else {
                            mView.toast(ToastDelegate.TOAST_TYPE_INFO, data.getErrorMsg());
                        }
                    }
                });
        addSubscrebe(rxSubscription);

    }

    @Override
    public void loadTopicsByDB() {
        mView.loadSuccessFromDB(CurrentTopic.getCurrentTopic().getTopicFromDB());
    }


    /**
     * 获取当前每条message的topic
     *
     * @param messages
     * @return
     */
     private List<MessageTopicDataTObject> getLatestTopics(List<MessageDataTObject> messages) {
        List<MessageTopicDataTObject> topics = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            MessageTopicDataTObject dataTObject = new MessageTopicDataTObject();
            dataTObject.setName(messages.get(i).getTopicName());
            dataTObject.setId(messages.get(i).getTopicId());
            dataTObject.setIcon(messages.get(i).getTopicIcon());
            topics.add(dataTObject);
        }
        return topics;
    }

}
