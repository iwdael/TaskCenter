package com.iwdael.taskcenter.task

import com.iwdael.taskcenter.interfaces.Task
import com.iwdael.taskcenter.source.Source2
import com.iwdael.taskcenter.source.Source3
import com.iwdael.taskcenter.util.Logger

class Task2 : Task<Source2, Collection<Source3>> {
    val tag = "Task2"
    override fun run(source: Source2): Collection<Source3> {
        Logger.tag(tag).v("run")
        return mutableListOf(Source3(), Source3(),Source3())
    }

    class Creator : Task.TreeFactory<Source2,Source3>{
        override fun make(source: Source2): Task<Source2, Collection<Source3>> = Task2()
    }
}