package com.iwdael.taskcenter.core;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class Task {
    protected TaskCallBack callBack = null;

    protected final void notifyProgress(int progress) {
        callBack.notifyProgress(progress);
    }

    protected final void keepFrame(@NonNull Object o) {
        callBack.keepFrame(o);
    }

    @Nullable
    protected final <T> T acquireFrame(Class<T> clazz) {
        return callBack.acquireFrame(clazz);
    }
}
