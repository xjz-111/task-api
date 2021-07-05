package com.leslie.task_api;

/**
 * 开启责任链传递
 *
 * 作者：xjzhao
 * 时间：2021-06-29 10:33
 */
final class StartInterceptor implements TaskInterceptor {

    @Override
    public void intercept(TaskChain chain) {
        chain.proceed(null, null);
    }
}
