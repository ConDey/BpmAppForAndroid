package com.eazytec.bpm.lib.common.message.dataobject;


import com.eazytec.bpm.lib.common.webservice.WebDataTObject;

import java.util.List;

/**
 * @author 16735
 * @version Id: MessageListDataTObject, v 0.1 2017-8-24 8:50 16735 Exp $$
 */
public class MessageListDataTObject extends WebDataTObject {

    private List<MessageDataTObject> messages;

    public List<MessageDataTObject> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDataTObject> messages) {
        this.messages = messages;
    }
}
