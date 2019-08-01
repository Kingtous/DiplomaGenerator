package cn.kingtous.diplomagenerator.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.kingtous.diplomagenerator.Base.BaseActivity;

public class PhotoPickActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initTopBar();
    }



}
