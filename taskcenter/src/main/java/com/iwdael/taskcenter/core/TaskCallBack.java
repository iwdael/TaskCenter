package com.iwdael.taskcenter.core;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
interface TaskCallBack {
    void notifyProgress(int progress);

    void keepFrame(Object o);

    <T> T acquireFrame(Class<T> clazz);
}
