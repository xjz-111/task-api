package com.leslie.task_api;

/**
 * 任务处理监听回调，对于在子线程中的，会有子线程中的成功/失败回调，也会切换到主线程中对成功/失败做监听
 *
 * 作者：xjzhao
 * 时间：2021-06-29 10:07
 */
public interface InitTaskCallback {

    /**
     * 任务完成，并且回调是在主线程中
     */
    void onSuccess();

    /**
     * 任务完成，并且回调还在子线程中。对于仅在主线线程中初始化的任务，该回调不执行
     */
    void onSuccessInThread();

    /**
     * 任务失败，并且回调是在主线程中
     * @param e
     */
    void onFailure(Exception e);


    /**
     * 任务失败，并且回调是在主线程中。对于仅在主线线程中初始化的任务，该回调不执行
     * @param e
     */
    void onFailureInThread(Exception e);
}
