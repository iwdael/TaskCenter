package com.iwdael.taskcenter

import com.iwdael.taskcenter.creator.Creator
import com.iwdael.taskcenter.task.TaskInterceptor
import com.iwdael.taskcenter.task.TaskRunnable
import com.iwdael.taskcenter.util.Logger
import com.iwdael.taskcenter.util.creator
import com.iwdael.taskcenter.util.isIntercept
import com.iwdael.taskcenter.util.obtain

class TaskNode(private val creator: Creator<*, *>) {
    private val nodes = mutableMapOf<Creator<*, *>, MutableList<Node>>()

    @Synchronized
    fun default(key: Creator<*, *>, src: Any): TaskNode {
        if (this.nodes.obtain(key).any { it.src == src }) return this
        this.nodes.obtain(key).add(Node(key, src))
        return this
    }

    @Synchronized
    fun handling(key: Creator<*, *>, src: Any) {
        this.nodes.obtain(key).firstOrNull { it.src == src }?.state = State.ING
    }

    @Synchronized
    fun handled(key: Creator<*, *>, src: Any) {
        this.nodes.obtain(key).firstOrNull { it.src == src }?.state = State.END
    }


    @Synchronized
    fun handle() {
        var key: Creator<*, *>? = creator.creator()
        while (key != null) {
            if (key.isIntercept()) {
                if (handlingBefore(key)) break
                val defSize = nodes.obtain(key).filter { it.def() }.size
                val endSize = nodes.obtain(key).filter { it.end() }.size
                val nodeSize = nodes.obtain(key).size
                Logger.tag("node").e("nodeSize:${nodeSize} , endSize:${endSize} ,defSize:${defSize}")
                if (defSize == nodeSize) {
                    val src = nodes.obtain(key).filter { it.def() }.map { it.src }
                    TaskCenter.executors().submit(TaskInterceptor(this, key, src))
                    break
                } else if (endSize != nodeSize) {
                    break
                }
            } else {
                nodes.obtain(key)
                    .filter { it.def() }
                    .map {
                        Logger.tag("node").e("runnable :${key?.javaClass} ${it.src.javaClass}")
                        Thread.sleep(1000)
                        TaskRunnable(this, key!!, it.src)
                    }
                    .forEach { TaskCenter.executors().submit(it) }
            }
            key = key.next
        }
    }


    @Synchronized
    fun handlingBefore(before: Creator<*, *>): Boolean {
        var key = creator
        while (key != before) {
            if (nodes.obtain(key).any { !it.end() }) return true
            key = key.next()
        }
        return false
    }

    class Node(
        var creator: Creator<*, *>,
        var src: Any,
        var state: State = State.DEF
    ) {
        fun def() = state == State.DEF
        fun ing() = state == State.ING
        fun end() = state == State.END
    }

    enum class State { DEF, ING, END }
}