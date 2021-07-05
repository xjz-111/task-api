package com.leslie.task_api;

import android.content.Context;

import com.leslie.task_annotation.Constant;
import com.leslie.task_annotation.ITask;
import com.leslie.task_annotation.TaskMeta;
import com.leslie.task_api.utils.ClassUtils;
import com.leslie.task_api.utils.VersionUtils;
import com.leslie.task_api.utils.logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 作者：xjzhao
 * 时间：2021-07-01 20:07
 */
class _Init {

    public static void init(Context context){
        try {
            List<TaskMeta> tasks = new ArrayList<>();
            Set<String> files = null;
            if (Constant.isDebug || VersionUtils.isNewVersion(context)) {
                files = ClassUtils.getFileNameByPackageName(context, Constant.PACKAGE);
                if (!files.isEmpty()) {
                    context.getSharedPreferences(Constant.TASK_CACHE_SP_KEY, Context.MODE_PRIVATE).edit().putStringSet(Constant.TASK_CACHE_SP_KEY_MAP, files).apply();
                }
                VersionUtils.updateVersion(context);
            } else {
                files = new HashSet<>(context.getSharedPreferences(Constant.TASK_CACHE_SP_KEY, Context.MODE_PRIVATE).getStringSet(Constant.TASK_CACHE_SP_KEY_MAP, new HashSet<String>()));
            }
            logger.info("扫描出所有的文件：" + files);
            for (String className : files) {
                if (className.startsWith(Constant.FILE_NAME_START_WITH_PACKAGE)) {
                    ITask iTask = (ITask) Class.forName(className).getConstructor().newInstance();
                    tasks.addAll(iTask.getTasks());
                }
            }

            // 启动任务
            TaskDispatcher.getInstance().addTask(tasks).execute(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
