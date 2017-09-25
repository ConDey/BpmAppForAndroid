package com.eazytec.bpm.app.notice.notice.detail;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.eazytec.bpm.app.notice.R;
import com.eazytec.bpm.app.notice.data.AttachmentsDataTObject;
import com.eazytec.bpm.app.notice.data.NoticeDetailDataTObject;
import com.eazytec.bpm.app.notice.notice.download.DownloadActivity;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.appstub.view.textview.htmltextview.HtmlTextView;
import com.eazytec.bpm.lib.common.activity.ContractViewActivity;
import com.eazytec.bpm.lib.utils.StringUtils;

import net.wequick.small.Small;

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

    //private TextView attachmentsTextView;
    private FloatingActionButton floatingActionButton;

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
        floatingActionButton = (FloatingActionButton) findViewById(R.id.notice_detail_download);


        id = getIntent().getStringExtra("id");

        Uri uri = Small.getUri(this);
        if (uri != null) {
            String numstring = uri.getQueryParameter("notice_id");
            if (!StringUtils.isSpace(numstring)) {
                id = numstring;
            }else{
                ToastDelegate.error(getContext(),"公告内容有问题，请联系管理员！");
            }
        }

        /**
        RxView.clicks(this.attachmentsTextView).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                     new MaterialDialog.Builder(getContext())
                             .title("请选择公告附件进行下载")
                              .items(attachmentNames)
                              .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                                  @Override
                                  public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                      DownloadHelper.download(NoticeDetailActivity.this, attachments.get(which).getId(), attachments.get(which).getName());
                                      return true;
                                  }
                              })
                              .positiveText("确认下载")
                              .negativeText("取消")
                              .negativeColor(Color.RED)
                              .show();
                    }
                });
         **/

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                startActivity(NoticeDetailActivity.this, DownloadActivity.class, bundle);
            }
        });

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
            this.floatingActionButton.setVisibility(View.VISIBLE);
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
