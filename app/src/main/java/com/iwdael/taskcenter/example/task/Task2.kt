package com.iwdael.taskcenter.example.task

import com.iwdael.taskcenter.core.Task
import com.iwdael.taskcenter.creator.DisperseCreator
import com.iwdael.taskcenter.example.source.Source2
import com.iwdael.taskcenter.example.source.Source3
import com.iwdael.taskcenter.util.Logger

class Task2 : Task<Source2, Collection<Source3>> {
    val tag = "Task2"
    override fun run(source: Source2): Collection<Source3> {
        Logger.tag(tag).v("Source2 -> List<Source3>")
        return mutableListOf(Source3(), Source3(),Source3())
    }

    class Creator : DisperseCreator<Source2, Source3> {
        override fun create(src: Source2): Task<Source2, Collection<Source3>> = Task2()
    }
}