package com.iwdael.taskcenter.core;

import androidx.annotation.NonNull;

import com.iwdael.taskcenter.TaskCenter;
import com.iwdael.taskcenter.interfaces.CloseCreator;
import com.iwdael.taskcenter.task.TaskClosure;

import org.jetbrains.annotations.NotNull;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class NodeClosure<S> extends Node<S, TaskClosure<S>, CloseCreator<S>> {

    public NodeClosure(Class<?> type, CloseCreator<S> creator) {
        super(type, creator);
    }

    @Override
    @NotNull
    protected TaskClosure<S> make(@NonNull S src) {
        return creator.create(src);
    }

    public TaskCenter.Chain build() {
        Node<Object, Object, Object> node = this.asNode();
        while (node.pre != null) {
            node = node.pre;
        }
        return new TaskCenter.Chain(type, node);
    }
}
