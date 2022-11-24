package com.iwdael.taskcenter.example

import com.iwdael.taskcenter.TaskCenter
import com.iwdael.taskcenter.core.TaskClosure
import com.iwdael.taskcenter.example.source.Source1
import com.iwdael.taskcenter.example.task.Task1
import com.iwdael.taskcenter.example.task.Task2
import com.iwdael.taskcenter.example.task.Task3
import com.iwdael.taskcenter.example.task.Task4
import com.iwdael.taskcenter.defaults.TaskInterceptor
import com.iwdael.taskcenter.example.source.Source3
import com.iwdael.taskcenter.example.source.Source4
import com.iwdael.taskcenter.example.source.Source5
import com.iwdael.taskcenter.operators.Mapper
import com.iwdael.taskcenter.util.Logger


fun main() {
    TaskCenter.init(TaskCenter.Config.defaultConfig)
    val chain = TaskCenter.Chain.newBuilder()
        .append(Source1::class.java, Task1.Creator())
        .append(Task2.Creator())
        .append(Task3.Creator())
        .append(object :Mapper<Source4,Source4>{
            override fun map(src: Source4): Source4 {
                Logger.tag("").v("------")
                return src
            }
        })
        .append(TaskInterceptor())
        .append(Task4.Creator())
        .append ( object : Mapper<Source5,Source5> {
            override fun map(src: Source5): Source5 {
                Logger.tag("v").v("==========")
                 return src
            }

        } )
        .append {
            TaskClosure { Logger.tag("main").v("Source5 -> Unit") }
        }.build()
    TaskCenter.taskChain(chain)
    TaskCenter.start(Source1())
    while (true) {
    }
}