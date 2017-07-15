package com.eazytec.bpm.app.photo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eazytec.bpm.app.photo.adapter.FullyGridLayoutManager;
import com.eazytec.bpm.app.photo.adapter.GridImageAdapter;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.DebugUtil;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import rx.Observer;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{


    private int maxSelectNum = 9;  //选择的文件数量上限


    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private List<LocalMedia> selectList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;

    //控件
    private RadioGroup  rgb_photo_mode;

    private int compressMode = PictureConfig.SYSTEM_COMPRESS_MODE;  //压缩模式，系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
    private int themeId;
    private int chooseMode = PictureMimeType.ofAll();   //全部格式的多媒体

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        themeId = R.style.picture_Custom_style; //自定义的样式，可以自行修改

        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.bpm_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitleTextView.setText("多媒体选择器");

        rgb_photo_mode = (RadioGroup) findViewById(R.id.media_picker_photo_mode);
        rgb_photo_mode.setOnCheckedChangeListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.media_picker_recycler);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(MainActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(MainActivity.this,onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);  //设置最大选择多媒体文件数量
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(MainActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(MainActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(MainActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });

        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new io.reactivex.Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(MainActivity.this);
                } else {
                    ToastDelegate.error(MainActivity.this,"您没有读取内存卡的权限");
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });

    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {

                // 进入相册 ：不需要的api可以不写的
                PictureSelector.create(MainActivity.this)
                        .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                     //   .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(maxSelectNum)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(4)// 每行显示个数
                        .selectionMode(PictureConfig.MULTIPLE )// 多选 or 单选 PictureConfig.SINGLE
                        .previewImage(true)// 是否可预览图片
                        .previewVideo(true)// 是否可预览视频
                        .enablePreviewAudio(false) // 是否可播放音频
                        .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                        .isCamera(true)// 是否显示拍照按钮
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                        .enableCrop(false)// 是否裁剪,不需要剪裁
                        .compress(true)// 是否压缩
                        .compressMode(compressMode)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                        //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                     //   .withAspectRatio(0, 0)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                      //  .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                        .isGif(false)// 是否显示gif图片
                    //    .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                   //     .circleDimmedLayer(false)// 是否圆形裁剪
                   //     .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    //    .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .openClickSound(false)// 是否开启点击声音
                        .selectionMedia(selectList)// 是否传入已选图片
                        //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                        //.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
                        //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                        //.rotateEnabled() // 裁剪是否可旋转图片
                        //.scaleEnabled()// 裁剪是否可放大缩小图片
                        //.videoQuality()// 视频录制质量 0 or 1
                        //.videoSecond()//显示多少秒以内的视频or音频也可适用
                        //.recordVideoSecond()//录制视频秒数 默认60s
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            //选择要获取的多媒体格式：全部，照片，音频，视频
            case R.id.media_picker_all:
                chooseMode = PictureMimeType.ofAll();
                break;
            case R.id.media_picker_image:
                chooseMode = PictureMimeType.ofImage();
                break;
            case  R.id.media_picker_video:
                chooseMode = PictureMimeType.ofVideo();
                break;
            case R.id.media_picker_audio:
                chooseMode = PictureMimeType.ofAudio();
                break;
        }
    }

}
