package com.iwdael.taskcenter.core;

import com.iwdael.taskcenter.creator.CloseCreator;
import com.iwdael.taskcenter.creator.DisperseCreator;
import com.iwdael.taskcenter.creator.UnitCreator;
import com.iwdael.taskcenter.operators.Interceptor;
import com.iwdael.taskcenter.operators.Mapper;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class NodeUnitary<PRE, SRC> extends Node<PRE, TaskUnitary<PRE, SRC>> {
    private final Class<?> type;
    private final UnitCreator<PRE, SRC> creator;

    public NodeUnitary(Class<?> type, UnitCreator<PRE, SRC> creator) {
        this.type = type;
        this.creator = creator;
    }


    @Override
    protected TaskUnitary<PRE, SRC> make(PRE pre) {
        return creator.create(pre);
    }

    public <DST> NodeDisperser<SRC, DST> append(DisperseCreator<SRC, DST> disperseCreator) {
        NodeDisperser<SRC, DST> node = new NodeDisperser<>(type, disperseCreator);
        this.next = node.asNode();
        this.next.index = this.index + 1;
        this.next.pre = this.asNode();
        return node;
    }

    public <DST> NodeUnitary<SRC, DST> append(UnitCreator<SRC, DST> unitCreator) {
        NodeUnitary<SRC, DST> node = new NodeUnitary<>(type, unitCreator);
        this.next = node.asNode();
        this.next.index = this.index + 1;
        this.next.pre = this.asNode();
        return node;
    }


    public NodeClosure<SRC> append(CloseCreator<SRC> closeCreator) {
        NodeClosure<SRC> node = new NodeClosure<>(type, closeCreator);
        this.next = node.asNode();
        this.next.index = this.index + 1;
        this.next.pre = this.asNode();
        return node;
    }

    public <DST> NodeMapper<SRC, DST> append(Mapper<SRC, DST> mapper) {
        NodeMapper<SRC, DST> node = new NodeMapper<>(type, mapper);
        this.next = node.asNode();
        this.next.index = this.index + 1;
        this.next.pre = this.asNode();
        return node;
    }

    public <DST> NodeInterceptor<SRC, DST> append(Interceptor<SRC, DST> interceptor) {
        NodeInterceptor<SRC, DST> node = new NodeInterceptor<>(type, interceptor);
        this.next = node.asNode();
        this.next.index = this.index + 1;
        this.next.pre = this.asNode();
        return node;
    }
}
