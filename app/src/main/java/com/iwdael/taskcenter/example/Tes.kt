package com.iwdael.taskcenter.example

import com.iwdael.taskcenter.TaskCenter
import com.iwdael.taskcenter.TaskChain
import com.iwdael.taskcenter.TaskConfig
import com.iwdael.taskcenter.core.TaskClosure
import com.iwdael.taskcenter.creator.CloseCreator
import com.iwdael.taskcenter.defaults.TaskInterceptor
import com.iwdael.taskcenter.example.source.Source1
import com.iwdael.taskcenter.example.source.Source4
import com.iwdael.taskcenter.example.source.Source5
import com.iwdael.taskcenter.example.task.Task1
import com.iwdael.taskcenter.example.task.Task2
import com.iwdael.taskcenter.example.task.Task3
import com.iwdael.taskcenter.example.task.Task4
import com.iwdael.taskcenter.operators.Mapper
import com.iwdael.taskcenter.util.Logger

fun main() {
    TaskCenter.init(TaskConfig.defaultConfig)
    val chain = TaskChain.newBuilder().append(Source1::class.java, Task1.Creator()).append(Task2.Creator()).append(Task3.Creator()).append(object : Mapper<Source4, Source4> {
            override fun map(src: Source4): Source4 {
                Logger.tag("main").v("Source4 -> Source4")
                return src
            }
        }).append(object : Mapper<Source4, Source4> {
            override fun map(src: Source4): Source4 {
                Logger.tag("main").v("Source4 -> Source4")
                return src
            }
        }).append(object : Mapper<Source4, Source4> {
            override fun map(src: Source4): Source4 {
                Logger.tag("main").v("Source4 -> Source4")
                return src
            }
        }).append(object : Mapper<Source4, Source4> {
            override fun map(src: Source4): Source4 {
                Logger.tag("main").v("Source4 -> Source4")
                return src
            }
        }).append(object : Mapper<Source4, Source4> {
            override fun map(src: Source4): Source4 {
                Logger.tag("main").v("Source4 -> Source4")
                return src
            }
        }).append(TaskInterceptor()).append(Task4.Creator()).append(object : CloseCreator<Source5> {
            override fun create(src: Source5): TaskClosure<Source5> {
                return object : TaskClosure<Source5> {
                    override fun run(src: Source5) {
                        Logger.tag("main").v("Source5 -> Unit")
                    }
                }
            }
        }).build()
    TaskCenter.taskChain(chain)
    TaskCenter.start(Source1())
    while (true) {
    }
}