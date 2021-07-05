package com.leslie.api;

import android.app.Application;

import com.leslie.task_api.TaskManager;


/**
 * 作者：xjzhao
 * 时间：2021-06-28 16:40
 */
public class MApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TaskManager.getInstance().isDebug(false);

    }

}
