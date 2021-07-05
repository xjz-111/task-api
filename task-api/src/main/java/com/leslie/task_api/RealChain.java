package com.leslie.task_api;

import android.content.Context;

import java.util.List;

/**
 * 主线程中的任务按照有优先级设计，责任链模式调用
 *
 * 作者：xjzhao
 * 时间：2021-06-29 10:46
 */
class RealChain implements TaskInterceptor.TaskChain{
    private List<TaskInterceptor> interceptors;
    private int index;

    RealChain(List<TaskInterceptor> interceptors, int index) {
        this.interceptors = interceptors;
        this.index = index;
    }

    @Override
    public void proceed(Context context, MTask task) {
        if (index >= interceptors.size()) throw new AssertionError();

        if (null != task) {
            try {
                task.task.init(context);
                if (null != task.callback){
                    task.callback.onSuccess();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (null != task.callback){
                    task.callback.onFailure(e);
                }
            }
        }

        TaskInterceptor interceptor = interceptors.get(index);
        interceptor.intercept(new RealChain(interceptors, index + 1));
    }
}
