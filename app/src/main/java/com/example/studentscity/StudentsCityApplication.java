package com.example.studentscity;

import android.app.Application;

import com.example.studentscity.data.executor.ExecutorProvider;

public class StudentsCityApplication extends Application {
    @Override
    public void onTerminate() {
        super.onTerminate();
        ExecutorProvider.getInstance().shutdown();
    }
} 