package com.iwdael.taskcenter.creator

import com.iwdael.taskcenter.interfaces.Task

class TreeCreator<I, M>(private val creator: Task.TreeFactory<I, M>) :
    Creator<I, Task<I, Collection<M>>>() {


    override fun make(input: I) = creator.make(input)

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