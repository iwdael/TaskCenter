package com.iwdael.taskcenter.core;

import com.iwdael.taskcenter.TaskCenter;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
class TaskMapRunnable implements Runnable {
    private final TaskProcessor taskProcessor;
    private final Node<Object, Object, Object> node;
    private final TaskPersistence.TaskFrame taskFrame;
    private final Object source;
    private final TaskCenter.Config config;
    private final String taskId;

    public TaskMapRunnable(TaskProcessor taskProcessor, TaskCenter.Config config, String taskId, Node<Object, Object, Object> node, TaskPersistence.TaskFrame taskFrame) {
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
            Object dst = this.node.make(this.source);
            this.taskProcessor.stagePrepared(this.node.next, dst);
            this.taskProcessor.stageAchieved(this.node, this.source);
            this.taskProcessor.keepFrame();
            this.taskProcessor.dispatch();
        } catch (Exception exception) {
            if (exception instanceof InterruptedException) {
                taskProcessor.stageSuspend(this.node,this.source);
                taskProcessor.notifySuspend();
                throw exception;
            } else {
                exception.printStackTrace();
                taskProcessor.stageError(this.node,this.source);
                taskProcessor.notifyError();
            }
        }
    }
}
