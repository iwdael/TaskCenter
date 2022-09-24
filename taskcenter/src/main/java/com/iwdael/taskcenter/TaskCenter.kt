package com.iwdael.taskcenter

import com.iwdael.taskcenter.creator.Creator
import com.iwdael.taskcenter.creator.NodeCreator
import com.iwdael.taskcenter.creator.TreeCreator
import com.iwdael.taskcenter.interfaces.Task
import com.iwdael.taskcenter.task.TaskMaker
import java.util.concurrent.Executors

class TaskCenter {
    private val creators = mutableMapOf<Class<*>, Creator<*, *>>()
    private val executors = Executors.newCachedThreadPool();

    companion object {
        private var sTaskCenter: TaskCenter? = null
            get() {
                if (field == null) {
                    synchronized(TaskCenter::class.java) {
                        if (field == null) {
                            field = TaskCenter()
                        }
                    }
                }
                return field
            }

        internal fun creators(clazz: Class<*>) = sTaskCenter!!.creators[clazz] as Creator<Any, Any>?
        internal fun executors() = sTaskCenter!!.executors
        fun <I, O> append(type: Class<I>, creator: Task.NodeFactory<I, O>): NodeCreator<I, O> {
            val c = NodeCreator(creator)
            sTaskCenter!!.creators[type] = c
            return c;
        }

        fun <I, O> append(type: Class<I>, creator: Task.TreeFactory<I, O>): TreeCreator<I, O> {
            val c = TreeCreator(creator)
            sTaskCenter!!.creators[type] = c
            return c
        }

        fun submit(src:Any){
            executors().submit(TaskMaker(src))
        }
    }


}