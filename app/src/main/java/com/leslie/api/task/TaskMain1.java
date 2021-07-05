package com.leslie.api.task;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.leslie.task_annotation.Task;
import com.leslie.task_annotation.TaskType;
import com.leslie.task_api.InitTask;
import com.leslie.task_api.InitTaskCallback;

/**
 * 作者：xjzhao
 * 时间：2021-06-29 12:48
 */
@Task(priority = 1, thread = TaskType.MAIN_THREAD, delayMillis = 10000)
public class TaskMain1 implements InitTask {
    @Override
    public void init(@NonNull Context context) {
        Log.d("xjzhao", getClass().getSimpleName() + " init! delayMillis = 10000");
    }

    @Override
    public InitTaskCallback callback() {
        return new InitTaskCallback() {
            @Override
            public void onSuccess() {
                Log.i("xjzhao", TaskMain1.class.getSimpleName() + " init onSuccess! delayMillis = 10000");
            }

            @Override
            public void onSuccessInThread() {
                Log.e("xjzhao", TaskMain1.class.getSimpleName() + " init onSuccessInThread! delayMillis = 10000");

            }

            @Override
            public void onFailure(Exception e) {
                Log.e("xjzhao", TaskMain1.class.getSimpleName() + " init onFailure!");

            }

            @Override
            public void onFailureInThread(Exception e) {
                Log.v("xjzhao", TaskMain1.class.getSimpleName() + " init onFailureInThread!");

            }
        };
    }
}
