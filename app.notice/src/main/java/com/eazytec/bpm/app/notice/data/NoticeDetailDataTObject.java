package com.eazytec.bpm.app.notice.data;

import com.eazytec.bpm.lib.common.webservice.WebDataTObject;

import java.util.List;

/**
 * @author Administrator
 * @version Id: NoticeDetailDataTObject, v 0.1 2017/7/10 9:39 Administrator Exp $$
 */
public class NoticeDetailDataTObject extends WebDataTObject {

    private String id;

    private String title;

    private String createdBy;

    private String createdTime;

    private String content;

    private List<AttachmentsDataTObject> attachments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<AttachmentsDataTObject> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentsDataTObject> attachments) {
        this.attachments = attachments;
    }
}

