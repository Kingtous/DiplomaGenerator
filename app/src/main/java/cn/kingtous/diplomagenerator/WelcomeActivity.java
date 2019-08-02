package cn.kingtous.diplomagenerator;

import android.os.Bundle;
import android.os.Handler;

import com.stephentuso.welcome.TitlePage;
import com.stephentuso.welcome.WelcomeConfiguration;

public class WelcomeActivity extends com.stephentuso.welcome.WelcomeActivity {

    private Handler mHandler=new Handler();
    // 启动图
    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultBackgroundColor(R.color.app_color_blue)
                .page(new TitlePage(R.drawable.welcome_screen,
                        "Diploma Generator")
                )
                .swipeToDismiss(true)
                .showActionBarBackButton(false)
                .showNextButton(false)
                .showNextButton(false)
                .useCustomDoneButton(true)
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },2000);
    }
}
