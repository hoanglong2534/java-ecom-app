package com.longg.gky;

import android.app.Application;

import com.longg.gky.data.DBRepository;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Khởi tạo DBRepository một lần duy nhất tại đây
        DBRepository.init(this);
    }
}
