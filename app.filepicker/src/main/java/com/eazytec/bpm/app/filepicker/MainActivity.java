package com.eazytec.bpm.app.filepicker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.eazytec.bpm.app.filepicker.filepicker.FilePickerActivity;
import com.eazytec.bpm.app.filepicker.filepicker.FilePickerBuilder;
import com.eazytec.bpm.app.filepicker.filepicker.FilePickerConst;
import com.eazytec.bpm.app.filepicker.models.Document;
import com.eazytec.bpm.app.filepicker.utils.PickerFileUtils;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.activity.CommonActivity;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.Collection;

import rx.functions.Action1;

/**
 * @author Administrator
 * @version Id: MainActivity, v 0.1 2017/7/18 16:11 Administrator Exp $$
 */
public class MainActivity extends CommonActivity {

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;


    private Button button;
    private GridView gridView;

    private int MAX_ATTACHMENT_COUNT = 5; //最多选择附件数,可以单选，多选，根据需要自定义
    private ArrayList<String> docPaths = new ArrayList<>();  //文件路径

    private Bitmap addPicBitmap;
    private AttachmentGridAdapter attachmentGridAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.bpm_toolbar_title);
        toolbarTitleTextView.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitleTextView.setText("文件选择器");

        addPicBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_common_addpic_focused);

        gridView = (GridView) findViewById(R.id.selected_file_gv);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        attachmentGridAdapter = new AttachmentGridAdapter(this);
        gridView.setAdapter(attachmentGridAdapter);
        attachmentGridAdapter.notifyDataSetChanged();

         initListener();

    }

    private void initListener(){

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                if(position == docPaths.size()){
                    // 附件格式
                    // final String[] zips = {".zip",".rar"};
                    // final String[] pdfs = {".pdf"};
                        RxPermissions rxPermissions = new RxPermissions(MainActivity.this);
                        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .subscribe(new Action1<Boolean>() {
                                    @Override
                                    public void call(Boolean aBoolean) {
                                        if(aBoolean){
                                           FilePickerBuilder.getInstance().setMaxCount(MAX_ATTACHMENT_COUNT)
                                               //     .setSelectedFiles(docPaths)
                                                    //  .addFileSupport("ZIP",zips,R.mipmap.ic_zip_box) 可以自定义选特定几个格式的附件，包括该格式的文件图片，这里加载所有格式，也可以在常量文件里自定义加载的文件格式
                                                    //  .addFileSupport("PDF",pdfs , R.mipmap.ic_pdf_box)
                                                 .pickFile(MainActivity.this);
                                            /**
                                            Intent intent1 = new Intent(MainActivity.this,FilePickerActivity.class);
                                            intent1.putStringArrayListExtra("FilePickerConst.KEY_SELECTED_DOCS",docPaths);
                                            intent1.putExtra("CUSTOM_MAX_COUNT","3");
                                            startActivityForResult(intent1,FilePickerConst.REQUEST_CODE_DOC);
                                             **/
                                        }else{
                                            ToastDelegate.info(getContext(),"您没有权限选择本机文件");
                                        }
                                    }
                                });

                } else{
                    new MaterialDialog.Builder(MainActivity.this)
                            .content("删除文件")
                            .inputType(InputType.TYPE_CLASS_TEXT)
                            .positiveText("确认")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    docPaths.remove(position);
                                    attachmentGridAdapter.notifyDataSetChanged();
                                }
                            })
                            .negativeText("取消")
                            .negativeColor(Color.BLUE)
                            .show();
                }
            }
        });

    }

    //返回路径
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case FilePickerConst.REQUEST_CODE_DOC:
                if(resultCode== Activity.RESULT_OK && data!=null)
                {
                    docPaths = new ArrayList<>();
                    docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                    attachmentGridAdapter.notifyDataSetChanged();

                }
                break;
        }
    }

    //附件的适配器
    @SuppressLint("HandlerLeak")
    public class AttachmentGridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition =-1;
        public AttachmentGridAdapter (Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            //MAX_FILE_NUM
            if(docPaths.size() >= MAX_ATTACHMENT_COUNT){
                return MAX_ATTACHMENT_COUNT;
            }
            return (docPaths.size()+1);
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_attachment_grid, parent, false);
                holder = new ViewHolder();
                holder.attactment = (ImageView) convertView.findViewById(R.id.item_attachment_image);
                holder.flietitle = (TextView) convertView.findViewById(R.id.item_attachment_textview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == docPaths.size()) {
                holder.attactment.setImageBitmap(addPicBitmap);  //文件图片
                holder.flietitle.setText("");
                if (position == MAX_ATTACHMENT_COUNT) {
                    holder.attactment.setVisibility(View.GONE);
                    holder.flietitle.setVisibility(View.GONE);
                }
            } else {
                holder.flietitle.setText(docPaths.get(position));  //文件地址
                holder.attactment.setImageResource(PickerFileUtils.getTypeDrawable(docPaths.get(position)));
               // holder.attactment.setVisibility(View.GONE);
            }
            return convertView;
        }

        public class ViewHolder {
            public ImageView attactment;
            public TextView  flietitle;
        }
    }

}
