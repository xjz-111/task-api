package com.leslie.task_api;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.leslie.task_annotation.TaskMeta;
import com.leslie.task_annotation.TaskType;
import com.leslie.task_api.utils.logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 作者：xjzhao
 * 时间：2021-06-29 11:31
 */
class TaskDispatcher {

    private static TaskDispatcher instance;
    private final int TASK_SUCCESS = 1;
    private final int TASK_FAILURE = 2;
    private final String KEY_ERR = "err";
    // 执行无优先级任务的线程池
    private ExecutorService executor;
    // 执行有优先级任务的线程池
    private ExecutorService executorPriority;
    // 无优先级的任务列表-子线程
    private final List<MTask> threadTasks = new ArrayList<>();
    // 有优先级的任务类别-子线程
    private final List<MTask> threadTasksPrioritys = new ArrayList<>();
    // 主线程中执行的任务列表-主线程
    private final List<MTask> mainTasks = new ArrayList<>();
    // 主线程中延时的任务-忽略优先级
    private final List<MTask> mainDelayTasks = new ArrayList<>();
    // 无优先级的线程池的核心线程数取CPU核心数
    private int corePoolSize = Runtime.getRuntime().availableProcessors();

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            MTask task = (MTask) msg.obj;
            if (null != task && null != task.callback){
                InitTaskCallback callback = task.callback;
                int code = msg.what;
                if (code == TASK_SUCCESS){
                    callback.onSuccess();
                }else {
                    Bundle bundle = msg.getData();
                    Exception e = (Exception) bundle.getSerializable(KEY_ERR);
                    callback.onFailure(e);
                }
            }
        }
    };


    static TaskDispatcher getInstance() {
        if (null == instance){
            synchronized (TaskDispatcher.class){
                if (null == instance){
                    instance = new TaskDispatcher();
                }
            }
        }
        return instance;
    }

    private TaskDispatcher() {
        executorService();
    }


    synchronized void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        ((ThreadPoolExecutor)executorPriority).setCorePoolSize(corePoolSize);
    }

    private synchronized void executorService() {

        executor = new ThreadPoolExecutor(
                corePoolSize,
                corePoolSize,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                threadFactory("ThreadTasks Dispatcher", false));

        executorPriority = new ThreadPoolExecutor(
                1,
                1,
                0,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                threadFactory("ThreadTasksPriority Dispatcher", false));


    }

    private ThreadFactory threadFactory(final String name, final boolean daemon) {
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread result = new Thread(runnable, name);
                result.setDaemon(daemon);
                return result;
            }
        };
    }

    synchronized TaskDispatcher addTask(@NonNull List<TaskMeta> tasks){
       for (TaskMeta meta : tasks){
           addTask(new MTask(meta));
       }
        return instance;
    }

    private void addTask(@NonNull MTask task){
        if (task.type == TaskType.CHILD_THREAD){
            if (task.priority < 0){
                threadTasks.add(task);
            }else {
                threadTasksPrioritys.add(task);
            }
        }else {
            if (task.delayMills > 0){
                mainDelayTasks.add(task);
            }else {
                mainTasks.add(task);
            }
        }
    }

    synchronized void execute(@NonNull final Context context){
        logger.info("任务启动");
        Collections.sort(mainTasks, new Comparator<MTask>() {
            @Override
            public int compare(MTask o1, MTask o2) {
                return o1.priority - o2.priority;
            }
        });

        Collections.sort(threadTasksPrioritys, new Comparator<MTask>() {
            @Override
            public int compare(MTask o1, MTask o2) {
                return o1.priority - o2.priority;
            }
        });


        // 1. 主线程中的有优先级的任务 - 责任链模式处理保证顺序
        MainTask.startWithPriority(context, mainTasks);

        // 2. 主线程中的延时任务直接启动处理
        MainTask.startWithDelay(context, mainDelayTasks);

        // 3. 无优先级的任务 - 有延时的任务就在这里同步处理了
        enqueue(context, threadTasks, executor, new Handler(Looper.getMainLooper()));

        // 4. 有优先级的任务
        enqueue(context, threadTasksPrioritys, executorPriority, null);
    }

    private void enqueue(@NonNull final Context context, List<MTask> list, final ExecutorService service, Handler handler){
        for (final MTask task : list){
            if (null != handler){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enqueue(context, task, service);
                    }
                }, task.delayMills);

            }else {
                enqueue(context, task, service);
            }
        }
    }

    /**
     * 执行任务
     */
    private void enqueue(@NonNull final Context context, @NonNull final MTask task, ExecutorService service){
        service.execute(new Runnable() {
            @Override
            public void run() {
                InitTaskCallback callback = task.callback;
                Message msg = Message.obtain();
                msg.obj = task;
                try {
                    task.task.init(context);

                    if (null != callback){
                        callback.onSuccessInThread();
                        msg.what = TASK_SUCCESS;
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                    if (null != callback){
                        callback.onFailureInThread(e);
                        msg.what = TASK_FAILURE;
                        handler.sendMessage(msg);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(KEY_ERR, e);
                        msg.setData(bundle);
                    }
                } finally {
                    handler.sendMessage(msg);
                }
            }
        });
    }


}
