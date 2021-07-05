package com.leslie.task_api;

/**
 * 结束责任链
 *
 * 作者：xjzhao
 * 时间：2021-06-29 10:36
 */
final class EndInterceptor implements TaskInterceptor {
    @Override
    public void intercept(TaskChain chain) {
        /**
         * 不调用chain.proceed() 让链传递断掉
         */
    }
}
