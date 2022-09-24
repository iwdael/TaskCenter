package com.iwdael.taskcenter.task

import com.iwdael.taskcenter.TaskNode
import com.iwdael.taskcenter.util.creator
import java.lang.Exception
import kotlin.Throws

class TaskInterceptor(private val taskNode: TaskNode, val creator: Any, val src: Any) : Runnable {
    init {
        src as Collection<Any>
        src.forEach { taskNode.handling(creator.creator(), it) }
    }

    @Throws(Exception::class)
    override fun run() {
        val dst = creator.creator().make(src)
        if (creator.creator().hasNext()) {
            taskNode.default(creator.creator().next(), dst)
        }
        src as Collection<Any>
        src.forEach { taskNode.handled(creator.creator(), it) }
        taskNode.handle()
    }
}