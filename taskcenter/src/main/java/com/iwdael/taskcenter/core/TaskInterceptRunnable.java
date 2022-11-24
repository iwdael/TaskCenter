package com.iwdael.taskcenter.core;

import java.util.Collection;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class TaskInterceptRunnable implements Runnable {
    private final TaskProcessingCenter taskProcessingCenter;
    private final Node<Object, Object> node;
    private final Collection<Object> sources;

    public TaskInterceptRunnable(TaskProcessingCenter taskProcessingCenter, Node<Object, Object> node, Collection<Object> sources) {
        this.taskProcessingCenter = taskProcessingCenter;
        this.node = node;
        this.sources = sources;
        for (Object object : this.sources) {
            this.taskProcessingCenter.handling(node, object);
        }
    }

    @Override
    public void run() {
        Object dst = this.node.make(this.sources);
        this.taskProcessingCenter.initialize(this.node.next, dst);
        for (Object source : sources) {
            this.taskProcessingCenter.handled(this.node, source);
        }
        this.taskProcessingCenter.dispatch();
    }
}
