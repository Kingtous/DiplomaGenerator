package cn.kingtous.diplomagenerator.Application;

import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        QMUISwipeBackActivityManager.init(this);
    }
}
