package com.iwdael.taskcenter.core;

import com.iwdael.taskcenter.TaskCenter;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class TaskRunnable implements Runnable {
    private final Object source;

    public TaskRunnable(Object source) {
        this.source = source;
    }

    @Override
    public void run() {
        Node<Object, Object> node = TaskCenter.obtainNode(source.getClass());
        if (node == null) throw new RuntimeException("not support class:" + source.getClass().getName());
        TaskProcessingCenter processingCenter = new TaskProcessingCenter(node);
        processingCenter.initialize(node, source);
        processingCenter.dispatch();
    }
}