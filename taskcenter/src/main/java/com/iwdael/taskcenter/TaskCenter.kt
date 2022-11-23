package com.iwdael.taskcenter

import com.iwdael.taskcenter.core.Node
import com.iwdael.taskcenter.core.TaskRunnable
import java.util.concurrent.ThreadPoolExecutor

/**
 * @author : iwdael
 * @mail : iwdael@outlook.com
 * @project : https://github.com/iwdael/TaskCenter
 */
object TaskCenter {
    @JvmStatic
    private val nodes: MutableMap<Class<*>, Node<Any, Any>> = HashMap()

    @JvmStatic
    private var executor: ThreadPoolExecutor? = null

    @JvmStatic
    fun init(config: TaskConfig.Config) {
        if (executor == null) {
            synchronized(TaskCenter::class) {
                if (executor == null) {
                    executor = ThreadPoolExecutor(config.corePoolSize, config.maximumPoolSize, config.keepAliveTime, config.unit, config.workQueue)
                }
            }
        }
    }

    private fun checkState() {
        if (executor == null) {
            synchronized(TaskCenter::class) {
                if (executor == null)
                    throw RuntimeException("please initial TaskCenter")
            }
        }
    }

    @JvmStatic
    fun start(task: Any) {
        checkState()
        executor!!.submit(TaskRunnable(task))
    }

    @JvmStatic
    fun taskChain(chain: TaskChain.Chain) {
        checkState()
        nodes[chain.type] = chain.node
    }

    @JvmStatic
    internal fun executors(): ThreadPoolExecutor {
        checkState()
        return executor!!
    }

    @JvmStatic
    internal fun node(type: Class<out Any?>): Node<Any, Any> {
        checkState()
        return nodes[type]!!
    }
}