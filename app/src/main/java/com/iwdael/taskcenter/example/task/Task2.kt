package com.iwdael.taskcenter.example.task

import com.iwdael.taskcenter.task.TaskUnitary
import com.iwdael.taskcenter.interfaces.DisperseCreator
import com.iwdael.taskcenter.example.source.Source2
import com.iwdael.taskcenter.example.source.Source3
import com.iwdael.taskcenter.util.Logger

class Task2 : TaskUnitary<Source2, Collection<Source3>>() {
    val tag = "Task2"
    override fun run(source: Source2): Collection<Source3> {
        val frame = acquireFrame(Int::class.java) ?: 0
        Logger.tag(tag).v("Source2 -> List<Source3>:$frame")
        for (index in frame..100) {
            notifyProgress(index)
            keepFrame(index)
            Thread.sleep(500)
        }
        return mutableListOf(Source3(1), Source3(2), Source3(3))
    }

    class Creator : DisperseCreator<Source2, Source3> {
        override fun create(src: Source2): TaskUnitary<Source2, Collection<Source3>> = Task2()
    }
}