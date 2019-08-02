package cn.kingtous.diplomagenerator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.isseiaoki.simplecropview.FreeCropImageView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.util.InnerToaster;
import com.lzy.imagepicker.view.CropImageView;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.stephentuso.welcome.WelcomeHelper;


import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.kingtous.diplomagenerator.Activity.GenSuccessActivity;
import cn.kingtous.diplomagenerator.Activity.PhotoEditActivity;
import cn.kingtous.diplomagenerator.Adapter.GlideImageLoader;
import cn.kingtous.diplomagenerator.Base.BaseActivity;
import cn.kingtous.diplomagenerator.Models.InfoModel;
import cn.kingtous.diplomagenerator.Tools.CustomKeyboardEditText;
import cn.kingtous.diplomagenerator.Tools.FileUtil;
import cn.kingtous.diplomagenerator.Tools.FileUtils;
import cn.kingtous.diplomagenerator.Tools.IDchecker;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivity extends BaseActivity {

    @BindView(R.id.input_name)
    MaterialEditText inputName;
    @BindView(R.id.input_age)
    MaterialEditText inputAge;
    @BindView(R.id.input_id)
    CustomKeyboardEditText inputId;
    @BindView(R.id.btn_photo_pick)
    QMUIRoundButton btn_PhotoPick;
    @BindView(R.id.btn_confirm)
    QMUIRoundButton btn_Confirm;


    QMUIPopup mNormalPopup;

    boolean isIDcorrect = false;

    String imagePath;
    String outputpath;

    public static final int IMAGE_PICKER = 1;
    public static final int IMAGE_EDITOR = 2;
    public static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 3;

    private Handler mHandler=new Handler();

    @BindView(R.id.image_id)
    ImageView imageId;
    @BindView(R.id.input_storage)
    MaterialEditText inputStorage;

    WelcomeHelper welcomeScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        welcomeScreen = new WelcomeHelper(this, WelcomeActivity.class);
        startActivity(new Intent(this,WelcomeActivity.class));
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        welcomeScreen.onSaveInstanceState(outState);
    }

    @Override
    protected void initView() {
        super.initView();
        initTopBar();
    }

    @Override
    protected void initTopBar() {
        mTopBar.setTitle("毕业证生成器");
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        inputId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 检验输入的是否为身份证号
                    String input_id = inputId.getText().toString();
                    if (!IDchecker.isIDNumber(input_id)) {
                        inputId.setError("身份证输入信息有误，请检查！");
                    } else {
                        isIDcorrect = true;
                        inputId.setError(null);
                    }

                }
            }
        });

        inputId.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String default_filename=inputName.getText().toString()+'_'+s.toString();
                inputStorage.setText(default_filename);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String default_filename=s.toString()+'_'+inputId.getText().toString();
                inputStorage.setText(default_filename);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

    }

    private void initNormalPopupIfNeed(String text) {
        if (mNormalPopup == null) {
            mNormalPopup = new QMUIPopup(this, QMUIPopup.DIRECTION_NONE);
            TextView textView = new TextView(this);
            textView.setLayoutParams(mNormalPopup.generateLayoutParam(
                    QMUIDisplayHelper.dp2px(this, 250),
                    WRAP_CONTENT
            ));
            textView.setLineSpacing(QMUIDisplayHelper.dp2px(this, 4), 1.0f);
            int padding = QMUIDisplayHelper.dp2px(this, 20);
            textView.setPadding(padding, padding, padding, padding);
            textView.setText(text);
            textView.setTextColor(ContextCompat.getColor(this, R.color.app_color_description));
            mNormalPopup.setContentView(textView);
        }
    }


    @OnClick({R.id.btn_photo_pick, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_photo_pick:
                ImagePicker imagePicker = ImagePicker.getInstance();
                imagePicker.setMultiMode(false);
                imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
                imagePicker.setShowCamera(true);  //显示拍照按钮
                imagePicker.setCrop(true);        //允许裁剪（单选才有效）
                imagePicker.setFreeCrop(true, FreeCropImageView.CropMode.FREE);//新版添加,自由裁剪，优先于setCrop
                imagePicker.setSaveRectangle(true); //是否按矩形区域保存
                imagePicker.setSelectLimit(1);    //选中数量限制
                imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
                imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
                imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
                imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
                imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
                imagePicker.setIToaster(this, new InnerToaster.IToaster() {
                    @Override
                    public void show(String msg) {
                        final QMUITipDialog dialog = new QMUITipDialog.Builder(MainActivity.this).setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO).setTipWord(msg).create();
                        dialog.show();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        },1500);
                    }

                    @Override
                    public void show(int resId) {
                        final QMUITipDialog dialog = new QMUITipDialog.Builder(MainActivity.this).setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO).setTipWord(getResources().getString(resId)).create();
                        dialog.show();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        },1500);
                    }
                });//设置吐司代理,保持lib与app中吐司风格一致
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
                break;
            case R.id.btn_confirm:
                if (imagePath == null) {
                    final QMUITipDialog dialog = new QMUITipDialog.Builder(this).setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL).setTipWord(getString(R.string.please_select_photo)).create();
                    dialog.show();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    },1500);
                    break;
                } else {
                    // 检测权限
                    String[] permissions = new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    };
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, permissions, PERMISSION_WRITE_EXTERNAL_STORAGE);
                    }
                    else {
                        // 将值传入infoModel，然后进入Activity
                        InfoModel model = new InfoModel(imagePath, inputAge.getText().toString(), inputId.getText().toString(), inputName.getText().toString(), null);
                        outputpath=Environment.getExternalStorageDirectory()+File.separator+"Pictures"+File.separator+"DiplomaGen"+File.separator+inputStorage.getText().toString()+".png";
                        if (inputStorage.getText().toString().equals("") || FileUtil.checkFileExist(outputpath)){
                            final QMUITipDialog dialog = new QMUITipDialog.Builder(this).setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO).setTipWord(getString(R.string.file_exists_empty)).create();
                            dialog.show();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                }
                            },1500);
                            break;
                        }
                        //生成一个空文件
                        FileUtils.genEditFile(inputStorage.getText().toString()+".png");
                        Intent mIntent=new Intent(this, PhotoEditActivity.class);
                        mIntent.putExtra(PhotoEditActivity.INTENT_INFO,new Gson().toJson(model));
                        mIntent.putExtra(PhotoEditActivity.INTENT_OUTPUT_PATH,outputpath);
                        startActivityForResult(mIntent,IMAGE_EDITOR);
                    }

                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_EDITOR){
            if (resultCode == PhotoEditActivity.RESULT_OK){
                Intent intent=new Intent(this, GenSuccessActivity.class);
                intent.putExtra(PhotoEditActivity.INTENT_OUTPUT_PATH,outputpath);
                FileUtil.ablumUpdate(this,outputpath);
                startActivity(intent);
            }
        }
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                ImageItem item = images.get(0);
                Glide.with(this).load(Uri.fromFile(new File(item.path))).into(imageId);
                imagePath = item.path;
            } else {
                final QMUITipDialog dialog = new QMUITipDialog.Builder(this).setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO).setTipWord(getString(R.string.cancel_operation)).create();
                dialog.show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                },1500);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    final QMUITipDialog dialog = new QMUITipDialog.Builder(this).setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS).setTipWord(getString(R.string.permission_request_success)).create();
                    dialog.show();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    },1500);
                }
                break;
            default:
                break;

        }
    }

}
