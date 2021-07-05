package com.leslie.task_api;

import android.content.Context;

/**
 * 主线程中的任务，用责任链模式保证顺序而已
 *
 * 作者：xjzhao
 * 时间：2021-06-29 10:17
 */
interface TaskInterceptor {

    void intercept(TaskChain chain);

    interface TaskChain {

        void proceed(Context context, MTask task);
    }
}
