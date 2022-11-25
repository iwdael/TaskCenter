package com.iwdael.taskcenter.example.task

import com.iwdael.taskcenter.task.TaskClosure
import com.iwdael.taskcenter.example.source.Source5
import com.iwdael.taskcenter.interfaces.CloseCreator
import com.iwdael.taskcenter.util.Logger


/**
 * @author  : iwdael
 * @mail    : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
class Task5 : TaskClosure<Source5>() {
    val tag = "task5"
    override fun run(src: Source5) {
        val frame = acquireFrame(Int::class.java) ?: 0
        Logger.tag(tag).v("close:$frame")
        for (index in frame..100) {
            notifyProgress(index)
            keepFrame(index)
            Thread.sleep(500)
        }
    }

    class Creator : CloseCreator<Source5> {
        override fun create(src: Source5): TaskClosure<Source5> {
            return Task5()
        }
    }
}