package com.eazytec.bpm.app.notice.notice.detail;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.eazytec.bpm.app.notice.R;
import com.eazytec.bpm.app.notice.data.AttachmentsDataTObject;
import com.eazytec.bpm.app.notice.data.NoticeDetailDataTObject;
import com.eazytec.bpm.app.notice.notice.list.NoticeListActivity;
import com.eazytec.bpm.appstub.view.textview.HtmlTextView;
import com.eazytec.bpm.lib.common.activity.ContractViewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知公告详情页
 * @author Beckett_W
 * @version Id: NoticeDetailActivity, v 0.1 2017/7/10 14:04 Administrator Exp $$
 */
public class NoticeDetailActivity  extends ContractViewActivity<NoticeDetailPresenter> implements NoticeDetailContract.View {

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private TextView titleTextView;
    private HtmlTextView contentTextView;
    private TextView authorTextView;
    private TextView timeTextView;

    private TextView attachmentsTextView;

    private String id;
    private List<AttachmentsDataTObject> attachments;
    private List<String> attachmentNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.bpm_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitleTextView.setText("公告内容");

        titleTextView = (TextView) findViewById(R.id.notice_detail_title_textview);
        contentTextView = (HtmlTextView) findViewById(R.id.notice_detail_content_textview);
        authorTextView = (TextView) findViewById(R.id.notice_detail_author_textview);
        timeTextView = (TextView) findViewById(R.id.notice_detail_time_textview);
        attachmentsTextView = (TextView) findViewById(R.id.notice_detail_attachments_textview);

        id = getIntent().getStringExtra("id");

        //差一个下载附件功能

        getPresenter().loadNoticeDetail(id);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoticeDetailActivity.this.finish();
            }
        });
    }

    @Override
    public void loadSuccess(NoticeDetailDataTObject dataTObject) {
        titleTextView.setText(dataTObject.getTitle());
        contentTextView.setRichText(dataTObject.getContent());
        authorTextView.setText(dataTObject.getCreatedBy());
        timeTextView.setText(dataTObject.getCreatedTime());

        if (dataTObject.getAttachments() != null && dataTObject.getAttachments().size() > 0) {
            this.attachmentsTextView.setVisibility(View.VISIBLE);
            this.attachments = dataTObject.getAttachments();

            this.attachmentNames = new ArrayList<>();

            for (AttachmentsDataTObject attach : attachments) {
                attachmentNames.add(attach.getName());
            }
        }
    }

    @Override
    protected NoticeDetailPresenter createPresenter() {
        return new NoticeDetailPresenter();
    }
}
