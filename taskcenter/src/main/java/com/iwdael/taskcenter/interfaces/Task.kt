package com.iwdael.taskcenter.interfaces

interface Task<I, O> {

    fun run(source: I): O

    interface Factory<I, O> {
        fun make(source: I): O
    }

    interface NodeFactory<I, O> : Factory<I, Task<I, O>>


    interface TreeFactory<I, O> : Factory<I, Task<I, Collection<O>>>

    interface Interceptor<I, O> {
        fun convert(source: Collection<I>): O
    }

}