package com.iwdael.taskcenter.core;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class TaskUnitRunnable implements Runnable {
    public static final String TAG = "TaskUnitRunnable";
    private final TaskProcessingCenter taskProcessingCenter;
    private final Node<Object, Object> node;
    private final Object source;

    public TaskUnitRunnable(TaskProcessingCenter taskProcessingCenter, Node<Object, Object> node, Object source) {
        this.taskProcessingCenter = taskProcessingCenter;
        this.node = node;
        this.source = source;
        this.taskProcessingCenter.handling(node, source);
    }

    @Override
    public void run() {
        Object dst = ((TaskUnitary) this.node.make(this.source)).run(this.source);
        this.taskProcessingCenter.initialize(this.node.next, dst);
        this.taskProcessingCenter.handled(this.node, this.source);
        this.taskProcessingCenter.dispatch();
    }
}
