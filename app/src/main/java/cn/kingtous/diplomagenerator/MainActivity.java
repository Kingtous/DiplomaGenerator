package cn.kingtous.diplomagenerator;

import android.os.Bundle;
import android.view.View;

import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import cn.kingtous.diplomagenerator.Activity.GenSuccessActivity;
import cn.kingtous.diplomagenerator.Base.BaseActivity;

public class MainActivity extends BaseActivity {

    QMUIRoundButton btn_confirm;
    QMUIRoundButton btn_photo_pick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }


    @Override
    protected void initView() {
        super.initView();
        btn_confirm=findViewById(R.id.btn_confirm);
        btn_photo_pick=findViewById(R.id.btn_photo_pick);
        initTopBar();
    }


    @Override
    protected void initEvent() {
        super.initEvent();
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(GenSuccessActivity.class);
            }
        });
    }
}
