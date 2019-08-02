package cn.kingtous.diplomagenerator.Application;

import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.squareup.leakcanary.LeakCanary;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化QMUI
        QMUISwipeBackActivityManager.init(this);
        // 内存泄露
//        if (LeakCanary.isInAnalyzerProcess(this)) {//1
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
    }
}
