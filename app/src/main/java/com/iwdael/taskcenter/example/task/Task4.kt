package com.iwdael.taskcenter.example.task

import com.iwdael.taskcenter.task.TaskUnitary
import com.iwdael.taskcenter.example.source.Source4
import com.iwdael.taskcenter.example.source.Source5
import com.iwdael.taskcenter.interfaces.UnitCreator
import com.iwdael.taskcenter.util.Logger

class Task4 : TaskUnitary<Collection<Source4>, Source5>() {
    val tag = "task4"
    override fun run(source: Collection<Source4>): Source5 {
        val frame = acquireFrame(Int::class.java) ?: 0
        Logger.tag(tag).v("List<Source4>(${source.map { it.index4 }}) -> Source5:$frame")
        for (index in frame..100) {
            notifyProgress(index)
            keepFrame(index)
            Thread.sleep(500)
        }
        return Source5(6)
    }

    class Creator : UnitCreator<Collection<Source4>, Source5> {
        override fun create(src: Collection<Source4>): TaskUnitary<Collection<Source4>, Source5> = Task4()
    }
}