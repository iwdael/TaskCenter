package com.iwdael.taskcenter.core;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */

public abstract class Node<SRC, DST> {
    protected int index = 0;
    protected Node<Object, Object> next;
    protected Node<Object, Object> pre;

    protected abstract DST make(SRC src);
    protected Node<Object,Object> asNode(){
        return (Node<Object, Object>) this;
    }
}
