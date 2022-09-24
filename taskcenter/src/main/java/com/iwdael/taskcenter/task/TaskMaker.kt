package com.iwdael.taskcenter.task

import com.iwdael.taskcenter.util.Logger
import com.iwdael.taskcenter.creator.Creator
import com.iwdael.taskcenter.TaskCenter
import com.iwdael.taskcenter.TaskNode

class TaskMaker(private val source: Any) : Runnable {
    companion object {
        private const val TAG = "TaskRunnable"
    }

    override fun run() {
        val creator: Creator<*, *>? = TaskCenter.creators(source::class.java)
        if (creator == null) {
            Logger.tag(TAG).e("not support class:${source::class.java.name}")
            return
        }
        val node = TaskNode(creator)
        node.default(creator, source)
        node.handle()
    }


}