package com.iwdael.taskcenter.core

import com.google.gson.Gson
import com.iwdael.taskcenter.TaskCenter
import com.iwdael.taskcenter.util.Logger

class TaskProcessingCenter(private val root: Node<Any, Any>) {
    private val sources = mutableMapOf<Int, MutableList<Source>>()

    @Synchronized
    fun default(key: Node<Any, Any>, src: Any): TaskProcessingCenter {
        if (obtainSummaries(key).any { it.src == src }) return this
        obtainSummaries(key).add(Source(src))
        return this
    }

    @Synchronized
    fun handling(key: Node<Any, Any>, src: Any) {
        obtainSummaries(key).first { it.src == src }.state = State.ING
    }

    @Synchronized
    fun handled(key: Node<Any, Any>, src: Any) {
        obtainSummaries(key).first { it.src == src }.state = State.END
    }


    @Synchronized
    fun handle() {
        var node: Node<Any, Any>? = root
        while (node != null) {
            if (node is NodeInterceptor<*, *>) {
                if (handlingBefore(node)) break
                val defSize = obtainSummaries(node).filter { it.def() }.size
                val endSize = obtainSummaries(node).filter { it.end() }.size
                val nodeSize = obtainSummaries(node).size
                if (defSize == nodeSize) {
                    val set = obtainSummaries(node).filter { it.def() }.map { it.src }
                    TaskCenter.executors().submit(TaskInterceptRunnable(this, node, set))
                    break
                } else if (endSize != nodeSize) {
                    break
                }
            } else {
                obtainSummaries(node).filter { it.def() }.map {
                    when (node!!) {
                        is NodeMapper<*, *> -> TaskMapRunnable(this, node!!, it.src)
                        is NodeDisperser<*, *> -> TaskDisperseRunnable(this, node!!, it.src)
                        is NodeClosure<*> -> TaskCloseRunnable(this, node!!, it.src)
                        else -> TaskUnitRunnable(this, node!!, it.src)
                    }
                }.forEach { TaskCenter.executors().submit(it) }
            }
            node = node.next
        }
        Logger.tag("hand").v(Gson().toJson(sources))
        Logger.tag("hand").v("handleAll:${handledAll()}")
    }

    @Synchronized
    fun handledAll(): Boolean {
        var node: Node<Any, Any>? = root
        while (node != null) {
            if (obtainSummaries(node).any { !it.end() }) return false
            node = node.next
        }
        return true
    }

    @Synchronized
    fun handlingBefore(before: Node<Any, Any>): Boolean {
        var key = root
        while (key != before) {
            if (obtainSummaries(key).any { !it.end() }) return true
            key = key.next()
        }
        return false
    }

    @Synchronized
    private fun obtainSummaries(node: Node<Any, Any>): MutableList<Source> {
        return sources.getOrPut(node.index) { mutableListOf() }
    }

    class Source(var src: Any, var state: State = State.DEF) {
        fun def() = state == State.DEF
        fun ing() = state == State.ING
        fun end() = state == State.END
    }

    enum class State { DEF, ING, END }
}