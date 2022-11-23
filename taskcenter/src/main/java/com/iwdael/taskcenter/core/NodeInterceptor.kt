package com.iwdael.taskcenter.core

import com.iwdael.taskcenter.creator.CloseCreator
import com.iwdael.taskcenter.creator.DisperseCreator
import com.iwdael.taskcenter.creator.UnitCreator
import com.iwdael.taskcenter.operators.Interceptor
import com.iwdael.taskcenter.operators.Mapper

class NodeInterceptor<PRE : Any, SRC : Any>(private val type: Class<*>, private val converter: Interceptor<PRE, SRC>) : Node<Collection<PRE>, SRC>() {
    override fun make(src: Collection<PRE>): SRC = converter.intercept(src)

    fun <DST : Any> append(creator: DisperseCreator<SRC, DST>): NodeDisperser<SRC, DST> {
        val n = NodeDisperser(type, creator)
        this.next = n.asNode()
        this.next!!.index = this.index + 1
        this.next!!.pre = this.asNode()
        return n
    }

    fun <DST : Any> append(creator: UnitCreator<SRC, DST>): NodeUnitary<SRC, DST> {
        val n = NodeUnitary(type, creator)
        this.next = n.asNode()
        this.next!!.index = this.index + 1
        this.next!!.pre = this.asNode()
        return n
    }

    fun append(factory: CloseCreator<SRC>): NodeClosure<SRC> {
        val n = NodeClosure(type, factory)
        this.next = n.asNode()
        this.next!!.index = this.index + 1
        this.next!!.pre = this.asNode()
        return n
    }

    fun <DST : Any> append(mapper: Mapper<SRC, DST>): NodeMapper<SRC, DST> {
        val n = NodeMapper(type, mapper)
        this.next = n.asNode()
        this.next!!.index = this.index + 1
        this.next!!.pre = this.asNode()
        return n
    }
}