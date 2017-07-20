package com.eazytec.bpm.app.notice.notice.download;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.eazytec.bpm.app.notice.R;
import com.eazytec.bpm.app.notice.adapter.DownloadListAdapter;
import com.eazytec.bpm.app.notice.data.AttachmentsDataTObject;
import com.eazytec.bpm.app.notice.data.NoticeDetailDataTObject;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.activity.ContractViewActivity;
import com.eazytec.bpm.lib.common.webservice.DownloadHelper;
import com.jakewharton.rxbinding.widget.RxAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * @author Administrator
 * @version Id: DownloadActivity, v 0.1 2017/7/12 12:25 Administrator Exp $$
 */
public class DownloadActivity extends ContractViewActivity<DownloadPresenter> implements DownloadContract.View {

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private ListView listView;
    private DownloadListAdapter downloadListAdapter;

    private String id;
    private List<AttachmentsDataTObject> attachments;
    private List<String> attachmentNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_download);

        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.bpm_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitleTextView.setText("附件列表");

        listView = (ListView) findViewById(R.id.activity_notice_download_listview);
        downloadListAdapter = new DownloadListAdapter(getContext());
        listView.setAdapter(downloadListAdapter);
        listView.setFocusable(false);

        id = getIntent().getStringExtra("id");
        getPresenter().loadNoticeDetail(id);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadActivity.this.finish();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              DownloadHelper.download(DownloadActivity.this, attachments.get(position).getId(), attachments.get(position).getName(), true, null);
            }
        });

    }

    @Override
    public void loadSuccess(NoticeDetailDataTObject dataTObject) {

        //只加载附件需要的信息
        if (dataTObject.getAttachments() != null && dataTObject.getAttachments().size() > 0) {

            this.attachments = dataTObject.getAttachments();
            this.attachmentNames = new ArrayList<>();
            for (AttachmentsDataTObject attach : attachments) {
                attachmentNames.add(attach.getName());
            }
            downloadListAdapter.resetList(dataTObject.getAttachments());
            downloadListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected DownloadPresenter createPresenter() {
        return new DownloadPresenter();
    }
}
