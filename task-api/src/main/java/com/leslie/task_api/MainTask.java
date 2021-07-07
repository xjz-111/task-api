package com.leslie.task_api;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

/**
 * 主线程中的任务
 *
 * 作者：xjzhao
 * 时间：2021-06-29 10:26
 */
class MainTask {

    private Builder builder;
    private List<TaskInterceptor> interceptors;

    static Builder builder(){
        return new Builder();
    }


    /**
     * 暴露出去的启动优先级任务
     * @param context
     * @param tasks
     */
    static void startWithPriority(final Context context, List<MTask> tasks){
        MainTask.Builder builder = MainTask.builder();
        for (final MTask task : tasks){
            builder.addInterceptor(new TaskInterceptor() {
                @Override
                public void intercept(TaskChain chain) {
                    chain.proceed(context, task);
                }
            });
        }
        builder.build().startTaskWithInterceptorChain(context);
    }

    /**
     * 暴露出去的启动有延时的任务
     * @param context
     * @param tasks
     */
    static void startWithDelay(final Context context, List<MTask> tasks){
        Handler handler = new Handler(Looper.getMainLooper());
        for (final MTask task : tasks){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        task.task.init(context);
                        if (null != task.callback){
                            task.callback.onSuccess();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (null != task.callback){
                            task.callback.onFailureInThread(e);
                        }
                    }
                }
            }, task.delayMills);
        }
    }


    private MainTask(Builder builder) {
        this.builder = builder;
    }

    private MainTask addInterceptor(TaskInterceptor i){
        interceptors.add(i);
        return this;
    }

    private MainTask addInterceptor(List<TaskInterceptor> list){
        interceptors.addAll(list);
        return this;
    }


    /**
     * 有优先级的任务
     * @param context
     */
    private void startTaskWithInterceptorChain(Context context){
        interceptors = new ArrayList<>();

        addInterceptor(builder.beforeInterceptors);

        // 添加用户自定义之后的系统任务
        addInterceptor(new StartInterceptor());

        addInterceptor(builder.afterInterceptors);

        // 添加结尾的系统任务，目的是为了结束责任链的传递
        addInterceptor(new EndInterceptor());

        // 传递一个空任务启动任务链
        TaskInterceptor.TaskChain chain = new RealChain(interceptors, 0);
        chain.proceed(context, null);
    }



    /**
     * 为了扩展性，用构造者模式创建参数
     */
    public static class Builder{
        private final List<TaskInterceptor> beforeInterceptors = new ArrayList<>();
        private final List<TaskInterceptor> afterInterceptors = new ArrayList<>();

        private Builder() {
        }

        Builder addInterceptor(TaskInterceptor i){
            beforeInterceptors.add(i);
            return this;
        }

        public Builder addAfterInterceptor(TaskInterceptor i){
            afterInterceptors.add(i);
            return this;
        }

        public MainTask build(){
            return new MainTask(this);
        }
    }
}
