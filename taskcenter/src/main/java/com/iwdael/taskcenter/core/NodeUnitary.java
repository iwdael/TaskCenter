package com.iwdael.taskcenter.core;

import com.iwdael.taskcenter.interfaces.CloseCreator;
import com.iwdael.taskcenter.interfaces.DisperseCreator;
import com.iwdael.taskcenter.interfaces.Interceptor;
import com.iwdael.taskcenter.interfaces.Mapper;
import com.iwdael.taskcenter.interfaces.UnitCreator;
import com.iwdael.taskcenter.task.TaskUnitary;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class NodeUnitary<P, S> extends Node<P, TaskUnitary<P, S>, UnitCreator<P, S>> {
    public NodeUnitary(Class<?> type, UnitCreator<P, S> creator) {
        super(type, creator);
    }

    @Override
    protected TaskUnitary<P, S> make(P pre) {
        return creator.create(pre);
    }

    public <D> NodeDisperser<S, D> append(DisperseCreator<S, D> disperseCreator) {
        NodeDisperser<S, D> node = new NodeDisperser<>(type, disperseCreator);
        this.next = node.asNode();
        this.next.index = this.index + 1;
        this.next.pre = this.asNode();
        return node;
    }

    public <D> NodeUnitary<S, D> append(UnitCreator<S, D> unitCreator) {
        NodeUnitary<S, D> node = new NodeUnitary<>(type, unitCreator);
        this.next = node.asNode();
        this.next.index = this.index + 1;
        this.next.pre = this.asNode();
        return node;
    }


    public NodeClosure<S> append(CloseCreator<S> closeCreator) {
        NodeClosure<S> node = new NodeClosure<>(type, closeCreator);
        this.next = node.asNode();
        this.next.index = this.index + 1;
        this.next.pre = this.asNode();
        return node;
    }

    public <D> NodeMapper<S, D> append(Mapper<S, D> mapper) {
        NodeMapper<S, D> node = new NodeMapper<>(type, mapper);
        this.next = node.asNode();
        this.next.index = this.index + 1;
        this.next.pre = this.asNode();
        return node;
    }

    public <D> NodeInterceptor<S, D> append(Interceptor<S, D> interceptor) {
        NodeInterceptor<S, D> node = new NodeInterceptor<>(type, interceptor);
        this.next = node.asNode();
        this.next.index = this.index + 1;
        this.next.pre = this.asNode();
        return node;
    }
}
