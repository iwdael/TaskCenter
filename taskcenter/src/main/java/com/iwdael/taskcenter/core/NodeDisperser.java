package com.iwdael.taskcenter.core;

import androidx.annotation.NonNull;

import com.iwdael.taskcenter.interfaces.CloseCreator;
import com.iwdael.taskcenter.interfaces.DisperseCreator;
import com.iwdael.taskcenter.interfaces.Interceptor;
import com.iwdael.taskcenter.interfaces.Mapper;
import com.iwdael.taskcenter.interfaces.UnitCreator;
import com.iwdael.taskcenter.task.TaskUnitary;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
public class NodeDisperser<P, S> extends Node<P, TaskUnitary<P, Collection<S>>, DisperseCreator<P, S>> {
    public NodeDisperser(Class<?> type, DisperseCreator<P, S> creator) {
        super(type, creator);
    }

    @NonNull
    @Override
    protected TaskUnitary<P, Collection<S>> make(@NonNull P pre) {
        return creator.create(pre);
    }

    @NotNull
    public <D> NodeDisperser<S, D> append(@NotNull DisperseCreator<S, D> disperseCreator) {
        NodeDisperser<S, D> node = new NodeDisperser<>(type, disperseCreator);
        this.next = node.asNode();
        this.next.index = this.index + 1;
        this.next.pre = this.asNode();
        return node;
    }

    @NotNull
    public <D> NodeUnitary<S, D> append(@NotNull UnitCreator<S, D> unitCreator) {
        NodeUnitary<S, D> node = new NodeUnitary<>(type, unitCreator);
        this.next = node.asNode();
        this.next.index = this.index + 1;
        this.next.pre = this.asNode();
        return node;
    }

    @NotNull
    public NodeClosure<S> append(@NotNull CloseCreator<S> closeCreator) {
        NodeClosure<S> node = new NodeClosure<>(type, closeCreator);
        this.next = node.asNode();
        this.next.index = this.index + 1;
        this.next.pre = this.asNode();
        return node;
    }

    @NotNull
    public <D> NodeMapper<S, D> append(@NotNull Mapper<S, D> mapper) {
        NodeMapper<S, D> node = new NodeMapper<>(type, mapper);
        this.next = node.asNode();
        this.next.index = this.index + 1;
        this.next.pre = this.asNode();
        return node;
    }

    @NotNull
    public <D> NodeInterceptor<S, D> append(@NotNull Interceptor<S, D> interceptor) {
        NodeInterceptor<S, D> node = new NodeInterceptor<>(type, interceptor);
        this.next = node.asNode();
        this.next.index = this.index + 1;
        this.next.pre = this.asNode();
        return node;
    }
}
