package com.iwdael.taskcenter.core;

import com.iwdael.taskcenter.annotations.TaskFieldCreator;

import org.jetbrains.annotations.NotNull;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */

public abstract class Node<S, D, C> {
    protected int index = 0;
    protected Node<Object, Object, Object> next;
    protected Node<Object, Object, Object> pre;
    protected final Class<?> type;
    @TaskFieldCreator
    protected final C creator;

    public Node(Class<?> type, C creator) {
        this.type = type;
        this.creator = creator;
    }

    @NotNull
    protected abstract D make(@NotNull S src);

    @NotNull
    protected Node<Object, Object, Object> asNode() {
        return (Node<Object, Object, Object>) this;
    }
}
