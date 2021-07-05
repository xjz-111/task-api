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
@Task
public class TaskThreadNo1 implements InitTask {
    @Override
    public void init(@NonNull Context context) {
        Log.d("xjzhao", getClass().getSimpleName() + " init!");
    }

    @Override
    public InitTaskCallback callback() {
        return new InitTaskCallback() {
            @Override
            public void onSuccess() {
                Log.i("xjzhao", TaskThreadNo1.class.getSimpleName() + " init onSuccess!");
            }

            @Override
            public void onSuccessInThread() {
                Log.e("xjzhao", TaskThreadNo1.class.getSimpleName() + " init onSuccessInThread!");

            }

            @Override
            public void onFailure(Exception e) {
                Log.e("xjzhao", TaskThreadNo1.class.getSimpleName() + " init onFailure!");

            }

            @Override
            public void onFailureInThread(Exception e) {
                Log.v("xjzhao", TaskThreadNo1.class.getSimpleName() + " init onFailureInThread!");

            }
        };
    }
}