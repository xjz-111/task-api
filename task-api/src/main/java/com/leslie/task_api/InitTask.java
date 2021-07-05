package com.leslie.task_api;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * 任务需要实现的接口
 *
 * 作者：xjzhao
 * 时间：2021-06-29 09:51
 */
public interface InitTask {


    /**
     * 加任务实际内容
     */
    void init(@NonNull Context context);


    /**
     * 获取任务监听器
     * @return
     */
    InitTaskCallback callback();

}
