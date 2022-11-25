package com.iwdael.taskcenter.core;

import com.google.gson.Gson;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
class TaskCallBackImpl implements TaskCallBack {
    private final TaskProcessor taskProcessor;

    private final TaskPersistence.TaskFrame taskFrame;

    public TaskCallBackImpl(TaskProcessor taskProcessor, TaskPersistence.TaskFrame taskFrame) {
        this.taskProcessor = taskProcessor;
        this.taskFrame = taskFrame;
    }

    @Override
    public void notifyProgress(int progress) {
        this.taskFrame.progress = progress;
        this.taskProcessor.notifyProgress();
    }

    @Override
    public void keepFrame(Object o) {
        this.taskFrame.frame = o;
        this.taskProcessor.keepFrame();
    }

    @Override
    public <T> T acquireFrame(Class<T> clazz) {
        return new Gson().fromJson(new Gson().toJson(taskFrame.frame), clazz);
    }
}
