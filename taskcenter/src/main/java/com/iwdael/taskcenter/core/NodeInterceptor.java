package com.iwdael.taskcenter.core;

import com.iwdael.taskcenter.creator.CloseCreator;
import com.iwdael.taskcenter.creator.DisperseCreator;
import com.iwdael.taskcenter.creator.UnitCreator;
import com.iwdael.taskcenter.operators.Interceptor;
import com.iwdael.taskcenter.operators.Mapper;

import java.util.Collection;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class NodeInterceptor<PRE, SRC> extends Node<Collection<PRE>, SRC> {
    private final Class<?> type;
    private final Interceptor<PRE, SRC> creator;

    public NodeInterceptor(Class<?> type, Interceptor<PRE, SRC> creator) {
        this.type = type;
        this.creator = creator;
    }


    @Override
    protected SRC make(Collection<PRE> pres) {
        return creator.intercept(pres);
    }
    public <DST> NodeDisperser<SRC, DST> append(DisperseCreator<SRC, DST> disperseCreator) {
        NodeDisperser<SRC, DST> node = new NodeDisperser<>(type, disperseCreator);
        this.next = node.asNode();
        this.next.index = this.index + 1;
        this.next.pre = this.asNode();
        return node;
    }

    public <DST extends Object> NodeUnitary<SRC, DST> append(UnitCreator<SRC, DST> unitCreator) {
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
