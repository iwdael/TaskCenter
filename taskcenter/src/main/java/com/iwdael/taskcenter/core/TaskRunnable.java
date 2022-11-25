package com.iwdael.taskcenter.core;

import com.iwdael.taskcenter.TaskCenter;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class TaskRunnable implements Runnable {
    private final Object task;
    private final String taskId;
    private final TaskProcessor taskProcessor;
    private final TaskCenter.Config config;

    public TaskRunnable(TaskProcessor taskProcessor, TaskCenter.Config config, Node<Object, Object, Object> node, Object task, String taskId) {
        this.taskProcessor = taskProcessor;
        this.config = config;
        this.task = task;
        this.taskId = taskId;
        this.taskProcessor.initPersistence();
        this.taskProcessor.stagePrepared(node, task);
    }

    @Override
    public void run() {
        try {
            taskProcessor.dispatch();
        } catch (Exception exception) {
            if (exception instanceof InterruptedException) {
                taskProcessor.notifySuspend();
                throw exception;
            } else {
                exception.printStackTrace();
                taskProcessor.notifyError();
            }
        }
    }
}