#### task-api
```diff
@@  一个自动注册的Android启动任务管理器，区分线程和优先级，可延时，省去各种init()调用，只需定义自己的Task。@@ 
```
#### 版本
模块|task-api|task-annotation|task-compiler
---|---|---|---
版本|[task-api-1.0.0.aar](https://jitpack.io/com/github/xjz-111/task-api/1.0.0/task-api-task-annotation-1.0.0.jar)|[task-annotation-1.0.0.jar](https://jitpack.io/com/github/xjz-111/task-annotation/1.0.0/task-annotation-1.0.0.jar)|[task-compiler-1.0.0.jar](https://jitpack.io/com/github/xjz-111/task-compiler/1.0.0/task-compiler-1.0.0.jar)

#### 一. 功能介绍
* 支持主线程或子线程任务  
* 支持延时任务（延时任务优于各线程中带优先级的任务，故同时存在时忽略任务的优先级）  
* 非延时的主线程任务默认按照优先级处理，未添加优先级的任务按照扫描添加顺序执行  
* 非延时的子线程任务分为带优先级和不带优先级的，默认不带优先级  
* 任务成功失败监听，子线程任务会同时有子/主线程的成功/失败监听
* 优先级参数小于0时视为自动放弃优先级逻辑，必须大于-1  
* 任务定义之后，无需手动注册，task-api中会自动处理  
* 支持组件化，各个模块都可自定义任务  
* 支持扫描缓存，debug模式每次都会扫描任务注解，release模式根据App版本号对比选择是否使用扫描缓存  
#### 二. 各个库介绍
* [task-api](https://github.com/xjz-111/task-api)：Android Library 处理各种任务+自动注册整个启动任务管理器
* [task-annotation](https://github.com/xjz-111/task-annotation)：Java Library 定义任务的注解Task
* [task-compiler](https://github.com/xjz-111/task-compiler)：Java Library 实现AbstractProcessor，完成注解的扫描和每个module下任务的收集代码处理
#### 四. 依赖关系
![image](https://github.com/xjz-111/task-api/blob/master/imgs/1.jpg)
###### 注意
* app壳必须依赖task-api来进行最终apk运行时对整个task manager的注册，但如果它没有Task，不需要依赖task-compiler
* task-compiler是在编译器处理注解和生成代码，所以无需打包进apk，使用annotationProcessor依赖
#### 四. 具体使用
1. 添加依赖和配置
    ``` gradle
    // 在project的build.gradle中添加
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
    
    // 在当前module的build.gradle中添加
    android {
        defaultConfig {
            ...
            javaCompileOptions {
                annotationProcessorOptions {
                    // 用于获取当前module的名字，生成java文件_TaskManager$$TASK$${TASK_MODULE_NAME}.java
                    arguments = [TASK_MODULE_NAME: project.getName()]
                }
            }
        }
    }
    dependencies {
        // 替换成最新版本
        implementation 'com.github.xjz-111:task-api:*.*.*'
        annotationProcessor 'com.github.xjz-111:task-compiler:*.*.*'
        ...
    }
    ```
  
2. 具体使用
    ``` java
    // ！！！仅仅在需要的module定义自己的任务便可。如下，添加@Task注解，实现InitTask，其他无需任何操作！！！
    
    /**
     * thread：     任务线程类型 - TaskType.MAIN_THREAD 和 TaskType.CHILD_THREAD
     * priority：   任务优先级 - 默认-1，即忽略优先级，只有大于-1是才有优先级，优先级相同时根据扫描顺序决定
     * delayMillis：延时毫秒数，有该参数时priority参数时效
     */
    @Task(thread = TaskType.MAIN_THREAD, priority = -1, delayMillis = 3000)
    public class MTask implements InitTask {
        @Override
        public void init(@NonNull Context context) {
            // 当前任务需要做的事情
        }

        // 任务处理结果监听，不需要的直接return null;
        @Override
        public InitTaskCallback callback() {
            return new InitTaskCallback() {
                @Override
                public void onSuccess() {
                    // 主线程中的成功回调  所有任务都有
                }

                @Override
                public void onSuccessInThread() {
                    // 子线程中的成功回调 - 仅子线程任务有
                }

                @Override
                public void onFailure(Exception e) {
                    // 主线程中的失败回调  所有任务都有
                }

                @Override
                public void onFailureInThread(Exception e) {
                    // 子线程中的失败回调 - 仅子线程任务有
                }
            };
        }
    }
    ```
3. debug开启
    ``` java
    // 开启debug模式，调试期间每次添加或删除Task会实时更新不走扫描缓存
    if (isDebug) {           
        TaskManager.getInstance().isDebug(isDebug);  
    }
    ```

#### 五. 其他
* 编译期扫描注解日志
![image](https://github.com/xjz-111/task-api/blob/master/imgs/build-log.jpg)
* 生成代码在app/build/generated/ap_generated_sources/debug/out/com/taskManager/task目录下
![image](https://github.com/xjz-111/task-api/blob/master/imgs/generate-code.jpg)  
#### 六. 小学生QQ
<img src="https://github.com/xjz-111/task-api/blob/master/imgs/qq.jpg" width="300" height="300"/><br/>






