package com.iwdael.taskcenter.core

import com.iwdael.taskcenter.TaskCenter
import com.iwdael.taskcenter.util.Logger

class TaskRunnable(private val source: Any) : Runnable {
    companion object {
        private const val TAG = "TaskLauncher"
    }

    override fun run() {
        val node: Node<Any, Any>? = TaskCenter.node(source::class.java)
        if (node == null) {
            Logger.tag(TAG).e("not support class:${source::class.java.name}")
            return
        }
        val taskProcessingCenter = TaskProcessingCenter(node)
        taskProcessingCenter.default(node, source)
        taskProcessingCenter.handle()
    }


}