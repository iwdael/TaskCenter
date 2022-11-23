package com.iwdael.taskcenter.core

class TaskInterceptRunnable(private val taskProcessingCenter: TaskProcessingCenter, val node: Node<Any, Any>, private val collection: Collection<Any>) : Runnable {
    init {
        collection.forEach { taskProcessingCenter.handling(node, it) }
    }

    @Throws(Exception::class)
    override fun run() {
        val dst = node.make(collection)
        if (node.hasNext()) {
            taskProcessingCenter.default(node.next(), dst)
        }
        collection.forEach { taskProcessingCenter.handled(node, it) }
        taskProcessingCenter.handle()
    }
}