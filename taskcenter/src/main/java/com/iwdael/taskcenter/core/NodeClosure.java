package com.iwdael.taskcenter.core;

import com.iwdael.taskcenter.TaskCenter;
import com.iwdael.taskcenter.creator.CloseCreator;
import com.iwdael.taskcenter.util.Logger;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class NodeClosure<SRC> extends Node<SRC, TaskClosure<SRC>> {
    private final Class<?> type;
    private final CloseCreator<SRC> creator;

    public NodeClosure(Class<?> type, CloseCreator<SRC> creator) {
        this.type = type;
        this.creator = creator;
    }

    @Override
    protected TaskClosure<SRC> make(SRC src) {
        return creator.create(src);
    }

    public TaskCenter.Chain build() {
        Node<Object, Object> node = this.asNode();
        while (node.pre != null) {
            node = node.pre;
        }
        return new TaskCenter.Chain(type, node);
    }
}
