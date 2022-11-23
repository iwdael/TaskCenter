package com.iwdael.taskcenter.core

import com.iwdael.taskcenter.util.*

class TaskDisperseRunnable(private val taskProcessingCenter: TaskProcessingCenter, val node: Node<Any, Any>, val src: Any) : Runnable {
    init {
        taskProcessingCenter.handling(node, src)
    }

    @Throws(Exception::class)
    override fun run() {
        val dst = (node.make(src) as Task<Any, Any>).run(src)
        if (node.hasNext()) {
            (dst as Collection<*>).forEach { taskProcessingCenter.default(node.next(), it as Any) }
        }
        taskProcessingCenter.handled(node, src)
        taskProcessingCenter.handle()
    }
}