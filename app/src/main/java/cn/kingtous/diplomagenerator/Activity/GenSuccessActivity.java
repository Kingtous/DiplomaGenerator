package cn.kingtous.diplomagenerator.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.kingtous.diplomagenerator.Base.BaseActivity;
import cn.kingtous.diplomagenerator.R;
import cn.kingtous.diplomagenerator.Tools.FileUtil;

public class GenSuccessActivity extends BaseActivity {


    @BindView(R.id.image_id)
    ImageView imageId;
    @BindView(R.id.btn_open_other)
    QMUIRoundButton btnOpenOther;
    @BindView(R.id.btn_send)
    QMUIRoundButton btnSend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);
        ButterKnife.bind(this);
        initView();
        initTopBar();
        initEvent();
    }

    @Override
    protected void initView() {
        super.initView();
        Glide.with(this).load(Uri.fromFile(new File(getIntent().getStringExtra(PhotoEditActivity.INTENT_OUTPUT_PATH)))).into(imageId);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil.sendFiles(GenSuccessActivity.this,getIntent().getStringExtra(PhotoEditActivity.INTENT_OUTPUT_PATH));
            }
        });
        btnOpenOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil.processFile(GenSuccessActivity.this,getIntent().getStringExtra(PhotoEditActivity.INTENT_OUTPUT_PATH));
            }
        });
    }
}
