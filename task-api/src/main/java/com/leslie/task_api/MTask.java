package com.leslie.task_api;


import com.leslie.task_annotation.TaskMeta;
import com.leslie.task_annotation.TaskType;

/**
 * 任务封装
 *
 * 作者：xjzhao
 * 时间：2021-06-29 10:13
 */
class MTask {
    int priority;
    long delayMills;
    TaskType type;
    InitTaskCallback callback;
    InitTask task;

    MTask(TaskMeta meta) {
        if (null != meta) {
            try {
                this.task = (InitTask) meta.getCls().newInstance();
                this.priority = meta.getPriority();
                this.delayMills = meta.getDelayMills();
                this.type = meta.getType();
                this.callback = task.callback();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}