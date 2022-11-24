package com.iwdael.taskcenter.core;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class TaskCloseRunnable implements Runnable {
    private final TaskProcessingCenter taskProcessingCenter;
    private final Node<Object, Object> node;
    private final Object source;

    public TaskCloseRunnable(TaskProcessingCenter taskProcessingCenter, Node<Object, Object> node, Object source) {
        this.taskProcessingCenter = taskProcessingCenter;
        this.node = node;
        this.source = source;
        taskProcessingCenter.handling(node, source);
    }

    @Override
    public void run() {
        ((TaskClosure) this.node.make(this.source)).run(this.source);
        this.taskProcessingCenter.handled(this.node, this.source);
        this.taskProcessingCenter.dispatch();
    }
}
