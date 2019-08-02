package cn.kingtous.diplomagenerator.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.squareup.haha.perflib.Main;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.kingtous.diplomagenerator.Base.BaseActivity;
import cn.kingtous.diplomagenerator.MainActivity;
import cn.kingtous.diplomagenerator.Models.InfoModel;
import cn.kingtous.diplomagenerator.R;
import cn.kingtous.diplomagenerator.Tools.BitmapTool;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.TextStyleBuilder;

public class PhotoEditActivity extends BaseActivity {

    @BindView(R.id.photoeditor)
    PhotoEditorView photoEditorView;
    PhotoEditor photoEditor;

    public static final String INTENT_INFO = "INFO";
    public static final String INTENT_OUTPUT_PATH = "OUTPUT_PATH";

    public static final int RESULT_OK = 1;
    public static final int RESULT_NONE = 0;
    public static final int RESULT_FAIL = -1;

    private InfoModel info;
    private String outputPath;
    private Handler mHandler= new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        initView();
        initTopBar();
        initEvent();
    }


    @Override
    protected void initView() {
        super.initView();
        info = new Gson().fromJson(getIntent().getStringExtra(INTENT_INFO),InfoModel.class);
        outputPath = getIntent().getStringExtra(INTENT_OUTPUT_PATH);
    }

    @SuppressLint("ResourceType")
    @Override
    protected void initEvent() {
        super.initEvent();
        setResult(RESULT_NONE);
        photoEditorView.getSource().setImageResource(R.raw.diploma);

        // 设置字体
        Typeface font=Typeface.createFromAsset(getAssets(),"fonts/linhuiti.ttf");
        photoEditor = new PhotoEditor.Builder(this,photoEditorView)
                .setPinchTextScalable(true)
                .setDefaultTextTypeface(font)
                .build();

        // 添加我们传入的内容
        TextStyleBuilder textStyleBuilder=new TextStyleBuilder();
        textStyleBuilder.withTextFont(font);
        textStyleBuilder.withTextColor(Color.rgb(0,0,0));
        textStyleBuilder.withTextSize(10f);


        photoEditor.addText(info.getName(),textStyleBuilder);
        photoEditor.addText(info.getAge(),textStyleBuilder);
        photoEditor.addText(info.getId(),textStyleBuilder);
        photoEditor.addImage(BitmapTool.decodeUri(this, Uri.fromFile(new File(info.getImagePath())),500,700));
        mTopBar.addRightTextButton("保存",R.layout.layout_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存操作
                //加载
                final QMUITipDialog loadDialog = new QMUITipDialog.Builder(PhotoEditActivity.this).setTipWord("正在保存").setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING).create();
                loadDialog.show();
                photoEditor.saveAsFile(outputPath, new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(@NonNull String imagePath) {
                        loadDialog.dismiss();
                        final QMUITipDialog dialog=new QMUITipDialog.Builder(PhotoEditActivity.this).setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                                .setTipWord("保存成功！").create();
                        dialog.show();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                setResult(RESULT_OK);
                                finish();
                            }
                        },1500);
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        loadDialog.dismiss();
                        final QMUITipDialog dialog=new QMUITipDialog.Builder(PhotoEditActivity.this).setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                                .setTipWord(getString(R.string.store_fail_reason_is)+exception.getMessage()).create();
                        dialog.show();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                setResult(RESULT_FAIL);
                            }
                        },1500);
                    }
                });
            }
        });
    }
}
