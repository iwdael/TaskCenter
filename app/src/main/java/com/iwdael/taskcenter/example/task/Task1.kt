package com.iwdael.taskcenter.example.task

import com.iwdael.taskcenter.task.TaskUnitary
import com.iwdael.taskcenter.example.source.Source1
import com.iwdael.taskcenter.example.source.Source2
import com.iwdael.taskcenter.interfaces.UnitCreator
import com.iwdael.taskcenter.util.Logger

class Task1 : TaskUnitary<Source1, Source2>() {
    val tag = "TASK1"
    override fun run(source: Source1): Source2 {
        val frame = acquireFrame(Int::class.java) ?: 0
        Logger.tag(tag).v("Source1 -> Source2: $frame")
        for (index in frame..100) {
            notifyProgress(index)
            keepFrame(index)
            Thread.sleep(500)
        }
        return Source2(1)
    }

    class Creator : UnitCreator<Source1, Source2> {
        override fun create(src: Source1): TaskUnitary<Source1, Source2> = Task1()
    }
}