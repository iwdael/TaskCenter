package com.iwdael.taskcenter.example.task

import com.iwdael.taskcenter.creator.UnitCreator
import com.iwdael.taskcenter.example.source.Source1
import com.iwdael.taskcenter.example.source.Source2
import com.iwdael.taskcenter.core.Task
import com.iwdael.taskcenter.util.Logger

class Task1 : Task<Source1, Source2> {
    val tag = "TASK1"
    override fun run(source: Source1): Source2 {
        Logger.tag(tag).v("Source1 -> Source2")
        return Source2()
    }

    class Creator : UnitCreator<Source1, Source2> {
        override fun create(src: Source1): Task<Source1, Source2> = Task1()
    }
}