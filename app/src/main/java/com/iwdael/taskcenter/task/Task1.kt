package com.iwdael.taskcenter.task

import com.iwdael.taskcenter.interfaces.Task
import com.iwdael.taskcenter.source.Source1
import com.iwdael.taskcenter.source.Source2
import com.iwdael.taskcenter.util.Logger

class Task1 : Task<Source1, Source2> {
    val tag = "TASK1"
    override fun run(source: Source1): Source2 {
        Logger.tag(tag).v("run")
        return Source2()
    }

    class Creator : Task.NodeFactory<Source1,Source2>{
        override fun make(source: Source1): Task<Source1, Source2> = Task1()
    }
}