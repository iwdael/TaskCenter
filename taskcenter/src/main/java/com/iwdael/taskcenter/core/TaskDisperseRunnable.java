package com.iwdael.taskcenter.core;

import com.iwdael.taskcenter.TaskCenter;
import com.iwdael.taskcenter.task.TaskUnitary;

import java.util.Collection;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
class TaskDisperseRunnable implements Runnable {
    private final TaskProcessor taskProcessor;
    private final Node<Object, Object, Object> node;
    private final TaskPersistence.TaskFrame taskFrame;
    private final Object source;
    private final TaskCenter.Config config;
    private final String taskId;

    public TaskDisperseRunnable(TaskProcessor taskProcessor, TaskCenter.Config config, String taskId, Node<Object, Object, Object> node, TaskPersistence.TaskFrame taskFrame) {
        this.taskProcessor = taskProcessor;
        this.node = node;
        this.taskFrame = taskFrame;
        this.source = taskFrame.source;
        this.config = config;
        this.taskId = taskId;
        this.taskProcessor.stageRunning(this.node, this.source);
    }

    @Override
    public void run() {
        try {
            TaskUnitary task = ((TaskUnitary) this.node.make(this.source));
            task.callBack = new TaskCallBackImpl(taskProcessor, taskFrame);
            Collection<Object> collection = (Collection<Object>) task.run(this.source);
            for (Object source : collection) {
                this.taskProcessor.stagePrepared(this.node.next, source);
            }
            this.taskProcessor.stageAchieved(this.node, this.source);
            this.taskFrame.progress = 100;
            this.taskProcessor.notifyProgress();
            this.taskProcessor.dispatch();
        } catch (Exception exception) {
            if (exception instanceof InterruptedException) {
                this.taskProcessor.stageSuspend(this.node, this.source);
                taskProcessor.notifySuspend();
                throw exception;
            } else {
                exception.printStackTrace();
                this.taskProcessor.stageError(this.node, this.source);
                taskProcessor.notifyError();
            }
        }
    }
}
