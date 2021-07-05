package com.leslie.task_api;

import com.leslie.task_annotation.Constant;
import com.leslie.task_api.utils.logger;

/**
 * 任务启动管理器
 *
 * 作者：xjzhao
 * 时间：2021-06-29 10:13
 */
public class TaskManager {

    private static TaskManager instance;

    private TaskManager() throws Exception {
        if (null != instance){
            throw new Exception("This instance has been!");
        }
    }

    public static TaskManager getInstance() {
        if (null == instance){
            synchronized (TaskManager.class){
                if (null == instance){
                    try {
                        instance = new TaskManager();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }

    /**
     * 有优先级的子线程中任务处理的线程池的核心线程数
     * @param corePoolSize
     * @return
     */
    public TaskManager setCorePoolSize(int corePoolSize){
        TaskDispatcher.getInstance().setCorePoolSize(corePoolSize);
        return instance;
    }

    /**
     * 开启debug
     * @param isDebug
     * @return
     */
    public TaskManager isDebug(boolean isDebug){
        Constant.isDebug = isDebug;
        logger.showLog(isDebug);
        return instance;
    }


//    /**
//     * 暴露出去的初始化，初始化在ContentProvider中自动做了
//     * @param context
//     */
//    public void init(Context context){
//        _Init.init(context);
//
//    }



}
