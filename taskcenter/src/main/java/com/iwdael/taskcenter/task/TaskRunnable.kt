package com.iwdael.taskcenter.task

import com.iwdael.taskcenter.TaskNode
import com.iwdael.taskcenter.util.*
import java.lang.Exception
import kotlin.Throws

class TaskRunnable(private val taskNode: TaskNode, val creator: Any, val src: Any) : Runnable {
    init {
        taskNode.handling(creator.creator(), src)
    }

    @Throws(Exception::class)
    override fun run() {
        val dst = creator.creator().make(src).task().run(src)
        if (creator.creator().hasNext()) {
            if (creator.creator().isTree())
                 dst.collection().forEach {  taskNode.default(creator.creator().next(), it) }
            else
                taskNode.default(creator.creator().next(), dst)
        }
        taskNode.handled(creator.creator(), src)
        taskNode.handle()
    }
}