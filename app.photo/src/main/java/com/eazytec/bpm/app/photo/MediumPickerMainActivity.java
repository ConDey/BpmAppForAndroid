package com.eazytec.bpm.app.photo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eazytec.bpm.app.photo.adapter.FullyGridLayoutManager;
import com.eazytec.bpm.app.photo.adapter.GridImageAdapter;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.activity.CommonActivity;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import rx.functions.Action1;

import static com.luck.picture.lib.config.PictureConfig.LUBAN_COMPRESS_MODE;

public class MediumPickerMainActivity extends CommonActivity implements RadioGroup.OnCheckedChangeListener ,CompoundButton.OnCheckedChangeListener{


    private int maxSelectNum = 9;  //选择的文件数量上限

    private Bitmap addPicBitmap;

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private List<LocalMedia> selectList = new ArrayList<>();


    //图片显示相关
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;

    //照片模式相关
    private RadioGroup rgb_medium_mode;

    //压缩相关
    private CheckBox cb_compress;
    private RadioGroup rgb_compress;

    //裁剪功能相关
    private RadioGroup rgb_crop;
    private CheckBox cb_crop,cb_crop_circular,cb_styleCrop, cb_showCropFrame,cb_showCropGrid,cb_hide;
    private int aspect_ratio_x, aspect_ratio_y;

    private int compressMode = PictureConfig.SYSTEM_COMPRESS_MODE;  //压缩模式，系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
    private int themeId;
    private int chooseMode = PictureMimeType.ofAll();   //默认全部格式的多媒体全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo(),音频 ofAudio

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       themeId = R.style.picture_Custom_style;  //自定义主题样式

        addPicBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_addpic_focused);

        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.bpm_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitleTextView.setText("多媒体选择器");

        rgb_medium_mode = (RadioGroup) findViewById(R.id.rgb_medium_mode);
        rgb_medium_mode.setOnCheckedChangeListener(this);

        //压缩
        cb_compress = (CheckBox) findViewById(R.id.medium_cb_compress);
        cb_compress.setOnCheckedChangeListener(this);
        rgb_compress = (RadioGroup) findViewById(R.id.rgb_medium_compress);
        rgb_compress.setOnCheckedChangeListener(this);

        //剪裁
        cb_crop = (CheckBox) findViewById(R.id.medium_cb_crop);
        cb_crop.setOnCheckedChangeListener(this);
        rgb_crop = (RadioGroup) findViewById(R.id.medium_rgb_crop);
        rgb_crop.setOnCheckedChangeListener(this);
        cb_crop_circular = (CheckBox) findViewById(R.id.medium_cb_crop_circular);
        cb_crop_circular.setOnCheckedChangeListener(this);
        cb_styleCrop = (CheckBox)findViewById(R.id.medium_cb_styleCrop);
        cb_styleCrop.setOnCheckedChangeListener(this);
        cb_showCropFrame = (CheckBox) findViewById(R.id.medium_cb_showCropFrame);
        cb_showCropFrame.setOnCheckedChangeListener(this);
        cb_showCropGrid = (CheckBox) findViewById(R.id.medium_cb_showCropGrid);
        cb_showCropGrid.setOnCheckedChangeListener(this);
        cb_hide = (CheckBox) findViewById(R.id.medium_cb_hide);
        cb_hide.setOnCheckedChangeListener(this);


        recyclerView = (RecyclerView) findViewById(R.id.medium_recyclerview);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(MediumPickerMainActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        adapter = new GridImageAdapter(MediumPickerMainActivity.this,onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
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
                            //PictureSelector.create(MediumPickerMainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(MediumPickerMainActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(MediumPickerMainActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(MediumPickerMainActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediumPickerMainActivity.this.finish();
            }
        });

        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(MediumPickerMainActivity.this);
                } else {
                    Toast.makeText(MediumPickerMainActivity.this,
                            getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });

    }

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


    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {

            PictureSelector.create(MediumPickerMainActivity.this)
                    .openGallery(chooseMode)//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                    .theme(themeId)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量 int
                    .minSelectNum(1)// 最小选择数量 int
                    .imageSpanCount(4)// 每行显示个数 int
                    .selectionMode(PictureConfig.MULTIPLE )// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                    .previewImage(true)// 是否可预览图片 true or false
                    .previewVideo(true)// 是否可预览视频 true or false
                    .enablePreviewAudio(true) // 是否可播放音频 true or false
                    .isCamera(true)// 是否显示拍照按钮 true or false
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效

                    //压缩功能设置
                    .compress(cb_compress.isChecked())
                    .compressMode(compressMode)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                    .compressGrade(Luban.FIRST_GEAR)// luban压缩档次，默认3档 Luban.THIRD_GEAR、Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                    //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                    // 是否裁剪 true or false
                    .enableCrop(cb_crop.isChecked())
                    .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪 true or false
                    .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                    .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                    .glideOverride(160,160)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(aspect_ratio_x,aspect_ratio_y)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(cb_hide.isChecked()?false:true)// 是否显示uCrop工具栏，默认不显示 true or false
                    .freeStyleCropEnabled(cb_styleCrop.isChecked())// 裁剪框是否可拖拽 true or false

                    .isGif(false)// 是否显示gif图片 默认false
                    .openClickSound(false)// 是否开启点击声音 true or false
                    .selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
                    .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                    //.cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                    //.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效 int
                    //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效  int
                    // .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                    //.rotateEnabled() // 裁剪是否可旋转图片 true or false
                    //.scaleEnabled()// 裁剪是否可放大缩小图片 true or false
                    // .videoQuality()// 视频录制质量 0 or 1 int
                    // .videoSecond()// 显示多少秒以内的视频or音频也可适用 int
                    // .recordVideoSecond()//视频秒数录制 默认60s int
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }
    };


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.medium_rb_all:
                chooseMode = PictureMimeType.ofAll(); //所有媒体类型
                cb_compress.setVisibility(View.VISIBLE);
                cb_crop.setVisibility(View.VISIBLE);
                break;
            case R.id.medium_rb_image:
                chooseMode = PictureMimeType.ofImage(); //光图片
                cb_compress.setVisibility(View.VISIBLE);
                cb_crop.setVisibility(View.VISIBLE);
                break;
            case R.id.medium_rb_video:
                chooseMode = PictureMimeType.ofVideo(); //光视频
                cb_compress.setVisibility(View.GONE);
                cb_crop.setVisibility(View.GONE);
                cb_hide.setVisibility( View.GONE);
                cb_crop_circular.setVisibility( View.GONE);
                rgb_crop.setVisibility(View.GONE);
                cb_styleCrop.setVisibility(View.GONE);
                cb_showCropFrame.setVisibility( View.GONE);
                cb_showCropGrid.setVisibility(View.GONE);
                break;
            case R.id.medium_rb_audio:
                chooseMode = PictureMimeType.ofAudio(); //光音频
                cb_compress.setVisibility(View.GONE);
                cb_crop.setVisibility(View.GONE);
                rgb_crop.setVisibility(View.GONE);
                cb_hide.setVisibility( View.GONE);
                cb_crop_circular.setVisibility( View.GONE);
                cb_styleCrop.setVisibility(View.GONE);
                cb_showCropFrame.setVisibility( View.GONE);
                cb_showCropGrid.setVisibility(View.GONE);
                break;
            //压缩的两种方式
            case R.id.medium_rb_compress_system:
                compressMode = PictureConfig.SYSTEM_COMPRESS_MODE;
                break;
            case R.id.medium_rb_compress_luban:
                compressMode = PictureConfig.LUBAN_COMPRESS_MODE;
                break;
            //裁剪
            case R.id.medium_rb_crop_default:
                aspect_ratio_x = 0;
                aspect_ratio_y = 0;
                break;
            case R.id.medium_rb_crop_1to1:
                aspect_ratio_x = 1;
                aspect_ratio_y = 1;
                break;
            case R.id.medium_rb_crop_3to4:
                aspect_ratio_x = 3;
                aspect_ratio_y = 4;
                break;
            case R.id.medium_rb_crop_3to2:
                aspect_ratio_x = 3;
                aspect_ratio_y = 2;
                break;
            case R.id.medium_rb_crop_16to9:
                aspect_ratio_x = 16;
                aspect_ratio_y = 9;
                break;
        }
    }

    private int x = 0, y = 0;
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.medium_cb_compress:
                     rgb_compress.setVisibility(isChecked? View.VISIBLE : View.GONE);
                     break;
            case R.id.medium_cb_crop:
                     rgb_crop.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                     cb_hide.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                     cb_crop_circular.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                     cb_styleCrop.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                     cb_showCropFrame.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                     cb_showCropGrid.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                     break;
            case R.id.medium_cb_crop_circular:
                if (isChecked) {
                    x = aspect_ratio_x;
                    y = aspect_ratio_y;
                    aspect_ratio_x = 1;
                    aspect_ratio_y = 1;
                } else {
                    aspect_ratio_x = x;
                    aspect_ratio_y = y;
                }
                rgb_crop.setVisibility(isChecked ? View.GONE : View.VISIBLE);
                if (isChecked) {
                    cb_showCropFrame.setChecked(false);
                    cb_showCropGrid.setChecked(false);
                } else {
                    cb_showCropFrame.setChecked(true);
                    cb_showCropGrid.setChecked(true);
                }
                break;
        }
    }
}
