package com.eazytec.bpm.lib.common.message.dataobject;


import com.eazytec.bpm.lib.common.webservice.WebDataTObject;

/**
 * @author 16735
 * @version Id: MessageTopicDataTObject, v 0.1 2017-8-24 8:39 16735 Exp $$
 */
public class MessageTopicListDataTObject extends WebDataTObject {

    private MessageTopicDataTObject topic;

    public MessageTopicDataTObject getTopic() {
        return topic;
    }

    public void setTopic(MessageTopicDataTObject topic) {
        this.topic = topic;
    }
}
