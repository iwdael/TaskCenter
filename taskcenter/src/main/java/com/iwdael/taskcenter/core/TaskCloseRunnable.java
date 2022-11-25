package com.iwdael.taskcenter.core;

import com.iwdael.taskcenter.TaskCenter;
import com.iwdael.taskcenter.task.TaskClosure;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
class TaskCloseRunnable implements Runnable {
    private final TaskProcessor taskProcessor;
    private final Node<Object, Object, Object> node;
    private final TaskPersistence.TaskFrame taskFrame;
    private final Object source;
    private final TaskCenter.Config config;
    private final String taskId;

    public TaskCloseRunnable(TaskProcessor taskProcessor, TaskCenter.Config config, String taskId, Node<Object, Object, Object> node, TaskPersistence.TaskFrame taskFrame) {
        this.taskProcessor = taskProcessor;
        this.node = node;
        this.taskFrame = taskFrame;
        this.source = taskFrame.source;
        this.config = config;
        this.taskId = taskId;
        taskProcessor.stageRunning(node, source);
    }

    @Override
    public void run() {
        try {
            TaskClosure task = (TaskClosure) this.node.make(this.source);
            task.callBack = new TaskCallBackImpl(taskProcessor, taskFrame);
            task.run(this.source);
            this.taskProcessor.stageAchieved(this.node, this.source);
            this.taskFrame.progress = 100;
            this.taskProcessor.notifyProgress();
            this.taskProcessor.dispatch();
        } catch (Exception exception) {
            if (exception instanceof InterruptedException) {
                taskProcessor.stageSuspend(node, this.source);
                taskProcessor.notifySuspend();
                throw exception;
            } else {
                exception.printStackTrace();
                taskProcessor.stageError(node, this.source);
                taskProcessor.notifyError();
            }
        }
    }
}
