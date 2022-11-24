package com.iwdael.taskcenter.core;

import java.util.Collection;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class TaskDisperseRunnable implements Runnable {
    private final TaskProcessingCenter taskProcessingCenter;
    private final Node<Object, Object> node;
    private final Object source;

    public TaskDisperseRunnable(TaskProcessingCenter taskProcessingCenter, Node<Object, Object> node, Object source) {
        this.taskProcessingCenter = taskProcessingCenter;
        this.node = node;
        this.source = source;
        this.taskProcessingCenter.handling(this.node, this.source);
    }

    @Override
    public void run() {
        Collection<Object> collection = (Collection<Object>) ((TaskUnitary) this.node.make(this.source)).run(this.source);
        for (Object source : collection) {
            this.taskProcessingCenter.initialize(this.node.next, source);
        }
        this.taskProcessingCenter.handled(this.node, this.source);
        this.taskProcessingCenter.dispatch();
    }
}
