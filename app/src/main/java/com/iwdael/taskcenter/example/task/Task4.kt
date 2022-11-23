package com.iwdael.taskcenter.example.task

import com.iwdael.taskcenter.creator.UnitCreator
import com.iwdael.taskcenter.core.Task
import com.iwdael.taskcenter.example.source.Source4
import com.iwdael.taskcenter.example.source.Source5
import com.iwdael.taskcenter.util.Logger

class Task4 : Task<Collection<Source4>, Source5> {
    val tag = "task4"
    override fun run(source: Collection<Source4>): Source5 {
        Logger.tag(tag).v("List<Source4>:${source.size} -> Source5")
        return Source5()
    }

    class Creator : UnitCreator<Collection<Source4>, Source5> {
        override fun create(src: Collection<Source4>): Task<Collection<Source4>, Source5> = Task4()
    }
}