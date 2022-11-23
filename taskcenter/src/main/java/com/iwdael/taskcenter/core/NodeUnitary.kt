package com.iwdael.taskcenter.core

import com.iwdael.taskcenter.creator.CloseCreator
import com.iwdael.taskcenter.creator.DisperseCreator
import com.iwdael.taskcenter.creator.UnitCreator
import com.iwdael.taskcenter.operators.Interceptor
import com.iwdael.taskcenter.operators.Mapper

class NodeUnitary<PRE : Any, SRC : Any>(private val type: Class<*>, private val creator: UnitCreator<PRE, SRC>) : Node<PRE, Task<PRE, SRC>>() {
    override fun make(src: PRE) = creator.create(src)
    fun <DST : Any> append(disperseCreator: DisperseCreator<SRC, DST>): NodeDisperser<SRC, DST> {
        val n = NodeDisperser(type, disperseCreator)
        this.next = n.asNode()
        this.next!!.index = this.index + 1
        this.next!!.pre = this.asNode()
        return n
    }

    fun <DST : Any> append(unitCreator: UnitCreator<SRC, DST>): NodeUnitary<SRC, DST> {
        val n = NodeUnitary(type, unitCreator)
        this.next = n.asNode()
        this.next!!.index = this.index + 1
        this.next!!.pre = this.asNode()
        return n
    }

    fun <DST : Any> append(interceptor: Interceptor<SRC, DST>): NodeInterceptor<SRC, DST> {
        val n = NodeInterceptor(type, interceptor)
        this.next = n.asNode()
        this.next!!.index = this.index + 1
        this.next!!.pre = this.asNode()
        return n
    }

    fun append(closeCreator: CloseCreator<SRC>): NodeClosure<SRC> {
        val n = NodeClosure(type, closeCreator)
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