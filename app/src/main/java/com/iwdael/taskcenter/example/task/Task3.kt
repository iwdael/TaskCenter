package com.iwdael.taskcenter.example.task

import com.iwdael.taskcenter.task.TaskUnitary
import com.iwdael.taskcenter.example.source.Source3
import com.iwdael.taskcenter.example.source.Source4
import com.iwdael.taskcenter.interfaces.DisperseCreator
import com.iwdael.taskcenter.util.Logger

class Task3 : TaskUnitary<Source3, Collection<Source4>>() {
    val tag = "task3"

    companion object {
        @Volatile
        var index = 12
    }

    override fun run(src: Source3): Collection<Source4> {
        val frame = acquireFrame(Int::class.java) ?: 0
        Logger.tag(tag).v("Source3(${src.index3}) -> List<Source4>:$frame")
        for (index in frame..100) {
            notifyProgress(index)
            keepFrame(index)
            Thread.sleep(500)
        }
        return arrayListOf(Source4(++index), Source4(++index), Source4(++index), Source4(++index))
    }

    class Creator : DisperseCreator<Source3, Source4> {
        override fun create(src: Source3): TaskUnitary<Source3, Collection<Source4>> = Task3()
    }
}