package com.iwdael.taskcenter.core;

import com.iwdael.taskcenter.TaskCenter;
import com.iwdael.taskcenter.util.Utils;

import java.util.Collection;
import java.util.List;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
class TaskInterceptRunnable implements Runnable {
    private final TaskProcessor taskProcessor;
    private final Node<Object, Object, Object> node;
    private final Collection<Object> sources;
    private final TaskCenter.Config config;
    private final String taskId;


    public TaskInterceptRunnable(TaskProcessor taskProcessor, TaskCenter.Config config, String taskId, Node<Object, Object, Object> node, List<TaskPersistence.TaskFrame> taskFrames) {
        this.taskProcessor = taskProcessor;
        this.node = node;
        this.config = config;
        this.taskId = taskId;
        this.sources = Utils.convert(taskFrames, taskFrame -> taskFrame.source);
        for (Object object : this.sources) {
            this.taskProcessor.stageRunning(node, object);
        }
    }

    @Override
    public void run() {
        try {
            Object dst = this.node.make(this.sources);
            this.taskProcessor.stagePrepared(this.node.next, dst);
            for (Object source : sources) {
                this.taskProcessor.stageAchieved(this.node, source);
            }
            this.taskProcessor.keepFrame();
            this.taskProcessor.dispatch();
        } catch (Exception exception) {
            if (exception instanceof InterruptedException) {
                for (Object source : sources) {
                    this.taskProcessor.stageSuspend(this.node, source);
                }
                taskProcessor.notifySuspend();
                throw exception;
            } else {
                exception.printStackTrace();
                for (Object source : sources) {
                    this.taskProcessor.stageError(this.node, source);
                }
                taskProcessor.notifyError();
            }
        }
    }
}
