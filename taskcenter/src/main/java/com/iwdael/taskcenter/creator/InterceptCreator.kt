package com.iwdael.taskcenter.creator

import com.iwdael.taskcenter.interfaces.Task

class InterceptCreator<I, M>(private val converter: Task.Interceptor<I, M>) :
    Creator<Collection<I>, M>() {
    override fun make(input: Collection<I>): M = converter.convert(input)

    fun <O> append(creator: Task.TreeFactory<M, O>): TreeCreator<M, O> {
        val n = TreeCreator(creator)
        next = n
        n.number = this.number + 1
        return n
    }

    fun <O> append(creator: Task.NodeFactory<M, O>): NodeCreator<M, O> {
        val n = NodeCreator(creator)
        next = n
        n.number = this.number + 1
        return n
    }
}