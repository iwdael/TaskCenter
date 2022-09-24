package com.iwdael.taskcenter.task

import com.iwdael.taskcenter.interfaces.Task
import com.iwdael.taskcenter.source.Source4
import com.iwdael.taskcenter.source.Source5
import com.iwdael.taskcenter.util.Logger

class Task4 : Task<Collection<Source4>, Source5> {
    val tag = "task4"
    override fun run(source: Collection<Source4>): Source5 {
        Logger.tag(tag).v("run")
        return Source5()
    }

    class Creator : Task.NodeFactory<Collection<Source4>,Source5>{
        override fun make(source: Collection<Source4>): Task<Collection<Source4>, Source5> = Task4()
    }
}