package com.leslie.api.task;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.leslie.task_annotation.Task;
import com.leslie.task_api.InitTask;
import com.leslie.task_api.InitTaskCallback;

/**
 * 作者：xjzhao
 * 时间：2021-07-02 01:18
 */
@Task(delayMillis = 10000)
public class TaskThreadNo2 implements InitTask {
    @Override
    public void init(@NonNull Context context) {
        Log.d("xjzhao", getClass().getSimpleName() + " init! delayMillis = 10000");
    }

    @Override
    public InitTaskCallback callback() {
        return new InitTaskCallback() {
            @Override
            public void onSuccess() {
                Log.i("xjzhao", TaskThreadNo2.class.getSimpleName() + " init onSuccess! delayMillis = 10000");
            }

            @Override
            public void onSuccessInThread() {
                Log.e("xjzhao", TaskThreadNo2.class.getSimpleName() + " init onSuccessInThread! delayMillis = 10000");

            }

            @Override
            public void onFailure(Exception e) {
                Log.e("xjzhao", TaskThreadNo2.class.getSimpleName() + " init onFailure! delayMillis = 10000");

            }

            @Override
            public void onFailureInThread(Exception e) {
                Log.v("xjzhao", TaskThreadNo2.class.getSimpleName() + " init onFailureInThread! delayMillis = 10000");

            }
        };
    }
}