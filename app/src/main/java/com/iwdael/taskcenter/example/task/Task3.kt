package com.iwdael.taskcenter.example.task

import com.iwdael.taskcenter.creator.DisperseCreator
import com.iwdael.taskcenter.example.source.Source3
import com.iwdael.taskcenter.example.source.Source4
import com.iwdael.taskcenter.core.Task
import com.iwdael.taskcenter.util.Logger

class Task3 : Task<Source3, Collection<Source4>> {
    val tag = "task3"
    override fun run(src: Source3): Collection<Source4> {
        Logger.tag(tag).v("Source3 -> List<Source4>")
        return arrayListOf(Source4(), Source4(), Source4(), Source4())
    }

    class Creator : DisperseCreator<Source3, Source4> {
        override fun create(src: Source3): Task<Source3, Collection<Source4>> = Task3()
    }
}