package com.iwdael.taskcenter.task

import com.iwdael.taskcenter.interfaces.Task
import com.iwdael.taskcenter.source.Source3
import com.iwdael.taskcenter.source.Source4
import com.iwdael.taskcenter.util.Logger

class Task3 : Task<Source3, Source4> {
    val tag = "task3"
    override fun run(source: Source3): Source4 {
        Logger.tag(tag).v("run")
        return Source4()
    }

    class Creator : Task.NodeFactory<Source3,Source4>{
        override fun make(source: Source3): Task<Source3, Source4> = Task3()
    }
}