package com.example.easedemo;

import android.app.Application;
import com.hyphenate.easeui.EaseUI;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        EaseUI.getInstance().init(this, null);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }
}
