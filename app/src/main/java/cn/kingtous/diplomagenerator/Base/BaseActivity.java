package cn.kingtous.diplomagenerator.Base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qmuiteam.qmui.arch.QMUIActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import cn.kingtous.diplomagenerator.Activity.GenSuccessActivity;
import cn.kingtous.diplomagenerator.R;

public class BaseActivity extends QMUIActivity {

    protected QMUITopBar mTopBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
    }

    protected void initView(){
        mTopBar = findViewById(R.id.topbar);
    }

    protected void initTopBar() {
        mTopBar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_still, R.anim.slide_out_right);
            }
        });
        mTopBar.setTitle("毕业证生成器");
    }


    protected void initEvent(){

    }

    protected void gotoActivity(Class<?> cls){
        Intent intent=new Intent(this,cls);
        startActivity(intent);
    }


}
