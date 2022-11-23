package com.iwdael.taskcenter.core

import com.iwdael.taskcenter.util.*

class TaskCloseRunnable(private val taskProcessingCenter: TaskProcessingCenter, val node: Node<Any, Any>, val src: Any) : Runnable {
    init {
        taskProcessingCenter.handling(node, src)
    }

    @Throws(Exception::class)
    override fun run() {
        (node.make(src) as Task<Any, Any>).run(src)
        taskProcessingCenter.handled(node, src)
        taskProcessingCenter.handle()
    }
}